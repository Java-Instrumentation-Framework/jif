/**
 * Copyright Copyright 2008 Simon Andrews
 *
 * This file is part of FocalPoint.
 *
 * FocalPoint is free software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * FocalPoint is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with StackMeasure; if
 * not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA
 */
package edu.mbl.jif.imaging.nav.dirtree;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class DirectoryTree extends JPanel implements TreeSelectionListener {

   // This class presents a windows explorer style view
   // of the directories on a filesystem.  It allows 
   // listeners to attach to be notified when the user
   // selects a new directory.
   private final JTree dirTree;
   private final DefaultTreeModel treeModel;
   private final Vector<DirectorySelectionListener> listeners = new Vector<DirectorySelectionListener>();
   private static final FileSystemView fileSystemView = FileSystemView.getFileSystemView();
   //protected JPopupMenu popup_menu;
   protected PopupMenuTree popup_menu;
   TreePath clickedPath;

   public DirectoryTree() {

      treeModel = new DefaultTreeModel(null);
      dirTree = new FixedPopupJTree();
      dirTree.setModel(treeModel);
      dirTree.setCellRenderer(new SystemIconRenderer());

      File[] roots = fileSystemView.getRoots();

      if (roots.length > 1) {
         DefaultMutableTreeNode root = new FolderNode(null, "Computer");
         treeModel.setRoot(root);
         for (int r = 0; r < roots.length; r++) {
            FolderNode n = new FolderNode(roots[r]);
            n.setAllowsChildren(true);
            root.add(n);
         }
      } else {
         FolderNode n = new FolderNode(roots[0]);
         // We need to add the children of this node too.
         treeModel.setRoot(n);
      }
      dirTree.addTreeSelectionListener(this);
      setLayout(new BorderLayout());
      add(new JScrollPane(dirTree), BorderLayout.CENTER);
      dirTree.addMouseListener(new PopupTrigger());
   }

   public void setStartIn(String startInStr) {
      File startInFile = new File(startInStr);
      if (startInFile != null) {
         FolderNode startIn = new FolderNode(startInFile);
         treeModel.setRoot(startIn);
      }
   }

   public void addListener(DirectorySelectionListener l) {
      if (l != null && !listeners.contains(l)) {
         listeners.add(l);
      }
   }

   public void removeListener(DirectorySelectionListener l) {
      if (l != null && listeners.contains(l)) {
         listeners.remove(l);
      }
   }

   @Override
   public void valueChanged(TreeSelectionEvent tse) {
      File f = ((FolderNode) tse.getPath().getLastPathComponent()).getFile();
      //TreePath path = getPath((DefaultMutableTreeNode) tse.getPath().getLastPathComponent());

      if (f != null) {
         Enumeration<DirectorySelectionListener> e = listeners.elements();
         while (e.hasMoreElements()) {
            e.nextElement().directorySelected(f);
         }
      }
   }

//   public TreePath getPath(TreeNode treeNode) {
//      List<Object> nodes = new ArrayList<Object>();
//      if (treeNode != null) {
//         nodes.add(treeNode);
//         treeNode = treeNode.getParent();
//         while (treeNode != null) {
//            nodes.add(0, treeNode);
//            treeNode = treeNode.getParent();
//         }
//      }
//      return nodes.isEmpty() ? null : new TreePath(nodes.toArray());
//   }
   //---------------------------------------------
   // Open to this Dir node...
   // GBH added this... 
   public void openDirNode(String dirPath) {

      StringTokenizer st2 = new StringTokenizer(dirPath, "/");
      String[] strArray = new String[st2.countTokens()-1];
      int n = 0;
      while (st2.hasMoreElements()) {
         
         if(n==0) 
            st2.nextElement();
         else 
            strArray[n-1] = (String) st2.nextElement();
         n++;
      }
      TreePath treePath = findByName(dirTree, strArray);
      
      //TreePath path = traverse(dirTree, dirPath);

//      TreePath path = find((DefaultMutableTreeNode) dirTree.getModel().getRoot(), dirPath);
//      if (path != null) {
//         // dirTree.setExpandsSelectedPaths(true);
//         dirTree.setSelectionPath(path);
//         dirTree.scrollPathToVisible(path);
//      } else {
//         System.out.println("Node with string " + dirPath + " not found");
//      }
   }
   // From http://stackoverflow.com/questions/8210630/how-to-search-a-particular-node-in-jtree-and-make-that-node-expanded

   private TreePath find(DefaultMutableTreeNode root, String s) {
      @SuppressWarnings("unchecked")
      Enumeration<DefaultMutableTreeNode> e = root.breadthFirstEnumeration();
      
      while (e.hasMoreElements()) {
         DefaultMutableTreeNode node = e.nextElement();
         String sNode = node.toString();
         System.out.println("sNode = " + sNode);
         if (node.toString().equalsIgnoreCase(s)) {
            return new TreePath(node.getPath());
         }
      }
      return null;
   }
//   public void openDirNode(String dirPath) {
//      DefaultMutableTreeNode node = searchNode(dirPath);
//      if (node != null) {
//         TreeNode[] nodes = treeModel.getPathToRoot(node);
//         TreePath path = new TreePath(nodes);
//         dirTree.scrollPathToVisible(path);
//         dirTree.setSelectionPath(path);
//      } else {
//         System.out.println("Node with string " + dirPath + " not found");
//      }
//   }

//   public DefaultMutableTreeNode searchNode(String nodeStr) {
//      DefaultMutableTreeNode node = null;
//      Object rootObject = treeModel.getRoot();
//      if ((rootObject != null) && (rootObject instanceof DefaultMutableTreeNode)) {
//         DefaultMutableTreeNode r = (DefaultMutableTreeNode) rootObject;
//         Enumeration e = r.depthFirstEnumeration();
//         while (e.hasMoreElements()) {
//            node = (DefaultMutableTreeNode) e.nextElement();
//            String nodeS = node.getUserObject().toString();
//            if (nodeStr.equals(nodeS)) {
//               return node;
//            }
//         }
//      }
//      return null;
//   }
//   public TreePath searchNode(String nodeStr) {
//      //DefaultMutableTreeNode node = null;
//      Object rootObject = treeModel.getRoot();
//      if ((rootObject != null) && (rootObject instanceof DefaultMutableTreeNode)) {
//         DefaultMutableTreeNode r = (DefaultMutableTreeNode) rootObject;
//         @SuppressWarnings("unchecked")
//         Enumeration<DefaultMutableTreeNode> e = r.depthFirstEnumeration();
//         while (e.hasMoreElements()) {
//            DefaultMutableTreeNode node = e.nextElement();
//            if (node.toString().equalsIgnoreCase(nodeStr)) {
//               return new TreePath(node.getPath());
//            }
//         }
//      }
//      return null;
//   }
//
   ////////////////////////////////////////////////////
   // findByName(tree, new String[] { "JTree", "A", "a" });
   public static TreePath findByName(JTree tree, String[] names) {
      TreeNode root = (TreeNode) tree.getModel().getRoot();
      return find(tree, new TreePath(root), names, 0);
   }

   private static TreePath find(JTree tree, TreePath parent, Object[] nodes, int depth) {
      TreeNode node = (TreeNode) parent.getLastPathComponent();
      Object o = node;

      if (o.equals(nodes[depth])) {
         if (depth == nodes.length - 1) {
            return parent;
         }
         if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements();) {
               TreeNode n = (TreeNode) e.nextElement();
               TreePath path = parent.pathByAddingChild(n);
               TreePath result = find(tree, path, nodes, depth + 1);
               if (result != null) {
                  return result;
               }
            }
         }
      }
      return null;
   }
   ////////////////////////////////////////////////////

   // TODO this is SLOW.... 
   public TreePath traverse(JTree tree, String dirPath) {
      TreeModel model = tree.getModel();
      if (model != null) {
         Object root = model.getRoot();
         //System.out.println(root.toString());
         TreePath treePath = walk(model, root, dirPath);
         return treePath;
      } else {
         //System.out.println("Tree is empty.");
      }
      return null;
   }

   protected TreePath walk(TreeModel model, Object o, String dirPath) {
      TreePath treePath = null;
      int cc;
      cc = model.getChildCount(o);
      for (int i = 0; i < cc; i++) {
         Object child = model.getChild(o, i);
         if (model.isLeaf(child)) {
            System.out.println(child.toString());
         } else {
            DefaultMutableTreeNode r = (DefaultMutableTreeNode) child;
            //System.out.println(dirPath);
            //System.out.println("r.getUserObject()" + r.getUserObject());
            File f = (File) r.getUserObject();
            //System.out.println("getAbsolutePath= "+f.getAbsolutePath());
            if (f.getAbsolutePath().equalsIgnoreCase(dirPath)) {
               return new TreePath(r.getPath());
            }
            //System.out.println(child.toString()+"--");
            treePath = walk(model, child, dirPath);
            if (treePath != null) {
               return treePath;
            }
         }
      }
      return null;
   }
   
   //=========================================================================================

   private class SystemIconRenderer extends DefaultTreeCellRenderer {

      public Component getTreeCellRendererComponent(JTree tree, Object value,
              boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {

         Component rc = super.getTreeCellRendererComponent(tree, value, selected, expanded,
                 leaf, row, hasFocus);
         if (rc instanceof JLabel && value instanceof FolderNode) {
            JLabel jl = (JLabel) rc;
            File f = ((FolderNode) value).getFile();
            jl.setText(fileSystemView.getSystemDisplayName(f));
            jl.setIcon(fileSystemView.getSystemIcon(f));
         }
         return rc;
      }
   }

   private class FolderNode extends DefaultMutableTreeNode {

      private final File file;
      private boolean hasBeenChecked = false;

      public FolderNode(File file) {
         super(file);
//			System.out.println("Made a node with name "+file.getName()+" with parent "+parent);
         this.file = file;
      }

      public FolderNode(File file, String name) {
         super(name);
         this.file = file;
      }

      @Override
      public boolean getAllowsChildren() {
         return true;
      }

      @Override
      public int getChildCount() {
         if (!hasBeenChecked) {
            addChildren();
         }
         return super.getChildCount();
      }

      private void addChildren() {
         hasBeenChecked = true;
         if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
               Arrays.sort(files);
               for (int i = 0; i < files.length; i++) {

                  /*
                   * Testing a floppy drive using isFile is REALLY slow
                   * hence the extra pre-tests in this case.
                   */
                  if (fileSystemView.isFloppyDrive(files[i])
                          || fileSystemView.isDrive(files[i]) || !files[i].isFile()) {
                     /*
                      * It seems daft to check that a file we've just listed actually
                      * exists, but if you have a symlink which doesn't point anywhere
                      * that's exactly what you get.
                      */
                     if (!files[i].isHidden() && files[i].exists()) {
                        add(new FolderNode(files[i]));
                     }
                  }
               }
            }
         }
      }

      public File getFile() {
         return file;
      }

      @Override
      public boolean isLeaf() {
         return false;
      }

      @Override
      public String toString() {
         if (file != null) {
            if (file.getName().length() > 0) {
               return FileSystemView.getFileSystemView().getSystemDisplayName(file);
            } else {
               return super.toString();
            }
         } else {
            return super.toString();
         }
      }
   }

   
   //<editor-fold defaultstate="collapsed" desc="PopupMenu">
   class PopupTrigger extends MouseAdapter {
      
      @Override
      public void mouseReleased(MouseEvent e) {
         if (e.isPopupTrigger()) {
            int x = e.getX();
            int y = e.getY();
            TreePath path = dirTree.getPathForLocation(x, y);
            if (path != null) {
               //               if (dirTree.isExpanded(path)) {
               //                  m_action.putValue(Action.NAME, "Collapse");
               //               } else {
               //                  m_action.putValue(Action.NAME, "Expand");
               //               }
               if (popup_menu != null) {
                  //popup_menu.show(dirTree, x, y);
                  popup_menu.show(dirTree, e.getPoint(),
                          (DefaultMutableTreeNode) dirTree.getLastSelectedPathComponent());
                  clickedPath = path;
               }
            }
         }
      }
   }
   //
   
   public void setPopup_menu(PopupMenuTree jPopupMenu) {
      this.popup_menu = jPopupMenu;
   }
   
   public JPopupMenu getPopup_menu() {
      return popup_menu;
   }
   //</editor-fold>

   
   //   public void addTreeListener() {
//
//      dirTree.addMouseListener(new MouseAdapter() {
//         public void mousePressed(MouseEvent evt) {
//            node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
//            loadDir(node);
//            if (evt.getButton() == 1 & evt.getClickCount() == 2) {
//               try {
//                  if (node != null) {
//                     if (((File) node.getUserObject()).isFile()) {
//                        Desktop.getDesktop().open((File) node.getUserObject());
//                     }
//                  }
//               } catch (IOException e) {
//                  JOptionPane.showMessageDialog(null, "Unable to read file", "Reading Error",
//                          JOptionPane.OK_OPTION);
//               }
//            }
//            if (evt.getButton() == 3) {
//               pMenu.show(tree, evt.getPoint(),
//                       (DefaultMutableTreeNode) tree.getLastSelectedPathComponent());
//            }
//         }
//      }
//
//   }
}
