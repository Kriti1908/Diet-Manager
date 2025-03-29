package com.yada.ui;

import com.yada.DietManager;
import com.yada.model.UserProfile;
import com.yada.user.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Main window for the application.
 */
public class MainWindow extends JFrame {
    private DietManager dietManager;
    private LocalDate currentDate;
    
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    
    private JPanel datePanel;
    private JButton prevDateButton;
    private JLabel dateLabel;
    private JButton nextDateButton;
    private JButton todayButton;
    
    private FoodPanel foodPanel;
    private DailyLogPanel dailyLogPanel;
    private JPanel profilePanel;
    
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem saveMenuItem;
    private JMenuItem exitMenuItem;
    private JMenu editMenu;
    private JMenuItem undoMenuItem;
    private JMenuItem redoMenuItem;
    private JMenu userMenu;
    private JMenuItem loginMenuItem;
    private JMenuItem logoutMenuItem;
    
    /**
     * Constructor for MainWindow.
     * 
     * @param dietManager The diet manager
     */
    public MainWindow(DietManager dietManager) {
        super("YADA - Yet Another Diet App");
        this.dietManager = dietManager;
        this.currentDate = LocalDate.now();
        
        initComponents();
        
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        // Show login dialog on startup
        showLoginDialog();
    }
    
    /**
     * Initialize the UI components.
     */
    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        
        // Date navigation panel
        datePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        prevDateButton = new JButton("<");
        dateLabel = new JLabel(formatDate(currentDate));
        nextDateButton = new JButton(">");
        todayButton = new JButton("Today");
        
        datePanel.add(prevDateButton);
        datePanel.add(dateLabel);
        datePanel.add(nextDateButton);
        datePanel.add(todayButton);
        
        mainPanel.add(datePanel, BorderLayout.NORTH);
        
        // Tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Food panel
        foodPanel = new FoodPanel(dietManager, this);
        tabbedPane.addTab("Foods", foodPanel);
        
        // Daily log panel
        dailyLogPanel = new DailyLogPanel(dietManager, this);
        tabbedPane.addTab("Daily Log", dailyLogPanel);
        
        // Profile panel
        profilePanel = createProfilePanel();
        tabbedPane.addTab("Profile", profilePanel);
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
        
        // Menu bar
        menuBar = new JMenuBar();
        
