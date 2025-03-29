package com.yada.ui;

import com.yada.DietManager;
import com.yada.model.Food;
import com.yada.model.LogEntry;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

/**
 * Panel for viewing and managing daily food log.
 */
public class DailyLogPanel extends JPanel {
    private DietManager dietManager;
    private MainWindow mainWindow;
    
    private JPanel summaryPanel;
    private JLabel caloriesConsumedLabel;
    private JLabel targetCaloriesLabel;
    private JLabel remainingCaloriesLabel;
    
    private JTable logTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    
    private JPanel actionPanel;
    private JButton addFoodButton;
    private JButton removeFoodButton;
    private JButton clearLogButton;
    
    /**
     * Constructor for DailyLogPanel.
     * 
     * @param dietManager The diet manager
     * @param mainWindow The main window
     */
    public DailyLogPanel(DietManager dietManager, MainWindow mainWindow) {
        this.dietManager = dietManager;
        this.mainWindow = mainWindow;
        
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        initComponents();
        refreshLog();
    }
    
    /**
     * Initialize the UI components.
     */
    private void initComponents() {
        // Summary panel
        summaryPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        summaryPanel.setBorder(new TitledBorder("Daily Summary"));
        
        summaryPanel.add(new JLabel("Calories Consumed:"));
        caloriesConsumedLabel = new JLabel("0");
        summaryPanel.add(caloriesConsumedLabel);
        
        summaryPanel.add(new JLabel("Target Calories:"));
        targetCaloriesLabel = new JLabel("0");
        summaryPanel.add(targetCaloriesLabel);
        
        summaryPanel.add(new JLabel("Remaining Calories:"));
        remainingCaloriesLabel = new JLabel("0");
        summaryPanel.add(remainingCaloriesLabel);
        
        add(summaryPanel, BorderLayout.NORTH);
        
        // Log table
        tableModel = new DefaultTableModel(
                new Object[]{"Food", "Servings", "Calories"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        logTable = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (column == 2) {  // Calories column
                    double calories = (double) getValueAt(row, 2);
                    if (calories > 300) {
                        c.setForeground(Color.RED);
                    } else if (calories > 150) {
                        c.setForeground(Color.ORANGE.darker());
                    } else {
                        c.setForeground(Color.GREEN.darker());
                    }
                } else {
                    c.setForeground(getForeground());
                }
                return c;
            }
        };
        
        logTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        logTable.getTableHeader().setReorderingAllowed(false);
        
        scrollPane = new JScrollPane(logTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Action panel
        actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addFoodButton = new JButton("Add Food");
        removeFoodButton = new JButton("Remove Food");
        clearLogButton = new JButton("Clear Log");
        
        actionPanel.add(addFoodButton);
        actionPanel.add(removeFoodButton);
        actionPanel.add(clearLogButton);
        
        add(actionPanel, BorderLayout.SOUTH);
        
        // Listeners
        addFoodButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFoodToLog();
            }
        });
        
        removeFoodButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeSelectedFood();
            }
        });
        
        clearLogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearLog();
            }
        });
        
        logTable.getSelectionModel().addListSelectionListener(e -> {
            removeFoodButton.setEnabled(logTable.getSelectedRow() != -1);
        });
        
        // Initial state
        removeFoodButton.setEnabled(false);
    }
    
    /**
     * Refresh the daily log display.
     */
    public void refreshLog() {
        LocalDate currentDate = mainWindow.getCurrentDate();
        List<LogEntry> entries = dietManager.getDailyLog().getEntriesForDate(currentDate);
        
        // Update table
        tableModel.setRowCount(0);
        for (LogEntry entry : entries) {
            tableModel.addRow(new Object[]{
                entry.getFood().getIdentifier(),
                entry.getServings(),
                entry.getCalories()
            });
        }
        
        // If log is empty, show a message prompting user to add food
        if (entries.isEmpty()) {
            // Create an info row to prompt user
            tableModel.addRow(new Object[]{
                "Your food log is empty. Click 'Add Food' to start tracking.",
                "", 
                ""
            });
        }
        
        // Update summary
        double caloriesConsumed = dietManager.getCaloriesConsumed(currentDate);
        double targetCalories = dietManager.getTargetCalories();
        double remainingCalories = dietManager.getRemainingCalories(currentDate);
        
        caloriesConsumedLabel.setText(String.format("%.1f", caloriesConsumed));
        targetCaloriesLabel.setText(String.format("%.1f", targetCalories));
        
        if (remainingCalories >= 0) {
            remainingCaloriesLabel.setText(String.format("%.1f", remainingCalories));
            remainingCaloriesLabel.setForeground(Color.GREEN.darker());
        } else {
            remainingCaloriesLabel.setText(String.format("%.1f (over)", -remainingCalories));
            remainingCaloriesLabel.setForeground(Color.RED);
        }
        
        // Disable remove button if log is empty or showing prompt
        removeFoodButton.setEnabled(entries.size() > 0 && logTable.getSelectedRow() != -1);
    }
    
    /**
     * Add a food to the daily log.
     */
    private void addFoodToLog() {
        // Create a food selection dialog
        JDialog dialog = new JDialog(mainWindow, "Add Food to Log", true);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Food selection
        JPanel selectionPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        selectionPanel.add(new JLabel("Select Food:"));
        
        // Get all foods for combo box
        Object[] foods = dietManager.getFoodDatabase().getAllFoods().toArray();
        JComboBox<Object> foodComboBox = new JComboBox<>(foods);
        selectionPanel.add(foodComboBox);
        
        selectionPanel.add(new JLabel("Servings:"));
        JTextField servingsField = new JTextField("1.0");
        selectionPanel.add(servingsField);
        
        panel.add(selectionPanel, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");
        
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(mainWindow);
        
        // Button listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Food selectedFood = (Food) foodComboBox.getSelectedItem();
                String servingsStr = servingsField.getText().trim();
                
                try {
                    double servings = Double.parseDouble(servingsStr);
                    if (servings <= 0) {
                        throw new NumberFormatException("Servings must be positive");
                    }
                    
                    dietManager.addFoodToLog(mainWindow.getCurrentDate(), selectedFood, servings);
                    refreshLog();
                    dialog.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, 
                            "Please enter a valid positive number for servings.", 
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
    
    /**
     * Remove the selected food from the log.
     */
    private void removeSelectedFood() {
        int selectedRow = logTable.getSelectedRow();
        if (selectedRow != -1) {
            // Check if this is the prompt row (empty log)
            List<LogEntry> entries = dietManager.getDailyLog().getEntriesForDate(mainWindow.getCurrentDate());
            if (entries.isEmpty()) {
                // This is the prompt row, don't try to remove it
                return;
            }
            
            int option = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to remove this food from the log?", 
                    "Confirm Removal", 
                    JOptionPane.YES_NO_OPTION);
            
            if (option == JOptionPane.YES_OPTION) {
                dietManager.removeFoodFromLog(mainWindow.getCurrentDate(), selectedRow);
                refreshLog();
            }
        }
    }
    
    /**
     * Clear the daily log for the current date.
     */
    private void clearLog() {
        int option = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to clear the log for this date?", 
                "Confirm Clear", 
                JOptionPane.YES_NO_OPTION);
        
        if (option == JOptionPane.YES_OPTION) {
            dietManager.getDailyLog().clearEntriesForDate(mainWindow.getCurrentDate());
            refreshLog();
        }
    }
}