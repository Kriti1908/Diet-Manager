#ifndef FOOD_DATABASE_H
#define FOOD_DATABASE_H

#include "food.h"
#include <memory>
#include <unordered_map>
#include <string>
#include <vector>

class C_FoodDatabase {
private:
    std::vector<std::shared_ptr<A_Food>> foods;
    std::unordered_map<std::string, std::shared_ptr<A_Food>> foodsByName;
    std::string databaseFilePath;
    bool modified;

public:
    C_FoodDatabase(const std::string& filePath);
    ~C_FoodDatabase();

    // Load database from file
    bool loadFromFile();
    
    // Save database to file
    bool saveToFile() const;
    
    // Add a food to the database
    bool addFood(std::shared_ptr<A_Food> food);
    
    // Find a food by name
    std::shared_ptr<A_Food> findFoodByName(const std::string& name) const;
    
    // Find foods by keywords (all or any)
    std::vector<std::shared_ptr<A_Food>> findFoodsByKeywords(
        const std::vector<std::string>& keywords, 
        bool matchAll = true) const;
    
    // Get all foods
    const std::vector<std::shared_ptr<A_Food>>& getAllFoods() const;
    
    // Check if database has been modified
    bool isModified() const;
    
    // Resolve references in composite foods after all foods are loaded
    void resolveReferences();
};

#endif // FOOD_DATABASE_H
