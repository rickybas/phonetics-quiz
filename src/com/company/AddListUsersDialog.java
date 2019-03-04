package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AddListUsersDialog extends JDialog implements ActionListener {
    private JTextArea usernameListBox;
    private Button addButton;
    private UserSystem userSystem;

    public AddListUsersDialog(JFrame parent) {
        super(parent,"Enter list of user names",true); // title text

        setLocationRelativeTo(null); // centers the window
        setPreferredSize(new Dimension(400, 400)); // width = 400px, height = 400px
        setResizable(false);

        userSystem = new UserSystem(); // used to manage user system interactions

        JPanel panel = new JPanel(); // main panel of the window
        panel.setLayout(new BorderLayout());

        JLabel usernameLabel = new JLabel("List of user names separated by line breaks: ");
        panel.add(usernameLabel, BorderLayout.NORTH); // position description label at the top

        usernameListBox = new JTextArea(); // input box
        panel.add(usernameListBox, BorderLayout.CENTER); // position input box in the center

        addButton = new Button("Add"); // main button to add usernames
        addButton.setFont(new Font("Serif", Font.BOLD, 20));
        addButton.addActionListener(this); // run actionPerformed when add button pressed
        panel.add(addButton, BorderLayout.SOUTH); // add button at the bottom

        getContentPane().add(panel); // add main panel to the window
        pack();
    }

    /**
     * Method run when add button pressed
     * @param actionEvent
     */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        // if action sent from add button
        if (source == addButton) {
            // if nothing but blank spaces in box
            if (usernameListBox.getText().replace(" ", "").replace("\n", "").equals("")){
                JOptionPane.showMessageDialog(this,
                        "Enter user names into box",
                        "Enter user names into box",
                        JOptionPane.WARNING_MESSAGE); // tell user to enter something in
                return; // finish method
            }

            /* Get all usernames in box by splitting each username by each line break and removing blank spaces
            and then add them to a set which will remove duplicates*/
            Set<String> numbersSet = new HashSet<> (Arrays.asList(usernameListBox.getText()
                    .replace(" ", "").split(System.getProperty("line.separator"))));

            String[] lines = numbersSet.toArray(new String[numbersSet.size()]);
            ArrayList<String> linesList = new ArrayList<>(Arrays.asList(lines)); // into an arraylist
            linesList.remove(""); // remove all lines with nothing on but blank spaces

            // loop through each line
            for (int i = 0; i < linesList.size(); i++) {
                String username = linesList.get(i);
                String normalizedUsername = Utils.normalizeUsername(username); // normalize username
                Utils.checkComment checkUsername = Utils.checkUsername(normalizedUsername); // validate username

//                if (checkUsername.equals("empty")) {
//                    JOptionPane.showMessageDialog(this,
//                            "Enter username",
//                            "Enter username",
//                            JOptionPane.WARNING_MESSAGE);
//                    return;
//                }

                // if username is longer than 18 characters show message
                if (checkUsername == Utils.checkComment.LONG) {
                    JOptionPane.showMessageDialog(this,
                            "Username is too long (greater than 18 characters)",
                            "Username is too long (greater than 18 characters)",
                            JOptionPane.WARNING_MESSAGE); // show message
                    return; // finish method
                }

                // if the user has already been created
                if (userSystem.userExists(normalizedUsername)) {
                    JOptionPane.showMessageDialog(this,
                            "User name, " + username + ", already exists",
                            "User name, " + username + ", already exists",
                            JOptionPane.WARNING_MESSAGE); // show message
                    return; // finish method
                }

                linesList.set(i, normalizedUsername); // add the checked and normalized username to the arraylist
            }

            // loop through all the checked and normalised usernames and create them
            for(String username : linesList){
                userSystem.addNewUser(username);
            }
        }
        dispose(); // close dialog
    }

    /**
     * Run to open dialog
     */
    public void run() {
        this.setVisible(true);
    }
}