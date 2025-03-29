package com.yada.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * User profile with personal information and calorie calculation.
 */
public class UserProfile {
    private static final String PROFILES_DIRECTORY = "database";
    private static final String LEGACY_PROFILE_FILE = "database/profile.txt";
    
    public static final String[] ACTIVITY_LEVELS = {"sedentary", "light", "moderate", "active", "very active"};
    public static final String[] CALCULATION_METHODS = {"Harris-Benedict", "Mifflin-St Jeor"};
    
    private String username;  // Associated username
    private String gender;
    private double height;  // in cm
    private double weight;  // in kg
    private int age;
    private String activityLevel;
    private String calorieCalculationMethod;
    
    /**
     * Constructor for UserProfile with default values.
     */
    public UserProfile() {
        // Default values
        this.username = null;
        gender = "female";
        height = 165.0;
        weight = 60.0;
        age = 30;
        activityLevel = "moderate";
        calorieCalculationMethod = "Harris-Benedict";
    }
    
    /**
     * Constructor for UserProfile with username.
     * 
     * @param username The username
     */
    public UserProfile(String username) {
        this(); // Set default values
        this.username = username;
        load(); // Load profile for this user if it exists
    }
    
    /**
     * Load profile from the profile file for current username.
     */
    private void load() {
        if (username == null) {
            return; // Cannot load without a username
        }
        
        // First check if there's a user-specific profile file
        File profileFile = getUserProfileFile();
        
        // If no user-specific profile exists, check if there's a legacy profile file
        if (!profileFile.exists()) {
            File legacyFile = new File(LEGACY_PROFILE_FILE);
            if (legacyFile.exists()) {
                profileFile = legacyFile;
                System.out.println("Using legacy profile file: " + LEGACY_PROFILE_FILE);
            } else {
                // No profile exists yet
                return;
            }
        }
        
        Properties properties = new Properties();
        try (BufferedReader reader = new BufferedReader(new FileReader(profileFile))) {
            properties.load(reader);
            
            gender = properties.getProperty("gender", gender);
            height = Double.parseDouble(properties.getProperty("height", String.valueOf(height)));
            weight = Double.parseDouble(properties.getProperty("weight", String.valueOf(weight)));
            age = Integer.parseInt(properties.getProperty("age", String.valueOf(age)));
            activityLevel = properties.getProperty("activityLevel", activityLevel);
            calorieCalculationMethod = properties.getProperty("calorieCalculationMethod", calorieCalculationMethod);
        } catch (IOException e) {
            System.err.println("Error loading profile for " + username + ": " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing profile data for " + username + ": " + e.getMessage());
        }
    }
    
    /**
     * Save profile to the profile file.
     */
    public void save() {
        if (username == null) {
            System.err.println("Cannot save profile: No username associated");
            return;
        }
        
        try {
            // Create database directory if it doesn't exist
            File directory = new File(PROFILES_DIRECTORY);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            Properties properties = new Properties();
            properties.setProperty("gender", gender);
            properties.setProperty("height", String.valueOf(height));
            properties.setProperty("weight", String.valueOf(weight));
            properties.setProperty("age", String.valueOf(age));
            properties.setProperty("activityLevel", activityLevel);
            properties.setProperty("calorieCalculationMethod", calorieCalculationMethod);
            
            // Always save to the user-specific profile file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(getUserProfileFile()))) {
                properties.store(writer, "YADA User Profile for " + username);
            }
        } catch (IOException e) {
            System.err.println("Error saving profile for " + username + ": " + e.getMessage());
        }
    }
    
    /**
     * Get the profile file for the current user.
     * 
     * @return The profile file
     */
    private File getUserProfileFile() {
        return new File(PROFILES_DIRECTORY + "/" + username + "_profile.txt");
    }
    
    /**
     * Set the username for this profile.
     * 
     * @param username The username
     */
    public void setUsername(String username) {
        this.username = username;
        load(); // Load profile for this user
    }
    
    /**
     * Calculate daily calories based on profile.
     * 
     * @return The daily calorie target
     */
    public double calculateDailyCalories() {
        double bmr;
        
        if (calorieCalculationMethod.equals("Harris-Benedict")) {
            // Harris-Benedict equation
            if (gender.equals("male")) {
                bmr = 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age);
            } else {
                bmr = 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age);
            }
        } else {
            // Mifflin-St Jeor equation (default)
            if (gender.equals("male")) {
                bmr = (10 * weight) + (6.25 * height) - (5 * age) + 5;
            } else {
                bmr = (10 * weight) + (6.25 * height) - (5 * age) - 161;
            }
        }
        
        // Apply activity multiplier
        double activityMultiplier;
        switch (activityLevel) {
            case "sedentary":
                activityMultiplier = 1.2;
                break;
            case "light":
                activityMultiplier = 1.375;
                break;
            case "moderate":
                activityMultiplier = 1.55;
                break;
            case "active":
                activityMultiplier = 1.725;
                break;
            case "very active":
                activityMultiplier = 1.9;
                break;
            default:
                activityMultiplier = 1.55;  // Default to moderate
        }
        
        return bmr * activityMultiplier;
    }
    
    /**
     * Get the gender.
     * 
     * @return The gender
     */
    public String getGender() {
        return gender;
    }
    
    /**
     * Set the gender.
     * 
     * @param gender The gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    /**
     * Get the height.
     * 
     * @return The height in cm
     */
    public double getHeight() {
        return height;
    }
    
    /**
     * Set the height.
     * 
     * @param height The height in cm
     */
    public void setHeight(double height) {
        this.height = height;
    }
    
    /**
     * Get the weight.
     * 
     * @return The weight in kg
     */
    public double getWeight() {
        return weight;
    }
    
    /**
     * Set the weight.
     * 
     * @param weight The weight in kg
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }
    
    /**
     * Get the age.
     * 
     * @return The age
     */
    public int getAge() {
        return age;
    }
    
    /**
     * Set the age.
     * 
     * @param age The age
     */
    public void setAge(int age) {
        this.age = age;
    }
    
    /**
     * Get the activity level.
     * 
     * @return The activity level
     */
    public String getActivityLevel() {
        return activityLevel;
    }
    
    /**
     * Set the activity level.
     * 
     * @param activityLevel The activity level
     */
    public void setActivityLevel(String activityLevel) {
        this.activityLevel = activityLevel;
    }
    
    /**
     * Get the calorie calculation method.
     * 
     * @return The calorie calculation method
     */
    public String getCalorieCalculationMethod() {
        return calorieCalculationMethod;
    }
    
    /**
     * Set the calorie calculation method.
     * 
     * @param calorieCalculationMethod The calorie calculation method
     */
    public void setCalorieCalculationMethod(String calorieCalculationMethod) {
        this.calorieCalculationMethod = calorieCalculationMethod;
    }
}