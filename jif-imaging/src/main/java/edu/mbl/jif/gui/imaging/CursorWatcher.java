package edu.mbl.jif.gui.imaging;

import java.util.Observable;
import java.util.Observer;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Graphics2D;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */


public class CursorWatcher
      implements Observer
{

   ResizableZoomImagePanel panel;

   public CursorWatcher (ResizableZoomImagePanel panel) {
      this.panel = panel;
      panel.addOverlay(new CursorOverlay());
   }


   public void update (Observable obs, Object o) {
      System.out.println("CursorWatcher Updated");
      if (o instanceof Point) {
         Point p = (Point) o;
         updatePixel(p);
      }
      if (o instanceof Rectangle) {
         Rectangle r = (Rectangle) o;
         //updateRectangle
         System.out.println(r);
      }

   }


   /**
    * updatePixel
    *
    * @param p Point
    */
   private void updatePixel (Point p) {
   }


   class CursorOverlay
         extends Overlay
   {
      public CursorOverlay () {}


      public void updateGraphics (Graphics2D g2) {
         g2.drawRect(10,10,100,100);

      }
   }
}
