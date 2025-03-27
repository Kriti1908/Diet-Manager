#include "utils.h"
#include <sstream>
#include <ctime>
#include <iomanip>
#include <regex>

std::vector<std::string> split(const std::string& str, char delimiter) {
    std::vector<std::string> tokens;
    std::stringstream ss(str);
    std::string token;
    
    while (std::getline(ss, token, delimiter)) {
        tokens.push_back(token);
    }
    
    return tokens;
}

std::string stringToLower(const std::string& str) {
    std::string result = str;
    std::transform(result.begin(), result.end(), result.begin(),
        [](unsigned char c) { return std::tolower(c); });
    return result;
}

std::string getCurrentDateString() {
    std::time_t t = std::time(nullptr);
    std::tm* now = std::localtime(&t);
    
    std::stringstream ss;
    ss << (now->tm_year + 1900) << '-'
       << std::setw(2) << std::setfill('0') << (now->tm_mon + 1) << '-'
       << std::setw(2) << std::setfill('0') << now->tm_mday;
    
    return ss.str();
}

bool isValidDateFormat(const std::string& date) {
    // Check format: YYYY-MM-DD
    std::regex dateRegex("^\\d{4}-\\d{2}-\\d{2}$");
    if (!std::regex_match(date, dateRegex)) {
        return false;
    }
    
    // Check valid ranges
    int year, month, day;
    sscanf(date.c_str(), "%d-%d-%d", &year, &month, &day);
    
    if (year < 1900 || year > 2100) return false;
    if (month < 1 || month > 12) return false;
    
    int daysInMonth[] = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    
    // Adjust for leap years
    if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
        daysInMonth[2] = 29;
    }
    
    if (day < 1 || day > daysInMonth[month]) return false;
    
    return true;
}
