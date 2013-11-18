/*
 * from: http://www.jroller.com/santhosh/entry/incremental_search_jtree
 */
package edu.mbl.jif.imaging.nav.util;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import java.net.URL;
import java.io.IOException;
import java.awt.Dimension;

public class TreeStructure extends JPanel implements TreeSelectionListener, ActionListener {

   private String textvalue;
   private JEditorPane htmlPane;
   private JTree tree;
   private URL helpURL;
   private static boolean DEBUG = false;
   private JPanel panel1;
   private JLabel label1;
   private JTextField txtarea;
   private JButton button1;
   private JButton button2;
//Optionally play with line styles. Possible values are 
//"Angled" (the default), "Horizontal", and "None". 
   private static boolean playWithLineStyle = false;
   private static String lineStyle = "Horizontal";
//Optionally set the look and feel. 
   private static boolean useSystemLookAndFeel = false;

   public TreeStructure() {
//super(new GridLayout(1,0)); 
      panel1 = new JPanel();
      label1 = new JLabel("Search For:");
      txtarea = new JTextField(15);
      button1 = new JButton("Search");
      button1.addActionListener(this);
      panel1.add(label1);
      panel1.add(txtarea);
      panel1.add(button1);



//Create the nodes. 
      DefaultMutableTreeNode top =
              new DefaultMutableTreeNode("THE TREE FOR TRIAL");
      createNodes(top);

//Create a tree that allows one selection at a time. 
      tree = new JTree(top);
      tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

//Listen for when the selection changes. 
      tree.addTreeSelectionListener(this);

      if (playWithLineStyle) {
         System.out.println("line style = " + lineStyle);
         tree.putClientProperty("JTree.lineStyle", lineStyle);
      }

//Create the scroll pane and add the tree to it. 
      JScrollPane treeView = new JScrollPane(tree);

//Create the HTML viewing pane. 
      htmlPane = new JEditorPane();
      htmlPane.setEditable(false);
      initHelp();
      JScrollPane htmlView = new JScrollPane(htmlPane);

//Add the scroll panes to a split pane. 
      JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
      splitPane.setLeftComponent(treeView);
//splitPane.setTopComponent(treeView); 
      splitPane.setRightComponent(htmlView);

      Dimension minimumSize = new Dimension(100, 100);
      htmlView.setMinimumSize(minimumSize);
      treeView.setMinimumSize(minimumSize);
      splitPane.setDividerLocation(100); //XXX: ignored in some releases 
//of Swing. bug 4101306 
//workaround for bug 4101306: 
//treeView.setPreferredSize(new Dimension(100, 100)); 

      splitPane.setPreferredSize(new Dimension(500, 300));

//Add the split pane to this panel. 
      add(panel1);
      add(splitPane);
   }

   public void actionPerformed(ActionEvent ae) {

      if (ae.getSource() == button1) {
         textvalue = txtarea.getText();
         DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getModel().getRoot();
         for (int i = 0; i < node.getChildCount(); i++) {

            searchTree(node);
         }
         while (!node.isLeaf()) {
         }
         System.out.println("hi");

      }

   }

   private void searchTree(DefaultMutableTreeNode node) {
      // System.out.println("First :::" + node.getUserObject().toString()); 
      String sName = null;
      node = node.getNextNode();
      // System.out.println("2 nd:::" + node.getUserObject().toString()); 
      System.out.println(node);
      //System.out.println(node.getUserObject()); 
      if (node.getUserObject().getClass().equals(String.class)) {
         sName = node.getUserObject().toString();
      }
      if (node.getUserObject().getClass().equals(NodeInfo.class)) {
         sName = ((NodeInfo) node.getUserObject()).toString();
      }
      if (sName.indexOf(textvalue) != -1) {
         //System.out.println("Path"+node.getUserObjectPath()); 
         //tree.addSelectionPaths((TreePath[]) node.getUserObjectPath()); 
         System.out.println("Path" + sName);
      }
      System.out.println("Node 1:::" + sName);
      searchTree(node);
      for (int i = 0; i < node.getSiblingCount(); i++) {

         if (node.getNextSibling() != null) {
            node = node.getNextSibling();
         }
         if (node.getUserObject().getClass().equals(String.class)) {
            sName = node.getUserObject().toString();
         }
         if (node.getUserObject().getClass().equals(NodeInfo.class)) {
            sName = ((NodeInfo) node.getUserObject()).toString();
         }
         // NodeInfo n1 = (NodeInfo) node.getUserObject(); 
         System.out.println("Node 2 :::" + sName);
      }
   }

