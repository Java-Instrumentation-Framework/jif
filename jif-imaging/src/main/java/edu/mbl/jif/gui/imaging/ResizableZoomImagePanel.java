package edu.mbl.jif.gui.imaging;

import edu.mbl.jif.gui.panel.PanelEnclosed;
import java.io.*;
import java.util.*;
import javax.imageio.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.event.*;


/**
 * PanelZoomImage - Maintains aspect ratio while scaling to available space
 */

public class ResizableZoomImagePanel
      extends PanelEnclosed
{
   boolean debug = true;

   BorderLayout borderLayout1 = new BorderLayout();
   private float zoomMagnification;
   int imageWidth;
   int imageHeight;
   float imageAspectRatio;
   Dimension minimumImageSize;
   Dimension minimumWindowSize;
   int winXdiff;
   int winYdiff;
   int scaledImageWidth;
   int scaledImageHeight;
   Dimension preferedImageSize;
   Dimension initialWinSize;
   Dimension canvasSize;
   Dimension largestImage;
   int xpadding;
   int ypadding;
   float ratio;

   Image zoomimage;
   Image overlay;
   Canvas canvas;

   Point pStart = null;
   Rectangle roiRect = null;
   Color colorRoi = Color.red;

   MyObservable _observable;

   public ResizableZoomImagePanel (Image _zoomimage, float _zoomMagnification,
                          boolean pixelRoiSelection) {
      this();
      setImage(_zoomimage, _zoomMagnification, pixelRoiSelection);
   }


   public ResizableZoomImagePanel () {
      try {
         jbInit();
      }
      catch (Exception ex) {
         ex.printStackTrace();
      }
   }


   void jbInit () throws Exception {
      this.setLayout(borderLayout1);
      _observable = new MyObservable();
   }


   public void showImage (Image _image) {
      zoomimage = _image;
      //canvas.repaint();
      repaint();
   }


   public Dimension getImageSize () {
      return new Dimension(imageWidth, imageHeight);
   }


   public void setImage (Image _zoomimage, float _zoomMagnification,
                         boolean pixelRoiSelection) {
      zoomimage = _zoomimage;
      zoomMagnification = _zoomMagnification;
      imageWidth = zoomimage.getWidth(null);
      imageHeight = zoomimage.getHeight(null);
      imageAspectRatio = imageWidth / (float) imageHeight;
      minimumImageSize = new Dimension(imageWidth, imageHeight);
      minimumWindowSize = new Dimension(getSize());
      winXdiff = minimumWindowSize.width - minimumImageSize.width;
      winYdiff = minimumWindowSize.height - minimumImageSize.height;
      scaledImageWidth = Math.round(imageWidth * zoomMagnification);
      scaledImageHeight = Math.round(imageHeight * zoomMagnification);
      preferedImageSize = new Dimension(scaledImageWidth, scaledImageHeight);
      // create Overlay for ROI, annotations, etc.
      overlay = new BufferedImage((int) imageWidth, (int) imageHeight,
                                  BufferedImage.TYPE_INT_ARGB);
      // Add the Image Canvas
      canvas = new Canvas();

      if (pixelRoiSelection) {
         canvas.addPixelRoiListener();
      }

      this.add(canvas);
      Dimension initialWinSize = new Dimension(preferedImageSize.width + winXdiff,
            preferedImageSize.height + winYdiff);
      setPreferredSize(initialWinSize);
      addNotify();
   }


   public void setRoiColor (Color c) {
      colorRoi = c;
   }


   public void addObserver (Observer obs) {
      _observable.addObserver(obs);
   }

   Vector overlays = new Vector();

   public void addOverlay (Overlay olay) {
      overlays.add(olay);
      }

   class Canvas
         extends JPanel
   {
      public Canvas () {
         super();
         this.setBackground(Color.black);
      }

      public void addPixelRoiListener () {
         // add listener for pixels and roi's
         PixelRoiListener myListener = new PixelRoiListener();
         addMouseListener(myListener);
         addMouseMotionListener(myListener);
      }

      public Dimension getMinimumSize () {
         return minimumImageSize;
      }


      public Dimension getPreferedSize () {
         return preferedImageSize;
      }

      public void paintComponent (Graphics g) {
         super.paintComponent(g);
         Graphics2D g2 = (Graphics2D) g;
         if (false) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
         }
         // get the requested new size
         canvasSize = getSize();
         largestImage = new Dimension(canvasSize);
         if ((largestImage.width / (float) largestImage.height)
             > imageAspectRatio) {
            largestImage.width = (int) Math.ceil(largestImage.height *
                  imageAspectRatio);
         } else {
            largestImage.height = (int) Math.ceil(largestImage.width /
                  imageAspectRatio);
         }
         xpadding = canvasSize.width - largestImage.width;
         ypadding = canvasSize.height - largestImage.height;
         g2.drawImage(zoomimage, xpadding / 2, ypadding / 2, largestImage.width,
                      largestImage.height, null);
         //System.out.println("Ration: " + ratio);
         drawOverlays(g2);
      }

      public void drawOverlays (Graphics2D g2) {
         if (roiRect != null) {
            drawScaledROI(g2, roiRect);
         }
         for (Iterator itero = overlays.iterator(); itero.hasNext(); ) {
            Overlay olay = (Overlay) itero.next();
            olay.updateGraphics(g2);
         }
      }





      // ROI Selection ------------------------------------------------------

      public void drawScaledROI (Graphics2D g2, Rectangle r) {
         Point z = getDescaledXY((int) r.getX(), (int) r.getY());
         int w = (int) (ratio * r.getWidth());
         int h = (int) (ratio * r.getHeight());
         if (w > 0 && h > 0) {
            g2.setColor(colorRoi);
            g2.drawRect(z.x, z.y, w, h);
         }
      }


      Point getScaledXY (MouseEvent e) {
         int x = e.getX();
         int y = e.getY();
         return getScaledXY(x, y);
      }


      Point getScaledXY (int x, int y) {
         if (xpadding > 0) {
            ratio = (float) canvasSize.getHeight() / (float) imageHeight;
         } else {
            ratio = (float) canvasSize.getWidth() / (float) imageWidth;
         }
         int iX = Math.round((x - (float) xpadding / 2f) / ratio);
         int iY = Math.round((y - (float) ypadding / 2f) / ratio);
         if (iX < 0) {
            iX = 0;
         }
         if (iX > imageWidth) {
            iX = imageWidth;
         }
         if (iY < 0) {
            iY = 0;
         }
         if (iY > imageHeight) {
            iY = imageHeight;
         }
         return new Point(iX, iY);
      }


      Point getDescaledXY (int x, int y) {
         if (xpadding > 0) {
            ratio = (float) canvasSize.getHeight() / (float) imageHeight;
         } else {
            ratio = (float) canvasSize.getWidth() / (float) imageWidth;
         }
         int iX = Math.round((x * ratio + (float) xpadding / 2f));
         int iY = Math.round((y * ratio + (float) ypadding / 2f));
         if (iX < 0) {
            iX = 0;
         }
//      if (iX > imageWidth) {
//        iX = imageWidth;
//      }
//      if (iY < 0) {
//        iY = 0;
//      }
//      if (iY > imageHeight) {
//        iY = imageHeight;
//      }
         return new Point(iX, iY);
      }


      // Listener --------------------------------------------------------------
      class PixelRoiListener
            extends MouseInputAdapter
      {
         public void mousePressed (MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
               pStart = getScaledXY(e);
               if (debug) {
                  System.out.println("Start: " + pStart.x + ", " + pStart.y);
               }
               roiRect = new Rectangle(pStart.x, pStart.y, 0, 0);
               repaint();
            }
            if (e.getClickCount() == 2) {
               if (debug) {
                  System.out.println(" double click");
               }
            }
            if (e.getButton() == MouseEvent.BUTTON3) {
               if (debug) {
                  System.out.println(" RightClick");
               }
            }
         }


         public void mouseDragged (MouseEvent e) {
            Point p = getScaledXY(e);
            int x = p.x;
            int y = p.y;
            int width = pStart.x - p.x;
            int height = pStart.y - p.y;
            //Make the width and height positive, if necessary.
            if (width < 0) {
               width = 0 - width;
               x = x - width + 1;
               if (x < 0) {
                  width += x;
                  x = 0;
               }
            }
            if (height < 0) {
               height = 0 - height;
               y = y - height + 1;
               if (y < 0) {
                  height += y;
                  y = 0;
               }
            }
            if (debug) {
               System.out.println(x + "," + y + "," + width + "," + height);
            }
            roiRect.setBounds(x, y, width, height);
            repaint();
         }


         public void mouseReleased (MouseEvent e) {
            if (debug) {
               System.out.println("ROI: " + roiRect);
            }
            _observable.notifyObservers(roiRect);
         }


         public void mouseMoved (MouseEvent e) {
            Point p = getScaledXY(e);
            //if (debug) { System.out.println("Pixel: " + p.x + ", " + p.y); }
            if(p!=null) {
               _observable.notifyObservers(p);
            }
//        Point pD = getDescaledXY(p.x, p.y);
//        System.out.println("e(" + e.getX() + ", " + e.getY() + ")  " +
//                           "p(" + p.x + ", " + p.y + ")  " +
//                           "pD(" + pD.x + ", " + pD.y + ")  ");
            /** @todo  */
         }
      }
   };

   class MyObservable
         extends Observable
   {
      public void clearChanged () {
         super.clearChanged();
      }


      public void setChanged () {
         super.setChanged();
      }


      public void notifyObservers (Object b) {
         // Otherwise it won't propagate changes:
         setChanged();
         super.notifyObservers(b);
      }
   }



   //  TEST ====================================================================
   public void test () {
      String image_file_name = 
             // edu.mbl.jif.Constants.testDataPath +  "images\\PSCollagenDark.gif";
      "jifSplashNoTitle.png";
      //Image testImage = new ImageIcon(ClassLoader.getSystemResource(image_file_name)).getImage();
      Image image = null;
      try {
         image = ImageIO.read(new File(image_file_name));
      }
      catch (Exception e) {
         System.out.println("Exception loading: " + image_file_name);
      }
      //String image_file_name2 = "PSCollagenDarkInv.gif"; // 589 x 421
      //Image testImage = new ImageIcon(ClassLoader.getSystemResource(image_file_name)).getImage();
//    Image image2 = null;
//    try {
//      image2 = ImageIO.read(new File(image_file_name2));
//    }
//    catch (Exception e) {
//      System.out.println("Exception loading: " + image_file_name);
//    }

      JFrame zoomwin = new JFrame();
      ResizableZoomImagePanel p = new ResizableZoomImagePanel();
      p.setImage(image, 0.1f, true);

      p.addObserver(new CursorWatcher(p));

      zoomwin.getContentPane().setLayout(new BorderLayout());
      zoomwin.getContentPane().add(p, BorderLayout.CENTER);
      zoomwin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      //zoomwin.getContentPane().add(SeriesPlayerController, BorderLayout.SOUTH);
      zoomwin.pack();
      zoomwin.setSize(400, 400);
      zoomwin.setVisible(true);
      try {
         Thread.sleep(500);
      }
      catch (InterruptedException ex) {
      }
      //p.showImage(image2);
   }


   public static void main (String[] args) {
      new ResizableZoomImagePanel().test();
   }

}
// end class ZoomWindow
