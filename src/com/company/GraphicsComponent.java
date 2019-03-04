package com.company;

import java.awt.*;

/**
 * Class that graphical components inherit to be able to draw to the game screen
 */
public class GraphicsComponent {
    public final String id; // unique identifier
    public final Font defaultFont = new Font("default", Font.PLAIN, 16);
    public Shape boundShape; // border around shape

    public GraphicsComponent(String id) {
        this.id = id;
    }

    public void draw(Graphics g) { }
}
