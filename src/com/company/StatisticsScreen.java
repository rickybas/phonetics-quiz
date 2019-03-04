package com.company;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;

public class StatisticsScreen extends HeaderScreen {
    private JTable table;
    private Button backButton;
    private JScrollPane scrollPane;
    private JPanel arrangeButtonPanel;

    private Button alphabeticalButton;
    private Button highScoreButton;
    private Button averageButton;
    private Button openBarChartButton;

    private String sortedBy;
    private ArrayList<User> sortedUsers;
    private String[] columnNames;
    private ArrayList<BarChartWindow> openBarChartWindows;
    private UserSystem userSystem;

    StatisticsScreen() {
        super("Statistics Screen", new BorderLayout());  // screen id
        setBackground(Color.BLACK);

        userSystem = new UserSystem(); // used to manage user system interactions
        openBarChartWindows = new ArrayList<>(); // a list of all the bar chart window open

        arrangeButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        alphabeticalButton = new Button("Alphabetical");
        alphabeticalButton.setFont(new Font("Serif", Font.BOLD, 20));
        alphabeticalButton.addActionListener(e -> updateTableByAlphabetical()); // run updateTableByAlphabetical on click

        highScoreButton = new Button("High Score");
        highScoreButton.setFont(new Font("Serif", Font.BOLD, 20));
        highScoreButton.addActionListener(e -> updateTableByHighScore()); // run updateTableByHighScore on click

        averageButton = new Button("Average Score");
        averageButton.setFont(new Font("Serif", Font.BOLD, 20));
        averageButton.addActionListener(e -> updateTableByAverage()); // run updateTableByAverage on click

        openBarChartButton = new Button("Bar chart");
        openBarChartButton.setFont(new Font("Serif", Font.BOLD, 20));
        openBarChartButton.setBackground(ColorScheme.ORANGE);
        openBarChartButton.addActionListener(e -> openBarChart()); // run openBarChart on click

        // add ordering button to the ip panel
        arrangeButtonPanel.add(alphabeticalButton);
        arrangeButtonPanel.add(highScoreButton);
        arrangeButtonPanel.add(averageButton);
        arrangeButtonPanel.add(openBarChartButton);

        columnNames = new String[]{
                "Name",
                "High Score / %",
                "Average Score / %"
        }; // fixed number of columns

        table = new JTable(new Object[][]{}, columnNames);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true); // stretch horizontally
        table.setFocusable(false);
        table.setRowSelectionAllowed(false); // non selectable visually

        // Create the scroll pane and add the table to it.
        scrollPane = new JScrollPane(table);

        backButton = new Button("Back");
        backButton.addActionListener(e -> fireSwitchScreenEvent(new SwitchScreen("Welcome Screen"))); // on click switch to welcome screen
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT); // center on the x-axis
        backButton.setBackground(ColorScheme.RED);

        body.setBorder(new EmptyBorder(20, 100, 100, 100)); // add margin around screen

        /* Add components as follows:
         * arrangeButtonPanel
         * scrollPane
         * backButton
         */

        body.add(arrangeButtonPanel, BorderLayout.NORTH);
        body.add(scrollPane, BorderLayout.CENTER);
        body.add(backButton, BorderLayout.SOUTH);
        body.revalidate();
    }

    private void openBarChart() {
        ArrayList<User> tempSortedUsers = new ArrayList<>(sortedUsers); // copy sortedUsers list
        tempSortedUsers.removeIf(user -> user.getAverageScore() == 0.0); // remove all users that have an average score of 0

        String[] labels = new String[tempSortedUsers.size()]; // labels list the same size as tempSortedUsers
        double[] values = new double[tempSortedUsers.size()]; // values list the same size as tempSortedUsers

        // loop through all users in tempSortedUsers
        for (int i = 0; i < tempSortedUsers.size(); i++) {
            labels[i] = tempSortedUsers.get(i).getUsername(); // add user's username to list

            // set value in list to user's average score or high score according to the sorted by value
            if (sortedBy.equals("alphabetical")) values[i] = tempSortedUsers.get(i).getAverageScore();
            else if (sortedBy.equals("high")) values[i] = tempSortedUsers.get(i).getHighScore();
            else if (sortedBy.equals("average")) values[i] = tempSortedUsers.get(i).getAverageScore();
        }

        BarChartWindow barChartWindow = new BarChartWindow(sortedBy, labels, values); // create bar chart window object
        // when bar chart window is closed remove it from the openBarChartWindows list
        barChartWindow.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                openBarChartWindows.remove(barChartWindow);
            }
        });
        barChartWindow.setVisible(true); // show bar chart window

        openBarChartWindows.add(barChartWindow); // add the bar chart window to the openBarChartWindows list
    }

    /**
     * Update the table so that its arranged alphabetically
     */
    private void updateTableByAlphabetical() {
        sortedBy = "alphabetical";

        // highlight the alphabeticalButton
        alphabeticalButton.setBackground(ColorScheme.CONCRETE);
        highScoreButton.setBackground(Color.WHITE);
        averageButton.setBackground(Color.WHITE);

        ArrayList<User> users = userSystem.getAllUsersAlphabetically();
        sortedUsers = users;
        updateTable(users);
        Logging.log(Level.INFO, "Update table alphabetically");
    }

    /**
     * Update the table so that its arranged by high score
     */
    private void updateTableByHighScore() {
        sortedBy = "high";

        // highlight the highScoreButton
        alphabeticalButton.setBackground(Color.WHITE);
        highScoreButton.setBackground(ColorScheme.CONCRETE);
        averageButton.setBackground(Color.WHITE);

        ArrayList<User> users = userSystem.getAllUsersByHighScore();
        sortedUsers = users;
        updateTable(users);
        Logging.log(Level.INFO, "Update table by high score");
    }

    /**
     * Update the table so that its arranged by average
     */
    private void updateTableByAverage() {
        sortedBy = "average";

        // highlight the averageButton
        alphabeticalButton.setBackground(Color.WHITE);
        highScoreButton.setBackground(Color.WHITE);
        averageButton.setBackground(ColorScheme.CONCRETE);

        ArrayList<User> users = userSystem.getAllUsersByAverageScore();
        sortedUsers = users;
        updateTable(users);
        Logging.log(Level.INFO, "Update table by average score");
    }

    /**
     * Update the table by the users parameter and by sortedBy variable
     * @param users
     */
    private void updateTable(ArrayList<User> users) {
        ArrayList<Object[]> data = new ArrayList<>();
        // add all users to table data by row
        for (User user : users) {
            data.add(new Object[]{
                    user.getUsername(),
                    user.getHighScore(),
                    user.getAverageScore()
            });
        }

        // convert the ArrayList<Object[]> variable to Object[][] to be used by the DefaultTableModel object
        Object[][] dataArray = data.toArray(new Object[data.size()][2]);
        DefaultTableModel model = new DefaultTableModel(dataArray, columnNames) {
            // every cell in the table is not changeable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setModel(model); // use the table model
        scrollPane.getVerticalScrollBar().setValue(0); // scroll the table to the top
    }

    @Override
    public void is_visible() {
        super.is_visible();
        updateTableByAlphabetical(); // when open screen create the table of users alphabetically (default value)
    }

    @Override
    public void is_not_visible() {
        super.is_not_visible();
        // loop through all registered open windows, close them and free up there memory
        for (BarChartWindow barChartWindow : openBarChartWindows) {
            barChartWindow.setVisible(false);
            barChartWindow.dispose(); // destroys the JFrame object
        }
    }

}
