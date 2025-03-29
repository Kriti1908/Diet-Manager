package com.yada.model;

/**
 * Represents a basic food item.
 */
public class BasicFood extends Food {
    private double caloriesPerServing;
    
    /**
     * Constructor for BasicFood.
     * 
     * @param identifier The food identifier
     * @param keywords The keywords
     * @param caloriesPerServing The calories per serving
     */
    public BasicFood(String identifier, String[] keywords, double caloriesPerServing) {
        super(identifier, keywords);
        this.caloriesPerServing = caloriesPerServing;
    }
    
    /**
     * Get the calories per serving.
     * 
     * @return The calories per serving
     */
    @Override
    public double getCaloriesPerServing() {
        return caloriesPerServing;
    }
}