   /**
    * Required by TreeSelectionListener interface.
    */
   public void valueChanged(TreeSelectionEvent e) {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

      if (node == null) {
         return;
      }

      Object nodeInfo = node.getUserObject();
      if (node.isLeaf()) {
         NodeInfo Node = (NodeInfo) nodeInfo;
         /*displayURL(node.bookURL); 
          if (DEBUG) { 
          System.out.print(book.bookURL + ": \n "); 
          } 
          } else { 
          displayURL(helpURL); 
          } 
          if (DEBUG) { 
          System.out.println(nodeInfo.toString()); 
          }*/
      }
   }

   private class NodeInfo {

      public String nodeName;
//public String bookName; 
//public URL bookURL; 

      public NodeInfo(String node) {
         nodeName = node;

      }

      public String toString() {
         return nodeName;
      }
   }

   private void initHelp() {
      String s = "TreeStructureHelp.html";
      helpURL = TreeStructure.class.getResource(s);
      if (helpURL == null) {
         System.err.println("Couldn't open help file: " + s);
      } else if (DEBUG) {
         System.out.println("Help URL is " + helpURL);
      }

      displayURL(helpURL);
   }

   private void displayURL(URL url) {
      try {
         if (url != null) {
            htmlPane.setPage(url);
         } else { //null url 
            htmlPane.setText("File Not Found");
            if (DEBUG) {
               System.out.println("Attempted to display a null URL.");
            }
         }
      } catch (IOException e) {
         System.err.println("Attempted to read a bad URL: " + url);
      }
   }

   private void createNodes(DefaultMutableTreeNode top) {
      DefaultMutableTreeNode category = null;
      DefaultMutableTreeNode book = null;

      category = new DefaultMutableTreeNode("Books for Java Programmers");
      top.add(category);

//original Tutorial 
      book = new DefaultMutableTreeNode(new NodeInfo("The Java Tutorial: A Short Course on the Basics"));
      category.add(book);

//Tutorial Continued 
      book = new DefaultMutableTreeNode(new NodeInfo("The Java Tutorial Continued: The Rest of the JDK"));
      category.add(book);

      category = new DefaultMutableTreeNode("Books for Oracle Programmers");
      top.add(category);

//original Tutorial 
      book = new DefaultMutableTreeNode(new NodeInfo("The Java Tutorial: A Short Course on the Basics"));
      category.add(book);

//Tutorial Continued 
      book = new DefaultMutableTreeNode(new NodeInfo("The Java Tutorial Continued: The Rest of the JDK"));
      category.add(book);

//JFC Swing Tutorial 
      book = new DefaultMutableTreeNode(new NodeInfo("The JFC Swing Tutorial: A Guide to Constructing GUIs"));
      category.add(book);

//Bloch 
      book = new DefaultMutableTreeNode(new NodeInfo("Effective Java Programming Language Guide"));
      category.add(book);

//Arnold/Gosling 
      book = new DefaultMutableTreeNode(new NodeInfo("The Java Programming Language"));
      category.add(book);

//Chan 
      book = new DefaultMutableTreeNode(new NodeInfo("The Java Developers Almanac"));
      category.add(book);

      category = new DefaultMutableTreeNode("Books for Java Implementers");
      top.add(category);

//VM 
      book = new DefaultMutableTreeNode(new NodeInfo("The Java Virtual Machine Specification"));
      category.add(book);

//Language Spec 
      book = new DefaultMutableTreeNode(new NodeInfo("The Java Language Specification"));
      category.add(book);
   }

   /**
    * Create the GUI and show it. For thread safety, this method should be invoked from the
    * event-dispatching thread.
    */
   private static void createAndShowGUI() {
      if (useSystemLookAndFeel) {
         try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
         } catch (Exception e) {
            System.err.println("Couldn't use system look and feel.");
         }
      }

//Make sure we have nice window decorations. 
      JFrame.setDefaultLookAndFeelDecorated(true);

//Create and set up the window. 
      JFrame frame = new JFrame("TreeStructure");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//Create and set up the content pane. 
      TreeStructure newContentPane = new TreeStructure();
      newContentPane.setOpaque(true); //content panes must be opaque 
      frame.setContentPane(newContentPane);

//Display the window. 
      frame.pack();
      frame.setVisible(true);
   }

   public static void main(String[] args) {
//Schedule a job for the event-dispatching thread: 
//creating and showing this application's GUI. 
      javax.swing.SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            createAndShowGUI();
         }
      });
   }
}
