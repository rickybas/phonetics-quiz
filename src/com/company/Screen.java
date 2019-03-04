package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Map;
import java.util.logging.Level;

public class Screen extends JPanel {
    String title;
    Map<String, String> metaData; // data set from other screens
    private EventListenerList listenerList;
    private BufferedImage tile;

    Screen(String title) {
        super(new BorderLayout()); // each screen has a border layout

        listenerList = new EventListenerList(); // manages the list of events that is occurring in the Screen

        setBackground(Color.BLACK);
        this.title = title; // the title and unique identifier for the window

        try {
            // get star tile.jpg from the resource directory as a buffered image
            tile = ImageIO.read(getClass().getClassLoader().getResource("tile.jpg")); // the background image, which will be tiled
        } catch (IOException e) {
            Logging.logger.log(Level.WARNING, e.getMessage());
            e.printStackTrace();
        }

        addAncestorListener(new AncestorListener() {
            public void ancestorAdded(AncestorEvent event) {
                /* run is_visible when the screen is shown on card layout,
                the class inheriting this will is able to set is_visible */
                is_visible();
            }

            public void ancestorRemoved(AncestorEvent event) {
                /* run is_not_visible when the screen not shown, from previously being shown, on the card layout,
                the class inheriting this will is able to set is_not_visible */
                is_not_visible();
            }

            public void ancestorMoved(AncestorEvent event) {
            }
        });
    }

    public void is_visible() {
        Logging.log(Level.INFO, title + " visible");
    }

    public void is_not_visible() {
        Logging.log(Level.INFO, title + " not visible");
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        int tileWidth = tile.getWidth();
        int tileHeight = tile.getHeight();
        // for each panel in tiled grid
        for (int y = 0; y < getHeight(); y += tileHeight) {
            for (int x = 0; x < getWidth(); x += tileWidth) {
                graphics2D.drawImage(tile, x, y, this); // draw the background image to the screen (x, y)
            }
        }
        graphics2D.dispose(); // free up resource memory
    }

    // below, we set up the switch screen event of the screen object

    /**
     * Add a listener to listen for a switch screen event
     *
     * @param listener
     */
    public void addSwitchScreenListener(SwitchScreenListener listener) {
        listenerList.add(SwitchScreenListener.class, listener);
    }

//    public void removeSwitchScreenListener(SwitchScreenListener listener) {
//        listenerList.remove(SwitchScreenListener.class, listener);
//    }

    /**
     * Run when the user wants to switch screen
     *
     * @param evt
     */
    void fireSwitchScreenEvent(SwitchScreen evt) {
        Object[] listeners = listenerList.getListenerList();
        // loop through all listeners and alert all
        for (int i = 0; i < listeners.length; i = i + 2) {
            if (listeners[i] == SwitchScreenListener.class) {
                ((SwitchScreenListener) listeners[i + 1]).SwitchScreenOccurred(evt);
            }
        }
    }

    interface SwitchScreenListener extends EventListener {
        void SwitchScreenOccurred(SwitchScreen evt);
    }

    /**
     * An EventObject class that will pass around the details for the switch screen event
     */
    class SwitchScreen extends EventObject {
        public String screen;
        public Map<String, String> metaData;

        /**
         * The constructor run when is no metadata available
         *
         * @param screen
         */
        public SwitchScreen(String screen) {
            super(new Object());
            this.screen = screen; // the id of the next screen
            this.metaData = null; // the sent metadata to the next screen, null by default
        }

        /**
         * This optional constructor run when the there is metadata available
         *
         * @param screen
         * @param metaData
         */
        public SwitchScreen(String screen, Map<String, String> metaData) {
            super(new Object());
            this.screen = screen; // the id of the next screen
            this.metaData = metaData; // the sent metadata to the next screen, null by default
        }
    }
}
