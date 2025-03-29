package com.yada.ui.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * A custom button with rounded corners and hover effects.
 */
public class RoundButton extends JButton {
    private Color backgroundColor = UIStyler.PRIMARY_COLOR;
    private Color hoverColor = new Color(52, 152, 219); // Slightly lighter blue
    private Color pressedColor = new Color(41, 128, 185); // Darker blue
    private Color textColor = Color.WHITE;
    private int cornerRadius = UIStyler.BORDER_RADIUS;
    
    private boolean isHovered = false;
    private boolean isPressed = false;
    
    /**
     * Creates a new RoundButton with default settings.
     */
    public RoundButton() {
        this("");
    }
    
    /**
     * Creates a new RoundButton with the specified text.
     * 
     * @param text The button text
     */
    public RoundButton(String text) {
        super(text);
        initButton();
    }
    
    /**
     * Creates a new RoundButton with the specified text and icon.
     * 
     * @param text The button text
     * @param icon The button icon
     */
    public RoundButton(String text, Icon icon) {
        super(text, icon);
        initButton();
    }
    
    /**
     * Creates a new RoundButton with the specified text and custom colors.
     * 
     * @param text The button text
     * @param backgroundColor The background color
     * @param hoverColor The hover color
     * @param pressedColor The pressed color
     * @param textColor The text color
     */
    public RoundButton(String text, Color backgroundColor, Color hoverColor, 
                      Color pressedColor, Color textColor) {
        super(text);
        this.backgroundColor = backgroundColor;
        this.hoverColor = hoverColor;
        this.pressedColor = pressedColor;
        this.textColor = textColor;
        initButton();
    }
    
    /**
     * Initializes the button.
     */
    private void initButton() {
        setOpaque(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        
        setForeground(textColor);
        setFont(UIStyler.BODY_FONT);
        
        setPreferredSize(new Dimension(getPreferredSize().width, UIStyler.BUTTON_HEIGHT));
        
        // Add hover and press effects
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Determine the current background color based on button state
        Color currentColor;
        if (isPressed) {
            currentColor = pressedColor;
        } else if (isHovered) {
            currentColor = hoverColor;
        } else {
            currentColor = backgroundColor;
        }
        
        // Draw button background
        g2.setColor(currentColor);
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));
        
        // Add subtle 3D effect - lighter top/left edge for "raised" look
        g2.setColor(new Color(255, 255, 255, 50));
        g2.setStroke(new BasicStroke(1f));
        g2.draw(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));
        
        // Let the UI delegate draw the text and icon
        super.paintComponent(g2);
        g2.dispose();
    }
    
    @Override
    public boolean contains(int x, int y) {
        // Make hit detection respect the rounded corners
        return new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius).contains(x, y);
    }
}