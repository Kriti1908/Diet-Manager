#include "user.h"
#include "utils.h"
#include <sstream>
#include <vector>

User::User(const std::string& username, const std::string& passwordHash)
    : username(username), passwordHash(passwordHash), profileCompleted(false) {
}

std::string User::getUsername() const {
    return username;
}

std::string User::getPasswordHash() const {
    return passwordHash;
}

C_DietProfile& User::getProfile() {
    return profile;
}

const C_DietProfile& User::getProfile() const {
    return profile;
}

bool User::isProfileCompleted() const {
    return profileCompleted;
}

void User::setProfileCompleted(bool completed) {
    profileCompleted = completed;
}

std::string User::toString() const {
    std::stringstream ss;
    ss << username << "," << passwordHash << "," << profileCompleted << ",";
    
    // Add profile data
    ss << profile.getGender() << "," 
       << profile.getHeight() << "," 
       << profile.getWeight() << "," 
       << profile.getAge() << "," 
       << profile.getActivityLevel() << "," 
       << profile.getCalorieCalculationMethod();
    
    return ss.str();
}

User User::fromString(const std::string& str) {
    std::vector<std::string> parts = split(str, ',');
    
    if (parts.size() < 9) {
        throw std::runtime_error("Invalid user data format");
    }
    
    User user(parts[0], parts[1]);
    user.profileCompleted = (parts[2] == "1" || parts[2] == "true" || parts[2] == "True");
    
    if (user.profileCompleted) {
        // Set profile data
        std::string gender = parts[3];
        double height = std::stod(parts[4]);
        double weight = std::stod(parts[5]);
        int age = std::stoi(parts[6]);
        std::string activityLevel = parts[7];
        std::string method = parts[8];
        
        user.profile.updateProfile(gender, height, weight, age, activityLevel);
        user.profile.setCalorieCalculationMethod(method);
    }
    
    return user;
}