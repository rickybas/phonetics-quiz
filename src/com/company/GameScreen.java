package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.logging.Level;

public class GameScreen extends Screen implements Runnable {
    private Thread mainThread; // the main processing of the game
    private BufferedImage backgroundImage; // the star background tile image
    private BufferedImage backgroundTile;
    private Graphics2D graphics; // the games graphics for the screen

    private boolean gameLoopRunning; // if the game loop is running
    private int gameHeight;
    private int gameWidth;

    private SoundPlayer soundPlayer; // manages the sounds for the game
    private MouseListener mouseListener; // listens for mouse event for the game
    private Timer timer; // the game timer, runs each second

    private TimerComponent timerComponent; // current countdown for each word
    private ArrayList<StarComponent> starComponents; // draggable stars on the screen
    private BackButtonComponent backButtonComponent; // goes to welcome screen
    private WordBoxComponent wordBoxComponent; // show current word
    private SkipButtonComponent skipButtonComponent; // goes to next word
    private InfoComponent infoComponent; // show information about the game at the top

    // the boundaries of the stars, get to be set
    private int topStarBoundary = 0;
    private int bottomStarBoundary = 0;
    private int leftStarBoundary = 0;
    private int rightStarBoundary = 0;

    private String username; // username of player
    private boolean wordSounds; // if the player wants the words to be spoken
    private boolean phoneticSounds; // if the player wants the phonetics to be spoken
    private int level; // the level chosen

    private ArrayList<String> levelWords; // words for the level, e.g. "ay" words
    int[] levelWordIds; // level words in terms of index
    private int currentWordId; // the current word index
    private int levelWordCount; // the number of words in the level count
    private StarComponent draggingComponent; // the component currently being dragged

    private int score = 0; // the global game score
    private int outOf = 0; // what the score is out of
    private int numTrysCorrect = 0; // the number of phonetics dragged to the word
    private String nextScreen; // the requested next screen
    private Map<String, String> nextMetaData; // the requested next screen's metadata

