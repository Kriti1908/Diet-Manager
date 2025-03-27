#include "foodDatabase.h"
#include "utils.h"
#include <fstream>
#include <sstream>
#include <iostream>
#include <algorithm>

C_FoodDatabase::C_FoodDatabase(const std::string& filePath)
    : databaseFilePath(filePath), modified(false) {
}

C_FoodDatabase::~C_FoodDatabase() {
    if (modified) {
        saveToFile();
    }
}

bool C_FoodDatabase::loadFromFile() {
    std::ifstream file(databaseFilePath);
    if (!file.is_open()) {
        std::cerr << "Could not open food database file: " << databaseFilePath << std::endl;
        return false;
    }
    
    foods.clear();
    foodsByName.clear();
    
    std::string line;
    std::vector<std::string> compositeFoodLines;
    
    // First pass: load all basic foods
    while (std::getline(file, line)) {
        if (line.empty() || line[0] == '#') {
            continue; // Skip empty lines and comments
        }
        
        if (line.substr(0, 5) == "BASIC") {
            try {
                auto basicFood = C_BasicFood::fromString(line);
                addFood(basicFood);
            } catch (const std::exception& e) {
                std::cerr << "Error loading basic food: " << e.what() << std::endl;
            }
        } else if (line.substr(0, 9) == "COMPOSITE") {
            compositeFoodLines.push_back(line);
        }
    }
    
    // Second pass: load all composite foods
    for (const auto& line : compositeFoodLines) {
        try {
            auto compositeFood = C_CompositeFood::fromString(line);
            addFood(compositeFood);
        } catch (const std::exception& e) {
            std::cerr << "Error loading composite food: " << e.what() << std::endl;
        }
    }
    
    // Third pass: resolve references in composite foods
    resolveReferences();
    
    modified = false;
    return true;
}

bool C_FoodDatabase::saveToFile() const {
    std::ofstream file(databaseFilePath);
    if (!file.is_open()) {
        std::cerr << "Could not open food database file for writing: " << databaseFilePath << std::endl;
        return false;
    }
    
    file << "# YADA Food Database - Format: TYPE|NAME|KEYWORDS|...\n";
    
    // Save basic foods first
    for (const auto& food : foods) {
        if (dynamic_cast<C_BasicFood*>(food.get())) {
            file << food->toString() << std::endl;
        }
    }
    
    // Then save composite foods
    for (const auto& food : foods) {
        if (dynamic_cast<C_CompositeFood*>(food.get())) {
            file << food->toString() << std::endl;
        }
    }
    
    return true;
}

bool C_FoodDatabase::addFood(std::shared_ptr<A_Food> food) {
    // Check if food with same name already exists
    if (foodsByName.find(food->getName()) != foodsByName.end()) {
        return false;
    }
    
    foods.push_back(food);
    foodsByName[food->getName()] = food;
    modified = true;
    return true;
}

std::shared_ptr<A_Food> C_FoodDatabase::findFoodByName(const std::string& name) const {
    auto it = foodsByName.find(name);
    if (it != foodsByName.end()) {
        return it->second;
    }
    return nullptr;
}

std::vector<std::shared_ptr<A_Food>> C_FoodDatabase::findFoodsByKeywords(
    const std::vector<std::string>& keywords, bool matchAll) const {
    
    std::vector<std::shared_ptr<A_Food>> results;
    
    for (const auto& food : foods) {
        bool matches = matchAll ? 
            food->containsAllKeywords(keywords) : 
            food->containsAnyKeyword(keywords);
        
        if (matches) {
            results.push_back(food);
        }
    }
    
    return results;
}

const std::vector<std::shared_ptr<A_Food>>& C_FoodDatabase::getAllFoods() const {
    return foods;
}

bool C_FoodDatabase::isModified() const {
    return modified;
}

void C_FoodDatabase::resolveReferences() {
    for (const auto& food : foods) {
        auto compositeFood = std::dynamic_pointer_cast<C_CompositeFood>(food);
        if (compositeFood) {
            // Parse the string representation to get component info
            std::string foodStr = food->toString();
            std::vector<std::string> parts = split(foodStr, '|');
            
            if (parts.size() < 4) continue;
            
            int componentCount = std::stoi(parts[3]);
            
            // Clear existing components and re-add them
            auto components = compositeFood->getComponents();
            components.clear();
            
            for (int i = 0; i < componentCount; i++) {
                if (4 + i >= parts.size()) break;
                
                std::vector<std::string> componentParts = split(parts[4 + i], ':');
                if (componentParts.size() != 2) continue;
                
                std::string componentName = componentParts[0];
                double servings = std::stod(componentParts[1]);
                
                auto componentFood = findFoodByName(componentName);
                if (componentFood) {
                    compositeFood->addComponent(componentFood, servings);
                }
            }
        }
    }
}
