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
    private Map<String, Map<LocalDate, List<LogEntry>>> userEntries; // Logs per user
    
    /**
     * Constructor for DailyLog.
     */
    public DailyLog() {
        userEntries = new HashMap<>();
        load();
    }
    
    /**
     * Load logs from the log file.
     */
    public void load() {
        File file = new File(LOG_FILE);
        if (!file.exists()) {
            return;
        }
    
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 5) {
                    String username = parts[0];
                    LocalDate date = LocalDate.parse(parts[1], DATE_FORMATTER);
                    String foodId = parts[2];
                    double servings = Double.parseDouble(parts[3]);
                    double calories = Double.parseDouble(parts[4]);
    
                    LogEntry entry = new LogEntry(new PlaceholderFood(foodId, calories / servings), servings);
                    addEntry(username, date, entry);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading logs: " + e.getMessage());
        }
    }
    
    /**
     * Save logs to the log file.
     */
    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE))) {
            for (Map.Entry<String, Map<LocalDate, List<LogEntry>>> userEntry : userEntries.entrySet()) {
                String username = userEntry.getKey();
                for (Map.Entry<LocalDate, List<LogEntry>> dateEntry : userEntry.getValue().entrySet()) {
                    LocalDate date = dateEntry.getKey();
                    for (LogEntry entry : dateEntry.getValue()) {
                        writer.write(username + "|" + date.format(DATE_FORMATTER) + "|");
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
     * Add an entry to the log for a specific user.
     * 
     * @param username The username
     * @param date The date
     * @param entry The log entry
     */
    public void addEntry(String username, LocalDate date, LogEntry entry) {
        userEntries.putIfAbsent(username, new HashMap<>());
        userEntries.get(username).putIfAbsent(date, new ArrayList<>());
        userEntries.get(username).get(date).add(entry);
    }
    
    /**
     * Remove an entry from the log for a specific user.
     * 
     * @param username The username
     * @param date The date
     * @param entry The log entry to remove
     * @return true if the entry was removed, false otherwise
     */
    public boolean removeEntry(String username, LocalDate date, LogEntry entry) {
        if (userEntries.containsKey(username) && userEntries.get(username).containsKey(date)) {
            return userEntries.get(username).get(date).remove(entry);
        }
        return false;
    }
    
    /**
     * Get entries for a specific user and date.
     * 
     * @param username The username
     * @param date The date
     * @return The list of entries for that user and date
     */
    public List<LogEntry> getEntriesForUserAndDate(String username, LocalDate date) {
        return userEntries.getOrDefault(username, new HashMap<>())
                        .getOrDefault(date, new ArrayList<>());
    }
    
    /**
     * Clear all entries for a specific user and date.
     * 
     * @param username The username
     * @param date The date
     */
    public void clearEntriesForUserAndDate(String username, LocalDate date) {
        if (userEntries.containsKey(username)) {
            userEntries.get(username).remove(date);
        }
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