package com.yada.ui;

import com.yada.DietManager;
import com.yada.model.Food;
import com.yada.ui.util.RoundButton;
import com.yada.ui.util.RoundPanel;
import com.yada.ui.util.RoundTextField;
import com.yada.ui.util.UIStyler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JLabel;

import com.yada.model.BasicFood;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Panel for displaying and manipulating foods.
 */
public class FoodPanel extends JPanel {
    private DietManager dietManager;
    private MainWindow mainWindow;
    
    private JPanel searchPanel;
    private JTextField searchField;
    private JButton searchButton;
    
    private JTable foodTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    
    private JPanel actionPanel;
    private JButton addFoodButton;
    private JButton addToLogButton;
    private JButton createCompositeButton;
    
    /**
     * Constructor for FoodPanel.
     * 
     * @param dietManager The diet manager
     * @param mainWindow The main window
     */
    public FoodPanel(DietManager dietManager, MainWindow mainWindow) {
        this.dietManager = dietManager;
        this.mainWindow = mainWindow;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(UIStyler.BACKGROUND_COLOR);
        
        initComponents();
        refreshFoods(null);  // Load all foods initially
    }
    
    /**
     * Initialize the UI components.
     */
    private void initComponents() {
        // Search panel
        RoundPanel searchRoundPanel = new RoundPanel();
        searchPanel = new JPanel(new BorderLayout(10, 10));
        searchPanel.setOpaque(false);
        searchPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Add title label to search panel
        JLabel searchTitle = UIStyler.styleTitleLabel(new JLabel("Search Foods"));
        searchPanel.add(searchTitle, BorderLayout.NORTH);
        
        // Search input panel with rounded components
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setOpaque(false);
        searchField = new RoundTextField(15);
        searchButton = new RoundButton("Search");

        // Add to actionPanel initialization:
        // JButton viewNutrientsButton = new RoundButton("View Nutrients", 
        //         UIStyler.INFO_COLOR, new Color(91, 192, 222),
        //         new Color(49, 176, 213), Color.WHITE);
        // actionPanel.add(viewNutrientsButton);
        
        JLabel keywordsLabel = UIStyler.styleLabel(new JLabel("Keywords: "));
        keywordsLabel.setBorder(new EmptyBorder(0, 0, 0, 5)); // Add some spacing
        
        inputPanel.add(keywordsLabel, BorderLayout.WEST);
        inputPanel.add(searchField, BorderLayout.CENTER);
        inputPanel.add(searchButton, BorderLayout.EAST);
        
        searchPanel.add(inputPanel, BorderLayout.CENTER);
        searchRoundPanel.add(searchPanel);
        
        add(searchRoundPanel, BorderLayout.NORTH);
        
        // Food table - Hide keywords column as requested by user
        RoundPanel tablePanel = new RoundPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Remove default padding
        
        tableModel = new DefaultTableModel(
                new Object[]{"Name", "Calories (per serving)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        foodTable = new JTable(tableModel);
        foodTable.setFont(UIStyler.BODY_FONT);
        foodTable.setRowHeight(30); // Taller rows for better readability
        foodTable.setShowGrid(false); // Remove grid lines for cleaner look
        foodTable.setIntercellSpacing(new Dimension(0, 0)); // Remove spacing between cells
        foodTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        foodTable.getTableHeader().setReorderingAllowed(false);
        
        // Style the table header
        JTableHeader header = foodTable.getTableHeader();
        header.setFont(UIStyler.TITLE_FONT);
        header.setBackground(UIStyler.PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 35)); // Taller header
        
        // Set column widths
        foodTable.getColumnModel().getColumn(0).setPreferredWidth(250); // Name column
        foodTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Calories column
        
        // Add some vertical padding inside the table
        JPanel tableContainerPanel = new JPanel(new BorderLayout());
        tableContainerPanel.setBackground(UIStyler.CARD_BACKGROUND_COLOR);
        tableContainerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        scrollPane = new JScrollPane(foodTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(UIStyler.CARD_BACKGROUND_COLOR);
        
        tableContainerPanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.add(tableContainerPanel, BorderLayout.CENTER);
        
        add(tablePanel, BorderLayout.CENTER);
        
        // Action panel - buttons
        RoundPanel actionRoundPanel = new RoundPanel();
        actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        actionPanel.setOpaque(false);
        
        addFoodButton = new RoundButton("Add Basic Food", 
                UIStyler.PRIMARY_COLOR, UIStyler.SECONDARY_COLOR, 
                new Color(41, 128, 185), Color.WHITE);
        
        addToLogButton = new RoundButton("Add to Today's Log", 
                UIStyler.ACCENT_COLOR, new Color(69, 179, 157), 
                new Color(46, 139, 87), Color.WHITE);
        
        createCompositeButton = new RoundButton("Create Composite Food", 
                UIStyler.WARNING_COLOR, new Color(230, 126, 34), 
                new Color(211, 84, 0), Color.WHITE);

        // Add View Nutrients button here
        JButton viewNutrientsButton = new RoundButton("View Nutrients", 
                UIStyler.INFO_COLOR, new Color(91, 192, 222),
                new Color(49, 176, 213), Color.WHITE);        
        
        actionPanel.add(addFoodButton);
        actionPanel.add(addToLogButton);
        actionPanel.add(createCompositeButton);
        actionPanel.add(viewNutrientsButton);  // Add this line
        
        actionRoundPanel.add(actionPanel);
        add(actionRoundPanel, BorderLayout.SOUTH);
        
        // Listeners
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keywords = searchField.getText().trim();
                refreshFoods(keywords.isEmpty() ? null : keywords);
            }
        });

