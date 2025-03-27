#ifndef UTILS_H
#define UTILS_H

#include <string>
#include <vector>
#include <algorithm>
#include <cctype>

// Split string by delimiter
std::vector<std::string> split(const std::string& str, char delimiter);

// Convert string to lowercase
std::string stringToLower(const std::string& str);

// Get current date as string (YYYY-MM-DD)
std::string getCurrentDateString();

// Validate date format (YYYY-MM-DD)
bool isValidDateFormat(const std::string& date);

#endif // UTILS_H
