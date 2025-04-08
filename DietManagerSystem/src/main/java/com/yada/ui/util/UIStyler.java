package com.yada.ui.util;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Utility class for styling UI components with a consistent visual theme.
 */
public class UIStyler {
    // Color palette
    public static final Color PRIMARY_COLOR = new Color(41, 128, 185); // Blue
    public static final Color SECONDARY_COLOR = new Color(52, 152, 219); // Light Blue
    public static final Color ACCENT_COLOR = new Color(46, 204, 113); // Green
    public static final Color WARNING_COLOR = new Color(230, 126, 34); // Orange
    public static final Color DANGER_COLOR = new Color(231, 76, 60); // Red
    public static final Color LIGHT_COLOR = new Color(236, 240, 241); // Light Gray
    public static final Color DARK_COLOR = new Color(52, 73, 94); // Dark Blue/Gray
    // In UIStyler.java
    public static final Color INFO_COLOR = new Color(91, 192, 222);
    
    // Background colors
    public static final Color BACKGROUND_COLOR = new Color(245, 248, 250); // Very Light Gray/Blue
    public static final Color CARD_BACKGROUND_COLOR = Color.WHITE;
    
    // Text colors
    public static final Color TEXT_PRIMARY_COLOR = new Color(44, 62, 80); // Dark
    public static final Color TEXT_SECONDARY_COLOR = new Color(127, 140, 141); // Medium Gray
    
    // Fonts
    public static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 18);
    public static final Font SUBTITLE_FONT = new Font("SansSerif", Font.BOLD, 16);
    public static final Font HEADER_FONT = new Font("SansSerif", Font.BOLD, 20);
    public static final Font BODY_FONT = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font LABEL_FONT = new Font("SansSerif", Font.BOLD, 14);
    public static final Font SMALL_FONT = new Font("SansSerif", Font.PLAIN, 12);
    
    // Dimensions
    public static final int BUTTON_HEIGHT = 35;
    public static final int TEXT_FIELD_HEIGHT = 35;
    public static final int BORDER_RADIUS = 15;
    public static final Dimension BUTTON_DIMENSION = new Dimension(150, BUTTON_HEIGHT);
    
    // Border and Insets
    public static final int PADDING = 15;
    public static final Insets LABEL_INSETS = new Insets(0, 0, 5, 0);
    
    /**
     * Styles a label with standard formatting.
     *
     * @param label The label to style
     * @return The styled label
     */
    public static JLabel styleLabel(JLabel label) {
        label.setFont(LABEL_FONT);
        label.setForeground(TEXT_PRIMARY_COLOR);
        return label;
    }
    
    /**
     * Styles a header label with larger, bolder formatting.
     *
     * @param label The label to style
     * @return The styled label
     */
    public static JLabel styleHeaderLabel(JLabel label) {
        label.setFont(HEADER_FONT);
        label.setForeground(TEXT_PRIMARY_COLOR);
        label.setBorder(new EmptyBorder(0, 0, 10, 0));
        return label;
    }
    
    /**
     * Styles a title label.
     *
     * @param label The label to style
     * @return The styled label
     */
    public static JLabel styleTitleLabel(JLabel label) {
        label.setFont(TITLE_FONT);
        label.setForeground(TEXT_PRIMARY_COLOR);
        label.setBorder(new EmptyBorder(0, 0, 8, 0));
        return label;
    }
    
    /**
     * Styles a password field.
     *
     * @param field The password field to style
     * @return The styled password field
     */
    public static JPasswordField stylePasswordField(JPasswordField field) {
        field.setFont(BODY_FONT);
        field.setPreferredSize(new Dimension(field.getPreferredSize().width, TEXT_FIELD_HEIGHT));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(204, 204, 204), 1, true),
                new EmptyBorder(5, 8, 5, 8)
        ));
        return field;
    }
    
    /**
     * Styles a combo box.
     *
     * @param comboBox The combo box to style
     * @return The styled combo box
     */
    public static <T> JComboBox<T> styleComboBox(JComboBox<T> comboBox) {
        comboBox.setFont(BODY_FONT);
        comboBox.setBackground(CARD_BACKGROUND_COLOR);
        comboBox.setForeground(TEXT_PRIMARY_COLOR);
        comboBox.setPreferredSize(new Dimension(comboBox.getPreferredSize().width, TEXT_FIELD_HEIGHT));
        return comboBox;
    }
    
    /**
     * Styles a tabbed pane.
     *
     * @param tabbedPane The tabbed pane to style
     * @return The styled tabbed pane
     */
    public static JTabbedPane styleTabbedPane(JTabbedPane tabbedPane) {
        tabbedPane.setFont(SUBTITLE_FONT);
        tabbedPane.setBackground(BACKGROUND_COLOR);
        tabbedPane.setForeground(TEXT_PRIMARY_COLOR);
        tabbedPane.setBorder(BorderFactory.createEmptyBorder());
        return tabbedPane;
    }
}