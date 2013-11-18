/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.imaging.nav.util;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author GBH
 */
public class BasicTreeSearcher {

// BasicTreeSearcher
// boolean equals(TreePath path, Object object) used for comparing TreePath with object
// TreePath getNextPath(TreePath path) - was used for geting next TreePath 
// TreePath getPrevPath(TreePath path) - was used for geting previous TreePath 
   private JTree tree;

   public BasicTreeSearcher(JTree tree) {
      this.tree = tree;
   }

   public boolean equals(TreePath path, Object object) {
      if (path == null || object == null) {
         return false;
      }

      String actualName = path.getLastPathComponent().toString();
      String searchedName = object.toString();

      if (actualName == null || searchedName == null) {
         return false;
      }

      int length = searchedName.length();

      if (actualName.length() < length) {
         return false;
      }

      actualName = actualName.substring(0, length);

      return (actualName.compareToIgnoreCase(searchedName) == 0);
   }

   protected TreePath getNextPath(TreePath path) {
      TreeModel model = tree.getModel();

      // return the first node 
      if (path == null) {
         return new TreePath(model.getRoot());
      }

      Object lastNode = path.getLastPathComponent();

      // if the NODE has children 

      if (model.getChildCount(lastNode) > 0) {
         return path.pathByAddingChild(model.getChild(lastNode, 0));
      }

      // if the NODE has NO children 

      int index = 0;
      int pathLength = path.getPathCount();
      Object parentNode = path.getPathComponent(pathLength - 2);

      if (pathLength > 1) {
         index = model.getIndexOfChild(parentNode, lastNode);
      }

      // if there is only root node 
      if (pathLength == 1) {
         return path;
      }

      // if there are still some siblings (brothers) after this node 
      if (index + 1 < model.getChildCount(parentNode)) {
         // replace the lastPathComponent by its next sibling 
         return path.getParentPath().pathByAddingChild(model.getChild(parentNode, index + 1));
      } else {
         while (true) {
            // we need to find next sibling for our father 
            path = path.getParentPath();

            // if we get to the end of tree then start if 
            if (path.getParentPath() == null) {
               // return the root path 
               return path;
            }

            pathLength = path.getPathCount();
            parentNode = path.getPathComponent(pathLength - 2);
            index = model.getIndexOfChild(parentNode, path.getLastPathComponent());

            // if there are still some siblings (brothers) after this node 
            if (index + 1 < model.getChildCount(parentNode)) {
               // replace the lastPathComponent by its next sibling 
               return path.getParentPath().pathByAddingChild(model.getChild(parentNode, index + 1));
            }
         }
      }
   }

   protected TreePath getPrevPath(TreePath path) {
      int childCount;
      Object lastNode;
      TreeModel model = tree.getModel();

      // return last component 
      if (path == null) {
         path = new TreePath(model.getRoot());

         lastNode = path.getLastPathComponent();

         while ((childCount = model.getChildCount(lastNode)) > 0) {
            // add the last child of the lastPathNode 
            path = path.pathByAddingChild(model.getChild(lastNode, childCount - 1));
            lastNode = path.getLastPathComponent();
         }

         return path;
      }

      int index = 0;
      int pathLength = path.getPathCount();
      Object parentNode = null;

      lastNode = path.getLastPathComponent();

      if (pathLength > 1) {
         parentNode = path.getPathComponent(pathLength - 2);
         index = model.getIndexOfChild(parentNode, lastNode);
      }

      // if there are still some siblings (brothers) before this node 
      if (index > 0) {
         TreePath siblingPath = path.getParentPath().pathByAddingChild(model.getChild(parentNode, index - 1));

         lastNode = siblingPath.getLastPathComponent();

         while ((childCount = model.getChildCount(lastNode)) > 0) {
            siblingPath = siblingPath.pathByAddingChild(model.getChild(lastNode, childCount - 1));
            lastNode = siblingPath.getLastPathComponent();
         }

         // return the siblingPath 
         return siblingPath;
      } else {
         TreePath parentPath = path.getParentPath();

         // if there is still some parent 
         if (parentPath != null) {
            // return his path 
            return parentPath;
         }

         lastNode = path.getLastPathComponent();

         while ((childCount = model.getChildCount(lastNode)) > 0) {
            // add the last child of the lastPathNode 
            path = path.pathByAddingChild(model.getChild(lastNode, childCount - 1));
            lastNode = path.getLastPathComponent();
         }

         return path;
      }
   }
   
/*   
TreePath searchedPath;
JTree tree = new JTree();
BasicTreeSearcher basicTreeSearcher = new BasicTreeSearcher(tree);


// here we will find the first node, which is starting with "ba"
searchedPath = basicTreeSearcher.findFirstPath("ba");
// here we will find next node, which is starting with "ba"
searchedPath = basicTreeSearcher.findNextPath(searchedPath, "ba");


// here we will selected the found node
tree.setSelectionPath(searchedPath);
// and scroll to it
tree.scrollPathToVisible(searchedPath);
*/

}
