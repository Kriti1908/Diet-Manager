#ifndef FOOD_H
#define FOOD_H

#include <string>
#include <vector>
#include <memory>
#include <iostream>

// Abstract base class for all food types
class A_Food {
protected:
    std::string name; // Identifying string
    std::vector<std::string> keywords; // Search keywords
    double caloriesPerServing; // Calories per serving

public:
    A_Food(const std::string& name, const std::vector<std::string>& keywords, double calories);
    virtual ~A_Food() = default;

    // Getters
    std::string getName() const;
    std::vector<std::string> getKeywords() const;
    double getCaloriesPerServing() const;
    
    // This method will be implemented differently for basic and composite foods
    virtual double calculateTotalCalories() const = 0;
    
    // String representation for saving to file
    virtual std::string toString() const = 0;
    
    // Check if food contains all keywords
    bool containsAllKeywords(const std::vector<std::string>& searchKeywords) const;
    
    // Check if food contains any keyword
    bool containsAnyKeyword(const std::vector<std::string>& searchKeywords) const;
};

// Basic food class representing atomic food items
class C_BasicFood : public A_Food {
public:
    C_BasicFood(const std::string& name, const std::vector<std::string>& keywords, double calories);
    
    // Implementation of abstract methods
    double calculateTotalCalories() const override;
    std::string toString() const override;
    
    // Static method to parse a basic food from string
    static std::shared_ptr<C_BasicFood> fromString(const std::string& str);
};

// Composite food made up of other foods
class C_CompositeFood : public A_Food {
private:
    std::vector<std::pair<std::shared_ptr<A_Food>, double>> components; // Pair of food and number of servings

public:
    C_CompositeFood(const std::string& name, const std::vector<std::string>& keywords);
    
    // Add a component with specified servings
    void addComponent(std::shared_ptr<A_Food> food, double servings);
    
    // Get all components
    const std::vector<std::pair<std::shared_ptr<A_Food>, double>>& getComponents() const;
    
    // Implementation of abstract methods
    double calculateTotalCalories() const override;
    std::string toString() const override;
    
    // Static method to parse a composite food from string
    static std::shared_ptr<C_CompositeFood> fromString(const std::string& str);
};

#endif // FOOD_H
