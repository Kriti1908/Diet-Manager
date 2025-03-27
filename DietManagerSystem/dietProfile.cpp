#include "dietProfile.h"
#include "utils.h"
#include <stdexcept>
#include <sstream>
#include <iostream>
#include <cmath>

// Harris-Benedict method implementation
double C_HarrisBenedictMethod::calculateCalories(
    const std::string& gender,
    double height,
    double weight,
    int age,
    const std::string& activityLevel) const {
    
    double bmr = 0.0;
    
    // Calculate BMR based on gender
    if (gender == "male" || gender == "Male") {
        bmr = 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age);
    } else {
        bmr = 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age);
    }
    
    // Apply activity multiplier
    double activityMultiplier = 1.2; // sedentary default
    
    if (activityLevel == "sedentary" || activityLevel == "Sedentary") {
        activityMultiplier = 1.2;
    } else if (activityLevel == "light" || activityLevel == "Light") {
        activityMultiplier = 1.375;
    } else if (activityLevel == "moderate" || activityLevel == "Moderate") {
        activityMultiplier = 1.55;
    } else if (activityLevel == "active" || activityLevel == "Active" || 
               activityLevel == "high" || activityLevel == "High") {
        activityMultiplier = 1.725;
    } else if (activityLevel == "very active" || activityLevel == "Very Active" || 
               activityLevel == "very high" || activityLevel == "Very High") {
        activityMultiplier = 1.9;
    }
    
    return std::round(bmr * activityMultiplier);
}

std::string C_HarrisBenedictMethod::getName() const {
    return "Harris-Benedict";
}

// Mifflin-St Jeor method implementation
double C_MifflinStJeorMethod::calculateCalories(
    const std::string& gender,
    double height,
    double weight,
    int age,
    const std::string& activityLevel) const {
    
    double bmr = 0.0;
    
    // Calculate BMR based on gender
    if (gender == "male" || gender == "Male") {
        bmr = (10 * weight) + (6.25 * height) - (5 * age) + 5;
    } else {
        bmr = (10 * weight) + (6.25 * height) - (5 * age) - 161;
    }
    
    // Apply activity multiplier
    double activityMultiplier = 1.2; // sedentary default
    
    if (activityLevel == "sedentary" || activityLevel == "Sedentary") {
        activityMultiplier = 1.2;
    } else if (activityLevel == "light" || activityLevel == "Light") {
        activityMultiplier = 1.375;
    } else if (activityLevel == "moderate" || activityLevel == "Moderate") {
        activityMultiplier = 1.55;
    } else if (activityLevel == "active" || activityLevel == "Active" || 
               activityLevel == "high" || activityLevel == "High") {
        activityMultiplier = 1.725;
    } else if (activityLevel == "very active" || activityLevel == "Very Active" || 
               activityLevel == "very high" || activityLevel == "Very High") {
        activityMultiplier = 1.9;
    }
    
    return std::round(bmr * activityMultiplier);
}

std::string C_MifflinStJeorMethod::getName() const {
    return "Mifflin-St Jeor";
}

// C_DietProfile implementation
C_DietProfile::C_DietProfile()
    : gender("male"), height(170.0), weight(70.0), age(30), activityLevel("moderate") {
    
    // Initialize available calculation methods
    availableMethods.push_back(std::make_shared<C_HarrisBenedictMethod>());
    availableMethods.push_back(std::make_shared<C_MifflinStJeorMethod>());
    
    // Set default method
    calorieMethod = availableMethods[0];
}

std::string C_DietProfile::getGender() const {
    return gender;
}

double C_DietProfile::getHeight() const {
    return height;
}

double C_DietProfile::getWeight() const {
    return weight;
}

int C_DietProfile::getAge() const {
    return age;
}

std::string C_DietProfile::getActivityLevel() const {
    return activityLevel;
}

void C_DietProfile::setGender(const std::string& g) {
    // Convert to lowercase for case-insensitive comparison
    std::string gLower = g;
    for (char& c : gLower) {
        c = std::tolower(c);
    }
    
    if (gLower == "male" || gLower == "female") {
        // Preserve the original case as provided by the user
        gender = g;
    } else {
        throw std::invalid_argument("Gender must be 'male' or 'female'");
    }
}

void C_DietProfile::setHeight(double h) {
    if (h > 0) {
        height = h;
    } else {
        throw std::invalid_argument("Height must be positive");
    }
}

void C_DietProfile::setWeight(double w) {
    if (w > 0) {
        weight = w;
    } else {
        throw std::invalid_argument("Weight must be positive");
    }
}

void C_DietProfile::setAge(int a) {
    if (a > 0) {
        age = a;
    } else {
        throw std::invalid_argument("Age must be positive");
    }
}

void C_DietProfile::setActivityLevel(const std::string& level) {
    // Convert to lowercase for case-insensitive comparison
    std::string levelLower = level;
    for (char& c : levelLower) {
        c = std::tolower(c);
    }
    
    if (levelLower == "sedentary" || 
        levelLower == "light" || 
        levelLower == "moderate" || 
        levelLower == "active" || levelLower == "high" || 
        levelLower == "very active" || levelLower == "very high") {
        // Preserve the original case as provided by the user
        activityLevel = level;
    } else {
        throw std::invalid_argument("Invalid activity level");
    }
}

void C_DietProfile::updateProfile(
    const std::string& g, double h, double w, int a, const std::string& level) {
    setGender(g);
    setHeight(h);
    setWeight(w);
    setAge(a);
    setActivityLevel(level);
}

void C_DietProfile::setCalorieCalculationMethod(const std::string& methodName) {
    for (const auto& method : availableMethods) {
        if (method->getName() == methodName) {
            calorieMethod = method;
            return;
        }
    }
    throw std::invalid_argument("Unknown calorie calculation method: " + methodName);
}

std::string C_DietProfile::getCalorieCalculationMethod() const {
    if (calorieMethod) {
        return calorieMethod->getName();
    }
    return "Unknown";
}

double C_DietProfile::calculateTargetCalories() const {
    return calorieMethod->calculateCalories(gender, height, weight, age, activityLevel);
}

std::vector<std::string> C_DietProfile::getAvailableMethodNames() const {
    std::vector<std::string> names;
    for (const auto& method : availableMethods) {
        names.push_back(method->getName());
    }
    return names;
}

std::string C_DietProfile::toString() const {
    std::stringstream ss;
    ss << gender << "|" 
       << height << "|" 
       << weight << "|" 
       << age << "|" 
       << activityLevel << "|"
       << calorieMethod->getName();
    return ss.str();
}

C_DietProfile C_DietProfile::fromString(const std::string& str) {
    std::vector<std::string> parts = split(str, '|');
    
    if (parts.size() != 6) {
        throw std::runtime_error("Invalid diet profile format: " + str);
    }
    
    C_DietProfile profile;
    profile.setGender(parts[0]);
    profile.setHeight(std::stod(parts[1]));
    profile.setWeight(std::stod(parts[2]));
    profile.setAge(std::stoi(parts[3]));
    profile.setActivityLevel(parts[4]);
    profile.setCalorieCalculationMethod(parts[5]);
    
    return profile;
}
