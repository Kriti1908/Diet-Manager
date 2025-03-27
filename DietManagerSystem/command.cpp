#include "command.h"
#include "dietManager.h"
#include <iostream>
#include <sstream>

// C_AddFoodCommand implementation
C_AddFoodCommand::C_AddFoodCommand(C_DietManager* manager, const std::string& foodName, double servings)
    : manager(manager), foodName(foodName), servings(servings) {
}

void C_AddFoodCommand::execute() {
    manager->getCurrentDailyLog().addFood(foodName, servings);
}

void C_AddFoodCommand::undo() {
    manager->getCurrentDailyLog().removeFood(foodName);
}

std::string C_AddFoodCommand::getDescription() const {
    std::stringstream ss;
    ss << "Add " << servings << " serving(s) of " << foodName;
    return ss.str();
}

// C_RemoveFoodCommand implementation
C_RemoveFoodCommand::C_RemoveFoodCommand(C_DietManager* manager, const std::string& foodName)
    : manager(manager), foodName(foodName) {
    
    // Store the original servings for undo
    const auto& consumedFoods = manager->getCurrentDailyLog().getConsumedFoods();
    auto it = consumedFoods.find(foodName);
    if (it != consumedFoods.end()) {
        originalServings = it->second;
    } else {
        originalServings = 0.0;
    }
}

void C_RemoveFoodCommand::execute() {
    manager->getCurrentDailyLog().removeFood(foodName);
}

void C_RemoveFoodCommand::undo() {
    if (originalServings > 0.0) {
        manager->getCurrentDailyLog().addFood(foodName, originalServings);
    }
}

std::string C_RemoveFoodCommand::getDescription() const {
    std::stringstream ss;
    ss << "Remove " << foodName;
    return ss.str();
}

// C_UpdateProfileCommand implementation
C_UpdateProfileCommand::C_UpdateProfileCommand(
    C_DietManager* manager, 
    const std::string& gender, 
    double height, 
    double weight, 
    int age, 
    const std::string& activityLevel)
    : manager(manager), 
      gender(gender), 
      height(height), 
      weight(weight), 
      age(age), 
      activityLevel(activityLevel) {
    
    // Store the original profile values for undo
    oldGender = manager->getDietProfile().getGender();
    oldHeight = manager->getDietProfile().getHeight();
    oldWeight = manager->getDietProfile().getWeight();
    oldAge = manager->getDietProfile().getAge();
    oldActivityLevel = manager->getDietProfile().getActivityLevel();
}

void C_UpdateProfileCommand::execute() {
    manager->getDietProfile().updateProfile(gender, height, weight, age, activityLevel);
    manager->getCurrentDailyLog().setProfileData(gender, height, weight, age, activityLevel);
}

void C_UpdateProfileCommand::undo() {
    manager->getDietProfile().updateProfile(oldGender, oldHeight, oldWeight, oldAge, oldActivityLevel);
    manager->getCurrentDailyLog().setProfileData(oldGender, oldHeight, oldWeight, oldAge, oldActivityLevel);
}

std::string C_UpdateProfileCommand::getDescription() const {
    std::stringstream ss;
    ss << "Update profile: " << gender << ", " << height << "cm, " 
       << weight << "kg, " << age << "y, " << activityLevel;
    return ss.str();
}

// C_CommandManager implementation
void C_CommandManager::executeCommand(std::shared_ptr<I_Command> command) {
    command->execute();
    history.push(command);
    
    // Clear redo stack when a new command is executed
    while (!redoStack.empty()) {
        redoStack.pop();
    }
}

bool C_CommandManager::canUndo() const {
    return !history.empty();
}

bool C_CommandManager::canRedo() const {
    return !redoStack.empty();
}

void C_CommandManager::undo() {
    if (canUndo()) {
        auto command = history.top();
        history.pop();
        
        command->undo();
        redoStack.push(command);
    } else {
        std::cout << "Nothing to undo." << std::endl;
    }
}

void C_CommandManager::redo() {
    if (canRedo()) {
        auto command = redoStack.top();
        redoStack.pop();
        
        command->execute();
        history.push(command);
    } else {
        std::cout << "Nothing to redo." << std::endl;
    }
}

void C_CommandManager::clearHistory() {
    while (!history.empty()) {
        history.pop();
    }
    
    while (!redoStack.empty()) {
        redoStack.pop();
    }
}

std::vector<std::string> C_CommandManager::getCommandHistory() const {
    std::vector<std::string> descriptions;
    
    // We can't iterate through a stack directly, so we need to copy it
    std::stack<std::shared_ptr<I_Command>> tempStack = history;
    
    while (!tempStack.empty()) {
        descriptions.push_back(tempStack.top()->getDescription());
        tempStack.pop();
    }
    
    // Reverse the descriptions to get the oldest first
    std::reverse(descriptions.begin(), descriptions.end());
    
    return descriptions;
}
