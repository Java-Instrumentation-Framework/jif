package edu.mbl.jif.gui.imaging;

import edu.mbl.jif.imaging.util.ImageFactoryGrayScale;
import edu.mbl.jif.utils.StaticSwingUtils;
import edu.mbl.jif.gui.imaging.zoom.core.ZoomGraphics;
import edu.mbl.jif.imaging.*;
import edu.mbl.jif.imaging.util.GraphicsUtilities;
import java.awt.*;
import java.awt.image.BufferedImage;

import java.io.File;


import javax.imageio.ImageIO;

import javax.swing.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: Test of ImageDisplayPanel This uses ZoomControl16 with MouseSensitiveZSP
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * @author gbh at mbl.edu
 * @version 1.0
 */
public class MultiViewerFrame extends JFrame {

   BorderLayout borderLayout1 = new BorderLayout();
   ImageDisplayPanel imagePanel;
   int w;
   int h;
   BufferedImage img = null;
   String title = "(none)";

   public MultiViewerFrame(BufferedImage _img, String title) {
      this(_img);
      this.setTitle(title);
   }

   public MultiViewerFrame(BufferedImage __img) {
      this(__img.getWidth(), __img.getHeight(), __img);
   }

   public MultiViewerFrame(int w, int h, BufferedImage img) {
      this.w = w;
      this.h = h;
      this.img = img;
      try {
         jbInit();
      } catch (Exception exception) {
         exception.printStackTrace();
      }
   }

   public ImageDisplayPanel getImageDisplayPanel() {
      return imagePanel;
   }

   private void jbInit() throws Exception {
//        try {
//            com.jgoodies.looks.plastic.Plastic3DLookAndFeel lookFeel =
//                    new com.jgoodies.looks.plastic.Plastic3DLookAndFeel();
//            com.jgoodies.looks.plastic.PlasticLookAndFeel.setPlasticTheme(
//                    //.setMyCurrentTheme(//new com.jgoodies.looks.plastic.theme.DesertBluer());
//                    // new com.jgoodies.looks.plastic.theme.Silver());
//                    //new com.jgoodies.looks.plastic.theme.SkyBluerTahoma());
//                    new com.jgoodies.looks.plastic.theme.DesertBlue());
//            com.jgoodies.looks.plastic.PlasticLookAndFeel.setTabStyle("Metal");
//            UIManager.setLookAndFeel(lookFeel);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
      getContentPane().setLayout(borderLayout1);
      Dimension imageDim = new Dimension(w, h);

      imagePanel = new ImageDisplayPanel(imageDim);
      imagePanel.addMagnifierButton();
      this.add(imagePanel, BorderLayout.CENTER);
      if (img != null) {
         imagePanel.changeImage(img);
      }
      //
      JPanel viewsPanel = new JPanel();
      JButton view1 = new JButton("View1");
      viewsPanel.add(view1);
      this.add(viewsPanel, BorderLayout.SOUTH);
      //
      this.setTitle(title);
      // size it
      setSize(StaticSwingUtils.sizeFrameForDefaultScreen(imageDim));
      setLocation(StaticSwingUtils.nextFramePosition());
      //new TestAppHarness(fid);
      imagePanel.fitImageToWindow();
      setVisible(true);
   }
   // +++ On maximizeFrame and restore, do FitToWindow

   public void changeImage(BufferedImage img) {
      imagePanel.changeImage(img);
   }

   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            runTest();
         }
      });
   }

   public static void runTest() {
      //      SwingUtilities.invokeLater(new Runnable()
      //      {
      //         public void run () {
      //            File file = new File(
      //            edu.mbl.jif.Constants.testDataPath + "Aster_azim.tif");
      //            // @todo Opener...
      //            BufferedImage img = null;
      //            try {
      //               img = ImageIO.read(file);
      //               if(img == null){
      //                  System.err.println("Couldn't load pond");
      //                  return;
      //               }
      //               FrameImageDisplayTest f = new FrameImageDisplayTest(img.getWidth(),
      //                     img.getHeight(), img);
      //
      //               f.setVisible(true); }
      //            catch (IOException ex) {
      //            }

      //         }
      //      });
      int width = 512;
      int height = 512;
      MultiViewerFrame fid = new MultiViewerFrame(
              GraphicsUtilities.toCompatibleImage(
              ImageFactoryGrayScale.testImageByte(width, height)), "byte");

      // Add an overlay
      GraphicOverlay overlay = new GraphicOverlay() {
         public void drawGraphicOverlay(ZoomGraphics zg) {
            zg.setColor(Color.red);
            zg.drawRect(20, 20, 40, 40);
         }
      };
      fid.getImageDisplayPanel().imagePane.addGraphicOverlay(overlay);
      // size it
//       // fid.setSize(StaticSwingUtils.sizeFrameForDefaultScreen(new Dimension(width, height)));
//        fid.setLocation(StaticSwingUtils.nextFramePosition());
//        //new TestAppHarness(fid);
//        fid.setVisible(true);
//        fid.imagePanel.fitImageToWindow();

      //new TestAppHarness((new FrameImageDisplay(ImageFactoryGrayScale.testImageFloat(), "floater")));
      //(new FrameImageDisplay(ImageFactoryGrayScale.testImageShort12(), "12")).setVisible(true);
      //(new FrameImageDisplay(ImageFactoryGrayScale.testImageShort16(), "16")).setVisible(true);
      //(new FrameImageDisplay(ImageFactoryGrayScale.testImageFloat(), "floater")).setVisible(true);
      // test with
      // ReallyBigXYImage
      //(new FrameImageDisplay(load("D:/_TestImages/2000Square8bit.tif"), "2000 Square")).setVisible(true);
   }

   public static BufferedImage load(String file) {
      Image image = null;
      try {
         image = ImageIO.read(new File(file));
      } catch (Exception e) {
         System.out.println("Exception loading: " + file);
      }
      return (BufferedImage) image;
   }
}
