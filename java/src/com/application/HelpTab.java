package com.application;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by CheokHo on 17/03/2016.
 */
public class HelpTab extends JDialog {

    public HelpTab() {
        setTitle("User Guide");
        createTabbedPane();

        setMinimumSize(new Dimension(700, 700));
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public void createTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        getContentPane().add(tabbedPane);
        //ImageIcon image1 = new ImageIcon("/com/helpimages/Screen 1.png");

        try {
            BufferedImage image1 = ImageIO.read(HelpTab.class.getClassLoader().getResource("com/helpimages/screen1.png"));
            BufferedImage image2 = ImageIO.read(HelpTab.class.getClassLoader().getResource("com/helpimages/screen2.png"));
            JPanelMod panel1 = new JPanelMod();
            JPanelMod panel2 = new JPanelMod();
            panel1.setBackground(image1);
            panel2.setBackground(image2);

            tabbedPane.addTab("Creating a new PDA", panel1);
            tabbedPane.addTab("Editing a PDA", panel2);

        } catch (IOException e) {
            e.printStackTrace();
        }

//        tabbedPane.addTab("Editing a PDA", panel2);
//        tabbedPane.addTab("Running a simulation", panel3);
//        tabbedPane.addTab("Saving and loading a PDA", panel4);
//        tabbedPane.addTab("Notation", panel5);
    }


}
