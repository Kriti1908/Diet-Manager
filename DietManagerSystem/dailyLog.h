#ifndef DAILY_LOG_H
#define DAILY_LOG_H

#include "food.h"
#include <string>
#include <memory>
#include <vector>
#include <map>
#include <utility>

// Class to track foods consumed on a specific date
class C_DailyLog {
private:
    std::string date; // Format: YYYY-MM-DD
    std::map<std::string, double> consumedFoods; // Map of food name to servings
    std::string logFilePath;
    bool modified;
    
    // Track user profile data for this day
    std::string gender;
    double height;
    double weight;
    int age;
    std::string activityLevel;

public:
    // Constructor with current date
    C_DailyLog(const std::string& filePath);
    
    // Constructor with specific date
    C_DailyLog(const std::string& filePath, const std::string& date);
    
    // Set current date
    void setDate(const std::string& date);
    
    // Get current date
    std::string getDate() const;
    
    // Add food to the log with number of servings
    void addFood(const std::string& foodName, double servings);
    
    // Remove food from the log
    bool removeFood(const std::string& foodName);
    
    // Get all consumed foods
    const std::map<std::string, double>& getConsumedFoods() const;
    
    // Calculate total calories consumed
    double calculateTotalCalories() const;
    
    // Load log from file
    bool loadFromFile();
    
    // Save log to file
    bool saveToFile() const;
    
    // Check if log has been modified
    bool isModified() const;
    
    // Profile data getters and setters
    void setProfileData(const std::string& gender, double height, double weight, 
                        int age, const std::string& activityLevel);
    std::string getGender() const;
    double getHeight() const;
    double getWeight() const;
    int getAge() const;
    std::string getActivityLevel() const;
};

#endif // DAILY_LOG_H
