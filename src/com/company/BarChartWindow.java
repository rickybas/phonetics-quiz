package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;

// http://www.javacodex.com/Graphics/Bar-Chart
public class BarChartWindow extends JFrame {

    /**
     * A bar chart window
     *
     * @param sortedBy will be used in the title for the bar chart
     * @param labels   a list of the labels of each bar
     * @param values   a list of the values of each bar
     */
    public BarChartWindow(String sortedBy, String[] labels, double[] values) {
        super("Bar Chart"); // title
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(1000, 500); // width = 1000, height = 500

        // create a bar chart component
        BarChart bc = new BarChart(labels, values,
                "Bar chart of " + sortedBy + " score (users removed with average score of zero)");
        JScrollPane scrollPane = new JScrollPane(bc); // add the bar chart to a scrollable view (x-axis)

        add(scrollPane); // only add scrollable bar chart to window
    }

    public class BarChart extends JPanel {
        private double[] values;
        private String[] labels;
        private String title;
        private int barWidth = 200;

        public BarChart(String[] labels, double[] values, String title) {
            this.labels = labels;
            this.values = values;
            this.title = title;
            setPreferredSize(new Dimension(values.length * barWidth, 400)); // size of component
            Logging.log(Level.INFO, "Opened BarChart window titled: " + title);
        }

        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            // if no values then don't paint anything
            if (values == null || values.length == 0) {
                return;
            }

            // find the minimum and maximum values of the dataset, used to calculate the heights of the bars
            double minValue = 0;
            double maxValue = 0;
            /* loop through all the values and if value smaller than the others that came before set minValue equal to it
             * and also if value larger than the others that came before set maxValue equal to it*/
            for (int i = 0; i < values.length; i++) {
                if (minValue > values[i]) {
                    minValue = values[i];
                }
                if (maxValue < values[i]) {
                    maxValue = values[i];
                }
            }

            Dimension dim = getSize(); // size of the component
            int panelHeight = dim.height; // get height from size

            // set title font
            Font titleFont = new Font("Book Antiqua", Font.BOLD, 15);
            FontMetrics titleFontMetrics = graphics.getFontMetrics(titleFont);

            // set label font
            Font labelFont = new Font("Book Antiqua", Font.PLAIN, 14);
            FontMetrics labelFontMetrics = graphics.getFontMetrics(labelFont);

            // title height = get title font height
            int stringHeight = titleFontMetrics.getAscent();
            int stringWidth = 10;
            graphics.setFont(titleFont);
            graphics.drawString(title, stringWidth, stringHeight); // draw title

            int top = titleFontMetrics.getHeight(); // position of top of title
            int bottom = labelFontMetrics.getHeight(); // position of top of label

            // if no difference in values then end method and draw no bars
            if (maxValue == minValue) {
                return;
            }

            // how big the bars should be in relation to the window size
            double scale = (panelHeight - top - bottom) / (maxValue - minValue);
            stringHeight = panelHeight - labelFontMetrics.getDescent();
            graphics.setFont(labelFont);

            // loop through each value
            for (int j = 0; j < values.length; j++) {
                // calculate the position of the bar and label
                int valueP = j * barWidth + 1;
                int valueQ = top;
                int height = (int) (values[j] * scale);
                if (values[j] >= 0) {
                    valueQ += (int) ((maxValue - values[j]) * scale);
                } else {
                    valueQ += (int) (maxValue * scale);
                    height = -height;
                }

                graphics.setColor(ColorScheme.CONCRETE);
                graphics.fillRect(valueP, valueQ, barWidth - 2, height); // draw concrete colored bar
                graphics.setColor(Color.black);
                graphics.drawRect(valueP, valueQ, barWidth - 2, height); // draw bar black outline

                // make label with the actual value of the bar
                String labelWithValue = labels[j] + " (" + Double.toString(values[j]) + "%)";

                // calculate the width of the label from the label string
                int labelWidth = labelFontMetrics.stringWidth(labelWithValue);
                stringWidth = j * barWidth + (barWidth - labelWidth) / 2;
                // draw label below bar
                graphics.drawString(labelWithValue, stringWidth, stringHeight);
            }
        }
    }

}
