package edu.mbl.jif.gui.file;

/**
 * $ $ License.
 *
 * Copyright $ L2FProd.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
import java.io.File;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import net.miginfocom.swing.MigLayout;

/**
 * A panel containing source and destination path choosers...
 *
 * Usage: PathChooserSourceDestination chooser = new PathChooserSourceDestination( "C:/",
 * "D:/", true); // then,
 *
 * Default is to choose from DIRECTORIES_ONLY
 * String sourcePath = chooser.getSourcePath(); boolean includeSubs =
 * chooser.isIncludeSubDirectories(); String destPath = chooser.getDestPath();
 *
 *  allowIncludeSubDirs means include the sub-dirs in the source path
 * @author GBH
 */
//
// TODO - Copy this to jif.gui
// TODO add persistence
// TODO if files allowed, set extension(s)
//
public class PathChooserSourceDestination extends JPanel {

   private final JTextField sourceText = new JTextField(35);
   private JCheckBox checkIncludeSubDirs = null;
   private final JTextField destText = new JTextField(35);
   private final boolean allowIncludeSubDirs = false;
   private int selectionMode = JFileChooser.DIRECTORIES_ONLY;

   public String getSourcePath() {
      return sourceText.getText();
   }

   public String getDestPath() {
      return destText.getText();
   }

   public boolean isIncludeSubDirectories() {
      if (checkIncludeSubDirs == null) {
         return false;
      }
      return checkIncludeSubDirs.isSelected();
   }

   public String getCurrentPath()
           throws IOException {
      File dir1 = new File(".");
      return dir1.getCanonicalPath();
   }

   // pass null paths for current directory
   public PathChooserSourceDestination(String sourcePath, int selectionMode, String destPath) {
      this(sourcePath, destPath, false);
      this.selectionMode = selectionMode;
   }
   
   public PathChooserSourceDestination(String sourcePath, String destPath) { 
      this(sourcePath, destPath, false);
   }

   public PathChooserSourceDestination(String sourcePath, String destPath, boolean allowIncludeSubDirs) {

      if (sourcePath == null) {
         try {
            sourcePath = getCurrentPath();
         } catch (IOException ex) {
            ex.printStackTrace();
         }
      }
      if (sourcePath != null) {
         sourceText.setText(sourcePath);
      }
      if (destPath == null) {
         try {
            destPath = getCurrentPath();
         } catch (IOException ex) {
            ex.printStackTrace();
         }
      }
      if (destPath != null) {
         destText.setText(destPath);
      }

      setLayout(new MigLayout());
      JLabel sourceLabel = new JLabel("Source:", SwingConstants.LEADING);
      final JButton sourceButton = new JButton("...");
      sourceButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            //selectDirectory(button, "C:\\ImageJ\\", "Select a Dir", "Dir set to:");
            dispatchToEDT(new Runnable() {
               public void run() {
                  String selected = selectDirectory(sourceButton, sourceText.getText(), "Select Source", null);
                  if (selected != null) {
                     sourceText.setText(selected);
                  }
               }
            });
         }
      });
      add(sourceLabel);
      add(sourceText, "grow");
      add(sourceButton, "wrap");
      if (allowIncludeSubDirs) {
         checkIncludeSubDirs = new JCheckBox("include sub-folders");
         // TODO persist... checkIncludeSubDirs.setSelected();
         add(checkIncludeSubDirs, "skip, right, wrap");
      }

      JLabel destLabel = new JLabel("Destination: ", SwingConstants.LEADING);
      final JButton destButton = new JButton("...");
      destButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            dispatchToEDT(new Runnable() {
               public void run() {
                  String selected = selectDirectory(destButton, destText.getText(), "Select Destination", null);
                  if (selected != null) {
                     destText.setText(selected);
                  }
               }
            });
         }
      });
      add(destLabel);
      add(destText, "grow");
      add(destButton);
      if (allowIncludeSubDirs) {
         this.setPreferredSize(new Dimension(400, 96));
      } else {
         this.setPreferredSize(new Dimension(400, 72));
      }
   }

   public String selectDirectory(Component parent, String startFile,
           String message, String confirmMsg) {

      JFileChooser j = new JFileChooser();
      j.setDialogTitle(message);
      j.setFileSelectionMode(this.selectionMode);
      j.setMultiSelectionEnabled(false); // <<<<<<<<
      // TODO add fileExtensionFilter
      if (startFile != null) {
         File selFile = new File(startFile);
         if (selFile.exists()) {
            if (selFile.isDirectory()) {
               j.setCurrentDirectory(selFile);
            } else {
               j.ensureFileIsVisible(selFile);
               j.setSelectedFile(selFile);
            }
         }
      }
      //j.setShowingCreateDirectory(true);
      //
      Integer status = j.showOpenDialog(parent);
      //
      if (status == JFileChooser.APPROVE_OPTION) {
         File selectedFile = j.getSelectedFile();
         return selectedFile.getAbsolutePath();
      } else if (status == JFileChooser.CANCEL_OPTION) {
         return null;
      }
      return null;
   }


   public static class QuickFrame extends JFrame {

      public QuickFrame(String title) {
         super(title);
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         setSize(640, 480);
         Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
         setLocation(
                 Math.max(0, screenSize.width / 2 - getWidth() / 2),
                 Math.max(0, screenSize.height / 2 - getHeight() / 2));
      }
   }

   public static void dispatchToEDT(Runnable runnable) {
      if (!SwingUtilities.isEventDispatchThread()) {
         SwingUtilities.invokeLater(runnable);
      } else {
         runnable.run();
      }
   }

   public static void dispatchToEDTWait(Runnable runnable) {
      if (!SwingUtilities.isEventDispatchThread()) {
         try {
            SwingUtilities.invokeAndWait(runnable);
         } catch (InterruptedException ex) {
            ex.printStackTrace();
         } catch (InvocationTargetException ex) {
            ex.printStackTrace();
         }
      } else {
         runnable.run();
      }
   }
   
   
   public static void main(String[] args)
           throws Exception {
      try {
         for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
               UIManager.setLookAndFeel(info.getClassName());
               break;
            }
         }
      } catch (Exception e) { }
      
      QuickFrame f = new QuickFrame("");
//      final PathChooserSourceDestination chooser = new PathChooserSourceDestination(
//              "C:/", "D:/", true);
      final PathChooserSourceDestination chooser = new PathChooserSourceDestination(
              "C:/", JFileChooser.FILES_AND_DIRECTORIES, "D:/");
      f.add(chooser);
      f.pack();
      f.setVisible(true);


   }
}
