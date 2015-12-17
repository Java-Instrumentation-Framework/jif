package edu.mbl.jif.imaging.nav;

import edu.mbl.jif.utils.Prefs;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author GBH <imagejdev.org>
 */
public enum ImageFileTypes {

   TIF(new String[]{"tif", "tiff"}),
   OME_TIF(new String[]{"ome.tif"}),
   PNG(new String[]{"png"}),
   JPG(new String[]{"jpg", "jpeg"}),
   GIF(new String[]{"gif"});
   // TODO AVI(new String[]{"avi"});
   // TODO MOV(new String[]{"mov"});

   private ImageFileTypes(String[] extensions) {
      this.extensions = extensions;
   }

   private boolean selected;

   private String[] extensions;

   public String[] getExtensions() {
      return extensions;
   }

   public boolean isSelected() {
      return selected;
   }

   public void setSelected(boolean selected) {
      this.selected = selected;
   }

   @Override
   public String toString() {
      String[] splitNames = name().toLowerCase().split("_");
      StringBuffer fixedName = new StringBuffer();
      for (int i = 0; i < splitNames.length; i++) {
         String firstLetter = splitNames[i].substring(0, 1).toUpperCase(),
                 restOfWord = splitNames[i].substring(1),
                 spacer = i == splitNames.length ? "" : " ";
         fixedName.append(firstLetter).append(restOfWord).append(spacer);
      }
      return fixedName.toString();
   }

   final static String TYPES_KEY = "imageFileTypes";

   public void saveToPrefs() {
      final List<String> selectedTypes = new ArrayList<String>();
      for (ImageFileTypes type : ImageFileTypes.values()) {
         if (type.selected) {
            selectedTypes.add(type.toString());
         }
      }
      Prefs.put(ImageNavigator.class, TYPES_KEY, selectedTypes);
   }

   public void loadFromPrefs() {
      final List<String> selectedTypes = Prefs.getList(ImageNavigator.class, TYPES_KEY);
      for (ImageFileTypes type : ImageFileTypes.values()) {
         if (selectedTypes.contains(type.toString())) {
            type.setSelected(true);
         } else {
            type.setSelected(false);
         }
      }
   }

   public static void main(String[] args) {
      for (ImageFileTypes type : ImageFileTypes.values()) {
         type.setSelected(true);
      }
      for (ImageFileTypes type : ImageFileTypes.values()) {
         System.out.println(type.name() + ": " + type.selected + " > " + type.toString());
      }

//      JComboBox fileTypeComboBox = new JComboBox();
//      ComboBoxModel cbModel = new DefaultComboBoxModel(ImageFileTypes.values());
//      fileTypeComboBox.setModel(cbModel);
//      fileTypeComboBox.addActionListener(new ActionListener() {
//         @Override
//         public void actionPerformed(ActionEvent evt) {
//            JComboBox cb = (JComboBox) evt.getSource();
//            ImageFileTypes fileType = (ImageFileTypes) cb.getSelectedItem();
//            switch (fileType) {
//               case ALL: {
//
//               }
//               case OME_TIF: {
//               }
//               //processing code...
//               case TIF: {
//               }
//
//            }
//         }
//      });
   }

}
