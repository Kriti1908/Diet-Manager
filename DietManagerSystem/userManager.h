#pragma once

#include "user.h"
#include <string>
#include <vector>
#include <unordered_map>

class UserManager {
private:
    std::vector<User> users;
    std::unordered_map<std::string, size_t> userIndices; // Maps username to index in users vector
    std::string usersFilePath;
    User* currentUser;
    
    // Helper method for hashing passwords
    std::string hashPassword(const std::string& password) const;
    
public:
    UserManager(const std::string& filePath);
    ~UserManager();
    
    // File operations
    bool loadFromFile();
    bool saveToFile() const;
    
    // User management
    bool registerUser(const std::string& username, const std::string& password);
    bool loginUser(const std::string& username, const std::string& password);
    User* getCurrentUser() const;
    bool userExists(const std::string& username) const;
    void logout();
};