#include "dietManager.h"
#include "userManager.h"
#include "utils.h"
#include <iostream>
#include <string>
#include <vector>
#include <limits>
#include <fstream>

// Forward declarations of menu functions
void displayMainMenu();
void handleFoodDatabaseMenu(C_DietManager& manager);
void handleDailyLogMenu(C_DietManager& manager);
void handleProfileMenu(C_DietManager& manager, UserManager& userManager);
void handleCommandHistoryMenu(C_DietManager& manager);
void displayLoginMenu();
bool handleLogin(UserManager& userManager);
bool handleSignup(UserManager& userManager);
bool setupUserProfile(User* user, C_DietManager& manager);
void clearScreen();

// Helper functions
void displayFoodInfo(const std::shared_ptr<A_Food>& food);
void displayAllFoods(C_DietManager& manager);
void searchFoods(C_DietManager& manager);
void addBasicFood(C_DietManager& manager);
void addCompositeFood(C_DietManager& manager);
void showFoodSummary(C_DietManager& manager);
void addFoodToLog(C_DietManager& manager);
void removeFoodFromLog(C_DietManager& manager);
void displayDailyLogSummary(C_DietManager& manager);
void changeDate(C_DietManager& manager);
void updateProfile(C_DietManager& manager, UserManager& userManager);
void changeCalorieMethod(C_DietManager& manager, UserManager& userManager);
void displayProfileSummary(C_DietManager& manager);

// Main function
int main() {
    // Create files if they don't exist
    std::ofstream foodsFile("foods.txt", std::ios::app);
    foodsFile.close();
    
    std::ofstream logFile("log.txt", std::ios::app);
    logFile.close();
    
    std::ofstream usersFile("users.txt", std::ios::app);
    usersFile.close();
    
    // Initialize user manager
    UserManager userManager("users.txt");
    userManager.loadFromFile();
    
    // Handle login/signup until successful
    bool loggedIn = false;
    
    while (!loggedIn) {
        displayLoginMenu();
        int choice;
        std::cout << "Enter your choice: ";
        std::cin >> choice;
        
        // Clear input buffer
        std::cin.clear();
        std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
        
        switch (choice) {
            case 1: // Login
                loggedIn = handleLogin(userManager);
                break;
            case 2: // Signup
                loggedIn = handleSignup(userManager);
                break;
            case 3: // Exit
                std::cout << "Exiting YADA Diet Manager. Goodbye!" << std::endl;
                return 0;
            default:
                std::cout << "Invalid choice. Please try again." << std::endl;
                break;
        }
    }
    
    // Initialize diet manager
    C_DietManager manager("foods.txt", "log.txt");
    
    // Load database and daily log
    if (!manager.loadFoodDatabase()) {
        std::cout << "Warning: Could not load food database. Starting with empty database." << std::endl;
    }
    
    if (!manager.loadDailyLog()) {
        std::cout << "Warning: Could not load daily log. Starting with empty log." << std::endl;
    }
    
    // Copy user profile data to the diet manager
    User* currentUser = userManager.getCurrentUser();
    if (currentUser && currentUser->isProfileCompleted()) {
        const C_DietProfile& userProfile = currentUser->getProfile();
        manager.updateProfile(
            userProfile.getGender(),
            userProfile.getHeight(),
            userProfile.getWeight(),
            userProfile.getAge(),
            userProfile.getActivityLevel()
        );
        manager.setCalorieCalculationMethod(userProfile.getCalorieCalculationMethod());
    }
    
    int choice = 0;
    
    do {
        displayMainMenu();
        std::cout << "Enter your choice: ";
        std::cin >> choice;
        
        // Clear input buffer
        std::cin.clear();
        std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
        
        switch (choice) {
            case 1:
                handleFoodDatabaseMenu(manager);
                break;
            case 2:
                handleDailyLogMenu(manager);
                break;
            case 3:
                handleProfileMenu(manager, userManager);
                break;
            case 4:
                handleCommandHistoryMenu(manager);
                break;
            case 5:
                manager.saveFoodDatabase();
                manager.saveDailyLog();
                std::cout << "Database and log saved successfully." << std::endl;
                break;
            case 6:
                // Exit - save automatically
                manager.saveFoodDatabase();
                manager.saveDailyLog();
                std::cout << "Exiting YADA Diet Manager. Goodbye!" << std::endl;
                break;
            default:
                std::cout << "Invalid choice. Please try again." << std::endl;
                break;
        }
        
        std::cout << std::endl;
    } while (choice != 6);
    
    return 0;
}

