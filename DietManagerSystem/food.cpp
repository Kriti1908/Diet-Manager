#include "food.h"
#include "utils.h"
#include <algorithm>
#include <sstream>

// A_Food implementation
A_Food::A_Food(const std::string& name, const std::vector<std::string>& keywords, double calories)
    : name(name), keywords(keywords), caloriesPerServing(calories) {}

std::string A_Food::getName() const {
    return name;
}

std::vector<std::string> A_Food::getKeywords() const {
    return keywords;
}

double A_Food::getCaloriesPerServing() const {
    return caloriesPerServing;
}

bool A_Food::containsAllKeywords(const std::vector<std::string>& searchKeywords) const {
    for (const auto& keyword : searchKeywords) {
        auto it = std::find_if(keywords.begin(), keywords.end(), 
            [&keyword](const std::string& k) {
                return stringToLower(k).find(stringToLower(keyword)) != std::string::npos;
            });
        
        if (it == keywords.end()) {
            return false;
        }
    }
    return true;
}

bool A_Food::containsAnyKeyword(const std::vector<std::string>& searchKeywords) const {
    for (const auto& keyword : searchKeywords) {
        auto it = std::find_if(keywords.begin(), keywords.end(), 
            [&keyword](const std::string& k) {
                return stringToLower(k).find(stringToLower(keyword)) != std::string::npos;
            });
        
        if (it != keywords.end()) {
            return true;
        }
    }
    return false;
}

// C_BasicFood implementation
C_BasicFood::C_BasicFood(const std::string& name, const std::vector<std::string>& keywords, double calories)
    : A_Food(name, keywords, calories) {}

double C_BasicFood::calculateTotalCalories() const {
    return caloriesPerServing;
}

std::string C_BasicFood::toString() const {
    std::stringstream ss;
    ss << "BASIC|" << name << "|";
    
    // Join keywords with commas
    for (size_t i = 0; i < keywords.size(); ++i) {
        ss << keywords[i];
        if (i < keywords.size() - 1) {
            ss << ",";
        }
    }
    
    ss << "|" << caloriesPerServing;
    return ss.str();
}

std::shared_ptr<C_BasicFood> C_BasicFood::fromString(const std::string& str) {
    std::vector<std::string> parts = split(str, '|');
    
    if (parts.size() != 4 || parts[0] != "BASIC") {
        throw std::runtime_error("Invalid basic food format: " + str);
    }
    
    std::string name = parts[1];
    std::vector<std::string> keywords = split(parts[2], ',');
    double calories = std::stod(parts[3]);
    
    return std::make_shared<C_BasicFood>(name, keywords, calories);
}

// C_CompositeFood implementation
C_CompositeFood::C_CompositeFood(const std::string& name, const std::vector<std::string>& keywords)
    : A_Food(name, keywords, 0) {} // Calories will be calculated based on components

void C_CompositeFood::addComponent(std::shared_ptr<A_Food> food, double servings) {
    components.push_back(std::make_pair(food, servings));
}

const std::vector<std::pair<std::shared_ptr<A_Food>, double>>& C_CompositeFood::getComponents() const {
    return components;
}

double C_CompositeFood::calculateTotalCalories() const {
    double total = 0.0;
    for (const auto& component : components) {
        total += component.first->getCaloriesPerServing() * component.second;
    }
    return total;
}

std::string C_CompositeFood::toString() const {
    std::stringstream ss;
    ss << "COMPOSITE|" << name << "|";
    
    // Join keywords with commas
    for (size_t i = 0; i < keywords.size(); ++i) {
        ss << keywords[i];
        if (i < keywords.size() - 1) {
            ss << ",";
        }
    }
    
    ss << "|" << components.size();
    
    for (const auto& component : components) {
        ss << "|" << component.first->getName() << ":" << component.second;
    }
    
    return ss.str();
}

std::shared_ptr<C_CompositeFood> C_CompositeFood::fromString(const std::string& str) {
    std::vector<std::string> parts = split(str, '|');
    
    if (parts.size() < 4 || parts[0] != "COMPOSITE") {
        throw std::runtime_error("Invalid composite food format: " + str);
    }
    
    std::string name = parts[1];
    std::vector<std::string> keywords = split(parts[2], ',');
    int componentCount = std::stoi(parts[3]);
    
    if (parts.size() != 4 + componentCount) {
        throw std::runtime_error("Component count mismatch in composite food: " + str);
    }
    
    auto compositeFood = std::make_shared<C_CompositeFood>(name, keywords);
    
    // Components will be added later when all foods are loaded
    
    return compositeFood;
}