        fileMenu = new JMenu("File");
        saveMenuItem = new JMenuItem("Save");
        exitMenuItem = new JMenuItem("Exit");
        fileMenu.add(saveMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
        
        editMenu = new JMenu("Edit");
        undoMenuItem = new JMenuItem("Undo");
        redoMenuItem = new JMenuItem("Redo");
        editMenu.add(undoMenuItem);
        editMenu.add(redoMenuItem);
        
        userMenu = new JMenu("User");
        loginMenuItem = new JMenuItem("Login");
        logoutMenuItem = new JMenuItem("Logout");
        userMenu.add(loginMenuItem);
        userMenu.add(logoutMenuItem);
        
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(userMenu);
        
        setJMenuBar(menuBar);
        
        // Listeners
        prevDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeDate(currentDate.minusDays(1));
            }
        });
        
        nextDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeDate(currentDate.plusDays(1));
            }
        });
        
        todayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeDate(LocalDate.now());
            }
        });
        
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dietManager.getFoodDatabase().save();
                dietManager.getDailyLog().save();
                dietManager.getUserProfile().save();
                
                JOptionPane.showMessageDialog(MainWindow.this, 
                        "All data saved successfully.", 
                        "Save", 
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        undoMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dietManager.undo();
                dailyLogPanel.refreshLog();
            }
        });
        
        redoMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dietManager.redo();
                dailyLogPanel.refreshLog();
            }
        });
        
        loginMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoginDialog();
            }
        });
        
        logoutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
    }
    
    /**
     * Create the profile panel.
     * 
     * @return The profile panel
     */
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Daily calorie target panel
        JPanel targetPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel targetLabel = new JLabel(String.format("Daily Calorie Target: %.1f", 
                dietManager.getUserProfile().calculateDailyCalories()));
        targetLabel.setFont(new Font("Dialog", Font.BOLD, 18));
        targetPanel.add(targetLabel);
        
        panel.add(targetPanel, BorderLayout.NORTH);
        
        // Profile summary panel
        JPanel summaryPanel = new JPanel(new GridLayout(7, 2, 15, 15));
        summaryPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        // Add labels with current profile values
        UserProfile profile = dietManager.getUserProfile();
        
        summaryPanel.add(createBoldLabel("Gender:"));
        summaryPanel.add(new JLabel(profile.getGender()));
        
        summaryPanel.add(createBoldLabel("Height:"));
        summaryPanel.add(new JLabel(String.format("%.1f cm", profile.getHeight())));
        
        summaryPanel.add(createBoldLabel("Weight:"));
        summaryPanel.add(new JLabel(String.format("%.1f kg", profile.getWeight())));
        
        summaryPanel.add(createBoldLabel("Age:"));
        summaryPanel.add(new JLabel(String.valueOf(profile.getAge())));
        
        summaryPanel.add(createBoldLabel("Activity Level:"));
        summaryPanel.add(new JLabel(profile.getActivityLevel()));
        
        summaryPanel.add(createBoldLabel("Calculation Method:"));
        summaryPanel.add(new JLabel(profile.getCalorieCalculationMethod()));
        
        // Activity level description
        summaryPanel.add(createBoldLabel("Activity Description:"));
        
        // Create description based on activity level
        String description = getActivityLevelDescription(profile.getActivityLevel());
        JTextArea descriptionArea = new JTextArea(description);
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBackground(panel.getBackground());
        descriptionArea.setBorder(BorderFactory.createEmptyBorder());
        
        summaryPanel.add(descriptionArea);
        
        panel.add(summaryPanel, BorderLayout.CENTER);
        
        // Button to open profile dialog
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton editProfileButton = new JButton("Edit Profile");
        editProfileButton.setFont(new Font("Dialog", Font.BOLD, 14));
        editProfileButton.setPreferredSize(new Dimension(150, 40));
        editProfileButton.setBackground(new Color(92, 184, 92));
        editProfileButton.setForeground(Color.WHITE);
        buttonPanel.add(editProfileButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Edit button listener
        editProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProfileDialog profileDialog = new ProfileDialog(MainWindow.this, dietManager, false);
                profileDialog.setVisible(true);
                
                if (profileDialog.isProfileSaved()) {
                    // Update the display with new profile values
                    targetLabel.setText(String.format("Daily Calorie Target: %.1f", 
                            dietManager.getUserProfile().calculateDailyCalories()));
                    
                    // Update entire panel by recreating it
                    tabbedPane.setComponentAt(2, createProfilePanel());
                    
                    // Refresh daily log to update calorie calculations
                    dailyLogPanel.refreshLog();
                }
            }
        });
        
        return panel;
    }
    
    /**
     * Get the description for an activity level.
     * 
     * @param activityLevel The activity level
     * @return The description
     */
    private String getActivityLevelDescription(String activityLevel) {
        switch (activityLevel) {
            case "sedentary":
                return "Little or no exercise, desk job (multiplier: 1.2)\n" +
                        "Examples: Office worker with no regular exercise, person with mobility limitations";
            case "light":
                return "Light exercise 1-3 days/week (multiplier: 1.375)\n" +
                        "Examples: Walking 1-3 days per week, light housework, casual cycling";
            case "moderate":
                return "Moderate exercise 3-5 days/week (multiplier: 1.55)\n" +
                        "Examples: Jogging, cycling, swimming, or gym workouts 3-5 days per week";
            case "active":
                return "Heavy exercise 6-7 days/week (multiplier: 1.725)\n" +
                        "Examples: Daily exercise, hard manual labor job, competitive sports training";
            case "very active":
                return "Very heavy exercise, physical job or training twice daily (multiplier: 1.9)\n" +
                        "Examples: Professional athletes, very intense workout schedule, heavy manual labor plus additional training";
            default:
                return "";
        }
    }
    
    /**
     * Create a bold label.
     * 
     * @param text The label text
     * @return The label
     */
    private JLabel createBoldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Dialog", Font.BOLD, 14));
        return label;
    }
    
    /**
     * Show the login dialog.
     */
    private void showLoginDialog() {
        LoginDialog loginDialog = new LoginDialog(this, dietManager);
        loginDialog.setVisible(true);
        
        User user = loginDialog.getLoggedInUser();
        if (user != null) {
            dietManager.setCurrentUser(user);
            JOptionPane.showMessageDialog(this, 
                    "Welcome, " + user.getUsername() + "!", 
                    "Login Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            updateUserMenuItems();
        } else {
            // If user canceled login and no previous user was logged in, exit the application
            if (dietManager.getCurrentUser() == null) {
                System.exit(0);
            }
        }
    }
    
    /**
     * Logout the current user.
     */
    private void logout() {
        if (dietManager.getCurrentUser() != null) {
            int option = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to logout?", 
                    "Confirm Logout", 
                    JOptionPane.YES_NO_OPTION);
            
            if (option == JOptionPane.YES_OPTION) {
                dietManager.setCurrentUser(null);
                updateUserMenuItems();
                showLoginDialog();
            }
        }
    }
    
    /**
     * Update the user menu items based on login status.
     */
    private void updateUserMenuItems() {
        boolean loggedIn = dietManager.getCurrentUser() != null;
        loginMenuItem.setEnabled(!loggedIn);
        logoutMenuItem.setEnabled(loggedIn);
        
        if (loggedIn) {
            userMenu.setText("User: " + dietManager.getCurrentUser().getUsername());
        } else {
            userMenu.setText("User");
        }
    }
    
    /**
     * Change the current date.
     * 
     * @param newDate The new date
     */
    private void changeDate(LocalDate newDate) {
        currentDate = newDate;
        dateLabel.setText(formatDate(currentDate));
        
        // Refresh panels that depend on the date
        dailyLogPanel.refreshLog();
    }
    
    /**
     * Format a date for display.
     * 
     * @param date The date to format
     * @return The formatted date string
     */
    private String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"));
    }
    
    /**
     * Get the current date.
     * 
     * @return The current date
     */
    public LocalDate getCurrentDate() {
        return currentDate;
    }
    
    /**
     * Get the daily log panel.
     * 
     * @return The daily log panel
     */
    public DailyLogPanel getDailyLogPanel() {
        return dailyLogPanel;
    }
    
    /**
     * Update the calorie display after profile changes.
     */
    public void updateCalorieDisplay() {
        dailyLogPanel.refreshLog();
    }
}