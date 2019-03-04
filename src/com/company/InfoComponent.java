package com.company;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class InfoComponent extends GraphicsComponent {
    public static final int width = 0;
    public static final int height = 0;
    public int x;
    public int y;
    public Color color = ColorScheme.GREEN;
    public String text;
    private int score;

    public InfoComponent(int x, int y, String text) {
        super("timer"); // unique identifier
        // set position
        this.x = x;
        this.y = y;
        score = 0;
        this.text = text; // text runs along the top of the game screen
        // the bound shape describes the area where the component is kept in
        boundShape = new Rectangle2D.Double(x, y, width, height);
    }

    public void update() { }

    public void draw(Graphics2D g) {
        g.setPaint(Color.WHITE);
        g.setFont(new Font("default", Font.BOLD, 20));
        g.drawString(text + " - Score: " + score, x, y + 20); // draw white info text

        g.setFont(defaultFont); // set back to default font
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}