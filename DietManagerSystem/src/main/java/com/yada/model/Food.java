package com.yada.model;

/**
 * Abstract base class for all food items.
 */
public abstract class Food {
    private String identifier;
    private String[] keywords;
    
    /**
     * Constructor for Food.
     * 
     * @param identifier The food identifier
     * @param keywords The keywords
     */
    public Food(String identifier, String[] keywords) {
        this.identifier = identifier;
        this.keywords = keywords;
    }
    
    /**
     * Get the food identifier.
     * 
     * @return The identifier
     */
    public String getIdentifier() {
        return identifier;
    }
    
    /**
     * Get the food keywords.
     * 
     * @return The keywords
     */
    public String[] getKeywords() {
        return keywords;
    }
    
    /**
     * Get the calories per serving.
     * 
     * @return The calories per serving
     */
    public abstract double getCaloriesPerServing();
    
    /**
     * Convert to string representation.
     * 
     * @return The string representation
     */
    @Override
    public String toString() {
        return identifier;
    }
}