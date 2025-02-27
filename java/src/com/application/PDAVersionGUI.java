package com.application;

import com.algorithms.InputStack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by CheokHo on 26/01/2016.
 */
public class PDAVersionGUI extends JDialog {

    private JLabel label;
    private JRadioButton ndpda;
    private JRadioButton dpda;
    private ButtonGroup group;
    private GridLayout buttonLayout;
    private JPanel buttonPanel;
    private JPanel continuePanel;
    private JButton continueButton;
    private boolean isNew;
    public static Boolean isNdpda;
    private PDAInGUI pdaInputGUI;
    private TopLevelGUI topLevelGUI;

    public PDAVersionGUI(TopLevelGUI topLevelGUI, boolean isNew) {
        this.isNew=isNew;
        if (!isNew) {
            setTitle("Select PDA Type");
        } else {
            setTitle("Create new PDA");
        }
        this.topLevelGUI=topLevelGUI;
        setLayout(new BorderLayout());
        createpdaType();
        setModal(true);
        setSize(300, 150);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void createpdaType() {
        if (!isNew) {
            label = new JLabel("Select PDA type:");
        } else {
            label = new JLabel("Select PDA you wish to create:");
        }
        label.setBorder(BorderFactory.createEmptyBorder(10,5,10,5));
        ndpda = new JRadioButton("Nondeterministic");
        dpda = new JRadioButton("Deterministic");

        if (PDAVersionGUI.isNdpda != null) {
            if (PDAVersionGUI.isNdpda) {
                ndpda.setSelected(true);
            } else if (!PDAVersionGUI.isNdpda) {
                dpda.setSelected(true);
            }
        }

        if (!isNew) {
            continueButton = new JButton("OK");
            continueButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (dpda.isSelected()) {isNdpda = false; dispose(); }
                    else if (ndpda.isSelected()) {isNdpda = true; dispose(); }
                    else { JOptionPane.showMessageDialog(new JPanel(), "Please select a PDA type before continuing.", "Error", JOptionPane.ERROR_MESSAGE); }
                }
            });
        } else {
            continueButton = new JButton("Continue");
            continueButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (dpda.isSelected()) {
                        isNdpda = false;
                        pdaInputGUI = new PDAInGUI(topLevelGUI, false);
                        pdaInputGUI.setInputStack(new InputStack());
                        pdaInputGUI.setVisible(true);
                        dispose();
                    } else if (ndpda.isSelected()) {
                        isNdpda = true;
                        pdaInputGUI = new PDAInGUI(topLevelGUI, false);
                        pdaInputGUI.setInputStack(new InputStack());
                        pdaInputGUI.setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(new JPanel(), "Please select a PDA type before continuing.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        }

        buttonPanel = new JPanel();
        continuePanel = new JPanel();
        continuePanel.setLayout(new FlowLayout());
        continuePanel.add(continueButton);
        buttonLayout = new GridLayout(2,1);
        buttonPanel.setLayout(buttonLayout);
        buttonPanel.add(dpda); buttonPanel.add(ndpda);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 5));
        group = new ButtonGroup();
        group.add(ndpda); group.add(dpda);
        add(label, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(continuePanel, BorderLayout.SOUTH);

    }


    public PDAInGUI getPdaInputGUI() {
        return pdaInputGUI;
    }

}
