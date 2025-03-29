package com.yada;

import com.yada.model.*;
import com.yada.ui.MainWindow;
import com.yada.user.User;
import com.yada.user.UserManager;
import com.yada.util.Command;
import com.yada.util.AddFoodCommand;
import com.yada.util.RemoveFoodCommand;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Main class for the diet management application.
 */
public class DietManager {
    private UserManager userManager;
    private UserProfile userProfile;
    private FoodDatabase foodDatabase;
    private DailyLog dailyLog;
    private Stack<Command> undoStack;
    private Stack<Command> redoStack;
    private User currentUser;
    
    /**
     * Constructor for DietManager.
     */
    public DietManager() {
        userManager = new UserManager();
        userProfile = new UserProfile(); // Start with empty profile
        foodDatabase = new FoodDatabase();
        dailyLog = new DailyLog();
        undoStack = new Stack<>();
        redoStack = new Stack<>();
        currentUser = null;
    }
    
    /**
     * Get the user manager.
     * 
     * @return The user manager
     */
    public UserManager getUserManager() {
        return userManager;
    }
    
    /**
     * Set the current user.
     * 
     * @param user The current user
     */
    // Check DietManager.java, method setCurrentUser()
    public void setCurrentUser(User user) {
        this.currentUser = user;
        
        // Load profile for this user or create a new one
        if (user != null) {
            userProfile = new UserProfile(user.getUsername());
            // Make sure the profile is loaded correctly
            System.out.println("Setting current user: " + user.getUsername() + 
                            ", Profile username: " + userProfile.getUsername());
        } else {
            userProfile = new UserProfile(); // Default profile when no user is logged in
        }
    }
    
    /**
     * Get the current user.
     * 
     * @return The current user
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Get the food database.
     * 
     * @return The food database
     */
    public FoodDatabase getFoodDatabase() {
        return foodDatabase;
    }
    
    /**
     * Get the daily log.
     * 
     * @return The daily log
     */
    public DailyLog getDailyLog() {
        return dailyLog;
    }
    
    /**
     * Get the user profile.
     * 
     * @return The user profile
     */
    public UserProfile getUserProfile() {
        return userProfile;
    }
    
    /**
     * Create a basic food.
     * 
     * @param identifier The food identifier
     * @param keywords The keywords
     * @param caloriesPerServing The calories per serving
     * @return The created food
     */
    public Food createBasicFood(String identifier, String[] keywords, double caloriesPerServing) {
        BasicFood food = new BasicFood(identifier, keywords, caloriesPerServing);
        boolean success = foodDatabase.addFood(food);
        return success ? food : null;
    }
    
    /**
     * Create a composite food.
     * 
     * @param identifier The food identifier
     * @param keywords The keywords
     * @param components The component foods
     * @param servings The servings for each component
     * @return The created food
     */
    public Food createCompositeFood(String identifier, String[] keywords, 
            List<Food> components, List<Double> servings) {
        if (components.size() != servings.size()) {
            return null;
        }
        
        CompositeFood food = new CompositeFood(identifier, keywords);
        for (int i = 0; i < components.size(); i++) {
            food.addComponent(components.get(i), servings.get(i));
        }
        
        boolean success = foodDatabase.addFood(food);
        return success ? food : null;
    }
    
    /**
     * Update the user profile.
     * 
     * @param gender The gender
     * @param height The height in cm
     * @param weight The weight in kg
     * @param age The age
     * @param activityLevel The activity level
     * @param calculationMethod The calorie calculation method
     */
    public void updateUserProfile(String gender, double height, double weight, 
            int age, String activityLevel, String calculationMethod) {
        userProfile.setGender(gender);
        userProfile.setHeight(height);
        userProfile.setWeight(weight);
        userProfile.setAge(age);
        userProfile.setActivityLevel(activityLevel);
        userProfile.setCalorieCalculationMethod(calculationMethod);
        userProfile.save();
    }
    
    /**
     * Add a food to the daily log.
     * 
     * @param date The date
     * @param food The food
     * @param servings The number of servings
     */
    public void addFoodToLog(LocalDate date, Food food, double servings) {
        Command command = new AddFoodCommand(dailyLog, date, food, servings);
        command.execute();
        undoStack.push(command);
        // We don't clear redoStack to maintain unlimited redo operations
    }
    
    /**
     * Remove a food from the daily log.
     * 
     * @param date The date
     * @param index The index of the food to remove
     */
    public void removeFoodFromLog(LocalDate date, int index) {
        List<LogEntry> entries = dailyLog.getEntriesForDate(date);
        if (index < 0 || index >= entries.size()) {
            return;
        }
        
        LogEntry entry = entries.get(index);
        Command command = new RemoveFoodCommand(dailyLog, date, entry);
        command.execute();
        undoStack.push(command);
        // We don't clear redoStack to maintain unlimited redo operations
    }
    
    /**
     * Undo the last command.
     * 
     * @return true if an undo was performed, false otherwise
     */
    public boolean undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.undo();
            redoStack.push(command);
            return true;
        }
        return false;
    }
    
    /**
     * Redo the last undone command.
     * 
     * @return true if a redo was performed, false otherwise
     */
    public boolean redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            undoStack.push(command);
            return true;
        }
        return false;
    }
    
    /**
     * Check if undo is available.
     * 
     * @return true if undo is available, false otherwise
     */
    public boolean canUndo() {
        return !undoStack.isEmpty();
    }
    
    /**
     * Check if redo is available.
     * 
     * @return true if redo is available, false otherwise
     */
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
    
    /**
     * Get the calories consumed on a specific date.
     * 
     * @param date The date
     * @return The calories consumed
     */
    public double getCaloriesConsumed(LocalDate date) {
        List<LogEntry> entries = dailyLog.getEntriesForDate(date);
        double total = 0;
        for (LogEntry entry : entries) {
            total += entry.getCalories();
        }
        return total;
    }
    
    /**
     * Get the target calories per day.
     * 
     * @return The target calories
     */
    public double getTargetCalories() {
        return userProfile.calculateDailyCalories();
    }
    
    /**
     * Get the remaining calories for a specific date.
     * 
     * @param date The date
     * @return The remaining calories
     */
    public double getRemainingCalories(LocalDate date) {
        return getTargetCalories() - getCaloriesConsumed(date);
    }
    
    /**
     * Search foods by keywords.
     * 
     * @param keywords The keywords to search for
     * @return The matching foods
     */
    public List<Food> searchFoods(String keywords) {
        return foodDatabase.searchFoods(keywords);
    }
    
    /**
     * Main method to start the application.
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        DietManager dietManager = new DietManager();
        MainWindow mainWindow = new MainWindow(dietManager);
        mainWindow.setVisible(true);
    }
}