package com.company;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * A class that is used to create consistent style labels for the whole game
 */
public class HeaderLabel extends JLabel {
    public HeaderLabel(String text) {
        super(text);
        setForeground(Color.WHITE); // white for color contrast
        setHorizontalAlignment(JLabel.CENTER);
        setFont(new Font("Serif", Font.BOLD, 40));
        setBorder(new EmptyBorder(10, 0, 0, 0)); // border around it for better positioning
    }
}
