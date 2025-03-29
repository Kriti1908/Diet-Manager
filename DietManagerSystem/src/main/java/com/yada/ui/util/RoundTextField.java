package com.yada.ui.util;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * A JTextField with rounded corners and modern styling.
 */
public class RoundTextField extends JTextField {
    private Color borderColor = new Color(204, 204, 204);
    private Color focusBorderColor = UIStyler.PRIMARY_COLOR;
    private int cornerRadius = UIStyler.BORDER_RADIUS;
    private int borderThickness = 1;
    private boolean focus = false;
    
    /**
     * Creates a new RoundTextField with default settings.
     */
    public RoundTextField() {
        initTextField();
    }
    
    /**
     * Creates a new RoundTextField with the specified number of columns.
     * 
     * @param columns The number of columns
     */
    public RoundTextField(int columns) {
        super(columns);
        initTextField();
    }
    
    /**
     * Creates a new RoundTextField with the specified text.
     * 
     * @param text The initial text
     */
    public RoundTextField(String text) {
        super(text);
        initTextField();
    }
    
    /**
     * Creates a new RoundTextField with the specified text and number of columns.
     * 
     * @param text The initial text
     * @param columns The number of columns
     */
    public RoundTextField(String text, int columns) {
        super(text, columns);
        initTextField();
    }
    
    /**
     * Initializes the text field.
     */
    private void initTextField() {
        setOpaque(false);
        setBackground(UIStyler.CARD_BACKGROUND_COLOR);
        setForeground(UIStyler.TEXT_PRIMARY_COLOR);
        setFont(UIStyler.BODY_FONT);
        setPreferredSize(new Dimension(getPreferredSize().width, UIStyler.TEXT_FIELD_HEIGHT));
        
        // Use custom rounded border
        setBorder(new RoundBorder(cornerRadius, borderThickness, borderColor));
        
        // Handle focus for visual feedback
        addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                focus = true;
                repaint();
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                focus = false;
                repaint();
            }
        });
        
        // Set text margin through a wrapper border
        setMargin(new Insets(5, 8, 5, 8));
    }
    
    /**
     * Sets the margin of the text field.
     * 
     * @param insets The margin insets
     */
    public void setMargin(Insets insets) {
        // Create a compound border with an empty border for text margin
        setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(cornerRadius, borderThickness, focus ? focusBorderColor : borderColor),
                new EmptyBorder(insets)
        ));
    }
    
    /**
     * Custom border for the rounded text field.
     */
    private class RoundBorder extends AbstractBorder {
        private final int radius;
        private final int thickness;
        private final Color color;
        
        public RoundBorder(int radius, int thickness, Color color) {
            this.radius = radius;
            this.thickness = thickness;
            this.color = color;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setStroke(new BasicStroke(thickness));
            g2.setColor(color);
            g2.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, radius, radius));
            g2.dispose();
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(thickness, thickness, thickness, thickness);
        }
        
        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = insets.top = insets.bottom = thickness;
            return insets;
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Paint text field background
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius));
        
        super.paintComponent(g);
        g2.dispose();
    }
}