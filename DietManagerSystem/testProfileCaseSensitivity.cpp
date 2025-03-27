#include <iostream>
#include "dietProfile.h"

int main() {
    std::cout << "Testing case sensitivity in diet profile...\n\n";
    C_DietProfile profile;
    
    std::cout << "Testing gender case sensitivity:\n";
    try {
        profile.setGender("Male");
        std::cout << " - Set gender to 'Male', got: " << profile.getGender() << "\n";
    } catch (const std::invalid_argument& e) {
        std::cout << " - Error setting to 'Male': " << e.what() << "\n";
    }
    
    try {
        profile.setGender("FEMALE");
        std::cout << " - Set gender to 'FEMALE', got: " << profile.getGender() << "\n";
    } catch (const std::invalid_argument& e) {
        std::cout << " - Error setting to 'FEMALE': " << e.what() << "\n";
    }
    
    std::cout << "\nTesting activity level case sensitivity:\n";
    try {
        profile.setActivityLevel("Sedentary");
        std::cout << " - Set activity level to 'Sedentary', got: " << profile.getActivityLevel() << "\n";
    } catch (const std::invalid_argument& e) {
        std::cout << " - Error setting to 'Sedentary': " << e.what() << "\n";
    }
    
    try {
        profile.setActivityLevel("MODERATE");
        std::cout << " - Set activity level to 'MODERATE', got: " << profile.getActivityLevel() << "\n";
    } catch (const std::invalid_argument& e) {
        std::cout << " - Error setting to 'MODERATE': " << e.what() << "\n";
    }
    
    try {
        C_DietProfile profile;
        std::cout << "\nTesting calorie calculation with different cases:\n";
        
        // Test with Harris-Benedict method
        profile.setGender("Male");
        profile.setHeight(180);
        profile.setWeight(80);
        profile.setAge(30);
        profile.setActivityLevel("Moderate");
        
        double calories = profile.calculateTargetCalories();
        std::cout << " - Harris-Benedict calories with 'Male'/'Moderate': " << calories << "\n";
        
        // Test with Mifflin-St Jeor method
        profile.setCalorieCalculationMethod("Mifflin-St Jeor");
        calories = profile.calculateTargetCalories();
        std::cout << " - Mifflin-St Jeor calories with 'Male'/'Moderate': " << calories << "\n";
        
    } catch (const std::exception& e) {
        std::cout << " - Unexpected error: " << e.what() << "\n";
    }
    
    std::cout << "\nTest completed.\n";
    return 0;
}