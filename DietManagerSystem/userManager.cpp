#include "userManager.h"
#include <fstream>
#include <sstream>
#include <iostream>
#include <functional>

// A simple hash function for passwords
// In a real application, you would use a proper cryptographic hash like bcrypt or Argon2
std::string UserManager::hashPassword(const std::string& password) const {
    std::hash<std::string> hasher;
    size_t hash = hasher(password);
    std::stringstream ss;
    ss << hash;
    return ss.str();
}

UserManager::UserManager(const std::string& filePath)
    : usersFilePath(filePath), currentUser(nullptr) {
}

UserManager::~UserManager() {
    saveToFile();
}

bool UserManager::loadFromFile() {
    std::ifstream file(usersFilePath);
    if (!file) {
        return false;
    }
    
    users.clear();
    userIndices.clear();
    
    std::string line;
    while (std::getline(file, line)) {
        if (line.empty()) continue;
        
        try {
            User user = User::fromString(line);
            userIndices[user.getUsername()] = users.size();
            users.push_back(user);
        } catch (const std::exception& e) {
            std::cerr << "Error parsing user data: " << e.what() << std::endl;
            continue;
        }
    }
    
    return true;
}

bool UserManager::saveToFile() const {
    std::ofstream file(usersFilePath);
    if (!file) {
        return false;
    }
    
    for (const auto& user : users) {
        file << user.toString() << std::endl;
    }
    
    return true;
}

bool UserManager::registerUser(const std::string& username, const std::string& password) {
    if (userExists(username)) {
        return false; // Username already exists
    }
    
    std::string hashedPassword = hashPassword(password);
    User newUser(username, hashedPassword);
    
    userIndices[username] = users.size();
    users.push_back(newUser);
    
    saveToFile();
    return true;
}

bool UserManager::loginUser(const std::string& username, const std::string& password) {
    if (!userExists(username)) {
        return false;
    }
    
    size_t userIndex = userIndices[username];
    std::string hashedPassword = hashPassword(password);
    
    if (users[userIndex].getPasswordHash() == hashedPassword) {
        currentUser = &users[userIndex];
        return true;
    }
    
    return false;
}

User* UserManager::getCurrentUser() const {
    return currentUser;
}

bool UserManager::userExists(const std::string& username) const {
    return userIndices.find(username) != userIndices.end();
}

void UserManager::logout() {
    currentUser = nullptr;
}