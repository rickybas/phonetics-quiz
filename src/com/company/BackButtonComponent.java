package com.company;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class BackButtonComponent extends GraphicsComponent {
    public static final int width = 150;
    public static final int height = width; // height = width because square
    public final Color color;
    public int x;
    public int y;
    RoundRectangle2D box;

    public BackButtonComponent(int x, int y) {
        super("back_button"); // set the unique identifier

        // the location of the button
        this.x = x;
        this.y = y;

        // color of button
        color = ColorScheme.RED;

        // the bound shape describes the area where the component is kept in
        boundShape = new Rectangle2D.Double(x, y, width, height);
        // the visual box of the button, round the corners of the rectangle
        box = new RoundRectangle2D.Double(x, y, width, height, 20, 20);
    }

//    public void update() { }

    public void draw(Graphics2D graphics2D) {
        graphics2D.setPaint(ColorScheme.RED);
        graphics2D.fill(box); // draw red box

        graphics2D.setPaint(Color.WHITE);
        graphics2D.setFont(new Font("default", Font.PLAIN, 20));
        graphics2D.drawString("Back Button", x + 10, y + 80); // draw white text

        graphics2D.setFont(defaultFont); // set back to default font
    }
}
