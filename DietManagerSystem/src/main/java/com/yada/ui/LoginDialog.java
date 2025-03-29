package com.yada.ui;

import com.yada.DietManager;
import com.yada.user.User;
import com.yada.user.UserManager;
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
 * Dialog for user login and registration.
 */
public class LoginDialog extends JDialog {
    private DietManager dietManager;
    private UserManager userManager;
    private User loggedInUser;
    
    private JTabbedPane tabbedPane;
    
    // Login tab components
    private JPanel loginPanel;
    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;
    private JButton loginButton;
    
    // Register tab components
    private JPanel registerPanel;
    private JTextField registerUsernameField;
    private JPasswordField registerPasswordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    
    /**
     * Constructor for LoginDialog.
     * 
     * @param parent The parent frame
     * @param dietManager The diet manager
     */
    public LoginDialog(JFrame parent, DietManager dietManager) {
        super(parent, "YADA Login", true);
        this.dietManager = dietManager;
        this.userManager = dietManager.getUserManager();
        this.loggedInUser = null;
        
        initComponents();
        
        pack();
        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
    
    /**
     * Initialize the UI components.
     */
    private void initComponents() {
        // Set main content panel with background
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(UIStyler.BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Create styled tabbed pane
        tabbedPane = UIStyler.styleTabbedPane(new JTabbedPane());
        
        // ===== Login tab =====
        RoundPanel loginRoundPanel = new RoundPanel();
        loginPanel = new JPanel(new BorderLayout(0, 15));
        loginPanel.setOpaque(false);
        
        // Create header section
        JPanel loginHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        loginHeaderPanel.setOpaque(false);
        JLabel loginHeaderLabel = UIStyler.styleHeaderLabel(new JLabel("Welcome Back"));
        loginHeaderPanel.add(loginHeaderLabel);
        loginPanel.add(loginHeaderPanel, BorderLayout.NORTH);
        
        // Create form section with grid bag for more control
        JPanel loginFormPanel = new JPanel(new GridBagLayout());
        loginFormPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Username field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        JLabel usernameLabel = UIStyler.styleLabel(new JLabel("Username:"));
        loginFormPanel.add(usernameLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        loginUsernameField = new RoundTextField(15);
        loginFormPanel.add(loginUsernameField, gbc);
        
        // Password field
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel passwordLabel = UIStyler.styleLabel(new JLabel("Password:"));
        loginFormPanel.add(passwordLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        loginPasswordField = new JPasswordField(15);
        UIStyler.stylePasswordField(loginPasswordField);
        loginFormPanel.add(loginPasswordField, gbc);
        
        loginPanel.add(loginFormPanel, BorderLayout.CENTER);
        
        // Create button panel
        JPanel loginButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        loginButtonPanel.setOpaque(false);
        loginButton = new RoundButton("Login");
        loginButtonPanel.add(loginButton);
        
        loginPanel.add(loginButtonPanel, BorderLayout.SOUTH);
        loginRoundPanel.add(loginPanel);
        
        // ===== Register tab =====
        RoundPanel registerRoundPanel = new RoundPanel();
        registerPanel = new JPanel(new BorderLayout(0, 15));
        registerPanel.setOpaque(false);
        
        // Create header section
        JPanel registerHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        registerHeaderPanel.setOpaque(false);
        JLabel registerHeaderLabel = UIStyler.styleHeaderLabel(new JLabel("Create Account"));
        registerHeaderPanel.add(registerHeaderLabel);
        registerPanel.add(registerHeaderPanel, BorderLayout.NORTH);
        
        // Create form section with grid bag for more control
        JPanel registerFormPanel = new JPanel(new GridBagLayout());
        registerFormPanel.setOpaque(false);
        GridBagConstraints rgbc = new GridBagConstraints();
        rgbc.fill = GridBagConstraints.HORIZONTAL;
        rgbc.insets = new Insets(5, 5, 5, 5);
        
        // Username field
        rgbc.gridx = 0;
        rgbc.gridy = 0;
        rgbc.gridwidth = 1;
        JLabel regUsernameLabel = UIStyler.styleLabel(new JLabel("Username:"));
        registerFormPanel.add(regUsernameLabel, rgbc);
        
        rgbc.gridx = 0;
        rgbc.gridy = 1;
        registerUsernameField = new RoundTextField(15);
        registerFormPanel.add(registerUsernameField, rgbc);
        
        // Password field
        rgbc.gridx = 0;
        rgbc.gridy = 2;
        JLabel regPasswordLabel = UIStyler.styleLabel(new JLabel("Password:"));
        registerFormPanel.add(regPasswordLabel, rgbc);
        
        rgbc.gridx = 0;
        rgbc.gridy = 3;
        registerPasswordField = new JPasswordField(15);
        UIStyler.stylePasswordField(registerPasswordField);
        registerFormPanel.add(registerPasswordField, rgbc);
        
        // Confirm Password field
        rgbc.gridx = 0;
        rgbc.gridy = 4;
        JLabel confirmPasswordLabel = UIStyler.styleLabel(new JLabel("Confirm Password:"));
        registerFormPanel.add(confirmPasswordLabel, rgbc);
        
        rgbc.gridx = 0;
        rgbc.gridy = 5;
        confirmPasswordField = new JPasswordField(15);
        UIStyler.stylePasswordField(confirmPasswordField);
        registerFormPanel.add(confirmPasswordField, rgbc);
        
        registerPanel.add(registerFormPanel, BorderLayout.CENTER);
        
        // Create button panel
        JPanel registerButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        registerButtonPanel.setOpaque(false);
        registerButton = new RoundButton("Register");
        registerButtonPanel.add(registerButton);
        
        registerPanel.add(registerButtonPanel, BorderLayout.SOUTH);
        registerRoundPanel.add(registerPanel);
        
        // Add tabs
        tabbedPane.addTab("Login", loginRoundPanel);
        tabbedPane.addTab("Register", registerRoundPanel);
        
        contentPanel.add(tabbedPane, BorderLayout.CENTER);
        setContentPane(contentPanel);
        
        // Listeners
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attemptLogin();
            }
        });
        
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attemptRegister();
            }
        });
    }
    
    /**
     * Attempt to log in with the provided credentials.
     */
    private void attemptLogin() {
        String username = loginUsernameField.getText().trim();
        String password = new String(loginPasswordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "Please enter both username and password.", 
                    "Login Error", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        User user = userManager.authenticate(username, password);
        if (user != null) {
            loggedInUser = user;
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                    "Invalid username or password.", 
                    "Login Error", 
                    JOptionPane.ERROR_MESSAGE);
            loginPasswordField.setText("");
        }
    }
    
    /**
     * Attempt to register a new user.
     */
    private void attemptRegister() {
        String username = registerUsernameField.getText().trim();
        String password = new String(registerPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "Please fill in all fields.", 
                    "Registration Error", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, 
                    "Passwords do not match.", 
                    "Registration Error", 
                    JOptionPane.ERROR_MESSAGE);
            registerPasswordField.setText("");
            confirmPasswordField.setText("");
            return;
        }
        
        if (username.contains("|")) {
            JOptionPane.showMessageDialog(this, 
                    "Username cannot contain the '|' character.", 
                    "Registration Error", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if username already exists
        if (userManager.userExists(username)) {
            JOptionPane.showMessageDialog(this, 
                    "Username already exists. Please choose another.", 
                    "Registration Error", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Register the user
        User user = userManager.register(username, password);
        if (user != null) {
            // User was registered successfully, set the current user temporarily to associate profile
            dietManager.setCurrentUser(user);
            
            JOptionPane.showMessageDialog(this, 
                    "Account created successfully! Now let's set up your profile.", 
                    "Registration Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            
            // Show profile setup dialog
            ProfileDialog profileDialog = new ProfileDialog((JFrame) getParent(), dietManager, true);
            profileDialog.setVisible(true);
            
            // Reset current user since they haven't officially logged in yet
            dietManager.setCurrentUser(null);
            
            if (profileDialog.isProfileSaved()) {
                // Clear fields and switch to login tab
                registerUsernameField.setText("");
                registerPasswordField.setText("");
                confirmPasswordField.setText("");
                tabbedPane.setSelectedIndex(0);
                
                // Pre-fill login form with the registered username
                loginUsernameField.setText(username);
                loginPasswordField.setText("");
                
                JOptionPane.showMessageDialog(this, 
                        "Registration complete! You can now log in.", 
                        "Registration Success", 
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Profile not created, but user is still registered
                JOptionPane.showMessageDialog(this, 
                        "Registration successful, but profile setup was not completed. " +
                        "You can update your profile later after logging in.", 
                        "Registration Partial", 
                        JOptionPane.WARNING_MESSAGE);
                
                // Clear fields and switch to login tab
                registerUsernameField.setText("");
                registerPasswordField.setText("");
                confirmPasswordField.setText("");
                tabbedPane.setSelectedIndex(0);
                
                // Pre-fill login form with the registered username
                loginUsernameField.setText(username);
                loginPasswordField.setText("");
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                    "Failed to register user. Please try again.", 
                    "Registration Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Get the logged-in user.
     * 
     * @return The logged-in user, or null if no user is logged in
     */
    public User getLoggedInUser() {
        return loggedInUser;
    }
}