package com.yada.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Daily log of food consumption.
 */
public class DailyLog {
    private static final String LOG_FILE = "database/logs.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    private Map<LocalDate, List<LogEntry>> entries;
    
    /**
     * Constructor for DailyLog.
     */
    public DailyLog() {
        entries = new HashMap<>();
        load();
    }
    
    /**
     * Load logs from the log file.
     */
    private void load() {
        File file = new File(LOG_FILE);
        if (!file.exists()) {
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 4) {
                    LocalDate date = LocalDate.parse(parts[0], DATE_FORMATTER);
                    String foodId = parts[1];
                    double servings = Double.parseDouble(parts[2]);
                    double calories = Double.parseDouble(parts[3]);
                    
                    // Create a "placeholder" food for the log entry
                    // The actual food details will be filled in when the food database is loaded
                    LogEntry entry = new LogEntry(new PlaceholderFood(foodId, calories / servings), servings);
                    addEntry(date, entry);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading logs: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error parsing log data: " + e.getMessage());
        }
    }
    
    /**
     * Save logs to the log file.
     */
    public void save() {
        try {
            File directory = new File("database");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE))) {
                for (Map.Entry<LocalDate, List<LogEntry>> dateEntry : entries.entrySet()) {
                    LocalDate date = dateEntry.getKey();
                    List<LogEntry> dateEntries = dateEntry.getValue();
                    
                    for (LogEntry entry : dateEntries) {
                        writer.write(date.format(DATE_FORMATTER) + "|");
                        writer.write(entry.getFood().getIdentifier() + "|");
                        writer.write(entry.getServings() + "|");
                        writer.write(String.valueOf(entry.getCalories()));
                        writer.newLine();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving logs: " + e.getMessage());
        }
    }
    
    /**
     * Add an entry to the log.
     * 
     * @param date The date
     * @param entry The log entry
     */
    public void addEntry(LocalDate date, LogEntry entry) {
        if (!entries.containsKey(date)) {
            entries.put(date, new ArrayList<>());
        }
        
        entries.get(date).add(entry);
    }
    
    /**
     * Remove an entry from the log.
     * 
     * @param date The date
     * @param entry The log entry to remove
     * @return true if the entry was removed, false otherwise
     */
    public boolean removeEntry(LocalDate date, LogEntry entry) {
        if (entries.containsKey(date)) {
            return entries.get(date).remove(entry);
        }
        return false;
    }
    
    /**
     * Get entries for a specific date.
     * 
     * @param date The date
     * @return The list of entries for that date
     */
    public List<LogEntry> getEntriesForDate(LocalDate date) {
        if (!entries.containsKey(date)) {
            entries.put(date, new ArrayList<>());
        }
        
        return entries.get(date);
    }
    
    /**
     * Clear all entries for a specific date.
     * 
     * @param date The date
     */
    public void clearEntriesForDate(LocalDate date) {
        entries.put(date, new ArrayList<>());
        save();
    }
    
    /**
     * Get all dates with log entries.
     * 
     * @return The list of dates
     */
    public List<LocalDate> getDates() {
        return new ArrayList<>(entries.keySet());
    }
    
    /**
     * Placeholder food for logs when the actual food isn't loaded yet.
     */
    private static class PlaceholderFood extends Food {
        private double caloriesPerServing;
        
        /**
         * Constructor for PlaceholderFood.
         * 
         * @param identifier The food identifier
         * @param caloriesPerServing The calories per serving
         */
        public PlaceholderFood(String identifier, double caloriesPerServing) {
            super(identifier, new String[0]);
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
}