#include "dailyLog.h"
#include "utils.h"
#include <fstream>
#include <sstream>
#include <iostream>
#include <ctime>
#include <iomanip>

C_DailyLog::C_DailyLog(const std::string& filePath)
    : logFilePath(filePath), modified(false),
      gender("male"), height(170.0), weight(70.0), age(30), activityLevel("moderate") {
    
    // Set current date
    std::time_t t = std::time(nullptr);
    std::tm* now = std::localtime(&t);
    std::stringstream ss;
    ss << (now->tm_year + 1900) << '-' 
       << std::setw(2) << std::setfill('0') << (now->tm_mon + 1) << '-'
       << std::setw(2) << std::setfill('0') << now->tm_mday;
    
    date = ss.str();
}

C_DailyLog::C_DailyLog(const std::string& filePath, const std::string& date)
    : logFilePath(filePath), date(date), modified(false),
      gender("male"), height(170.0), weight(70.0), age(30), activityLevel("moderate") {
}

void C_DailyLog::setDate(const std::string& newDate) {
    date = newDate;
    modified = true;
}

std::string C_DailyLog::getDate() const {
    return date;
}

void C_DailyLog::addFood(const std::string& foodName, double servings) {
    if (servings <= 0) {
        return;
    }
    
    // If food already exists, add to servings
    if (consumedFoods.find(foodName) != consumedFoods.end()) {
        consumedFoods[foodName] += servings;
    } else {
        consumedFoods[foodName] = servings;
    }
    
    modified = true;
}

bool C_DailyLog::removeFood(const std::string& foodName) {
    auto it = consumedFoods.find(foodName);
    if (it != consumedFoods.end()) {
        consumedFoods.erase(it);
        modified = true;
        return true;
    }
    return false;
}

const std::map<std::string, double>& C_DailyLog::getConsumedFoods() const {
    return consumedFoods;
}

double C_DailyLog::calculateTotalCalories() const {
    // This is actually implemented in DietManager since it needs access to the food database
    return 0.0;
}

bool C_DailyLog::loadFromFile() {
    std::ifstream file(logFilePath);
    if (!file.is_open()) {
        std::cerr << "Could not open log file: " << logFilePath << std::endl;
        return false;
    }
    
    consumedFoods.clear();
    
    std::string line;
    bool foundDate = false;
    
    while (std::getline(file, line)) {
        if (line.empty() || line[0] == '#') {
            continue; // Skip empty lines and comments
        }
        
        std::vector<std::string> parts = split(line, '|');
        
        if (parts.size() >= 1 && parts[0] == "DATE") {
            if (parts.size() >= 2 && parts[1] == date) {
                foundDate = true;
                
                // If there's profile data, load it
                if (parts.size() >= 7) {
                    gender = parts[2];
                    height = std::stod(parts[3]);
                    weight = std::stod(parts[4]);
                    age = std::stoi(parts[5]);
                    activityLevel = parts[6];
                }
            } else if (foundDate) {
                // We've gone past our date section
                break;
            }
        } else if (foundDate && parts.size() >= 2) {
            std::string foodName = parts[0];
            double servings = std::stod(parts[1]);
            consumedFoods[foodName] = servings;
        }
    }
    
    modified = false;
    return true;
}

bool C_DailyLog::saveToFile() const {
    // First, read the entire log file
    std::ifstream inFile(logFilePath);
    std::stringstream buffer;
    
    if (inFile.is_open()) {
        buffer << inFile.rdbuf();
        inFile.close();
    }
    
    std::string content = buffer.str();
    
    // Find and remove the section for our date
    size_t dateSection = content.find("DATE|" + date);
    if (dateSection != std::string::npos) {
        size_t nextDateSection = content.find("DATE|", dateSection + 1);
        if (nextDateSection != std::string::npos) {
            content.erase(dateSection, nextDateSection - dateSection);
        } else {
            content.erase(dateSection);
        }
    }
    
    // Open the file for writing
    std::ofstream outFile(logFilePath);
    if (!outFile.is_open()) {
        std::cerr << "Could not open log file for writing: " << logFilePath << std::endl;
        return false;
    }
    
    // Write the modified content
    outFile << content;
    
    // Write our date section
    outFile << "DATE|" << date << "|" 
            << gender << "|" << height << "|" << weight << "|" 
            << age << "|" << activityLevel << std::endl;
    
    // Write consumed foods
    for (const auto& [foodName, servings] : consumedFoods) {
        outFile << foodName << "|" << servings << std::endl;
    }
    
    return true;
}

bool C_DailyLog::isModified() const {
    return modified;
}

void C_DailyLog::setProfileData(
    const std::string& g, double h, double w, int a, const std::string& level) {
    gender = g;
    height = h;
    weight = w;
    age = a;
    activityLevel = level;
    modified = true;
}

std::string C_DailyLog::getGender() const {
    return gender;
}

double C_DailyLog::getHeight() const {
    return height;
}

double C_DailyLog::getWeight() const {
    return weight;
}

int C_DailyLog::getAge() const {
    return age;
}

std::string C_DailyLog::getActivityLevel() const {
    return activityLevel;
}