void displayMainMenu() {
    std::cout << "=======================================" << std::endl;
    std::cout << "    YADA - Yet Another Diet Assistant  " << std::endl;
    std::cout << "=======================================" << std::endl;
    std::cout << "1. Food Database Management" << std::endl;
    std::cout << "2. Daily Log Management" << std::endl;
    std::cout << "3. Diet Profile Management" << std::endl;
    std::cout << "4. Command History (Undo/Redo)" << std::endl;
    std::cout << "5. Save Changes" << std::endl;
    std::cout << "6. Exit" << std::endl;
}

void handleFoodDatabaseMenu(C_DietManager& manager) {
    int choice = 0;
    
    do {
        std::cout << "\n--- Food Database Management ---" << std::endl;
        std::cout << "1. Display All Foods" << std::endl;
        std::cout << "2. Search Foods" << std::endl;
        std::cout << "3. Add Basic Food" << std::endl;
        std::cout << "4. Add Composite Food" << std::endl;
        std::cout << "5. Save Food Database" << std::endl;
        std::cout << "6. Return to Main Menu" << std::endl;
        std::cout << "Enter your choice: ";
        std::cin >> choice;
        
        // Clear input buffer
        std::cin.clear();
        std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
        
        switch (choice) {
            case 1:
                displayAllFoods(manager);
                break;
            case 2:
                searchFoods(manager);
                break;
            case 3:
                addBasicFood(manager);
                break;
            case 4:
                addCompositeFood(manager);
                break;
            case 5:
                if (manager.saveFoodDatabase()) {
                    std::cout << "Food database saved successfully." << std::endl;
                } else {
                    std::cout << "Failed to save food database." << std::endl;
                }
                break;
            case 6:
                // Return to main menu
                break;
            default:
                std::cout << "Invalid choice. Please try again." << std::endl;
                break;
        }
    } while (choice != 6);
}

void handleDailyLogMenu(C_DietManager& manager) {
    int choice = 0;
    
    do {
        std::cout << "\n--- Daily Log Management ---" << std::endl;
        std::cout << "Current Date: " << manager.getCurrentDailyLog().getDate() << std::endl;
        std::cout << "Total Calories: " << manager.calculateTotalCalories() << std::endl;
        std::cout << "Target Calories: " << manager.calculateTargetCalories() << std::endl;
        std::cout << "Remaining Calories: " << manager.calculateRemainingCalories() << std::endl;
        std::cout << "1. Show Food Summary" << std::endl;
        std::cout << "2. Add Food to Log" << std::endl;
        std::cout << "3. Remove Food from Log" << std::endl;
        std::cout << "4. Change Date" << std::endl;
        std::cout << "5. Save Daily Log" << std::endl;
        std::cout << "6. Return to Main Menu" << std::endl;
        std::cout << "Enter your choice: ";
        std::cin >> choice;
        
        // Clear input buffer
        std::cin.clear();
        std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
        
        switch (choice) {
            case 1:
                showFoodSummary(manager);
                break;
            case 2:
                addFoodToLog(manager);
                break;
            case 3:
                removeFoodFromLog(manager);
                break;
            case 4:
                changeDate(manager);
                break;
            case 5:
                if (manager.saveDailyLog()) {
                    std::cout << "Daily log saved successfully." << std::endl;
                } else {
                    std::cout << "Failed to save daily log." << std::endl;
                }
                break;
            case 6:
                // Return to main menu
                break;
            default:
                std::cout << "Invalid choice. Please try again." << std::endl;
                break;
        }
    } while (choice != 6);
}

