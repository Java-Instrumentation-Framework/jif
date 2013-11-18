/*
 * PanelValuePointPS.java
 *
 * Created on April 23, 2007, 11:47 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mbl.jif.gui.imaging;

import edu.mbl.jif.gui.imaging.event.RoiChangeEvent;
import edu.mbl.jif.gui.value.ValueNoLabel;
import edu.mbl.jif.gui.imaging.event.PixelChangeEvent;
import edu.mbl.jif.utils.ColorUtils;

import java.awt.Color;

/**
 *
 * @author GBH
 */
public class PanelValuePointPS extends PanelValuePointPixel {
    ValueNoLabel valueRetardance = new ValueNoLabel();
   ValueNoLabel valueOrientation = new ValueNoLabel();
   
    public PanelValuePointPS() {
        super(0);
      this.add(valueRetardance, null);
      this.add(valueOrientation, null);
   }

   // Pixel changed, PixelChangeListener implementation
    @Override
   public void pixelChanged(PixelChangeEvent evnt) {
      if (evnt.value < 0) {
         setValuePixel();
      } else {
         setValuePixel(evnt.value, evnt.x, evnt.y);
      }
   }

   // ROI changed, RoiChangeListener implementation
    @Override
    public void roiChanged(RoiChangeEvent roiEvt) {
      if (roiEvt.w > 0) {
         setValueStats(roiEvt.min, roiEvt.mean, roiEvt.max, Color.blue);
         setValueROI(roiEvt.x, roiEvt.y, roiEvt.w, roiEvt.h);
      } else {
         // show values for full image
         setValueStats(roiEvt.min, roiEvt.mean, roiEvt.max, Color.black);
         setValueROI();
      }
   }

   //----------------------------------------------------------------------
   public void setValuePixel(int v, int x, int y) {
      valuePixel.set(fixedWidth(v, 3) + " (" + String.valueOf(x) + ", " + String.valueOf(y) + ")");
   }

   String fixedWidth(int v, int width) {
      String s = String.valueOf(v);
      while (s.length() <= width)
         s = " " + s;
      return s;
   }

   public void setValuePixel() {
      valuePixel.set("");
   }

   public void setValueStats(int min, float mean, int max) {
      valueStats.set(String.valueOf(min) + " < " + fmtDec1.format(mean) + " <  " +
         String.valueOf(max));
   }

   public void setValueStats(int min, float mean, int max, Color color) {
      valueStats.set(String.valueOf(min) + " < " + fmtDec1.format(mean) + " <  " +
         String.valueOf(max), color);
   }

   public void setValueStats() {
      valueStats.set("");
   }

   public void setValueROI(int x, int y, int w, int h) {
      valueROI.set(String.valueOf(x) + ", " + String.valueOf(y) + " (" + String.valueOf(w) + " x " +
         String.valueOf(h) + ")", ColorUtils.darkblue);
   }

   public void setValueROI() {
      valueROI.set("");
   }

   public void blankAll() {
      valuePixel.set("");
      valueStats.set("");
      valueROI.set("");
   }   

    
}
