package edu.mbl.jif.imaging.nav.dirtree;

import java.awt.Point;
import java.awt.event.*;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.*;
import javax.swing.tree.*;

// TODO: Not Used
@SuppressWarnings("serial")
public class PopupMenuCell extends JPopupMenu {

   private JTree tree;
   private DefaultTreeModel treeModel;
   private DefaultMutableTreeNode lastSelNode, curNode;
   private JMenuItem menuItem;
   private boolean cut;

   public PopupMenuCell() {
      add(menuItem = new JMenuItem("Rename"));
      menuItem.setIcon(new ImageIcon("edit.png"));
      menuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            String str = curNode.toString();
            str = str.substring(str.lastIndexOf("\\") + 1, str.length());
            if (JOptionPane.showConfirmDialog(tree, "Rename " + str, "Rename",
                    JOptionPane.ERROR_MESSAGE)
                    == JOptionPane.YES_OPTION) {
               String reply = JOptionPane.showInputDialog(null, "Rename " + str);
               if (reply != "" && reply != null) {
                  str = curNode.toString();
                  str = str.substring(0, str.lastIndexOf("\\"));
                  ((File) curNode.getUserObject()).renameTo(new File(str + "\\" + reply));
                  curNode.setUserObject(new File(str + "\\" + reply));
               }
            }
         }
      });
      add(menuItem = new JMenuItem("Delete"));
      menuItem.setIcon(new ImageIcon("delete.png"));
      menuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            if (JOptionPane.showConfirmDialog(tree, "Delete " + curNode, "Delete File",
                    JOptionPane.ERROR_MESSAGE) == JOptionPane.YES_OPTION) {
               if (((File) curNode.getUserObject()).isFile()) {
                  ((File) curNode.getUserObject()).delete();
               } else {
                  DelDir((File) curNode.getUserObject());
               }
               treeModel = (DefaultTreeModel) tree.getModel();
               treeModel.removeNodeFromParent(curNode);
            }
         }
      });
      addSeparator();
      add(menuItem = new JMenuItem("Cut"));
      menuItem.setIcon(new ImageIcon("cut.png"));
      menuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            lastSelNode = curNode;
            cut = true;
         }
      });
      add(menuItem = new JMenuItem("Copy"));
      menuItem.setIcon(new ImageIcon("copy.png"));
      menuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            lastSelNode = curNode;
            cut = false;
         }
      });
      add(menuItem = new JMenuItem("Paste"));
      menuItem.setIcon(new ImageIcon("paste.png"));
      menuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent ae) {
            String str = lastSelNode.toString();
            str = str.substring(str.lastIndexOf("\\") + 1, str.length());
            ExecutorService threadExecutor = Executors.newFixedThreadPool(1);
            threadExecutor.execute
                    (new CopyDialog((File) lastSelNode.getUserObject(), 
                    new File(curNode.toString() + "\\" + str), cut));
            threadExecutor.shutdown();
            DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new File(
                    ((MutableTreeNode) tree.getLastSelectedPathComponent()).toString() + "\\" + str));
            if (lastSelNode.getChildCount() != 0) {
               newNode.add(new DefaultMutableTreeNode("**"));
            }
            treeModel.insertNodeInto(newNode, (MutableTreeNode) tree.getLastSelectedPathComponent(), 0);
            if (cut) {
               treeModel.removeNodeFromParent(lastSelNode);
            }
            tree.repaint();
         }
      });
   }

   public void show(JTree comp, Point p, DefaultMutableTreeNode node) {
      if (comp != null & p != null & node != null) {
         curNode = node;
         tree = comp;
         show(comp, p.x, p.y);
         if (lastSelNode != null & ((File) node.getUserObject()).isDirectory()) {
            if (!lastSelNode.isNodeDescendant(curNode)) {
               menuItem.setEnabled(true);
            }
         } else {
            menuItem.setEnabled(false);
         }
      }
   }

   public static void DelDir(File dir) {
      if (dir.isDirectory()) {
         File[] files = dir.listFiles();
         for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
               DelDir(files[i]);
               files[i].delete();
            } else {
               files[i].delete();
            }
         }
         dir.delete();
      }
      if (dir.exists()) {
         DelDir(dir);
      }
   }
}
