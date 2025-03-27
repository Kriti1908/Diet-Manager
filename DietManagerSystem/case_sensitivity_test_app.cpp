#include <iostream>
#include <string>
#include "user.h"
#include "dietProfile.h"
#include "utils.h"

// Simple test to verify case sensitivity handling in the application
int main() {
    std::cout << "=== YADA Case Sensitivity Test ===\n\n";
    
    // Test creating a user with mixed case inputs
    try {
        std::cout << "Creating user with mixed case inputs...\n";
        
        User user("testuser", "password");
        
        // Test with mixed case gender and activity level
        C_DietProfile& profile = user.getProfile();
        profile.setGender("Male");
        profile.setHeight(180.0);
        profile.setWeight(75.0);
        profile.setAge(30);
        profile.setActivityLevel("Moderate");
        
        std::cout << "User created successfully!\n";
        std::cout << "Profile details:\n";
        std::cout << " - Gender: " << profile.getGender() << "\n";
        std::cout << " - Height: " << profile.getHeight() << " cm\n";
        std::cout << " - Weight: " << profile.getWeight() << " kg\n";
        std::cout << " - Age: " << profile.getAge() << " years\n";
        std::cout << " - Activity Level: " << profile.getActivityLevel() << "\n";
        std::cout << " - Calculation Method: " << profile.getCalorieCalculationMethod() << "\n";
        std::cout << " - Target Calories: " << profile.calculateTargetCalories() << " kcal\n\n";
        
        // Test with uppercase inputs
        std::cout << "Testing with uppercase inputs...\n";
        profile.setGender("FEMALE");
        profile.setActivityLevel("VERY ACTIVE");
        
        std::cout << "Updated profile details:\n";
        std::cout << " - Gender: " << profile.getGender() << "\n";
        std::cout << " - Activity Level: " << profile.getActivityLevel() << "\n";
        std::cout << " - Target Calories: " << profile.calculateTargetCalories() << " kcal\n";
        
    } catch (const std::exception& e) {
        std::cout << "Error: " << e.what() << "\n";
    }
    
    std::cout << "\nTest completed.\n";
    return 0;
}