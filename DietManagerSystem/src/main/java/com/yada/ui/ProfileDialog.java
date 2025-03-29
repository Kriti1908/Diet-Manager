package com.yada.ui;

import com.yada.DietManager;
import com.yada.model.UserProfile;
import com.yada.ui.util.RoundButton;
import com.yada.ui.util.RoundPanel;
import com.yada.ui.util.RoundTextField;
import com.yada.ui.util.UIStyler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Dialog for creating or editing a user profile.
 */
public class ProfileDialog extends JDialog {
    private DietManager dietManager;
    private boolean isNewUser;
    private boolean profileSaved;
    
    private JComboBox<String> genderComboBox;
    private JTextField heightField;
    private JTextField weightField;
    private JTextField ageField;
    private JComboBox<String> activityComboBox;
    private JComboBox<String> methodComboBox;
    private JTextArea activityDescriptionArea;
    
    /**
     * Constructor for ProfileDialog.
     * 
     * @param parent The parent frame
     * @param dietManager The diet manager
     * @param isNewUser Whether this is for a new user
     */
    public ProfileDialog(JFrame parent, DietManager dietManager, boolean isNewUser) {
        super(parent, isNewUser ? "Create Your Profile" : "Edit Profile", true);
        this.dietManager = dietManager;
        this.isNewUser = isNewUser;
        this.profileSaved = false;
        
        initComponents();
        
        pack();
        setMinimumSize(new Dimension(550, 600));
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(isNewUser ? WindowConstants.DO_NOTHING_ON_CLOSE : WindowConstants.DISPOSE_ON_CLOSE);
    }
    
