package com.yada.util;

import com.yada.model.DailyLog;
import com.yada.model.Food;
import com.yada.model.LogEntry;

import java.time.LocalDate;

/**
 * Command to add a food to the daily log.
 */
public class AddFoodCommand implements Command {
    private DailyLog dailyLog;
    private LocalDate date;
    private Food food;
    private double servings;
    private LogEntry entry;
    
    /**
     * Constructor for AddFoodCommand.
     * 
     * @param dailyLog The daily log
     * @param date The date
     * @param food The food to add
     * @param servings The number of servings
     */
    public AddFoodCommand(DailyLog dailyLog, LocalDate date, Food food, double servings) {
        this.dailyLog = dailyLog;
        this.date = date;
        this.food = food;
        this.servings = servings;
    }
    
    /**
     * Execute the command to add the food.
     */
    @Override
    public void execute() {
        entry = new LogEntry(food, servings);
        dailyLog.addEntry(date, entry);
        dailyLog.save();
    }
    
    /**
     * Undo the command by removing the food.
     */
    @Override
    public void undo() {
        if (entry != null) {
            dailyLog.removeEntry(date, entry);
            dailyLog.save();
        }
    }
}