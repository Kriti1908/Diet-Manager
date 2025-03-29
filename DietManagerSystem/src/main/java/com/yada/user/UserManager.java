package com.yada.user;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages user authentication and registration.
 */
public class UserManager {
    private static final String USERS_FILE = "database/users.txt";
    private Map<String, User> users;
    
    /**
     * Constructor for UserManager.
     */
    public UserManager() {
        users = new HashMap<>();
        loadUsers();
    }
    
    /**
     * Load users from the users file.
     */
    private void loadUsers() {
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    String username = parts[0];
                    String encryptedPassword = parts[1];
                    users.put(username, new User(username, encryptedPassword));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }
    
    /**
     * Save users to the users file.
     */
    private void saveUsers() {
        try {
            File directory = new File("database");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
                for (User user : users.values()) {
                    writer.write(user.getUsername() + "|" + user.getEncryptedPassword());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }
    
    /**
     * Encrypt a password (simple encryption for demonstration).
     * 
     * @param password The password to encrypt
     * @return The encrypted password
     */
    private String encryptPassword(String password) {
        // Simple encryption for demonstration purposes
        StringBuilder encrypted = new StringBuilder();
        for (char c : password.toCharArray()) {
            encrypted.append((int) c);
        }
        return encrypted.toString();
    }
    
    /**
     * Check if a user exists.
     * 
     * @param username The username to check
     * @return true if the user exists, false otherwise
     */
    public boolean userExists(String username) {
        return users.containsKey(username);
    }
    
    /**
     * Register a new user.
     * 
     * @param username The username
     * @param password The password
     * @return The registered user, or null if registration failed
     */
    public User register(String username, String password) {
        if (userExists(username)) {
            return null;
        }
        
        String encryptedPassword = encryptPassword(password);
        User user = new User(username, encryptedPassword);
        users.put(username, user);
        saveUsers();
        
        return user;
    }
    
    /**
     * Authenticate a user.
     * 
     * @param username The username
     * @param password The password
     * @return The authenticated user, or null if authentication failed
     */
    public User authenticate(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getEncryptedPassword().equals(encryptPassword(password))) {
            return user;
        }
        
        return null;
    }
}