package edu.mbl.jif.gui.file;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * From http://geek.starbean.net/?p=275
 *
 * The default JFileChooser doesn’t prompt to overwrite files when saving. It also doesn’t
 * automatically add an extension to the file if the user didn’t type one. I had to do a custom one
 * to support these features for better usability. The limitation of this specific implementation is
 * it only supports one “image” extension, and it’s not i18n-ed. Customize it more for your own
 * needs.
 */
public class CustomFileChooser extends JFileChooser {

   private String[] extensions;

   public CustomFileChooser(String description, String extension) {
      this(description, new String[]{extension});
   }
   
   public CustomFileChooser(String description, String[] extensions) {
      super();
      this.extensions = extensions;
      ExtensionFileFilter filter = new ExtensionFileFilter(description, extensions);
      addChoosableFileFilter(filter);
//      addChoosableFileFilter(new FileNameExtensionFilter(
//              String.format("%1$s Images (*.%1$s)", extension), extension));
      setAcceptAllFileFilterUsed(false);  // only the type specified
   }

   @Override
   public File getSelectedFile() {
      File selectedFile = super.getSelectedFile();

      if (selectedFile != null) {
         String name = selectedFile.getName();
         if (!name.contains(".")) {
            selectedFile = new File(selectedFile.getParentFile(),
                    name + '.' + extensions[0]);
         }
      }
      return selectedFile;
   }

   @Override
   public void approveSelection() {
      if (getDialogType() == JFileChooser.SAVE_DIALOG) {
         File selectedFile = getSelectedFile();
         if ((selectedFile != null) && selectedFile.exists()) {
            int response = JOptionPane.showConfirmDialog(this,
                    "The file " + selectedFile.getName()
                    + " already exists. Do you want to replace the existing file?",
                    "Ovewrite file", JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (response != JOptionPane.YES_OPTION) {
               return;
            }
         }
      }
      super.approveSelection();
   }

   public static void main(String[] args) {
      CustomFileChooser ch = new CustomFileChooser("Image Files","tif");
      ch.setSelectedFile(new File("Test.tif"));
      int result = ch.showSaveDialog(null);
      if (result == JFileChooser.APPROVE_OPTION) {
         System.out.println(ch.getSelectedFile());
      }
   }
}
