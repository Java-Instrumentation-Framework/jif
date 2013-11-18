package edu.mbl.jif.imaging.nav;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 *
 * @author GBH <imagejdev.org>
 */
enum ImageFileTypes {

   ALL,
   TIF,
   OME_TIF;

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

   public static void main(String[] args) {

      JComboBox fileTypeComboBox = new JComboBox();
      ComboBoxModel cbModel = new DefaultComboBoxModel(ImageFileTypes.values());
      fileTypeComboBox.setModel(cbModel);
      fileTypeComboBox.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent evt) {
            JComboBox cb = (JComboBox) evt.getSource();
            ImageFileTypes fileType = (ImageFileTypes) cb.getSelectedItem();
            switch (fileType) {
               case ALL: {

               }
               case OME_TIF: {
               }
               //processing code...
               case TIF: {
               }

            }
         }
      });
   }

}
