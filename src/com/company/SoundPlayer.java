package com.company;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.logging.Level;

/**
 * This class is used to play sounds for the program
 */
public class SoundPlayer {

    /**
     * Run to play a .wav file stored in the resources/sounds/ directory
     * @param filename
     */
    private void sound(String filename) {
        // run in a thread to stop little-endian not supported error
        Thread thread = new Thread() {
            public void run() {
                AudioInputStream audioIn = null; // the system audio system
                try {
                    // get the resource file
                    audioIn = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource("sounds/" + filename + ".wav"));
                } catch (UnsupportedAudioFileException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // store sound as a clip object
                Clip clip = null;
                try {
                    clip = AudioSystem.getClip();
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                }

                // open it in to memory
                try {
                    clip.open(audioIn);
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // play the sound
                clip.start();
            }
        };

        thread.start();
    }

    /**
     * Run to speak a word or phonetic
     * @param text
     */
    public void speak(String text) {
        sound(text);
        Logging.log(Level.INFO, "Word/phonetic: " + text + " spoken");
    }

    /**
     * Run to play applause sound effect
     */
    public void applause() {
        sound("applause");
        Logging.log(Level.INFO, "Applause sound effect");
    }
}
