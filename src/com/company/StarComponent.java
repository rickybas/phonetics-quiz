package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;

public class StarComponent extends GraphicsComponent {
    public static final int width = 180;
    public static final int height = width; // height = width because square
    public int x;
    public int y;
    public int originalX;
    public int originalY;
    private int preX;
    private int preY;
    private double brightnessCount;
    private double randomDelay;
    private double randomTimeShrink;
    private BufferedImage starImage;

    public StarComponent(String id, int x, int y) {
        super(id); // the unique identifier

        // position
        this.x = x;
        this.y = y;

        // the position of the star before its dragged
        this.originalX = x;
        this.originalY = y;

        // the position of the star in the frame before
        this.preX = this.x;
        this.preY = this.y;

        // a count updated every frame that determines the brightness
        this.brightnessCount = 0.0;

        // the border of the shape
        boundShape = new Rectangle2D.Double(x, y, width, height);

        /* set up random numbers that will create a sin wave that starts at a random position
        *  and is stretched by a random amount */
        Random random = new Random();
        randomDelay = (random.nextInt(630) + 0)/100; // double between 0 and 6.3 (~2PI)
        randomTimeShrink = 0.5 + random.nextDouble()*3; // double between 0.5 and 3

        Logging.log(Level.INFO, "Star " + id + " (" + x + ", " + y + "), random motion: delay = " + randomDelay + ", stretch = " + randomTimeShrink);

        try {
            starImage = ImageIO.read(this.getClass().getResource("/star.png")); // load the star image from resources
        } catch (IOException e) {
            Logging.logger.log(Level.WARNING, e.getMessage());
            e.printStackTrace();
        }
    }

    public void update() {
        brightnessCount += 2*Math.PI/60; // 2PI/60 equal intervals in each frame (60 frames per seconds)
        /* Move sin wave up by 1 and then times by half to get the values between 0 and 1.
        *  randomTimeShrink shrinks the sin wave, brightness count is the x position of the brightness on the sin wave (increased every frame),
        *  random delay starts the sin wave off at a random position. */
        float brightness = (float) (1 + 0.5 * Math.sin(randomTimeShrink * (brightnessCount + randomDelay)));
        brightness = 0.7f + brightness * 0.3f;

        // update image brightness
        RescaleOp rescaleOp = new RescaleOp(brightness, 15, null);
        rescaleOp.filter(starImage, starImage);

        // if x, y position has changed
        if (preX != this.x || preY != this.y) {
            // update position and bound box
            boundShape = new Rectangle2D.Double(x, y, width, height);

            // make periods position on the frame the current position
            preX = this.x;
            preY = this.y;
        }
    }

    public void draw(Graphics2D graphics2D) {
        // g.draw(boundShape);
        graphics2D.drawImage(starImage, x, y, width, height, null); // draw star image
        graphics2D.setPaint(Color.BLACK);
        graphics2D.setFont(new Font("default", Font.BOLD, 32));
        graphics2D.drawString(id, x - 25 + width / 2, y + 20 + height / 2); // draw star phonetic text in black

        graphics2D.setPaint(Color.WHITE);
        graphics2D.setFont(defaultFont); // set back to default font
    }

}
