package edu.mbl.jif.gui.file;

import java.io.File;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import net.miginfocom.swing.MigLayout;

/**
 * A panel containing a single file selection - showing current value and a chooser button.
 *
 * Usage: FileChooserPanel fileChooz = new FileChooserPanel( "C:/", "ext", true); public
 * FileChooserPanel(final String label, String sourcePath, final String extension) { // then,
 *
 * String filePath = fileChooz.getFilePath();
 *
 *
 * @author GBH
 */
// TODO - Copy this to jif.gui
public class FileChooserPanel extends JPanel {

   public enum Type {

      OPEN, SAVE, SELECT;
   }

   private final JTextField filePath = new JTextField(35);
   private final JButton sourceButton = new JButton("...");
   private FileFilter fileFilter = null;
   private Type type;

   // TODO option to make it an Open or Save dialog.
   // default is Open (or select)
   // TODO Use CustomFileChooser
   //
   public FileChooserPanel(final String label, String sourcePath, final String[] extensions, Type type) {
      this(label, sourcePath, type);
      this.fileFilter = new ExtensionFileFilter(label, extensions);
   }

   public FileChooserPanel(final String label, String sourcePath, final String extension, Type type) {
      this(label, sourcePath, type);

      this.fileFilter = new FileFilter() {
         @Override
         public boolean accept(File f) {
            return f.getName().endsWith("." + extension);
         }

         @Override
         public String getDescription() {
            // TODO generalize, pass in descript
            return extension;
         }
      };
   }

   public FileChooserPanel(final String label, String sourcePath, final FileFilter filter, Type type) {
      this(label, sourcePath, type);
      this.fileFilter = filter;
   }

   public FileChooserPanel(final String label, String sourcePath, Type type) {
      this.type = type;
      if (sourcePath == null) {
         try {
            sourcePath = getCurrentPath();
         } catch (IOException ex) {
            ex.printStackTrace();
         }
      }
      if (sourcePath != null) {
         filePath.setText(sourcePath);
      }
      setLayout(new MigLayout());
      JLabel sourceLabel = new JLabel(label, SwingConstants.LEADING);
      sourceButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            //selectDirectory(button, "C:\\ImageJ\\", "Select a Dir", "Dir set to:");
            dispatchToEDT(new Runnable() {
               public void run() {
                  String selected = selectFile(sourceButton, filePath.getText(), label, null);
                  if (selected != null) {
                     filePath.setText(selected);
                  }
               }
            });
         }
      });
      add(sourceLabel);
      add(filePath, "grow");
      add(sourceButton);
      //this.setPreferredSize(new Dimension(400, 72));
   }

   @Override
   public void setEnabled(final boolean t) {
      dispatchToEDT(new Runnable() {
         public void run() {
            filePath.setEnabled(t);
            sourceButton.setEnabled(t);

         }
      });
   }

   public String selectFile(Component parent, String startFile,
           String message, String confirmMsg) {

      JFileChooser j = new JFileChooser();

      j.setDialogTitle(message);
      j.setFileSelectionMode(JFileChooser.FILES_ONLY);
      j.setMultiSelectionEnabled(false);
      j.setAcceptAllFileFilterUsed(false);
      if (this.fileFilter != null) {
         j.addChoosableFileFilter(fileFilter);
      }
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

      //
      Integer status;
      if (type == Type.OPEN) {
         status = j.showOpenDialog(parent);
      } else if (type == Type.SAVE) {
         //j.setShowingCreateDirectory(true);
         status = j.showSaveDialog(parent);
      } else {
         status = j.showDialog(parent, "Select");
      }

      //
      if (status == JFileChooser.APPROVE_OPTION) {
         File selectedFile = j.getSelectedFile();
         return selectedFile.getAbsolutePath();
      } else if (status == JFileChooser.CANCEL_OPTION) {
         return null;
      }

      return null;
   }

   public String getFilePath() {
      return filePath.getText();
   }

   private String getCurrentPath()
           throws IOException {
      File dir1 = new File(".");
      return dir1.getCanonicalPath();

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
      } catch (Exception e) {
         // If Nimbus is not available, you can set the GUI to another look and feel.
      }
      QuickFrame f = new QuickFrame("");
      f.setLayout(new MigLayout());
      final FileChooserPanel chooser = new FileChooserPanel("Coord File",
              "C:/GitHub/jif/mfmconverter/testPoints.coord",
              "coord", FileChooserPanel.Type.SELECT);
      f.add(chooser, "wrap");
      final FileChooserPanel chooser2 = new FileChooserPanel("Image File",
              "C:/",
              new String[]{"jpg", "gif", "tif"}, FileChooserPanel.Type.OPEN);
      f.add(chooser2, "wrap");
      final FileChooserPanel chooser3 = new FileChooserPanel("Image File",
              "C:/",
              new String[]{"jpg", "gif", "tif"}, FileChooserPanel.Type.SAVE);
      f.add(chooser3, "wrap");

      f.pack();
      f.setVisible(true);

   }

}
