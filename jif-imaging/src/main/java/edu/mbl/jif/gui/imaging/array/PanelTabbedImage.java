package edu.mbl.jif.gui.imaging.array;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class PanelTabbedImage
      extends JTabbedPane {

   ArrayList images;
   
   public PanelTabbedImage() {}

   public PanelTabbedImage(ArrayList _images) {
      super();
      images = _images;
      if (images == null) {
         System.err.println("No images in array.");
         return;
      }
      try {
         jbInit();
      } catch (Exception ex) {
         ex.printStackTrace();
      }
   }

   void jbInit() throws Exception {
      setTabPlacement(JTabbedPane.BOTTOM);
      try {
         for (int i = 0; i < images.size(); i++) {
            ImagePanel ip = new ImagePanel();
            JScrollPane sp = new JScrollPane(ip);
            sp.getVerticalScrollBar().setUnitIncrement(100);
            sp.getHorizontalScrollBar().setUnitIncrement(100);
            ip.setImage( (BufferedImage) images.get(i));
            System.out.println("" + i);
            addTab("" + i, sp);
         }
      } catch (IndexOutOfBoundsException ioobe) {
      }
   }
   
    class ImagePanel extends JComponent{
        
        protected BufferedImage image=new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB);
        
        public ImagePanel(){
//    ImageIO.scanForPlugins();
        }
        
        public BufferedImage getImage(){
            return image;
        }
        
        public void setImage(BufferedImage image){
            this.image=image;
            repaint();
        }
        
        public void paint(Graphics gc){
            Graphics2D g=(Graphics2D)gc;
            g.drawImage(image,0,0,null);
        }
        
        public Dimension getPreferredSize(){
            return new Dimension(image.getWidth(),image.getHeight());
        }
        
    }
    
   public static void main(String[] args) {
      ArrayList imgs = null;
      JFrame f = new JFrame();
      PanelTabbedImage tip = new PanelTabbedImage(imgs);
      f.getContentPane().add(tip, BorderLayout.CENTER);
      f.setBounds(0, 0, 500, 500);
      f.setVisible(true);
   }
}
