package com.yada.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Database of food items.
 */
public class FoodDatabase {
    private static final String FOODS_FILE = "database/foods.txt";
    private Map<String, Food> foods;
    
    /**
     * Constructor for FoodDatabase.
     */
    public FoodDatabase() {
        foods = new HashMap<>();
        load();
    }
    
    /**
     * Load foods from the foods file.
     */
    private void load() {
        File file = new File(FOODS_FILE);
        System.out.println("Looking for food file at: " + file.getAbsolutePath()); 
        if (!file.exists()) {
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 4) {
                    String type = parts[0];
                    String identifier = parts[1];
                    String[] keywords = parts[2].split(",");
                    
                    if (type.equals("BasicFood")) {
                        double calories = Double.parseDouble(parts[3]);
                        BasicFood food = new BasicFood(identifier, keywords, calories);
                        foods.put(identifier, food);
                    } else if (type.equals("CompositeFood")) {
                        CompositeFood food = new CompositeFood(identifier, keywords);
                        String[] componentParts = parts[3].split(",");
                        
                        for (String componentPart : componentParts) {
                            String[] componentInfo = componentPart.split(":");
                            if (componentInfo.length == 2) {
                                String componentIdentifier = componentInfo[0];
                                double servings = Double.parseDouble(componentInfo[1]);
                                
                                // Add component if it exists
                                Food component = foods.get(componentIdentifier);
                                if (component != null) {
                                    food.addComponent(component, servings);
                                }
                            }
                        }
                        
                        foods.put(identifier, food);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading foods: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing food data: " + e.getMessage());
        }
    }
    
    /**
     * Save foods to the foods file.
     */
    public void save() {
        try {
            File directory = new File("database");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FOODS_FILE))) {
                for (Food food : foods.values()) {
                    if (food instanceof BasicFood) {
                        writer.write("BasicFood|");
                        writer.write(food.getIdentifier() + "|");
                        writer.write(String.join(",", food.getKeywords()) + "|");
                        writer.write(String.valueOf(food.getCaloriesPerServing()));
                        writer.newLine();
                    } else if (food instanceof CompositeFood) {
                        CompositeFood compositeFood = (CompositeFood) food;
                        writer.write("CompositeFood|");
                        writer.write(food.getIdentifier() + "|");
                        writer.write(String.join(",", food.getKeywords()) + "|");
                        
                        StringBuilder components = new StringBuilder();
                        List<Food> componentFoods = compositeFood.getComponents();
                        List<Double> servings = compositeFood.getServings();
                        
                        for (int i = 0; i < componentFoods.size(); i++) {
                            if (i > 0) {
                                components.append(",");
                            }
                            components.append(componentFoods.get(i).getIdentifier())
                                      .append(":")
                                      .append(servings.get(i));
                        }
                        
                        writer.write(components.toString());
                        writer.newLine();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving foods: " + e.getMessage());
        }
    }
    
    /**
     * Add a food to the database.
     * 
     * @param food The food to add
     * @return true if the food was added, false if a food with the same identifier already exists
     */
    public boolean addFood(Food food) {
        if (foods.containsKey(food.getIdentifier())) {
            return false;
        }
        
        foods.put(food.getIdentifier(), food);
        save();
        return true;
    }
    
    /**
     * Get a food by its identifier.
     * 
     * @param identifier The food identifier
     * @return The food, or null if not found
     */
    public Food getFoodByIdentifier(String identifier) {
        return foods.get(identifier);
    }
    
    /**
     * Get all foods in the database.
     * 
     * @return A list of all foods
     */
    public List<Food> getAllFoods() {
        return new ArrayList<>(foods.values());
    }
    
    /**
     * Search for foods by keywords.
     * 
     * @param keywordsStr The keywords string
     * @return A list of matching foods
     */
    public List<Food> searchFoods(String keywordsStr) {
        List<Food> results = new ArrayList<>();
        String[] searchKeywords = keywordsStr.toLowerCase().split("\\s+");
        
        for (Food food : foods.values()) {
            boolean match = true;
            
            for (String searchKeyword : searchKeywords) {
                boolean keywordMatch = false;
                
                // Check food identifier
                if (food.getIdentifier().toLowerCase().contains(searchKeyword)) {
                    keywordMatch = true;
                }
                
                // Check food keywords
                if (!keywordMatch) {
                    for (String foodKeyword : food.getKeywords()) {
                        if (foodKeyword.toLowerCase().contains(searchKeyword)) {
                            keywordMatch = true;
                            break;
                        }
                    }
                }
                
                if (!keywordMatch) {
                    match = false;
                    break;
                }
            }
            
            if (match) {
                results.add(food);
            }
        }
        
        return results;
    }
}