    GameScreen() {
        super("Game Screen"); // screen id
        setFocusable(true);

        soundPlayer = new SoundPlayer(); // manages sound playing for the game
        mouseListener = new GameMouseAdapter();
        addMouseListener(mouseListener); // add custom mouse listener
        addMouseMotionListener((MouseMotionListener) mouseListener); // add custom mouse motion listener

        try {
            backgroundTile = ImageIO.read(getClass().getClassLoader().getResource("tile.jpg")); // load stars background tile
        } catch (IOException e) {
            Logging.logger.log(Level.WARNING, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        gameLoopRunning = true; // true when game is running the main game loop

        // reset score and star components
        score = 0;
        outOf = 0;
        starComponents = new ArrayList<>();

        Logging.logger.log(Level.INFO, "Score = 0");

        gameWidth = this.getBounds().width; // get game screen width
        gameHeight = this.getBounds().height; // get game screen height

        // the area where stars can be added
        topStarBoundary = 150;
        bottomStarBoundary = this.getBounds().height - WordBoxComponent.height - StarComponent.height - 40;
        leftStarBoundary = 40;
        rightStarBoundary = this.getBounds().width - StarComponent.width - 40;

        levelWordCount = 0;

        // get data from the choose game screen via the metadata
        username = metaData.get("username");
        wordSounds = metaData.get("has_word_sounds").equals("true");
        phoneticSounds = metaData.get("has_phonetic_sounds").equals("true");

        level = Utils.WORDS_TO_NUMBERS.get(metaData.get("level")) - 1; // get the level as a number
        levelWords = Utils.getLevelWords().get(level); // get the words for the level

        Collections.shuffle(levelWords); // randomise level words

        levelWordIds = new int[levelWords.size()]; // stores level words as numbers
        for (int i = 0; i < levelWordIds.length; i++) {
            levelWordIds[i] = i; // store the index of the word in the level array so it can be referenced better
        }

        currentWordId = levelWordIds[levelWordCount]; // get first word id

        Logging.log(Level.INFO, "First word: " + levelWords.get(currentWordId));

        int margin = 10;
        // top info component with the level, username, score and sound options
        infoComponent = new InfoComponent(margin, margin, "Level: " + Utils.NUMBERS_TO_WORDS.get(level + 1) + " - " + username
                + " - Phonetic Sounds: " + (phoneticSounds ? "(*)" : "( )") + " - Word Sounds: " + (wordSounds ? "(*)" : "( )"));

        starComponents = drawRandomStars(); // a list of randomly placed stars in the stars area

        // back button, bottom left, when clicked goes to welcome screen
        backButtonComponent = new BackButtonComponent(margin, gameHeight - BackButtonComponent.height - 20);
        // timer, bottom right, 30 second countdown
        timerComponent = new TimerComponent(gameWidth - TimerComponent.width - 10, gameHeight - TimerComponent.height - 20, 30);
        // skip button, bottom second from the right, when clicked goes to next word
        skipButtonComponent = new SkipButtonComponent(timerComponent.x - margin - SkipButtonComponent.width, gameHeight - SkipButtonComponent.height - 20);
        // word box, bottom middle, current word, drags phonetics on top and click to repeat word sound if words sounds chosen
        wordBoxComponent = new WordBoxComponent(backButtonComponent.x + backButtonComponent.width + margin, gameHeight - WordBoxComponent.height - 20, (skipButtonComponent.x - backButtonComponent.x - backButtonComponent.width) - 20, wordSounds);
        wordBoxComponent.text = levelWords.get(currentWordId);

        startTimer(); // start 30 seconds countdown

        if (wordSounds) soundPlayer.speak(levelWords.get(currentWordId)); // play word if option chosen

        backgroundImage = new BufferedImage(gameWidth, gameHeight, BufferedImage.TYPE_INT_RGB); // background image from load image
        graphics = (Graphics2D) backgroundImage.getGraphics();

        // update render draw game loop
        while (true) {
            // double buffering cycle
            gameUpdate(); // update positions of components
            gameRender(); // draw the components to an external graphics screen
            gameDraw(); // then draw external game screen to the actual screen

            try {
                mainThread.sleep(30); // pause for 30 milliseconds, one frame every 30 milliseconds
            } catch (InterruptedException e) {
                Logging.logger.log(Level.WARNING, e.getMessage());
                e.printStackTrace();
            }
            if (!gameLoopRunning) break; // quit game loop
        }

        mainThread = null; // discard game thread
        fireSwitchScreenEvent(new SwitchScreen(nextScreen, nextMetaData)); // switch to the next chosen screen (next screen)
    }

    /**
     * A method that updates each component object in the game, for example position
     */
    private void gameUpdate() {
        // update all components on the game screen

        // infoComponent.update();

        // make sure star components is synced if it is change while in the loop
        synchronized (starComponents) {
            for (StarComponent star : starComponents) {
                star.update();
            }
        }

        // backButtonComponent.update();
        // timerComponent.update();
        // skipButtonComponent.update();
        // wordBoxComponent.update();
    }

    /**
     * A method that draws every component off to an off screen image (double buffering)
     */
    private void gameRender() {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON); // antialiasing improves pixel rendering by filling in extra pixels

        int tileWidth = backgroundTile.getWidth();
        int tileHeight = backgroundTile.getHeight();
        // loop through all background tile positions and draw tile
        for (int y = 0; y < getHeight(); y += tileHeight) {
            for (int x = 0; x < getWidth(); x += tileWidth) {
                graphics.drawImage(backgroundTile, x, y, this);
            }
        }

        // draw all other components
        infoComponent.draw(graphics);
        backButtonComponent.draw(graphics);
        timerComponent.draw(graphics);
        skipButtonComponent.draw(graphics);
        wordBoxComponent.draw(graphics);

        // make sure star components is synced if it is change while in the loop
        synchronized (starComponents) {
            for (StarComponent star : starComponents) {
                // tempStarComponents = starComponents;
                star.draw(graphics);
            }
        }
    }

    /**
     * A method that then draws the off screen image to the screen
     */
    private void gameDraw() {
        Graphics2D g2 = (Graphics2D) this.getGraphics(); // get drawn graphics on external display
        g2.drawImage(backgroundImage, 0, 0, null); // draw it
        g2.dispose(); // free up resource memory
    }

    private void nextWord() {
        levelWordCount += 1; // next word add one to count
        numTrysCorrect = 0; // reset number of correct phonetics placed on word
        timerComponent.reset();
        wordBoxComponent.color = ColorScheme.BLUE; // flash blue

        // when level over, all words done
        if (levelWordCount == levelWords.size()) {
            // create metadata to send to game score screen
            Map<String, String> metaData = new HashMap<>();
            metaData.put("username", username);
            metaData.put("level", Utils.NUMBERS_TO_WORDS.get(level + 1));
            metaData.put("score", Integer.toString(score));
            metaData.put("outOf", Integer.toString(outOf));

            Logging.log(Level.INFO, "Game over");
            leaveGameScreen("Game Score Screen", metaData); // leave game with metadata
            return;
        }

        currentWordId = levelWordIds[levelWordCount]; // next word index
        wordBoxComponent.text = levelWords.get(currentWordId);

        // make sure star components is synced if it is change while in the loop
        synchronized (starComponents) {
            starComponents = drawRandomStars();
        }

        Logging.log(Level.INFO, "Next word: " + levelWords.get(currentWordId));
        // speak word if word sound option chosen
        if (!wordSounds) return;
        soundPlayer.speak(levelWords.get(currentWordId));

    }

    /**
     * Finds positions of the stars in random positions in the star area
     * @return randomly placed stars
     */
    private ArrayList<StarComponent> drawRandomStars() {
        // this part find random phonetics along with the phonetics needed to complete the word
        String[] phoneticsForWord = Utils.WORDS.get(levelWords.get(levelWordCount)); // get all phonetics for words
        outOf += phoneticsForWord.length; // add all phonetics for word to the score outof variable

        List<String> allPhonetics = Utils.getAllPhonetics(); // all the phonetics form all the words

        // remove phonetic from allPhonetics if its contained in side the words phonetic
        for (int i = 0; i < allPhonetics.size() - 1; i++) {
            for (String phonetic : phoneticsForWord) {
                if (allPhonetics.get(i).equals(phonetic)) {
                    allPhonetics.remove(i);
                    if (i != 0) {
                        i = i - 1; // since remove current item go back by one in the loop index
                    }
                }
            }
        }

        Collections.shuffle(allPhonetics); // randomize allPhonetics list
        allPhonetics = allPhonetics.subList(0, 3); // pick the first 4
        allPhonetics.addAll(new ArrayList<>(Arrays.asList(phoneticsForWord))); // add these random phonetics to the start of the list

        // this part find the random position of each phonetic star
        Random random = new Random();

        // store x,y positions
        ArrayList<Integer[]> positions = new ArrayList<>();
        while (positions.size() != allPhonetics.size()) {
            int randomX = 0;
            int randomY = 0;

            while (true) {
                // find random x position from the left to right boundary
                randomX = random.nextInt(rightStarBoundary + 1 - leftStarBoundary) + leftStarBoundary;
                // find random y position from the top to bottom boundary
                randomY = random.nextInt(bottomStarBoundary + 1 - topStarBoundary) + topStarBoundary;

                // check to see if position does not overlap
                boolean doesntOverlap = true;
                // check random position against other existing positions
                for (Integer[] pos : positions) {
                    // if does not overlap
                    // https://stackoverflow.com/questions/40795709/checking-whether-two-rectangles-overlap-in-python-using-two-bottom-left-corners
                    if ((randomX < pos[0] + StarComponent.width && randomX + StarComponent.width > pos[0]
                            && randomY < pos[1] + StarComponent.width && randomY + StarComponent.width > pos[1])) {
                        doesntOverlap = false;
                        break; // found a good position
                    }
                }
                if (doesntOverlap) break;

            }

            positions.add(new Integer[]{randomX, randomY}); // add good position to list
        }

        // make stars list from the random positions and phonetics
        ArrayList<StarComponent> stars = new ArrayList<>();
        for (int i = 0; i < allPhonetics.size(); i++) {
            Integer[] XY = positions.get(i);
            stars.add(new StarComponent(allPhonetics.get(i), XY[0], XY[1]));
        }

        return stars;
    }

    /**
     * Used to move to another screen from here
     * @param screen
     * @param metaData
     */
    private void leaveGameScreen(String screen, Map<String, String> metaData) {
        timer.cancel();
        gameLoopRunning = false;
        nextScreen = screen; // chose next screen to go to
        nextMetaData = metaData; // transfer custom metadata to next screen
    }

    private void startTimer() {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                if (timerComponent.minus_one_second() == 0) {
                    timerComponent.reset();
                    nextWord(); // if timer runs out go to the next word and reset the timer
                }
            }
        };
        timer.scheduleAtFixedRate(task, 1000, 1000); // run method described above every 1000ms = 1sec
    }

    /**
     * If phonetic dragged into box correct
     * @param phonetic
     * @param word
     * @return
     */
    private boolean phoneticCorrect(String phonetic, String word) {
        return Arrays.asList(Utils.WORDS.get(word)).contains(phonetic); // if chosen phonetic in word phonetics
    }

    public void is_visible() {
        super.is_visible();
        // when not visible then discard thread
        if (mainThread == null) {
            mainThread = new Thread(this);
            mainThread.start();
        }
    }

    /**
     * This class deals with the mouse interactions with the user on the game
     */
    class GameMouseAdapter extends MouseAdapter implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent me) {
            super.mouseClicked(me);

            // clicked on skip
            if (skipButtonComponent.boundShape.contains(me.getPoint())) {
                Logging.log(Level.INFO, "Skip word");
                nextWord(); // go to the next word
            }

            // click on word bpx
            if (wordBoxComponent.boundShape.contains(me.getPoint())) {
                // play word sound if option chosen
                if (!wordSounds) return;
                soundPlayer.speak(levelWords.get(currentWordId));
            }

            // click back button
            if (backButtonComponent.boundShape.contains(me.getPoint())) {
                Logging.log(Level.INFO, "Game screen back clicked");
                leaveGameScreen("Welcome Screen", null); // go to welcome screen
            }
        }

        public void mousePressed(MouseEvent e) {
            // make sure star components is synced if it is change while in the loop
            synchronized (starComponents) {
                for (StarComponent starComponent : starComponents) {
                    // if mouse pressed on each star box
                    if (starComponent.boundShape.contains(e.getPoint())) {
                        draggingComponent = starComponent; // now dragging this one
                        if (phoneticSounds) {
                            soundPlayer.speak(draggingComponent.id); // play the phonetic if option chosen
                        }
                        break;
                    }
                }
            }
        }

        public void mouseDragged(MouseEvent e) {
            updateLocation(e); // update the position of the dragged star
            // check to see if dragging something
            if (draggingComponent != null) {
                // if dragging over word box, highlight word box
                if (wordBoxComponent.boundShape.contains(new Point(e.getPoint().x, e.getPoint().y))) {
                    wordBoxComponent.color = ColorScheme.CONCRETE;
                } else {
                    wordBoxComponent.color = ColorScheme.BLUE;
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            updateLocation(e); // update the position of the dragged star

            // check to see if dragging something
            if (draggingComponent != null) {
                // if mouse released on word box
                if (wordBoxComponent.boundShape.contains(new Point(e.getPoint().x, e.getPoint().y))) {
                    // if correct
                    if (phoneticCorrect(draggingComponent.id, levelWords.get(currentWordId))) {
                        Logging.log(Level.INFO, "Dropped star on word box, is correct");
                        numTrysCorrect += 1;
                        // make sure star components is synced if it is change while in the loop
                        synchronized (starComponents) {
                            starComponents.remove(draggingComponent); // discard dragged components from screen
                        }

                        // flash green for 300 milliseconds
                        wordBoxComponent.color = ColorScheme.GREEN;
                        new java.util.Timer().schedule(
                                new java.util.TimerTask() {
                                    @Override
                                    public void run() {
                                        wordBoxComponent.color = ColorScheme.BLUE;
                                        // if last word
                                        if (numTrysCorrect == Utils.WORDS.get(levelWords.get(currentWordId)).length) {
                                            wordBoxComponent.color = ColorScheme.BLUE;
                                            nextWord(); // return to original color and go to the next word

                                        }
                                    }
                                },
                                300
                        );
                        score += 1;
                        Logging.logger.log(Level.INFO, "Score +1 = " + score);
                    } else {
                        // when incorrect
                        Logging.log(Level.INFO, "Dropped star on word box, is incorrect");

                        // flash red for 600 milliseconds
                        wordBoxComponent.color = ColorScheme.RED;
                        new java.util.Timer().schedule(
                                new java.util.TimerTask() {
                                    @Override
                                    public void run() {
                                        wordBoxComponent.color = ColorScheme.BLUE;
                                        nextWord(); // go to the next word
                                    }
                                },
                                600
                        );
                        // score -= 1;
                    }
                }
                // reset the star to its starting position if not removed
                draggingComponent.x = draggingComponent.originalX;
                draggingComponent.y = draggingComponent.originalY;
                infoComponent.setScore(score); // update the top score
            }

            draggingComponent = null; // reset dragging component
        }

        public void updateLocation(MouseEvent e) {
            // update the position of the component being dragged
            int newX = e.getX();
            int newY = e.getY();

            // when being dragged the mouse is in the center of the star
            if (draggingComponent != null) {
                draggingComponent.x = newX - StarComponent.width / 2; // x center of star
                draggingComponent.y = newY - StarComponent.height / 2; // y center of star
            }
        }

    }
}
