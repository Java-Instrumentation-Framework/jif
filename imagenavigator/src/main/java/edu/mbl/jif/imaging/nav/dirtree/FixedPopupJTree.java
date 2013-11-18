/*
 From http://www.objectdefinitions.com/odblog/2011/how-to-fix-right-click-selection-and-jpopupmenu-so-your-jtree-feels-native/
 */
package edu.mbl.jif.imaging.nav.dirtree;
//import com.jgoodies.looks.windows.WindowsLookAndFeel;
import javax.swing.*;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

// TODO: Copy to jif.gui
/**
 * A JTree with mouse listener logic to select items on right click,
 * before showing a popup
 * 
 * Also sets the magic PopupMenu property to fix the windows look and 
 * feel, so that clicking outside the popup will select items in the 
 * tree first time, rather than even being consumed by the closing popup
 */

public class FixedPopupJTree extends JTree {

    static {
        //Set the magic property which makes the first click outside the popup
        //capable of selecting tree nodes, as well as dismissing the popup.
        UIManager.put("PopupMenu.consumeEventOnClose", Boolean.FALSE);
    }

    public FixedPopupJTree() {
        getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        addMouseListener(new ShowPopupMouseListener());
    }

    private class ShowPopupMouseListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            showMenuIfPopupTrigger(e);
        }

        public void mouseClicked(MouseEvent e) {
            showMenuIfPopupTrigger(e);
        }

        public void mouseReleased(MouseEvent e) {
            showMenuIfPopupTrigger(e);
        }

        private void showMenuIfPopupTrigger(final MouseEvent e) {
            if (e.isPopupTrigger()) {
                //set the new selections before showing the popup
                setSelectedItemsOnPopupTrigger(e);

                //build an example popup menu from selections
                JPopupMenu menu = new JPopupMenu();
                for ( TreePath p : getSelectionPaths()) {
                    menu.add(new JMenuItem(p.getLastPathComponent().toString()));
                }

                //show the menu, offsetting from the mouse click slightly
                menu.show((Component)e.getSource(), e.getX() + 3, e.getY() + 3);
            }
        }

         /**
         * Fix for right click not selecting tree nodes -
         * We want to implement the following behaviour which matches windows explorer:
         * If the item under the click is not already selected, clear the current selections and select the
         * item, prior to showing the popup.
         * If the item under the click is already selected, keep the current selection(s)
         */
        private void setSelectedItemsOnPopupTrigger(MouseEvent e) {
            TreePath p = getPathForLocation(e.getX(), e.getY());
            if ( ! getSelectionModel().isPathSelected(p)) {
                getSelectionModel().setSelectionPath(p);
            }
        }
    }

       //A simple test frame to show the effect of these fixes
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
//                try {
//                    UIManager.setLookAndFeel(WindowsLookAndFeel.class.getName());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                FixedPopupJTree fixedPopupJTree = new FixedPopupJTree();

                TreeModel treeModel = createExampleTreeModel();
                fixedPopupJTree.setModel(treeModel);

                JFrame frame = new JFrame();
                frame.getContentPane().add(new JScrollPane(fixedPopupJTree));
                frame.setSize(new Dimension(300, 600));
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null); //centre on screen
                frame.setVisible(true);
            }
        });
    }

    private static TreeModel createExampleTreeModel() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        for ( int child = 0; child <=10 ; child++) {
            root.add(new DefaultMutableTreeNode("child-" + child));
        }
        DefaultTreeModel treeModel = new DefaultTreeModel(root);
        return treeModel;
    }
}