        // Add listener:
        viewNutrientsButton.addActionListener(e -> {
            int row = foodTable.getSelectedRow();
            if (row >= 0) {
                String foodName = (String) tableModel.getValueAt(row, 0);
                Food food = dietManager.getFoodDatabase().getFoodByIdentifier(foodName);
                if (food instanceof BasicFood) {
                    showNutrientDialog((BasicFood) food);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Nutrition information is only available for basic foods.",
                            "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        addFoodButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBasicFood();
            }
        });
        
        addToLogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSelectedFoodToLog();
            }
        });
        
        createCompositeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCreateCompositeDialog();
            }
        });
        
        foodTable.getSelectionModel().addListSelectionListener(e -> {
            addToLogButton.setEnabled(foodTable.getSelectedRow() != -1);
        });
        
        // Initial state
        addToLogButton.setEnabled(false);
    }
    
    /**
     * Refresh the food list based on keywords.
     * 
     * @param keywords The keywords to search for, or null for all foods
     */
    public void refreshFoods(String keywords) {
        List<Food> foods;
        if (keywords != null && !keywords.isEmpty()) {
            foods = dietManager.searchFoods(keywords);
        } else {
            foods = dietManager.getFoodDatabase().getAllFoods();
        }
        
        tableModel.setRowCount(0);
        
        // If no foods found, show a helpful message
        if (foods.isEmpty()) {
            if (keywords != null && !keywords.isEmpty()) {
                // No search results
                tableModel.addRow(new Object[]{
                    "No foods found for '" + keywords + "'",
                    ""
                });
            } else {
                // Empty food database
                tableModel.addRow(new Object[]{
                    "Your food database is empty. Click 'Add Food' to create foods.",
                    ""
                });
            }
        } else {
            // Display found foods
            for (Food food : foods) {
                // We no longer display keywords in the table - they're still used for search
                // but hidden from the UI as requested
                tableModel.addRow(new Object[]{
                    food.getIdentifier(),
                    food.getCaloriesPerServing()
                });
            }
        }
        
        // Disable add to log button if no actual foods are available
        addToLogButton.setEnabled(foods.size() > 0 && foodTable.getSelectedRow() != -1);
    }
    
    /**
     * Add a basic food to the database.
     */
    private void addBasicFood() {
        // Create a food creation dialog with modern styling
        JDialog dialog = new JDialog(mainWindow, "Add Basic Food", true);
        
        // Main content panel with background
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(UIStyler.BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Create a round panel for the main form
        RoundPanel mainPanel = new RoundPanel();
        mainPanel.setLayout(new BorderLayout(15, 15));
        
        // Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);
        JLabel titleLabel = UIStyler.styleHeaderLabel(new JLabel("Add New Food"));
        titlePanel.add(titleLabel);
        
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Form panel with grid bag for better control
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.weightx = 1.0;
        
        // Name field
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(UIStyler.styleLabel(new JLabel("Name:")), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        RoundTextField nameField = new RoundTextField(20);
        formPanel.add(nameField, gbc);
        
        // Keywords field
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(UIStyler.styleLabel(new JLabel("Keywords (comma separated):")), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        RoundTextField keywordsField = new RoundTextField(20);
        formPanel.add(keywordsField, gbc);
        
        // Calories field
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(UIStyler.styleLabel(new JLabel("Calories per serving:")), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        RoundTextField caloriesField = new RoundTextField(20);
        formPanel.add(caloriesField, gbc);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        RoundButton cancelButton = new RoundButton("Cancel", 
                UIStyler.LIGHT_COLOR, 
                new Color(225, 229, 234),
                new Color(213, 216, 220),
                UIStyler.TEXT_PRIMARY_COLOR);
        
        RoundButton addButton = new RoundButton("Add Food", 
                UIStyler.ACCENT_COLOR, 
                new Color(69, 179, 157), 
                new Color(46, 139, 87), 
                Color.WHITE);
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(addButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        contentPanel.add(mainPanel, BorderLayout.CENTER);
        dialog.setContentPane(contentPanel);
        
        // Set size and position
        dialog.pack();
        dialog.setMinimumSize(new Dimension(400, 400));
        dialog.setLocationRelativeTo(mainWindow);
        
        // Button listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String keywordsText = keywordsField.getText().trim();
                String caloriesText = caloriesField.getText().trim();
                
                if (name.isEmpty() || keywordsText.isEmpty() || caloriesText.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, 
                            "Please fill in all fields.", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Check if name is already in use
                String identifier = name;
                Food existing = dietManager.getFoodDatabase().getFoodByIdentifier(identifier);
                if (existing != null) {
                    JOptionPane.showMessageDialog(dialog, 
                            "A food with this name already exists.", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                try {
                    double calories = Double.parseDouble(caloriesText);
                    if (calories <= 0) {
                        throw new NumberFormatException("Calories must be positive");
                    }
                    
                    // Parse keywords
                    String[] keywords = keywordsText.split(",");
                    for (int i = 0; i < keywords.length; i++) {
                        keywords[i] = keywords[i].trim();
                    }
                    
                    // Create the food
                    boolean success = dietManager.createBasicFood(identifier, keywords, calories) != null;
                    
                    if (success) {
                        refreshFoods(null);  // Refresh to show the new food
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, 
                                "Failed to create food.", 
                                "Error", 
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, 
                            "Please enter a valid positive number for calories.", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        
        dialog.setVisible(true);
    }

    // Add new method:
    private void showNutrientDialog(BasicFood food) {
        JDialog dialog = new JDialog(mainWindow, "Nutrition Facts: " + food.getIdentifier(), true);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Create table for nutrients
        String[] columns = {"Nutrient", "Amount per Serving"};
        Map<String, Double> nutrients = food.getAllNutrients();
        Object[][] data = nutrients.entrySet().stream()
            .map(e -> new Object[]{e.getKey(), e.getValue()})
            .toArray(Object[][]::new);

        JTable table = new JTable(data, columns);
        table.setFont(UIStyler.BODY_FONT);
        table.setRowHeight(25);

        // Right-align numeric values
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Add close button
        JButton closeButton = new RoundButton("Close", 
                UIStyler.LIGHT_COLOR, new Color(225, 229, 234),
                new Color(213, 216, 220), UIStyler.TEXT_PRIMARY_COLOR);
        closeButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(closeButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setSize(300, 400);
        dialog.setLocationRelativeTo(mainWindow);
        dialog.setVisible(true);
    }
    
    /**
     * Add the selected food to today's log.
     */
    private void addSelectedFoodToLog() {
        int selectedRow = foodTable.getSelectedRow();
        if (selectedRow != -1) {
            // The name column is now at index 0 after we removed the keywords column
            String foodName = (String) tableModel.getValueAt(selectedRow, 0);
            Food food = dietManager.getFoodDatabase().getFoodByIdentifier(foodName);
            
            if (food != null) {
                // Create a custom serving dialog with modern styling
                JDialog servingDialog = new JDialog(mainWindow, "Add to Daily Log", true);
                
                // Main content panel with background
                JPanel contentPanel = new JPanel(new BorderLayout());
                contentPanel.setBackground(UIStyler.BACKGROUND_COLOR);
                contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
                
                // Create a round panel for the dialog content
                RoundPanel mainPanel = new RoundPanel();
                mainPanel.setLayout(new BorderLayout(15, 15));
                
                // Create header panel
                JPanel headerPanel = new JPanel(new BorderLayout());
                headerPanel.setOpaque(false);
                
                JLabel titleLabel = UIStyler.styleTitleLabel(new JLabel("Add to Today's Log"));
                headerPanel.add(titleLabel, BorderLayout.NORTH);
                
                // Add food info
                JPanel foodInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                foodInfoPanel.setOpaque(false);
                JLabel foodLabel = new JLabel("<html>Food: <b>" + food.getIdentifier() + 
                        "</b><br>Calories per serving: <b>" + food.getCaloriesPerServing() + "</b></html>");
                foodLabel.setFont(UIStyler.BODY_FONT);
                foodInfoPanel.add(foodLabel);
                
                headerPanel.add(foodInfoPanel, BorderLayout.CENTER);
                mainPanel.add(headerPanel, BorderLayout.NORTH);
                
                // Create form panel
                JPanel formPanel = new JPanel(new BorderLayout(10, 10));
                formPanel.setOpaque(false);
                formPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
                
                JLabel servingsLabel = UIStyler.styleLabel(new JLabel("Number of servings:"));
                RoundTextField servingsField = new RoundTextField(10);
                servingsField.setText("1.0");
                
                formPanel.add(servingsLabel, BorderLayout.NORTH);
                formPanel.add(servingsField, BorderLayout.CENTER);
                
                mainPanel.add(formPanel, BorderLayout.CENTER);
                
                // Create button panel
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
                buttonPanel.setOpaque(false);
                
                RoundButton cancelButton = new RoundButton("Cancel", 
                        UIStyler.LIGHT_COLOR, 
                        new Color(225, 229, 234),
                        new Color(213, 216, 220),
                        UIStyler.TEXT_PRIMARY_COLOR);
                
                RoundButton addButton = new RoundButton("Add to Log", 
                        UIStyler.ACCENT_COLOR, 
                        new Color(69, 179, 157), 
                        new Color(46, 139, 87), 
                        Color.WHITE);
                
                buttonPanel.add(cancelButton);
                buttonPanel.add(addButton);
                
                mainPanel.add(buttonPanel, BorderLayout.SOUTH);
                
                contentPanel.add(mainPanel, BorderLayout.CENTER);
                servingDialog.setContentPane(contentPanel);
                
                // Set size and position
                servingDialog.pack();
                servingDialog.setMinimumSize(new Dimension(350, 250));
                servingDialog.setLocationRelativeTo(mainWindow);
                
                // Add button listeners
                addButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String servingsStr = servingsField.getText().trim();
                        
                        if (servingsStr.isEmpty()) {
                            JOptionPane.showMessageDialog(servingDialog, 
                                    "Please enter number of servings.", 
                                    "Error", 
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        
                        try {
                            double servings = Double.parseDouble(servingsStr);
                            if (servings <= 0) {
                                throw new NumberFormatException("Servings must be positive");
                            }
                            
                            dietManager.addFoodToLog(mainWindow.getCurrentDate(), food, servings);
                            
                            // Show success message with food name and calories
                            double totalCalories = food.getCaloriesPerServing() * servings;
                            JOptionPane.showMessageDialog(servingDialog, 
                                    String.format("%s added to log (%.1f servings, %.1f calories).", 
                                            food.getIdentifier(), servings, totalCalories), 
                                    "Success", 
                                    JOptionPane.INFORMATION_MESSAGE);
                            
                            // Refresh the daily log panel if available
                            if (mainWindow.getDailyLogPanel() != null) {
                                mainWindow.getDailyLogPanel().refreshLog();
                            }
                            
                            servingDialog.dispose();
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(servingDialog, 
                                    "Please enter a valid positive number for servings.", 
                                    "Error", 
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
                
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        servingDialog.dispose();
                    }
                });
                
                servingDialog.setVisible(true);
            }
        }
    }
    
    /**
     * Show the dialog for creating a composite food.
     */
    private void showCreateCompositeDialog() {
        CreateCompositeFoodDialog dialog = new CreateCompositeFoodDialog(mainWindow, dietManager);
        dialog.setVisible(true);
        
        if (dialog.isSuccess()) {
            refreshFoods(null);  // Refresh to show the new food
        }
    }
}