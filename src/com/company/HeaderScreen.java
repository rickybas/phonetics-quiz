package com.company;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

class HeaderScreen extends Screen {
    JPanel body; // the main area to add graphics
    private HeaderLabel header; // the top header label

    /**
     * Run if only one parameter added, the layout is defaulted to a BoxLayout
     *
     * @param title
     */
    HeaderScreen(String title) {
        super(title);

        header = new HeaderLabel(title); // set new HeaderLabel object with title
        body = new JPanel(); // set new main body object (JPanel)
        body.setLayout(new BoxLayout(body, BoxLayout.PAGE_AXIS)); // set the body layout to BoxLayout
        body.setOpaque(false); // body is transparent
        body.setPreferredSize(new Dimension(Utils.WINDOW_WIDTH, 900)); // set the size of the body panel
        body.setBorder(new EmptyBorder(20, 20, 20, 20)); // set the body border to have a 20px margin around it

        add(header, BorderLayout.NORTH); // add the header to top of the page
        add(body, BorderLayout.SOUTH); // add the body to the bottom of the page
    }

    /**
     * Run if only two parameters added, the layout is changed to the parameter bodyLayoutManager
     *
     * @param title
     * @param bodyLayoutManager
     */
    HeaderScreen(String title, LayoutManager bodyLayoutManager) {
        super(title);

        header = new HeaderLabel(title); // set new HeaderLabel object with title
        body = new JPanel(bodyLayoutManager); // set body to inputted parameter
        body.setOpaque(false); // body is transparent
        body.setPreferredSize(new Dimension(Utils.WINDOW_WIDTH, 900)); // set the size of the body panel
        body.setBorder(new EmptyBorder(20, 20, 20, 20)); // set the body border to have a 20px margin around it

        add(header, BorderLayout.NORTH); // add the header to top of the page
        add(body, BorderLayout.SOUTH); // add the body to the bottom of the page
    }

}
