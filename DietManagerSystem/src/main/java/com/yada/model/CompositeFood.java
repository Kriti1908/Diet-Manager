package com.yada.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a composite food item made up of other foods.
 */
public class CompositeFood extends Food {
    private List<Food> components;
    private List<Double> servings;
    
    /**
     * Constructor for CompositeFood.
     * 
     * @param identifier The food identifier
     * @param keywords The keywords
     */
    public CompositeFood(String identifier, String[] keywords) {
        super(identifier, keywords);
        this.components = new ArrayList<>();
        this.servings = new ArrayList<>();
    }
    
    /**
     * Add a component food to this composite food.
     * 
     * @param food The component food
     * @param servings The number of servings
     */
    public void addComponent(Food food, double servings) {
        components.add(food);
        this.servings.add(servings);
    }
    
    /**
     * Get the components of this composite food.
     * 
     * @return The component foods
     */
    public List<Food> getComponents() {
        return components;
    }
    
    /**
     * Get the servings for each component.
     * 
     * @return The servings
     */
    public List<Double> getServings() {
        return servings;
    }
    
    /**
     * Get the calories per serving (sum of all components).
     * 
     * @return The calories per serving
     */
    @Override
    public double getCaloriesPerServing() {
        double total = 0;
        for (int i = 0; i < components.size(); i++) {
            total += components.get(i).getCaloriesPerServing() * servings.get(i);
        }
        return total;
    }
}