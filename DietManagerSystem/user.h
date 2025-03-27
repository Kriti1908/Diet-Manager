#pragma once

#include <string>
#include "dietProfile.h"

class User {
private:
    std::string username;
    std::string passwordHash;
    C_DietProfile profile;
    bool profileCompleted;

public:
    User(const std::string& username, const std::string& passwordHash);
    
    // Getters
    std::string getUsername() const;
    std::string getPasswordHash() const;
    C_DietProfile& getProfile();
    const C_DietProfile& getProfile() const;
    bool isProfileCompleted() const;
    
    // Setters
    void setProfileCompleted(bool completed);
    
    // Serialization
    std::string toString() const;
    static User fromString(const std::string& str);
};