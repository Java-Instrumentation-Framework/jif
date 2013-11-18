package net.trevize.galatee;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JCheckBox;

import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 *
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]] GCellPanel.java -
 * May 17, 2009
 */
public class GCellPanel extends JPanel {

   private GItem gitem;
   private JTextArea textArea;
   private JCheckBox check; // ++
   //default outer.
   private Dimension outer = new Dimension(
           GalateeProperties.getCell_outer_width(),
           GalateeProperties.getCell_outer_height());
   //distance from the edge of the cell to the rectangle rect. 
   //this rectangle indicates the state selected|unselected.
   //do not be > outer width and height. 
   private Dimension padding = new Dimension(
           GalateeProperties.getCell_padding_width(),
           GalateeProperties.getCell_padding_height());
   //default space between the image and his description. 
   private int imageDescriptionSpacer = GalateeProperties.getImage_description_spacer();
   private Dimension gitemImageDimension;
   private int gitemDescriptionWidth;
   private int preferredWidth;
   private int minHeight;
   private Rectangle rect;
   private Point imagePosition;
   private Point textAreaPosition;
   private boolean selected;
   public static final Color SELECTED_ITEM_BACKGROUND_COLOR = Color
           .decode(GalateeProperties.getSelected_item_background_color());
   public static final Color UNSELECTED_ITEM_BACKGROUND_COLOR = Color
           .decode(GalateeProperties.getUnselected_item_background_color());

   public GCellPanel(Dimension gitemImageDimension, int gitemDescriptionWidth) {
      super();

      setLayout(null);

      this.gitemImageDimension = gitemImageDimension;
      this.gitemDescriptionWidth = gitemDescriptionWidth;
      if (gitemDescriptionWidth == 0) {
         imageDescriptionSpacer = 0;
      }

      preferredWidth = outer.width + gitemImageDimension.width
              + imageDescriptionSpacer + gitemDescriptionWidth + outer.width;

      minHeight = outer.height + gitemImageDimension.height + outer.height;

      rect = new Rectangle(padding.width, padding.height, preferredWidth
              - padding.width - padding.width, minHeight - padding.height
              - padding.height);

      imagePosition = new Point(outer.width, outer.height);

      textAreaPosition = new Point(outer.width + gitemImageDimension.width
              + imageDescriptionSpacer, outer.height);

      textArea = new JTextArea();
      textArea.setLineWrap(true);
      textArea.setWrapStyleWord(true);
      textArea.setEditable(false);
      add(textArea);
      textArea.setLocation(outer.width + gitemImageDimension.width
              + imageDescriptionSpacer, outer.height);
      textArea.setSize(gitemDescriptionWidth, this.gitemImageDimension.height);
      
      // checkbox for selecting
//      check = new JCheckBox();
//      add(check);
//      check.setLocation(outer.width + gitemImageDimension.width + 2, outer.height + 32);
//      check.setSize(16,16);
      
      //setting the initial preferred size.
      setPreferredSize(new Dimension(preferredWidth, minHeight));
      setSize(new Dimension(preferredWidth, minHeight));
      setBackground(GCellPanel.UNSELECTED_ITEM_BACKGROUND_COLOR);
   }

   public int getItemWidth() {
      return preferredWidth;
   }

   public int getItemMinHeight() {
      return minHeight;
   }

   public int getPreferredHeight() {
      return outer.height + textArea.getPreferredSize().height + outer.height;
   }

   public JTextArea getTextArea() {
      return textArea;
   }

   public void setText(String s) {
      textArea.setText(s);
      // GBH
      textArea.setFont(new java.awt.Font("SansSerif", 1, 10));
      textArea.setSize(textArea.getSize().width,
              textArea.getPreferredSize().height);
   }

   public void setSelected(boolean selected) {
      this.selected = selected;
   }

   public void setGItem(GItem gitem) {
      this.gitem = gitem;
   }

   @Override
   public void paintComponent(Graphics g) {

      super.paintComponent(g);

      if (textArea.getPreferredSize().height < getSize().height) {
         textArea.setSize(textArea.getSize().width, getSize().height
                 - outer.height - outer.height);
      }

      rect.height = getSize().height - padding.height - padding.height;

      if (selected) {
         g.setColor(GCellPanel.SELECTED_ITEM_BACKGROUND_COLOR);
         g.fillRect(rect.x, rect.y, rect.width, rect.height);
      } else {
         g.setColor(GCellPanel.UNSELECTED_ITEM_BACKGROUND_COLOR);
         g.fillRect(rect.x, rect.y, rect.width, rect.height);
      }
      int y = outer.height;
      if (gitem.getImage() != null) {
         y = outer.height - (rect.height - gitem.getImage().getHeight()) / 2;
      }
      //check.setSelected(gitem.isChosen());
      //g.drawImage(gitem.getImage(), outer.width, y, null);
      g.drawImage(gitem.getImage(), outer.width, outer.height, null);

   }
}
