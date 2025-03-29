package com.yada.model;

/**
 * This interface represents a strategy for calculating daily calorie needs.
 * It enables the application to support multiple calculation methods.
 */
public interface CalorieCalculationMethod {
    /**
     * Calculate the daily calorie needs based on user profile data.
     * 
     * @param gender        The user's gender (male/female)
     * @param height        The user's height in cm
     * @param weight        The user's weight in kg
     * @param age           The user's age in years
     * @param activityLevel The user's activity level (sedentary to very active)
     * @return The calculated daily calorie needs
     */
    double calculateCalories(String gender, double height, double weight, int age, String activityLevel);
    
    /**
     * Get the name of this calculation method.
     * 
     * @return The name of the method
     */
    String getName();
    
    /**
     * Get a description of this calculation method.
     * 
     * @return A description of how the method works
     */
    String getDescription();
}
