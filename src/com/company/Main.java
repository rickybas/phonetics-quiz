package com.company;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;

public class Main {
    /**
     * the first method to be run
     * @param args
     */
    public static void main(String[] args) {
        /*
         Set up the program logging, can be turn off and on in Utils.LOGGING.
         Logging should be turn on in development, debugging and testing stages.
         Logging should be turned off in production as the messages will be useful for the user and will take up unnecessary storage. */
        Logging.logger.setUseParentHandlers(false); // turn off console logging

        if (Utils.LOGGING) {
            Logging.logger.setLevel(Level.ALL); // store all logs
            try {
                FileHandler fileHtml = new FileHandler("logs/log-"
                        + new SimpleDateFormat("M-d_HHmmss").format(new Date()) + ".html"); // log html file path + filename

                Logging.HtmlFormatter formatterHTML = new Logging.HtmlFormatter();
                fileHtml.setFormatter(formatterHTML); // format the html file using my html formatter

                Logging.logger.addHandler(fileHtml); // store all logs for the current program in html file
            } catch (IOException e) {
                Logging.logger.log(Level.WARNING, e.getMessage());
                e.printStackTrace();
            }
        } else {
            Logging.logger.setLevel(Level.OFF); // log nothing
        }


        File usersDirectory = new File(Utils.USERS_DIRECTORY);
        if(!usersDirectory.exists()) {
            usersDirectory.mkdirs();
        }

        MainWindow mainWindow = new MainWindow(); // create main window object
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close whole window when close requested by user
        mainWindow.setResizable(false); // window is not resizeable
        mainWindow.setSize(Utils.WINDOW_WIDTH, Utils.WINDOW_HEIGHT); // window size = const width & height in Utils
        mainWindow.setVisible(true); // show the window to the screen
        // when program close event occurs exit the whole program
        mainWindow.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                Utils.exit();
            }
        });

    }
}
