package com.company;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.logging.Level;

public class SettingsScreen extends HeaderScreen {
    Button backButton;
    JPanel listBox;
    JScrollPane scrollPane;
    UserSystem userSystem;

    SettingsScreen() {
        super("Settings Screen", new BorderLayout()); // screen id
        setBackground(Color.BLACK);

        userSystem = new UserSystem(); // used to manage user system interactions

        JPanel btnPnl = new JPanel(new FlowLayout(FlowLayout.CENTER)); // holds top add & remove buttons

        Button addListButton = new Button("Add new from list");
        addListButton.addActionListener(e -> openNewListUsersDialog()); // run openNewListUsersDialog on click
        addListButton.setFont(new Font("Serif", Font.BOLD, 20));

        Button addNewButton = new Button("Add new");
        addNewButton.addActionListener(e -> openNewUserDialog()); // run openNewUserDialog on click
        addNewButton.setFont(new Font("Serif", Font.BOLD, 20));

        Button removeAllButton = new Button("Remove all");
        removeAllButton.setFont(new Font("Serif", Font.BOLD, 20));
        // on click run a "are you sure dialog"
        removeAllButton.addActionListener(e -> {
            int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove all users?",
                    "Remove all users?", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {
                // if the user clicks on yes then loop through all users and delete each user
                Logging.log(Level.INFO, "Remove all users");
                for (User user : userSystem.getAllUsers()) {
                    userSystem.removeUser(user.getUsername());
                }

                updateList(); // update the user list
            }
        });
        removeAllButton.setBackground(ColorScheme.RED);

        // add top buttons to panel
        btnPnl.add(addListButton);
        btnPnl.add(addNewButton);
        btnPnl.add(removeAllButton);

        // a panel that will hold a list of users vertically
        listBox = new JPanel();
        listBox.setLayout(new BoxLayout(listBox, BoxLayout.Y_AXIS)); // vertical adding components

        // create the scroll pane and add the table to it.
        scrollPane = new JScrollPane(listBox);

        backButton = new Button("Back");
        backButton.addActionListener(e -> fireSwitchScreenEvent(new SwitchScreen("Welcome Screen"))); // on click switch to welcome screen
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT); // center on the x-axis
        backButton.setBackground(ColorScheme.RED);

        body.setBorder(new EmptyBorder(20, 100, 100, 100)); // create a margin around the screen

        /* Add components as follows:
         * btnPnl
         * scrollPane
         * backButton
         */

        body.add(btnPnl, BorderLayout.NORTH);
        body.add(scrollPane, BorderLayout.CENTER);
        body.add(backButton, BorderLayout.SOUTH);
        body.revalidate();
    }

    /**
     * Run when user clicks on create users from list button.
     * Opens the AddListUsersDialog and updates the user list once closed.
     */
    private void openNewListUsersDialog() {
        Logging.log(Level.INFO, "Open new list users dialog");
        AddListUsersDialog addListUsersDialog = new AddListUsersDialog((JFrame) SwingUtilities.getWindowAncestor(this));
        addListUsersDialog.run(); // open AddListUsersDialog
        updateList(); // update the user list
    }

    private void openNewUserDialog() {
        Logging.log(Level.INFO, "Open new user dialog");
        AddUserDialog addUserDialog = new AddUserDialog((JFrame) SwingUtilities.getWindowAncestor(this));
        addUserDialog.run(); // open AddUserDialog
        updateList(); // update the user list
    }

    public void updateList() {
        listBox.removeAll(); // empty users list
        // loop through all the registered users
        for (User user : userSystem.getAllUsersAlphabetically()) {
            JPanel userContainer = new JPanel(); // horizontal row panel for adding the username, average score and high score
            userContainer.setLayout(new BorderLayout());
            // size when list doesnt take up the whole height of the screen width = 500, height = 40
            userContainer.setMaximumSize(new Dimension(500, 40));
            userContainer.add(new JLabel(user.getUsername()), BorderLayout.CENTER); // add username to the left
            userContainer.setBackground(Color.WHITE);
            userContainer.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // create a black border line around one row

            Button removeButton = new Button("Remove");
            removeButton.setFont(new Font("Serif", Font.BOLD, 15));
            removeButton.setBackground(ColorScheme.RED);
            // remove user on click and update list
            removeButton.addActionListener(e -> {
                userSystem.removeUser(user.getUsername());
                updateList(); // update users list
            });

            userContainer.add(removeButton, BorderLayout.EAST); // add remove button on the right of row

            listBox.add(userContainer); // add row to the container
        }

        listBox.revalidate();
        scrollPane.getVerticalScrollBar().setValue(0); // scroll to the top of the user container
        Logging.log(Level.INFO, "Update list");
        body.revalidate();
        body.repaint();
    }

    @Override
    public void is_visible() {
        super.is_visible();
        updateList(); // update user list when screen visible
    }
}