package com.company;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class WordBoxComponent extends GraphicsComponent {
    public static final int height = 150;
    public final int width;
    public int x;
    public int y;
    public Color color = ColorScheme.BLUE;
    public String text;
    RoundRectangle2D box;
    private boolean withSound;

    public WordBoxComponent(int x, int y, int width, boolean withSound) {
        super("word_box_button");
        this.x = x;
        this.y = y;
        this.width = width;
        this.text = "";
        this.withSound = withSound;

        // the visual box with rounded corners
        box = new RoundRectangle2D.Double(x, y, width, height, 20, 20);
        // the bound shape describes the area where the component is kept in
        boundShape = new Rectangle2D.Double(x, y, width, height);
    }

    public void update() {
        // box = new RoundRectangle2D.Double(x, y, width, height, 20, 20);
    }

    public void draw(Graphics2D g) {
        g.setPaint(color);
        g.fill(box); // draw box
        g.setPaint(Color.WHITE);
        g.setFont(new Font("default", Font.BOLD, 64));
        g.drawString(text, x + 30, y + 90); // draw word box text
        g.setFont(defaultFont); // set back to default

        /* the user has chosen the game with word sounds then draw a click to replay text in the corner
         * to tell the user that it can click on this box to replay the word
         */
        if (withSound) {
            g.drawString("(click to replay)", x + width - 140, y + 80);
        }

    }
}
