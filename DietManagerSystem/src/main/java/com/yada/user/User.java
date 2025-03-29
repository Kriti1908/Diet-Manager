package com.yada.user;

/**
 * Represents a user of the application.
 */
public class User {
    private String username;
    private String encryptedPassword;
    
    /**
     * Constructor for User.
     * 
     * @param username The username
     * @param encryptedPassword The encrypted password
     */
    public User(String username, String encryptedPassword) {
        this.username = username;
        this.encryptedPassword = encryptedPassword;
    }
    
    /**
     * Get the username.
     * 
     * @return The username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Get the encrypted password.
     * 
     * @return The encrypted password
     */
    public String getEncryptedPassword() {
        return encryptedPassword;
    }
}