package com.company;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class SkipButtonComponent extends GraphicsComponent {
    public static final int width = 150;
    public static final int height = width; // height = width because square
    public int x;
    public Color color = ColorScheme.RED;
    RoundRectangle2D box;
    private int y;

    public SkipButtonComponent(int x, int y) {
        super("skip_button"); // unique identifier

        // position
        this.x = x;
        this.y = y;

        // border of the components
        boundShape = new Rectangle2D.Double(x, y, width, height);
        // the box that will be drawn
        box = new RoundRectangle2D.Double(x, y, width, height, 20, 20);
    }

    // public void update() { }

    public void draw(Graphics2D g) {
        g.setPaint(ColorScheme.YELLOW);
        g.fill(box); // draw yellow box
        g.setPaint(Color.BLACK);
        g.setFont(new Font("default", Font.PLAIN, 20));
        g.drawString("Skip Button", x + 10, y + 80); // draw black button text

        g.setFont(defaultFont); // set back to default font
    }
}