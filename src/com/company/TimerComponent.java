package com.company;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.logging.Level;

public class TimerComponent extends GraphicsComponent {
    public static final int width = 150;
    public static final int height = width; // width is the same as height because its a square
    final public int duration;
    public int x;
    public int y;
    public Color color = ColorScheme.GREEN;
    public int progression;
    RoundRectangle2D box;

    public TimerComponent(int x, int y, int duration) {
        super("timer");
        this.x = x;
        this.y = y;
        this.duration = duration;
        this.progression = duration;
        boundShape = new Rectangle2D.Double(x, y, width, height);
        box = new RoundRectangle2D.Double(x, y, width, height, 20, 20);
    }

    public int minus_one_second() {
        // take away one from the progression of the timer is the timer is not zero
        if (progression - 1 == 0) {
            progression = 0;
            return 0; // is progression is about the equal zero just return zero
        }

        progression = progression - 1;
        return progression;
    }

    public void reset() {
        progression = duration; // reset the timer progression to the start
        Logging.log(Level.INFO, "Countdown timer reset");
    }

    public void update() { }

    public void draw(Graphics2D graphics2D) {
        graphics2D.setPaint(ColorScheme.GREEN);
        graphics2D.fill(box); // draw the main box
        graphics2D.setPaint(Color.BLACK);
        graphics2D.setFont(new Font("default", Font.PLAIN, 50));
        graphics2D.drawString(Integer.toString(progression), x + 30, y + 90); // draw the timer count in black in the box

        graphics2D.setFont(defaultFont); // set back to default font
    }
}