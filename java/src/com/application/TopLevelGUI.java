package com.application;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.handler.mxConnectionHandler;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by CheokHo on 25/01/2016.
 */
public class TopLevelGUI extends JFrame{

    private Object parent;
    private mxGraph graph;
    private ArrayList<Node> nodeArray;
    private PDAVersionGUI pdaTypeGUI;

    public TopLevelGUI(){
        super("Pushdown Automata Tool");
        graph = new mxGraph();
        nodeArray = new ArrayList<Node>();
        createGraphPane();
        createMenuBar();
    }


    public void createMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu file = new JMenu("File");
        JMenuItem menuNew = new JMenuItem("New");
        JMenuItem menuOpen = new JMenuItem("Open");
        JMenuItem menuSave = new JMenuItem("Save");
        JMenuItem menuClose = new JMenuItem("Close");
        menuClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        file.add(menuNew); file.add(menuOpen); file.add(menuSave); file.add(menuClose);

        menuNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pdaTypeGUI = new PDAVersionGUI(getTopLevelGUI());
            }
        });

        JMenu edit = new JMenu("Edit");
        JMenuItem menuUndo = new JMenuItem("Undo");
        JMenuItem menuRedo = new JMenuItem("Redo");
        JMenuItem menuDelete = new JMenuItem("Delete");
        edit.add(menuUndo); edit.add(menuRedo); edit.add(menuDelete);

        menuBar.add(file); menuBar.add(edit);

    }

    public void createGraphPane(){
//        Edge edge = new Edge(graph);
//        try
//        {
//            Object v1 = graph.insertVertex(parent, null, "a", 20, 20, 80,
//                    60);
//            Object v2 = graph.insertVertex(parent, null, "b", 240, 150,
//                    80, 60);
//            graph.insertEdge(parent, null, "Edge", v1, v2);
//        }
//        finally
//        {
//            graph.getModel().endUpdate();
//        }
//        Node nodeA = new Node(graph, "a", false);
//        nodeA.createNode(20,20);
//        Node nodeB = new Node(graph, "b", true);
//        nodeB.createNode(240,150);
//        try {
//            edge.addEdge(nodeA.getNode(), nodeB.getNode(), "transition rule temp");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        parent = graph.getDefaultParent();
        //Object a=createNode(20,20, "a", false);
        //Object b=createNode(240,150, "b", true);
        //addEdge(a, b, "temp trans rule");

        mxGraphComponent graphComponent = new mxGraphComponent(graph) {
            @Override
            protected mxConnectionHandler createConnectionHandler() {
                return new mxConnectionHandler(this) {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        super.mouseReleased(e);
                    }
                };
            }
        };



        //new mxKeyboardHandler(graphComponent); needs to be fixed for backend (keyboard deleting)

        //This handles edge creation handlers.
        graphComponent.getConnectionHandler().addListener(mxEvent.CONNECT, new mxEventSource.mxIEventListener() {
            @Override
            public void invoke(Object sender, mxEventObject evt) {
                System.out.println("edge: "+evt.getProperty("cell"));
                graph.getModel().remove(evt.getProperty("cell"));
            }
        });


        //This handles node creation handlers.
        graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
            Object nodePressed;
            mxCell cellPressed;
            public void mousePressed(MouseEvent e) {
                nodePressed = graphComponent.getCellAt(e.getX(), e.getY());
                //Left click (maybe not needed)
                if (SwingUtilities.isLeftMouseButton(e)) {
                    cellPressed = (mxCell) graphComponent.getCellAt(e.getX(), e.getY());
                    if (cellPressed!=null) {
                        System.out.println("Left Click Cell Value: "+cellPressed.getValue().toString());
                    }
                }
                if (SwingUtilities.isRightMouseButton(e)) {
                    cellPressed = (mxCell) graphComponent.getCellAt(e.getX(), e.getY());
                    if (cellPressed != null) {
                        System.out.println("Right Click Cell Value: "+cellPressed.getValue().toString());
                        JPopupMenu popup = new JPopupMenu();
                        JMenuItem delete = new JMenuItem("Delete");
                        popup.add(delete);
                        popup.show(e.getComponent(), e.getX(), e.getY());
                        delete.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                // TODO Auto-generated method stub
                                graph.removeCells(new Object[]{cellPressed});
                                for (int i = 0; i < nodeArray.size(); i++) {
                                    if (nodeArray.get(i).toString().equals(cellPressed.getValue().toString())) {
                                        nodeArray.remove(i);
                                    }
                                }
                                System.out.println("Updated Node Array: " + nodeArray.toString());
                                repaint();
                            }
                        });
                    }
                }
            }

            public void mouseReleased(MouseEvent e) {
                mxCell cell = (mxCell) graphComponent.getCellAt(e.getX(), e.getY());
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (cell != null && cell.isVertex() && e.getClickCount() == 2) {
                        TransitionRuleGUI transRule = new TransitionRuleGUI(cellPressed.getValue().toString(), cell.getValue().toString(),pdaTypeGUI.getPdaInputGUI().getStackArray(), pdaTypeGUI.getPdaInputGUI().getInputArray());
                        System.out.println("STACK ARRAY ON TRANS RULE RELEASE "+pdaTypeGUI.getPdaInputGUI().getStackArray());
                        System.out.println("INPUT ARRAY ON TRANS RULE RELEASE "+pdaTypeGUI.getPdaInputGUI().getInputArray());
                        graph.insertEdge(parent, null, "self loop", nodePressed, (Object) cell);
                    }
                    else if (cell != null && cell.isVertex() && !cellPressed.getValue().equals(cell.getValue())) {
                        TransitionRuleGUI transRule = new TransitionRuleGUI(cellPressed.getValue().toString(), cell.getValue().toString(),pdaTypeGUI.getPdaInputGUI().getStackArray(), pdaTypeGUI.getPdaInputGUI().getInputArray());
                        System.out.println("STACK ARRAY ON TRANS RULE RELEASE "+pdaTypeGUI.getPdaInputGUI().getStackArray());
                        System.out.println("INPUT ARRAY ON TRANS RULE RELEASE "+pdaTypeGUI.getPdaInputGUI().getInputArray());
                        graph.insertEdge(parent, null, "test for now", nodePressed, (Object) cell);
                    }
                }
            }
        });

        getContentPane().add(graphComponent);
    }

    public Object createNode(int x,int y, String state, boolean isAccepting) {
        graph.getModel().beginUpdate();
        Object node;
        Node newNode;
        try
        {
            if (isAccepting) {
                node = graph.insertVertex(parent, null, state, x, y, 80, 60, "shape=doubleEllipse");
            }
            else {
                node = graph.insertVertex(parent, null, state, x, y, 80, 60, "shape=ellipse");
            }
            newNode = new Node(node, state);
            nodeArray.add(newNode);

            graph.setVertexLabelsMovable(false);
            graph.setCellsEditable(false);
        }
        finally
        {
            graph.getModel().endUpdate();
        }
        return node;
    }

//    //Instead of taking in an Object for nodes, pass in Node object instead.
//    public void addEdge(Node nodeFrom, Node nodeTo, String transRule){
//        Object edge;
//        Edge newEdge;
//        //Node newNodeFrom = new Node(nodeFrom, ); //need to get the cell name to create this.
//        Node newNodeTo;
//        try
//        {
//            edge = graph.insertEdge(parent, null, transRule, nodeFrom.getNode(), nodeTo.getNode());
//            newEdge = new Edge(edge);
//            //newEdge.setEdgeRule();
//            graph.setAllowDanglingEdges(false);
//            graph.setEdgeLabelsMovable(false);
//            graph.setCellsEditable(false);
//        }
//        finally
//        {
//            graph.getModel().endUpdate();
//        }
//    }

    public ArrayList<Node> getNodeArray() {
        return nodeArray;
    }

    public TopLevelGUI getTopLevelGUI() {
        return this;
    }
}
