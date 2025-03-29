package com.yada.model;

/**
 * Entry in the daily log.
 */
public class LogEntry {
    private Food food;
    private double servings;
    
    /**
     * Constructor for LogEntry.
     * 
     * @param food The food
     * @param servings The number of servings
     */
    public LogEntry(Food food, double servings) {
        this.food = food;
        this.servings = servings;
    }
    
    /**
     * Get the food.
     * 
     * @return The food
     */
    public Food getFood() {
        return food;
    }
    
    /**
     * Get the number of servings.
     * 
     * @return The number of servings
     */
    public double getServings() {
        return servings;
    }
    
    /**
     * Get the total calories.
     * 
     * @return The total calories
     */
    public double getCalories() {
        return food.getCaloriesPerServing() * servings;
    }
}