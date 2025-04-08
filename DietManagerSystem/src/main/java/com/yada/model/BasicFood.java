// src/main/java/com/yada/model/BasicFood.java
package com.yada.model;

import java.util.HashMap;
import java.util.Map;

public class BasicFood extends Food {
    private double caloriesPerServing;
    private Map<String, Double> nutrients;

    public BasicFood(String identifier, String[] keywords, double caloriesPerServing) {
        super(identifier, keywords);
        this.caloriesPerServing = caloriesPerServing;
        this.nutrients = new HashMap<>();
        this.nutrients.put("calories", caloriesPerServing);
    }

    public void addNutrient(String name, double amount) {
        nutrients.put(name.toLowerCase(), amount);
    }

    public double getNutrient(String name) {
        return nutrients.getOrDefault(name.toLowerCase(), 0.0);
    }

    public Map<String, Double> getAllNutrients() {
        return new HashMap<>(nutrients);
    }

    @Override
    public double getCaloriesPerServing() {
        return caloriesPerServing;
    }
}