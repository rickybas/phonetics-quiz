package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;

public class Button extends JButton {

    public Button(String text) {
        super();

        setFocusable(false); // button not focusable
        setBorderPainted(false); // don't paint border
        this.setText(text); // button text
        setBackground(Color.WHITE); // default background color is white
        setFont(new Font("Serif", Font.BOLD, 40));

        // when clicked
        addActionListener((e) -> Logging.log(Level.INFO, text + " button clicked"));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // don't show button border
        if (isSelected()) {
            setBorder(BorderFactory.createEmptyBorder());
        } else {
            setBorder(BorderFactory.createLoweredBevelBorder());
        }
    }
}
