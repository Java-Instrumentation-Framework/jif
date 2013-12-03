package edu.mbl.jif.imaging.nav.util;

import javax.swing.JLabel;

/**
 * A JLabel that displays a file path abbreviated/shorted to a length, 
 * and adds a tooltip that shows the full path.
 * 
 * @author GBH
 */
public class AbbreviatedFilePathLabel extends JLabel {

   private int length = 32;
   private String fullPath = "";

   public AbbreviatedFilePathLabel(String text) {
      super();
      setText(text);
   }

   public AbbreviatedFilePathLabel(String text, int length) {
      super();
      setLength(length);
      setText(text);
   }
   
      public void setLength(int length) {
      this.length = length;
   }

   @Override
   public void setText(String text) {
      this.fullPath = text;
      String abbrevPath = FilePathUtils.limitPath(text, length);
      super.setText(abbrevPath);
      super.setToolTipText(fullPath);
      
   }
   

}
