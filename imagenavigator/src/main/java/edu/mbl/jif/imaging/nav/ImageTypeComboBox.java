/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.imaging.nav;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 *
 * @author GBH
 */
public class ImageTypeComboBox extends JComboBox {

   public ImageTypeComboBox() {
   
   
     ComboBoxModel cbModel = new DefaultComboBoxModel(ImageFileTypes.values());
      setModel(cbModel);
      addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent evt) {
            JComboBox cb = (JComboBox) evt.getSource();
            ImageFileTypes fileType = (ImageFileTypes) cb.getSelectedItem();
            // set image type.
            switch (fileType) {
               case OME_TIF:
               //processing code...
            }
         }
      });
   }
}
