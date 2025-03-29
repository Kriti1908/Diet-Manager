package com.yada.ui.util;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * A custom panel with rounded corners and optional shadow effect.
 */
public class RoundPanel extends JPanel {
    private Color shadowColor = new Color(0, 0, 0, 50);
    private int cornerRadius = UIStyler.BORDER_RADIUS;
    private int shadowSize = 4;
    private boolean dropShadow = true;
    
    /**
     * Creates a new RoundPanel with default settings.
     */
    public RoundPanel() {
        setOpaque(false);
        setLayout(new BorderLayout());
        setBackground(UIStyler.CARD_BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
    
    /**
     * Creates a new RoundPanel with the specified corner radius.
     * 
     * @param cornerRadius The radius of the corners
     */
    public RoundPanel(int cornerRadius) {
        this();
        this.cornerRadius = cornerRadius;
    }
    
    /**
     * Creates a new RoundPanel with the specified corner radius and background color.
     * 
     * @param cornerRadius The radius of the corners
     * @param backgroundColor The background color
     */
    public RoundPanel(int cornerRadius, Color backgroundColor) {
        this(cornerRadius);
        setBackground(backgroundColor);
    }
    
    /**
     * Sets whether this panel should draw a shadow.
     * 
     * @param dropShadow true to draw a shadow, false otherwise
     */
    public void setDropShadow(boolean dropShadow) {
        this.dropShadow = dropShadow;
        repaint();
    }
    
    /**
     * Sets the shadow size.
     * 
     * @param shadowSize The shadow size in pixels
     */
    public void setShadowSize(int shadowSize) {
        this.shadowSize = shadowSize;
        repaint();
    }
    
    /**
     * Sets the shadow color.
     * 
     * @param shadowColor The shadow color
     */
    public void setShadowColor(Color shadowColor) {
        this.shadowColor = shadowColor;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Shadow (if enabled)
        if (dropShadow) {
            g2.setColor(shadowColor);
            g2.fill(new RoundRectangle2D.Double(
                    shadowSize, shadowSize,
                    getWidth() - 2 * shadowSize,
                    getHeight() - 2 * shadowSize,
                    cornerRadius, cornerRadius));
        }
        
        // Main panel background
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Double(
                0, 0,
                getWidth() - shadowSize,
                getHeight() - shadowSize,
                cornerRadius, cornerRadius));
        
        g2.dispose();
    }
}