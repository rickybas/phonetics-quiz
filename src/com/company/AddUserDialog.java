package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

public class AddUserDialog extends JDialog implements ActionListener {
    private JTextField usernameBox;
    private Button addButton;
    private UserSystem userSystem;

    public AddUserDialog(JFrame parent) {
        super(parent, "Enter new username", true); // title text2

        setLocationRelativeTo(null); // centers the window
        setPreferredSize(new Dimension(500, 50)); // width = 500px, height = 50px
        setResizable(false);

        userSystem = new UserSystem(); // used to manage user system interactions

        JPanel panel = new JPanel(); // main panel of the window
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        JLabel usernameLabel = new JLabel("Username: ");
        panel.add(usernameLabel); // add username label to the right of the window

        usernameBox = new JTextField(30); // main input box, one line
        panel.add(usernameBox); // add the right of the username label

        addButton = new Button("Add");
        addButton.setFont(new Font("Serif", Font.BOLD, 20));
        addButton.addActionListener(this); // run when actionPerformed when addButton clicked
        panel.add(addButton); // add the add button to the right of the main input box
        getRootPane().setDefaultButton(addButton); // when enter pressed, add button is pressed

        getContentPane().add(panel); // add the main panel to the window
        pack();
    }

    /**
     * Method run when add button pressed
     *
     * @param actionEvent
     */
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        // if action sent from add button
        if (source == addButton) {
            String normalizedUsername = Utils.normalizeUsername(usernameBox.getText()); // normalize username
            Utils.checkComment checkUsername = Utils.checkUsername(normalizedUsername); // check username

            /* check usernames, if the user name is empty (only contains blank spaces or nothing),
             * is longer than 18 characters or the user already exists then do not create the user */

            if (checkUsername == Utils.checkComment.EMPTY) {
                JOptionPane.showMessageDialog(this,
                        "Enter username",
                        "Enter username",
                        JOptionPane.WARNING_MESSAGE);
                return; // finish method
            }

            if (checkUsername == Utils.checkComment.LONG) {
                JOptionPane.showMessageDialog(this,
                        "Username is too long (greater than 18 characters)",
                        "Username is too long (greater than 18 characters)",
                        JOptionPane.WARNING_MESSAGE);
                return; // finish method
            }


            if (userSystem.addNewUser(normalizedUsername) == null) {
                JOptionPane.showMessageDialog(this,
                        "User already exists",
                        "User already exists",
                        JOptionPane.WARNING_MESSAGE);
                return; // finish method
            }
        }
        dispose(); // close dialog
    }

    /**
     * run to open dialog
     */
    public void run() {
        this.setVisible(true);
    }
}