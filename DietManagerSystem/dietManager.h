#ifndef DIET_MANAGER_H
#define DIET_MANAGER_H

#include "foodDatabase.h"
#include "dietProfile.h"
#include "dailyLog.h"
#include "command.h"
#include <memory>
#include <string>

// Main class that coordinates the diet management system
class C_DietManager {
private:
    C_FoodDatabase foodDb;
    C_DietProfile profile;
    C_DailyLog currentLog;
    C_CommandManager commandManager;
    
    // Helper function to calculate calories for a specific food
    double calculateFoodCalories(const std::string& foodName, double servings) const;

public:
    C_DietManager(const std::string& foodDbPath, const std::string& logPath);
    
    // Food database operations
    bool loadFoodDatabase();
    bool saveFoodDatabase();
    bool addBasicFood(const std::string& name, const std::vector<std::string>& keywords, double calories);
    bool addCompositeFood(const std::string& name, const std::vector<std::string>& keywords, 
                         const std::vector<std::pair<std::string, double>>& components);
    std::vector<std::shared_ptr<A_Food>> findFoods(const std::vector<std::string>& keywords, bool matchAll = true) const;
    std::shared_ptr<A_Food> findFoodByName(const std::string& name) const;
    
    // Log operations
    bool loadDailyLog();
    bool saveDailyLog();
    void addFoodToLog(const std::string& foodName, double servings);
    void removeFoodFromLog(const std::string& foodName);
    void setLogDate(const std::string& date);
    double calculateTotalCalories() const;
    double calculateRemainingCalories() const;
    
    // Profile operations
    void updateProfile(const std::string& gender, double height, double weight, int age, const std::string& activityLevel);
    void setCalorieCalculationMethod(const std::string& methodName);
    double calculateTargetCalories() const;
    std::vector<std::string> getAvailableMethodNames() const;
    
    // Command operations
    void undo();
    void redo();
    std::vector<std::string> getCommandHistory() const;
    
    // Getters
    C_FoodDatabase& getFoodDatabase();
    C_DietProfile& getDietProfile();
    C_DailyLog& getCurrentDailyLog();
};

#endif // DIET_MANAGER_H