void handleProfileMenu(C_DietManager& manager, UserManager& userManager) {
    int choice = 0;
    
    do {
        std::cout << "\n--- Diet Profile Management ---" << std::endl;
        displayProfileSummary(manager);
        std::cout << "1. Update Profile" << std::endl;
        std::cout << "2. Change Calorie Calculation Method" << std::endl;
        std::cout << "3. Return to Main Menu" << std::endl;
        std::cout << "Enter your choice: ";
        std::cin >> choice;
        
        // Clear input buffer
        std::cin.clear();
        std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
        
        switch (choice) {
            case 1:
                updateProfile(manager, userManager);
                break;
            case 2:
                changeCalorieMethod(manager, userManager);
                break;
            case 3:
                // Return to main menu
                break;
            default:
                std::cout << "Invalid choice. Please try again." << std::endl;
                break;
        }
    } while (choice != 3);
}

void handleCommandHistoryMenu(C_DietManager& manager) {
    int choice = 0;
    
    do {
        std::cout << "\n--- Command History ---" << std::endl;
        
        // Display command history
        auto history = manager.getCommandHistory();
        if (history.empty()) {
            std::cout << "No commands in history." << std::endl;
        } else {
            std::cout << "Command History:" << std::endl;
            for (size_t i = 0; i < history.size(); ++i) {
                std::cout << (i + 1) << ". " << history[i] << std::endl;
            }
        }
        
        std::cout << "\n1. Undo Last Command" << std::endl;
        std::cout << "2. Redo Last Undone Command" << std::endl;
        std::cout << "3. Return to Main Menu" << std::endl;
        std::cout << "Enter your choice: ";
        std::cin >> choice;
        
        // Clear input buffer
        std::cin.clear();
        std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
        
        switch (choice) {
            case 1:
                manager.undo();
                std::cout << "Command undone." << std::endl;
                break;
            case 2:
                manager.redo();
                std::cout << "Command redone." << std::endl;
                break;
            case 3:
                // Return to main menu
                break;
            default:
                std::cout << "Invalid choice. Please try again." << std::endl;
                break;
        }
    } while (choice != 3);
}

