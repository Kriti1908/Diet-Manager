#ifndef DIET_PROFILE_H
#define DIET_PROFILE_H

#include <string>
#include <memory>
#include <functional>
#include <vector>

// Abstract base class for calorie calculation methods
class A_CalorieCalculationMethod {
public:
    virtual ~A_CalorieCalculationMethod() = default;
    virtual double calculateCalories(
        const std::string& gender,
        double height,
        double weight,
        int age,
        const std::string& activityLevel) const = 0;
    virtual std::string getName() const = 0;
};

// Implementation of Harris-Benedict equation for calorie calculation
class C_HarrisBenedictMethod : public A_CalorieCalculationMethod {
public:
    double calculateCalories(
        const std::string& gender,
        double height,
        double weight,
        int age,
        const std::string& activityLevel) const override;
    
    std::string getName() const override;
};

// Implementation of Mifflin-St Jeor equation for calorie calculation
class C_MifflinStJeorMethod : public A_CalorieCalculationMethod {
public:
    double calculateCalories(
        const std::string& gender,
        double height,
        double weight,
        int age,
        const std::string& activityLevel) const override;
    
    std::string getName() const override;
};

// Class representing a user's diet profile
class C_DietProfile {
private:
    std::string gender;
    double height;  // in cm
    double weight;  // in kg
    int age;
    std::string activityLevel;
    
    // The currently selected calorie calculation method
    std::shared_ptr<A_CalorieCalculationMethod> calorieMethod;
    
    // Available calorie calculation methods
    std::vector<std::shared_ptr<A_CalorieCalculationMethod>> availableMethods;

public:
    C_DietProfile();
    
    // Getters
    std::string getGender() const;
    double getHeight() const;
    double getWeight() const;
    int getAge() const;
    std::string getActivityLevel() const;
    
    // Setters
    void setGender(const std::string& gender);
    void setHeight(double height);
    void setWeight(double weight);
    void setAge(int age);
    void setActivityLevel(const std::string& level);
    
    // Update profile with all values
    void updateProfile(const std::string& gender, double height, double weight, int age, const std::string& activityLevel);
    
    // Calorie calculation
    void setCalorieCalculationMethod(const std::string& methodName);
    std::string getCalorieCalculationMethod() const;
    double calculateTargetCalories() const;
    
    // Get all available calculation methods
    std::vector<std::string> getAvailableMethodNames() const;
    
    // Save/load profile
    std::string toString() const;
    static C_DietProfile fromString(const std::string& str);
};

#endif // DIET_PROFILE_H
