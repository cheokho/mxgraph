package com.application;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxEdgeLabelLayout;
import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxEdgeStyle;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cheeeky on 13/02/2016.
 */
public class TransitionRuleGUI extends JDialog {

    private ArrayList<String> stackArray;
    private ArrayList<String> inputArray;
    private ArrayList<String> pushOperation;
    private ArrayList<Edge> edgeArray;
    private TopLevelGUI topLevelGUI;

    private JPanel centerPanel;
    private JLabel inputLabel;
    private JComboBox<String> inputComboBox;
    private JLabel stackLabel;
    private JComboBox<String> stackComboBox;
    private JLabel transitionLabel;

    private JPanel botPanel;

    private ButtonGroup group;
    private JRadioButton pushButton;
    private JRadioButton popButton;
    private JRadioButton nothingButton;

    private JButton okButton;

    public TransitionRuleGUI(TopLevelGUI topLevelGUI, String fromNode, String toNode, ArrayList<String> stackArray, ArrayList<String> inputArray) {
//        super("Transition rule from '"+fromNode+"' to '"+toNode+"'.");
        setTitle("Transition rule from '"+fromNode+"' to '"+toNode+"'.");
        this.stackArray=stackArray;
        this.inputArray=inputArray;
        this.topLevelGUI=topLevelGUI;
        edgeArray = topLevelGUI.getEdgeArray();
        createTransitionGUI();
        pack();
        setModal(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public void createTransitionGUI() {
        setLayout(new BorderLayout());
        centerPanel=new JPanel();
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        centerPanel.setLayout(new GridLayout(4, 2, 0, 10));
        inputLabel = new JLabel("If input symbol=");
        inputComboBox = new JComboBox<>();
        inputComboBox.setModel(new DefaultComboBoxModel(inputArray.toArray()));
        pushOperation = new ArrayList<String>();

        stackLabel=new JLabel("and top of stack symbol=");
        stackComboBox = new JComboBox<>();
        stackComboBox.setModel(new DefaultComboBoxModel(stackArray.toArray()));

        transitionLabel=new JLabel("then perform the following operation:");
        pushButton = new JRadioButton("push(__)");
        pushButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (String s:stackArray) {
                    s = "push("+s+")";
                    pushOperation.add(s);
                }
                String s = (String)JOptionPane.showInputDialog(null,"", "Select push operation", JOptionPane.QUESTION_MESSAGE, null, pushOperation.toArray(), pushOperation.get(0));

                if ((s != null) && (s.length() > 0)) {
                    pushButton.setText(s);
                }
            }
        });

        popButton = new JRadioButton("pop");
        nothingButton = new JRadioButton("ε (do nothing)");
        group = new ButtonGroup();
        group.add(pushButton); group.add(popButton); group.add(nothingButton);
        nothingButton.setSelected(true);

        FlowLayout botLayout = new FlowLayout();
        botPanel = new JPanel();
        botPanel.setLayout(botLayout);
        botPanel.add(pushButton); botPanel.add(popButton); botPanel.add(nothingButton);

        centerPanel.add(inputLabel); centerPanel.add(inputComboBox);
        centerPanel.add(stackLabel); centerPanel.add(stackComboBox);
        centerPanel.add(transitionLabel);
        centerPanel.add(botPanel);

        JPanel buttonPanel=new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        okButton = new JButton("OK");
        buttonPanel.add(okButton);
        //create Edge object here with transition rule defined.
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String edgeRule = "{" + inputComboBox.getSelectedItem().toString() + ", " + stackComboBox.getSelectedItem().toString() + ", " + getSelectedButtonText(group) + "}";
                addEdge(topLevelGUI.getGraph(), edgeRule);
                topLevelGUI.getGraphComponent().repaint();
                Edge edge = null;
                for (Node node: topLevelGUI.getNodeArray()) {
                    Node fromNode = null;
                    Node toNode = null;
                    if (node.toString().equals(topLevelGUI.getCellPressed().getValue().toString())) {
                        fromNode = node;
                    }
                    if (node.toString().equals(topLevelGUI.getCellReleased().getValue().toString())) {
                        toNode = node;
                    }
                    edge = new Edge(fromNode, toNode, edgeRule);
                }
                edgeArray.add(edge);
                System.out.println("Updated edge array: " + edgeArray);
                dispose();
            }
        });
        add(centerPanel, BorderLayout.NORTH);
        add(botPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void addEdge(mxGraph graph, String transRule) {
        graph.getModel().beginUpdate();
        try {
            Object parent = graph.getDefaultParent();

            mxIGraphLayout layout = new mxParallelEdgeLayout(graph, 30);
            layout.execute(parent);

            Map<String, Object> edgeStyle = new HashMap<String, Object>();
            edgeStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_CONNECTOR);
            edgeStyle.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
            edgeStyle.put(mxConstants.STYLE_STROKECOLOR, "#000000");
            edgeStyle.put(mxConstants.STYLE_FONTCOLOR, "#000000");
            edgeStyle.put(mxConstants.STYLE_ROUNDED, true);
            edgeStyle.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_SIDETOSIDE);
            //edgeStyle.put(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, "#ffffff");
            //edgeStyle.put(mxConstants.STYLE_EDGE, mxEdgeStyle.SegmentConnector);

            mxStylesheet stylesheet = new mxStylesheet();
            stylesheet.setDefaultEdgeStyle(edgeStyle);
            graph.setStylesheet(stylesheet);


            graph.setCellsBendable(true);
            graph.setCellsDisconnectable(false);
            graph.setEdgeLabelsMovable(false);

            graph.insertEdge(parent, null, transRule, (Object) topLevelGUI.getCellPressed(), (Object) topLevelGUI.getCellReleased());
        }
        finally {
            graph.getModel().endUpdate();
        }
    }

    public String getSelectedButtonText(ButtonGroup buttonGroup) {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                return button.getText();
            }
        }

        return null;
    }
//
//    public ArrayList<Edge> getEdgeArray() {
//        return edgeArray;
//    }

}