    /**
     * Initialize the UI components.
     */
    private void initComponents() {
        UserProfile profile = dietManager.getUserProfile();
        
        // Set main content panel with background
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(UIStyler.BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Create a round panel for the main content
        RoundPanel mainPanel = new RoundPanel();
        mainPanel.setLayout(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        JLabel titleLabel = UIStyler.styleHeaderLabel(
                new JLabel(isNewUser ? "Welcome! Let's set up your profile" : "Update Your Profile"));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        
        // Add a subtitle
        JLabel subtitleLabel = new JLabel("This information helps us calculate your daily calorie needs");
        subtitleLabel.setFont(UIStyler.BODY_FONT);
        subtitleLabel.setForeground(UIStyler.TEXT_SECONDARY_COLOR);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Form panel with GridBagLayout for better control
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.weightx = 1.0;
        
        // Gender
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(createStyledLabel("Gender:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        String[] genders = {"male", "female"};
        genderComboBox = UIStyler.styleComboBox(new JComboBox<>(genders));
        genderComboBox.setSelectedItem(profile.getGender());
        formPanel.add(genderComboBox, gbc);
        
        // Height
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(createStyledLabel("Height (cm):"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        heightField = new RoundTextField(10);
        heightField.setText(String.valueOf(profile.getHeight()));
        formPanel.add(heightField, gbc);
        
        // Weight
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(createStyledLabel("Weight (kg):"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        weightField = new RoundTextField(10);
        weightField.setText(String.valueOf(profile.getWeight()));
        formPanel.add(weightField, gbc);
        
        // Age
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(createStyledLabel("Age:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        ageField = new RoundTextField(10);
        ageField.setText(String.valueOf(profile.getAge()));
        formPanel.add(ageField, gbc);
        
        // Activity Level
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(createStyledLabel("Activity Level:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        String[] activityLevels = {"sedentary", "light", "moderate", "active", "very active"};
        activityComboBox = UIStyler.styleComboBox(new JComboBox<>(activityLevels));
        activityComboBox.setSelectedItem(profile.getActivityLevel());
        formPanel.add(activityComboBox, gbc);
        
        // Calculation Method
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(createStyledLabel("Calorie Calculation Method:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        String[] methods = {"Harris-Benedict", "Mifflin-St Jeor"};
        methodComboBox = UIStyler.styleComboBox(new JComboBox<>(methods));
        methodComboBox.setSelectedItem(profile.getCalorieCalculationMethod());
        formPanel.add(methodComboBox, gbc);
        
        // Activity Level Description - span both columns
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        formPanel.add(createStyledLabel("Activity Level Description:"), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        
        activityDescriptionArea = new JTextArea(4, 20);
        activityDescriptionArea.setEditable(false);
        activityDescriptionArea.setLineWrap(true);
        activityDescriptionArea.setWrapStyleWord(true);
        activityDescriptionArea.setFont(UIStyler.BODY_FONT);
        activityDescriptionArea.setForeground(UIStyler.TEXT_PRIMARY_COLOR);
        activityDescriptionArea.setBackground(UIStyler.LIGHT_COLOR);
        activityDescriptionArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(218, 223, 225), 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        updateActivityDescription((String) activityComboBox.getSelectedItem());
        
        JScrollPane descScrollPane = new JScrollPane(activityDescriptionArea);
        descScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        formPanel.add(descScrollPane, gbc);
        
        // Add form to main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        RoundButton saveButton = new RoundButton("Save Profile", 
                UIStyler.ACCENT_COLOR, 
                new Color(69, 179, 157), 
                new Color(46, 139, 87), 
                Color.WHITE);
        
        if (!isNewUser) {
            RoundButton cancelButton = new RoundButton("Cancel", 
                    UIStyler.LIGHT_COLOR, 
                    new Color(225, 229, 234),
                    new Color(213, 216, 220),
                    UIStyler.TEXT_PRIMARY_COLOR);
            
            buttonPanel.add(cancelButton);
            
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
        }
        
        buttonPanel.add(saveButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        contentPanel.add(mainPanel, BorderLayout.CENTER);
        setContentPane(contentPanel);
        
        // Listeners
        activityComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateActivityDescription((String) activityComboBox.getSelectedItem());
            }
        });
        
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveProfile();
            }
        });
    }
    
    /**
     * Create a styled label.
     * 
     * @param text The label text
     * @return The label
     */
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIStyler.TITLE_FONT);
        label.setForeground(UIStyler.TEXT_PRIMARY_COLOR);
        return label;
    }
    
    /**
     * Update the activity level description.
     * 
     * @param activityLevel The activity level
     */
    private void updateActivityDescription(String activityLevel) {
        String description;
        switch (activityLevel) {
            case "sedentary":
                description = "Little or no exercise, desk job (multiplier: 1.2)\n\n" +
                        "Examples: Office worker with no regular exercise, person with mobility limitations";
                break;
            case "light":
                description = "Light exercise 1-3 days/week (multiplier: 1.375)\n\n" +
                        "Examples: Walking 1-3 days per week, light housework, casual cycling";
                break;
            case "moderate":
                description = "Moderate exercise 3-5 days/week (multiplier: 1.55)\n\n" +
                        "Examples: Jogging, cycling, swimming, or gym workouts 3-5 days per week";
                break;
            case "active":
                description = "Heavy exercise 6-7 days/week (multiplier: 1.725)\n\n" +
                        "Examples: Daily exercise, hard manual labor job, competitive sports training";
                break;
            case "very active":
                description = "Very heavy exercise, physical job or training twice daily (multiplier: 1.9)\n\n" +
                        "Examples: Professional athletes, very intense workout schedule, heavy manual labor plus additional training";
                break;
            default:
                description = "";
        }
        
        activityDescriptionArea.setText(description);
    }
    
    /**
     * Save the profile.
     */
    private void saveProfile() {
        try {
            String gender = (String) genderComboBox.getSelectedItem();
            double height = Double.parseDouble(heightField.getText().trim());
            double weight = Double.parseDouble(weightField.getText().trim());
            int age = Integer.parseInt(ageField.getText().trim());
            String activityLevel = (String) activityComboBox.getSelectedItem();
            String calculationMethod = (String) methodComboBox.getSelectedItem();
            
            if (height <= 0 || weight <= 0 || age <= 0) {
                throw new NumberFormatException("Values must be positive");
            }
            
            dietManager.updateUserProfile(gender, height, weight, age, 
                    activityLevel, calculationMethod);
            
            profileSaved = true;
            
            if (isNewUser) {
                JOptionPane.showMessageDialog(this, 
                        "Profile created successfully!", 
                        "Profile Created", 
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Profile updated successfully!", 
                        "Profile Updated", 
                        JOptionPane.INFORMATION_MESSAGE);
            }
            
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                    "Please enter valid numeric values. Height, weight, and age must be positive numbers.", 
                    "Invalid Input", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Check if the profile was saved.
     * 
     * @return Whether the profile was saved
     */
    public boolean isProfileSaved() {
        return profileSaved;
    }
}