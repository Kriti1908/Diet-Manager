#include "dietManager.h"
#include <iostream>

C_DietManager::C_DietManager(const std::string& foodDbPath, const std::string& logPath)
    : foodDb(foodDbPath), currentLog(logPath) {
}

bool C_DietManager::loadFoodDatabase() {
    return foodDb.loadFromFile();
}

bool C_DietManager::saveFoodDatabase() {
    return foodDb.saveToFile();
}

bool C_DietManager::addBasicFood(const std::string& name, const std::vector<std::string>& keywords, double calories) {
    auto food = std::make_shared<C_BasicFood>(name, keywords, calories);
    return foodDb.addFood(food);
}

bool C_DietManager::addCompositeFood(
    const std::string& name, 
    const std::vector<std::string>& keywords,
    const std::vector<std::pair<std::string, double>>& components) {
    
    auto compositeFood = std::make_shared<C_CompositeFood>(name, keywords);
    
    for (const auto& [foodName, servings] : components) {
        auto componentFood = foodDb.findFoodByName(foodName);
        if (!componentFood) {
            std::cerr << "Component food not found: " << foodName << std::endl;
            return false;
        }
        
        compositeFood->addComponent(componentFood, servings);
    }
    
    return foodDb.addFood(compositeFood);
}

std::vector<std::shared_ptr<A_Food>> C_DietManager::findFoods(
    const std::vector<std::string>& keywords, bool matchAll) const {
    return foodDb.findFoodsByKeywords(keywords, matchAll);
}

std::shared_ptr<A_Food> C_DietManager::findFoodByName(const std::string& name) const {
    return foodDb.findFoodByName(name);
}

bool C_DietManager::loadDailyLog() {
    return currentLog.loadFromFile();
}

bool C_DietManager::saveDailyLog() {
    return currentLog.saveToFile();
}

void C_DietManager::addFoodToLog(const std::string& foodName, double servings) {
    auto command = std::make_shared<C_AddFoodCommand>(this, foodName, servings);
    commandManager.executeCommand(command);
}

void C_DietManager::removeFoodFromLog(const std::string& foodName) {
    auto command = std::make_shared<C_RemoveFoodCommand>(this, foodName);
    commandManager.executeCommand(command);
}

void C_DietManager::setLogDate(const std::string& date) {
    currentLog.setDate(date);
    loadDailyLog();
}

double C_DietManager::calculateFoodCalories(const std::string& foodName, double servings) const {
    auto food = foodDb.findFoodByName(foodName);
    if (food) {
        return food->getCaloriesPerServing() * servings;
    }
    return 0.0;
}

double C_DietManager::calculateTotalCalories() const {
    double total = 0.0;
    
    for (const auto& [foodName, servings] : currentLog.getConsumedFoods()) {
        total += calculateFoodCalories(foodName, servings);
    }
    
    return total;
}

double C_DietManager::calculateRemainingCalories() const {
    return calculateTargetCalories() - calculateTotalCalories();
}

void C_DietManager::updateProfile(
    const std::string& gender, 
    double height, 
    double weight, 
    int age, 
    const std::string& activityLevel) {
    
    auto command = std::make_shared<C_UpdateProfileCommand>(
        this, gender, height, weight, age, activityLevel);
    commandManager.executeCommand(command);
}

void C_DietManager::setCalorieCalculationMethod(const std::string& methodName) {
    profile.setCalorieCalculationMethod(methodName);
}

double C_DietManager::calculateTargetCalories() const {
    return profile.calculateTargetCalories();
}

std::vector<std::string> C_DietManager::getAvailableMethodNames() const {
    return profile.getAvailableMethodNames();
}

void C_DietManager::undo() {
    commandManager.undo();
}

void C_DietManager::redo() {
    commandManager.redo();
}

std::vector<std::string> C_DietManager::getCommandHistory() const {
    return commandManager.getCommandHistory();
}

C_FoodDatabase& C_DietManager::getFoodDatabase() {
    return foodDb;
}

C_DietProfile& C_DietManager::getDietProfile() {
    return profile;
}

C_DailyLog& C_DietManager::getCurrentDailyLog() {
    return currentLog;
}