void displayFoodInfo(const std::shared_ptr<A_Food>& food) {
    std::cout << "Name: " << food->getName() << std::endl;
    
    std::cout << "Keywords: ";
    const auto& keywords = food->getKeywords();
    for (size_t i = 0; i < keywords.size(); ++i) {
        std::cout << keywords[i];
        if (i < keywords.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << std::endl;
    
    std::cout << "Calories per serving: " << food->getCaloriesPerServing() << std::endl;
    
    auto compositeFood = std::dynamic_pointer_cast<C_CompositeFood>(food);
    if (compositeFood) {
        std::cout << "Component foods:" << std::endl;
        const auto& components = compositeFood->getComponents();
        for (const auto& [component, servings] : components) {
            std::cout << "  - " << component->getName() << " (" << servings << " servings)" << std::endl;
        }
    }
}

void displayAllFoods(C_DietManager& manager) {
    const auto& foods = manager.getFoodDatabase().getAllFoods();
    
    if (foods.empty()) {
        std::cout << "No foods in the database." << std::endl;
        return;
    }
    
    std::cout << "\n--- All Foods ---" << std::endl;
    std::cout << "Basic Foods:" << std::endl;
    for (const auto& food : foods) {
        if (dynamic_cast<C_BasicFood*>(food.get())) {
            std::cout << "- " << food->getName() << " (" << food->getCaloriesPerServing() << " cal/serving)" << std::endl;
        }
    }
    
    std::cout << "\nComposite Foods:" << std::endl;
    for (const auto& food : foods) {
        if (dynamic_cast<C_CompositeFood*>(food.get())) {
            std::cout << "- " << food->getName() << " (" << food->getCaloriesPerServing() << " cal/serving)" << std::endl;
        }
    }
}

void searchFoods(C_DietManager& manager) {
    std::string keywordStr;
    std::cout << "Enter search keywords (comma-separated): ";
    std::getline(std::cin, keywordStr);
    
    auto keywords = split(keywordStr, ',');
    for (auto& keyword : keywords) {
        // Trim whitespace
        keyword = keyword.substr(keyword.find_first_not_of(" \t"));
        keyword = keyword.substr(0, keyword.find_last_not_of(" \t") + 1);
    }
    
    char matchChoice;
    std::cout << "Match (A)ll keywords or (S)ome keywords? [A/S]: ";
    std::cin >> matchChoice;
    std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
    
    bool matchAll = (toupper(matchChoice) == 'A');
    
    auto results = manager.findFoods(keywords, matchAll);
    
    if (results.empty()) {
        std::cout << "No foods found matching the given keywords." << std::endl;
        return;
    }
    
    std::cout << "\n--- Search Results ---" << std::endl;
    for (size_t i = 0; i < results.size(); ++i) {
        std::cout << (i + 1) << ". " << results[i]->getName() 
                 << " (" << results[i]->getCaloriesPerServing() << " cal/serving)" << std::endl;
    }
    
    int choice = 0;
    std::cout << "\nEnter a number to view details (0 to cancel): ";
    std::cin >> choice;
    std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
    
    if (choice > 0 && choice <= static_cast<int>(results.size())) {
        std::cout << "\n--- Food Details ---" << std::endl;
        displayFoodInfo(results[choice - 1]);
    }
}

void addBasicFood(C_DietManager& manager) {
    std::string name;
    std::string keywordStr;
    double calories;
    
    std::cout << "Enter food name: ";
    std::getline(std::cin, name);
    
    std::cout << "Enter keywords (comma-separated): ";
    std::getline(std::cin, keywordStr);
    
    std::cout << "Enter calories per serving: ";
    std::cin >> calories;
    std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
    
    auto keywords = split(keywordStr, ',');
    for (auto& keyword : keywords) {
        // Trim whitespace
        keyword = keyword.substr(keyword.find_first_not_of(" \t"));
        keyword = keyword.substr(0, keyword.find_last_not_of(" \t") + 1);
    }
    
    if (manager.addBasicFood(name, keywords, calories)) {
        std::cout << "Basic food added successfully." << std::endl;
    } else {
        std::cout << "Failed to add food. A food with the same name might already exist." << std::endl;
    }
}

void addCompositeFood(C_DietManager& manager) {
    std::string name;
    std::string keywordStr;
    
    std::cout << "Enter composite food name: ";
    std::getline(std::cin, name);
    
    std::cout << "Enter keywords (comma-separated): ";
    std::getline(std::cin, keywordStr);
    
    auto keywords = split(keywordStr, ',');
    for (auto& keyword : keywords) {
        // Trim whitespace
        keyword = keyword.substr(keyword.find_first_not_of(" \t"));
        keyword = keyword.substr(0, keyword.find_last_not_of(" \t") + 1);
    }
    
    std::vector<std::pair<std::string, double>> components;
    
    std::cout << "\nAdding components to " << name << std::endl;
    std::cout << "Enter 'done' as food name when finished." << std::endl;
    
    while (true) {
        std::string componentName;
        double servings;
        
        std::cout << "Enter component food name: ";
        std::getline(std::cin, componentName);
        
        if (componentName == "done") {
            break;
        }
        
        auto food = manager.findFoodByName(componentName);
        if (!food) {
            std::cout << "Food not found: " << componentName << std::endl;
            continue;
        }
        
        std::cout << "Enter number of servings: ";
        std::cin >> servings;
        std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
        
        if (servings <= 0) {
            std::cout << "Servings must be positive." << std::endl;
            continue;
        }
        
        components.push_back(std::make_pair(componentName, servings));
        std::cout << "Added " << servings << " serving(s) of " << componentName << std::endl;
    }
    
    if (components.empty()) {
        std::cout << "Composite food must have at least one component. Aborting." << std::endl;
        return;
    }
    
    if (manager.addCompositeFood(name, keywords, components)) {
        std::cout << "Composite food added successfully." << std::endl;
    } else {
        std::cout << "Failed to add composite food." << std::endl;
    }
}

void showFoodSummary(C_DietManager& manager) {
    const auto& consumedFoods = manager.getCurrentDailyLog().getConsumedFoods();
    
    if (consumedFoods.empty()) {
        std::cout << "No foods consumed on " << manager.getCurrentDailyLog().getDate() << std::endl;
        return;
    }
    
    std::cout << "\n--- Foods Consumed on " << manager.getCurrentDailyLog().getDate() << " ---" << std::endl;
    double totalCalories = 0.0;
    
    for (const auto& [foodName, servings] : consumedFoods) {
        auto food = manager.findFoodByName(foodName);
        if (food) {
            double calories = food->getCaloriesPerServing() * servings;
            totalCalories += calories;
            std::cout << "- " << foodName << " (" << servings << " serving(s), " << calories << " cal)" << std::endl;
        } else {
            std::cout << "- " << foodName << " (" << servings << " serving(s), unknown calories)" << std::endl;
        }
    }
    
    std::cout << "\nTotal calories: " << totalCalories << std::endl;
    std::cout << "Target calories: " << manager.calculateTargetCalories() << std::endl;
    
    double remaining = manager.calculateRemainingCalories();
    if (remaining < 0) {
        std::cout << "Calories over target: " << -remaining << std::endl;
    } else {
        std::cout << "Calories remaining: " << remaining << std::endl;
    }
}

void addFoodToLog(C_DietManager& manager) {
    std::string foodName;
    double servings;
    
    std::cout << "Enter food name (or search term): ";
    std::getline(std::cin, foodName);
    
    auto food = manager.findFoodByName(foodName);
    
    if (!food) {
        // Try to search for the food
        auto results = manager.findFoods(std::vector<std::string>{foodName}, false);
        
        if (results.empty()) {
            std::cout << "No foods found matching: " << foodName << std::endl;
            return;
        }
        
        std::cout << "\n--- Found Foods ---" << std::endl;
        for (size_t i = 0; i < results.size(); ++i) {
            std::cout << (i + 1) << ". " << results[i]->getName() 
                     << " (" << results[i]->getCaloriesPerServing() << " cal/serving)" << std::endl;
        }
        
        int choice = 0;
        std::cout << "Select a food (0 to cancel): ";
        std::cin >> choice;
        std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
        
        if (choice <= 0 || choice > static_cast<int>(results.size())) {
            std::cout << "Cancelled." << std::endl;
            return;
        }
        
        food = results[choice - 1];
        foodName = food->getName();
    }
    
    std::cout << "Enter number of servings: ";
    std::cin >> servings;
    std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
    
    if (servings <= 0) {
        std::cout << "Servings must be positive." << std::endl;
        return;
    }
    
    manager.addFoodToLog(foodName, servings);
    std::cout << "Added " << servings << " serving(s) of " << foodName << " to the log." << std::endl;
}

void removeFoodFromLog(C_DietManager& manager) {
    const auto& consumedFoods = manager.getCurrentDailyLog().getConsumedFoods();
    
    if (consumedFoods.empty()) {
        std::cout << "No foods in the log to remove." << std::endl;
        return;
    }
    
    std::cout << "\n--- Foods in Log ---" << std::endl;
    int index = 1;
    std::vector<std::string> foodNames;
    
    for (const auto& [foodName, servings] : consumedFoods) {
        std::cout << index << ". " << foodName << " (" << servings << " serving(s))" << std::endl;
        foodNames.push_back(foodName);
        index++;
    }
    
    int choice = 0;
    std::cout << "Enter the number of the food to remove (0 to cancel): ";
    std::cin >> choice;
    std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
    
    if (choice <= 0 || choice > static_cast<int>(foodNames.size())) {
        std::cout << "Cancelled." << std::endl;
        return;
    }
    
    std::string foodName = foodNames[choice - 1];
    manager.removeFoodFromLog(foodName);
    std::cout << "Removed " << foodName << " from the log." << std::endl;
}

void changeDate(C_DietManager& manager) {
    std::string date;
    std::cout << "Enter date (YYYY-MM-DD): ";
    std::getline(std::cin, date);
    
    if (!isValidDateFormat(date)) {
        std::cout << "Invalid date format. Please use YYYY-MM-DD." << std::endl;
        return;
    }
    
    manager.setLogDate(date);
    std::cout << "Changed to date: " << date << std::endl;
}

void updateProfile(C_DietManager& manager, UserManager& userManager) {
    std::string gender;
    double height, weight;
    int age;
    std::string activityLevel;
    
    std::cout << "Enter gender (male/female): ";
    std::getline(std::cin, gender);
    
    if (gender != "male" && gender != "female") {
        std::cout << "Invalid gender. Must be 'male' or 'female'." << std::endl;
        return;
    }
    
    std::cout << "Enter height in cm: ";
    std::cin >> height;
    
    std::cout << "Enter weight in kg: ";
    std::cin >> weight;
    
    std::cout << "Enter age: ";
    std::cin >> age;
    std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
    
    std::cout << "Enter activity level (sedentary, light, moderate, active, very active): ";
    std::getline(std::cin, activityLevel);
    
    if (activityLevel != "sedentary" && activityLevel != "light" && 
        activityLevel != "moderate" && activityLevel != "active" && 
        activityLevel != "very active") {
        std::cout << "Invalid activity level." << std::endl;
        return;
    }
    
    manager.updateProfile(gender, height, weight, age, activityLevel);
    
    // Also update the user's profile in the UserManager
    User* currentUser = userManager.getCurrentUser();
    if (currentUser) {
        currentUser->getProfile().updateProfile(gender, height, weight, age, activityLevel);
        userManager.saveToFile(); // Save the changes to file
    }
    
    std::cout << "Profile updated successfully." << std::endl;
}

void changeCalorieMethod(C_DietManager& manager, UserManager& userManager) {
    auto methodNames = manager.getAvailableMethodNames();
    
    std::cout << "Available calculation methods:" << std::endl;
    for (size_t i = 0; i < methodNames.size(); ++i) {
        std::cout << (i + 1) << ". " << methodNames[i] << std::endl;
    }
    
    int choice = 0;
    std::cout << "Select a method: ";
    std::cin >> choice;
    std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
    
    if (choice <= 0 || choice > static_cast<int>(methodNames.size())) {
        std::cout << "Invalid choice." << std::endl;
        return;
    }
    
    std::string methodName = methodNames[choice - 1];
    manager.setCalorieCalculationMethod(methodName);
    
    // Also update the user's profile method in the UserManager
    User* currentUser = userManager.getCurrentUser();
    if (currentUser) {
        currentUser->getProfile().setCalorieCalculationMethod(methodName);
        userManager.saveToFile(); // Save the changes to file
    }
    
    std::cout << "Changed calculation method to: " << methodName << std::endl;
}

void displayProfileSummary(C_DietManager& manager) {
    const auto& profile = manager.getDietProfile();
    
    std::cout << "Gender: " << profile.getGender() << std::endl;
    std::cout << "Height: " << profile.getHeight() << " cm" << std::endl;
    std::cout << "Weight: " << profile.getWeight() << " kg" << std::endl;
    std::cout << "Age: " << profile.getAge() << " years" << std::endl;
    std::cout << "Activity Level: " << profile.getActivityLevel() << std::endl;
    std::cout << "Target Calories: " << manager.calculateTargetCalories() << std::endl;
}

void displayLoginMenu() {
    std::cout << "=======================================" << std::endl;
    std::cout << "    YADA - Yet Another Diet Assistant  " << std::endl;
    std::cout << "=======================================" << std::endl;
    std::cout << "1. Login" << std::endl;
    std::cout << "2. Sign Up" << std::endl;
    std::cout << "3. Exit" << std::endl;
}

bool handleLogin(UserManager& userManager) {
    std::string username, password;
    int maxAttempts = 3;
    int attempts = 0;
    
    while (attempts < maxAttempts) {
        std::cout << "\n--- Login ---" << std::endl;
        if (attempts > 0) {
            std::cout << "Attempt " << attempts + 1 << " of " << maxAttempts << std::endl;
        }
        
        std::cout << "Username (or type 'cancel' to go back): ";
        std::getline(std::cin, username);
        
        if (username == "cancel") {
            return false;
        }
        
        std::cout << "Password: ";
        std::getline(std::cin, password);
        
        if (userManager.loginUser(username, password)) {
            User* user = userManager.getCurrentUser();
            std::cout << "\nWelcome back, " << user->getUsername() << "!" << std::endl;
            
            if (!user->isProfileCompleted()) {
                std::cout << "\nYour profile is incomplete. Please set up your profile." << std::endl;
                // Create a temporary diet manager for profile setup
                C_DietManager tempManager("foods.txt", "log.txt");
                if (!setupUserProfile(user, tempManager)) {
                    std::cout << "Profile setup cancelled. Please complete your profile to use YADA." << std::endl;
                    userManager.logout();
                    return false;
                }
            }
            
            return true;
        } else {
            std::cout << "Invalid username or password." << std::endl;
            attempts++;
            
            if (attempts < maxAttempts) {
                std::cout << "Please try again or type 'cancel' on the username prompt to go back." << std::endl;
            } else {
                std::cout << "Maximum login attempts reached. Returning to main menu." << std::endl;
            }
        }
    }
    
    return false;
}

bool handleSignup(UserManager& userManager) {
    std::string username, password, confirmPassword;
    bool validUsername = false;
    bool validPassword = false;
    
    // Get valid username
    while (!validUsername) {
        std::cout << "\n--- Sign Up ---" << std::endl;
        std::cout << "Username (or type 'cancel' to go back): ";
        std::getline(std::cin, username);
        
        if (username == "cancel") {
            return false;
        }
        
        if (userManager.userExists(username)) {
            std::cout << "Username already exists. Please choose another one." << std::endl;
        } else {
            validUsername = true;
        }
    }
    
    // Get valid password
    while (!validPassword) {
        std::cout << "Password (or type 'cancel' to go back): ";
        std::getline(std::cin, password);
        
        if (password == "cancel") {
            return false;
        }
        
        std::cout << "Confirm Password: ";
        std::getline(std::cin, confirmPassword);
        
        if (password != confirmPassword) {
            std::cout << "Passwords do not match. Please try again." << std::endl;
        } else {
            validPassword = true;
        }
    }
    
    if (userManager.registerUser(username, password)) {
        std::cout << "\nRegistration successful!" << std::endl;
        
        // Login the user automatically
        userManager.loginUser(username, password);
        User* user = userManager.getCurrentUser();
        
        std::cout << "\nWelcome, " << user->getUsername() << "!" << std::endl;
        std::cout << "Please set up your profile to get started." << std::endl;
        
        // Create a temporary diet manager for profile setup
        C_DietManager tempManager("foods.txt", "log.txt");
        if (!setupUserProfile(user, tempManager)) {
            std::cout << "Profile setup cancelled. Please complete your profile to use YADA." << std::endl;
            userManager.logout();
            return false;
        }
        
        return true;
    } else {
        std::cout << "Registration failed. Please try again." << std::endl;
        return false;
    }
}

bool setupUserProfile(User* user, C_DietManager& manager) {
    std::string gender;
    double height = 0, weight = 0;
    int age = 0;
    std::string activityLevel;
    
    std::cout << "\n--- Profile Setup ---" << std::endl;
    std::cout << "Please provide your information to calculate diet targets." << std::endl;
    
    // Gender
    while (true) {
        std::cout << "Gender (M/F): ";
        std::getline(std::cin, gender);
        
        if (gender == "M" || gender == "m" || gender == "F" || gender == "f") {
            gender = (gender == "M" || gender == "m") ? "Male" : "Female";
            break;
        } else {
            std::cout << "Invalid input. Please enter 'M' for Male or 'F' for Female." << std::endl;
        }
    }
    
    // Height
    while (true) {
        std::cout << "Height (in cm): ";
        if (!(std::cin >> height) || height <= 0) {
            std::cin.clear();
            std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
            std::cout << "Invalid input. Please enter a positive number." << std::endl;
        } else {
            std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
            break;
        }
    }
    
    // Weight
    while (true) {
        std::cout << "Weight (in kg): ";
        if (!(std::cin >> weight) || weight <= 0) {
            std::cin.clear();
            std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
            std::cout << "Invalid input. Please enter a positive number." << std::endl;
        } else {
            std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
            break;
        }
    }
    
    // Age
    while (true) {
        std::cout << "Age: ";
        if (!(std::cin >> age) || age <= 0) {
            std::cin.clear();
            std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
            std::cout << "Invalid input. Please enter a positive number." << std::endl;
        } else {
            std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
            break;
        }
    }
    
    // Activity Level
    std::cout << "Activity Levels:" << std::endl;
    std::cout << "1. Sedentary (little or no exercise)" << std::endl;
    std::cout << "2. Lightly active (light exercise/sports 1-3 days/week)" << std::endl;
    std::cout << "3. Moderately active (moderate exercise/sports 3-5 days/week)" << std::endl;
    std::cout << "4. Very active (hard exercise/sports 6-7 days a week)" << std::endl;
    std::cout << "5. Extra active (very hard exercise/physical job/training twice a day)" << std::endl;
    
    int activityChoice = 0;
    while (true) {
        std::cout << "Enter activity level (1-5): ";
        if (!(std::cin >> activityChoice) || activityChoice < 1 || activityChoice > 5) {
            std::cin.clear();
            std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
            std::cout << "Invalid input. Please enter a number between 1 and 5." << std::endl;
        } else {
            std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
            break;
        }
    }
    
    // Map activity level to string
    switch (activityChoice) {
        case 1: activityLevel = "Sedentary"; break;
        case 2: activityLevel = "Light"; break;
        case 3: activityLevel = "Moderate"; break;
        case 4: activityLevel = "High"; break;
        case 5: activityLevel = "Very High"; break;
        default: activityLevel = "Moderate"; break;
    }
    
    // Calorie Calculation Method
    std::vector<std::string> methods = manager.getAvailableMethodNames();
    std::cout << "\nCalorie Calculation Methods:" << std::endl;
    for (size_t i = 0; i < methods.size(); ++i) {
        std::cout << (i + 1) << ". " << methods[i] << std::endl;
    }
    
    int methodChoice = 0;
    while (true) {
        std::cout << "Enter method choice (1-" << methods.size() << "): ";
        if (!(std::cin >> methodChoice) || methodChoice < 1 || methodChoice > static_cast<int>(methods.size())) {
            std::cin.clear();
            std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
            std::cout << "Invalid input. Please enter a valid choice." << std::endl;
        } else {
            std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
            break;
        }
    }
    
    // Update the profile in the user
    user->getProfile().updateProfile(gender, height, weight, age, activityLevel);
    user->getProfile().setCalorieCalculationMethod(methods[methodChoice - 1]);
    user->setProfileCompleted(true);
    
    // Calculate and show target calories
    double targetCalories = user->getProfile().calculateTargetCalories();
    std::cout << "\nProfile setup complete!" << std::endl;
    std::cout << "Your target calorie intake: " << targetCalories << " calories per day." << std::endl;
    
    return true;
}

void clearScreen() {
    // This is a cross-platform way to clear the screen
    std::cout << std::string(100, '\n');
}
