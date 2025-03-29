package com.yada.ui;

import com.yada.DietManager;
import com.yada.model.Food;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Dialog for creating composite foods.
 */
public class CreateCompositeFoodDialog extends JDialog {
    private DietManager dietManager;
    private boolean success;
    
    private JPanel mainPanel;
    private JPanel topPanel;
    private JPanel componentsPanel;
    private JPanel buttonPanel;
    
    private JTextField nameField;
    private JTextField keywordsField;
    
    private JComboBox<Food> foodComboBox;
    private JTextField servingsField;
    private JButton addComponentButton;
    private JButton removeComponentButton;
    
    private JTable componentsTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    
    private JButton createButton;
    private JButton cancelButton;
    
    /**
     * Constructor for CreateCompositeFoodDialog.
     * 
     * @param parent The parent frame
     * @param dietManager The diet manager
     */
    public CreateCompositeFoodDialog(JFrame parent, DietManager dietManager) {
        super(parent, "Create Composite Food", true);
        this.dietManager = dietManager;
        this.success = false;
        
        initComponents();
        loadFoods();
        
        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
    
    /**
     * Initialize the UI components.
     */
    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Top panel for name and keywords
        topPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        topPanel.add(new JLabel("Name:"));
        nameField = new JTextField(20);
        topPanel.add(nameField);
        topPanel.add(new JLabel("Keywords (comma separated):"));
        keywordsField = new JTextField(20);
        topPanel.add(keywordsField);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Components panel
        componentsPanel = new JPanel(new BorderLayout());
        componentsPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        // Panel for adding components
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addPanel.add(new JLabel("Add Food:"));
        foodComboBox = new JComboBox<>();
        addPanel.add(foodComboBox);
        addPanel.add(new JLabel("Servings:"));
        servingsField = new JTextField("1.0", 5);
        addPanel.add(servingsField);
        addComponentButton = new JButton("Add");
        addPanel.add(addComponentButton);
        
        componentsPanel.add(addPanel, BorderLayout.NORTH);
        
        // Table for components
        tableModel = new DefaultTableModel(
                new Object[]{"Food", "Servings", "Calories"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        componentsTable = new JTable(tableModel);
        componentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        componentsTable.getTableHeader().setReorderingAllowed(false);
        
        scrollPane = new JScrollPane(componentsTable);
        componentsPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Remove button
        JPanel removePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        removeComponentButton = new JButton("Remove Selected");
        removeComponentButton.setEnabled(false);
        removePanel.add(removeComponentButton);
        
        componentsPanel.add(removePanel, BorderLayout.SOUTH);
        
        mainPanel.add(componentsPanel, BorderLayout.CENTER);
        
        // Button panel
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        createButton = new JButton("Create");
        cancelButton = new JButton("Cancel");
        
        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        setContentPane(mainPanel);
        
        // Listeners
        addComponentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addComponent();
            }
        });
        
        removeComponentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeComponent();
            }
        });
        
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createCompositeFood();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        componentsTable.getSelectionModel().addListSelectionListener(e -> {
            removeComponentButton.setEnabled(componentsTable.getSelectedRow() != -1);
        });
    }
    
    /**
     * Load available foods into the combo box.
     */
    private void loadFoods() {
        foodComboBox.removeAllItems();
        List<Food> allFoods = dietManager.getFoodDatabase().getAllFoods();
        
        for (Food food : allFoods) {
            foodComboBox.addItem(food);
        }
        
        if (foodComboBox.getItemCount() > 0) {
            foodComboBox.setSelectedIndex(0);
        }
    }
    
    /**
     * Add a component food to the composite food.
     */
    private void addComponent() {
        Food selectedFood = (Food) foodComboBox.getSelectedItem();
        if (selectedFood == null) {
            JOptionPane.showMessageDialog(this, 
                    "Please select a food to add.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String servingsStr = servingsField.getText().trim();
        double servings;
        
        try {
            servings = Double.parseDouble(servingsStr);
            if (servings <= 0) {
                throw new NumberFormatException("Servings must be positive");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                    "Please enter a valid positive number for servings.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        double calories = selectedFood.getCaloriesPerServing() * servings;
        
        tableModel.addRow(new Object[]{
            selectedFood,
            servings,
            calories
        });
        
        servingsField.setText("1.0");  // Reset to default
    }
    
    /**
     * Remove a component food from the composite food.
     */
    private void removeComponent() {
        int selectedRow = componentsTable.getSelectedRow();
        if (selectedRow != -1) {
            tableModel.removeRow(selectedRow);
        }
    }
    
    /**
     * Create the composite food.
     */
    private void createCompositeFood() {
        String name = nameField.getText().trim();
        String keywordsText = keywordsField.getText().trim();
        
        if (name.isEmpty() || keywordsText.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                    "Please enter both name and keywords.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, 
                    "Please add at least one component food.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Check if name is already in use
        String identifier = name;
        Food existing = dietManager.getFoodDatabase().getFoodByIdentifier(identifier);
        if (existing != null) {
            JOptionPane.showMessageDialog(this, 
                    "A food with this name already exists.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Parse keywords
        String[] keywords = keywordsText.split(",");
        for (int i = 0; i < keywords.length; i++) {
            keywords[i] = keywords[i].trim();
        }
        
        // Get components and servings
        List<Food> components = new ArrayList<>();
        List<Double> servings = new ArrayList<>();
        
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Food food = (Food) tableModel.getValueAt(i, 0);
            double serving = (Double) tableModel.getValueAt(i, 1);
            
            components.add(food);
            servings.add(serving);
        }
        
        // Create the composite food
        success = dietManager.createCompositeFood(identifier, keywords, 
                components, servings) != null;
        
        if (success) {
            JOptionPane.showMessageDialog(this, 
                    "Composite food created successfully.", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, 
                    "Failed to create composite food.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Check if the composite food was created successfully.
     * 
     * @return true if successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }
}