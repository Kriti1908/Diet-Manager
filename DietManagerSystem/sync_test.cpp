#include <iostream>
#include <string>
#include <fstream>
#include <cmath>
#include "user.h"
#include "userManager.h"
#include "dietManager.h"
#include "dietProfile.h"

int main() {
    // Clean up any existing test files
    std::remove("test_users.txt");
    std::remove("test_foods.txt");
    std::remove("test_log.txt");
    
    // Create empty files
    std::ofstream foodsFile("test_foods.txt", std::ios::app);
    foodsFile.close();
    
    std::ofstream logFile("test_log.txt", std::ios::app);
    logFile.close();
    
    std::ofstream usersFile("test_users.txt", std::ios::app);
    usersFile.close();
    
    // Initialize user manager and diet manager
    UserManager userManager("test_users.txt");
    C_DietManager dietManager("test_foods.txt", "test_log.txt");
    
    // Register a test user
    std::cout << "Registering test user..." << std::endl;
    if (userManager.registerUser("testuser", "password")) {
        std::cout << "Registration successful!" << std::endl;
    } else {
        std::cout << "Registration failed!" << std::endl;
        return 1;
    }
    
    // Login with the test user
    std::cout << "Logging in test user..." << std::endl;
    if (userManager.loginUser("testuser", "password")) {
        std::cout << "Login successful!" << std::endl;
    } else {
        std::cout << "Login failed!" << std::endl;
        return 1;
    }
    
    // Get the current user
    User* user = userManager.getCurrentUser();
    if (!user) {
        std::cout << "Failed to get current user!" << std::endl;
        return 1;
    }
    
    // Set up user profile
    std::cout << "Setting up user profile..." << std::endl;
    user->getProfile().updateProfile("Male", 175.0, 70.0, 30, "Moderate");
    user->getProfile().setCalorieCalculationMethod("Harris-Benedict");
    user->setProfileCompleted(true);
    
    // Calculate target calories from user profile
    double userTargetCalories = user->getProfile().calculateTargetCalories();
    std::cout << "User profile target calories: " << userTargetCalories << std::endl;
    
    // Copy user profile to diet manager
    std::cout << "Copying user profile to diet manager..." << std::endl;
    dietManager.updateProfile(
        user->getProfile().getGender(),
        user->getProfile().getHeight(),
        user->getProfile().getWeight(),
        user->getProfile().getAge(),
        user->getProfile().getActivityLevel()
    );
    dietManager.setCalorieCalculationMethod(user->getProfile().getCalorieCalculationMethod());
    
    // Calculate target calories from diet manager
    double dietManagerTargetCalories = dietManager.calculateTargetCalories();
    std::cout << "Diet manager target calories: " << dietManagerTargetCalories << std::endl;
    
    // Test if the calories match
    if (abs(userTargetCalories - dietManagerTargetCalories) < 0.01) {
        std::cout << "✓ Target calories match between User and DietManager!" << std::endl;
    } else {
        std::cout << "✗ Target calories don't match between User and DietManager!" << std::endl;
        return 1;
    }
    
    // Now update the profile through the diet manager
    std::cout << "\nUpdating profile through diet manager..." << std::endl;
    dietManager.updateProfile("Male", 180.0, 75.0, 32, "High");
    
    // Also update the user's profile in the UserManager (simulating our fix)
    user->getProfile().updateProfile("Male", 180.0, 75.0, 32, "High");
    userManager.saveToFile();
    
    // Calculate new target calories
    double newUserTargetCalories = user->getProfile().calculateTargetCalories();
    double newDietManagerTargetCalories = dietManager.calculateTargetCalories();
    
    std::cout << "New user profile target calories: " << newUserTargetCalories << std::endl;
    std::cout << "New diet manager target calories: " << newDietManagerTargetCalories << std::endl;
    
    // Test if the new calories match
    if (abs(newUserTargetCalories - newDietManagerTargetCalories) < 0.01) {
        std::cout << "✓ Updated target calories match between User and DietManager!" << std::endl;
    } else {
        std::cout << "✗ Updated target calories don't match between User and DietManager!" << std::endl;
        return 1;
    }
    
    // Change calorie calculation method
    std::cout << "\nChanging calorie calculation method..." << std::endl;
    std::string newMethod = "Mifflin-St Jeor";
    dietManager.setCalorieCalculationMethod(newMethod);
    
    // Also update the user's profile method in the UserManager (simulating our fix)
    user->getProfile().setCalorieCalculationMethod(newMethod);
    userManager.saveToFile();
    
    // Calculate calories with new method
    double methodUserTargetCalories = user->getProfile().calculateTargetCalories();
    double methodDietManagerTargetCalories = dietManager.calculateTargetCalories();
    
    std::cout << "User profile target calories (new method): " << methodUserTargetCalories << std::endl;
    std::cout << "Diet manager target calories (new method): " << methodDietManagerTargetCalories << std::endl;
    
    // Test if the method calories match
    if (abs(methodUserTargetCalories - methodDietManagerTargetCalories) < 0.01) {
        std::cout << "✓ Method target calories match between User and DietManager!" << std::endl;
    } else {
        std::cout << "✗ Method target calories don't match between User and DietManager!" << std::endl;
        return 1;
    }
    
    std::cout << "\nAll profile synchronization tests passed!" << std::endl;
    
    // Clean up test files
    std::remove("test_users.txt");
    std::remove("test_foods.txt");
    std::remove("test_log.txt");
    
    return 0;
}