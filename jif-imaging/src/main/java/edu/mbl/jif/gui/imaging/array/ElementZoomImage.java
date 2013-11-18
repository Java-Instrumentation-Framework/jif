package edu.mbl.jif.gui.imaging.array;

import edu.mbl.jif.gui.imaging.ResizableZoomImagePanel;
import java.awt.*;

/**
 * <p>Title: </p>
 * <p>Description: An element in a ImageArrayDisplayPanel</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ElementZoomImage
      extends ResizableZoomImagePanel
{
   BorderLayout borderLayout1 = new BorderLayout();
   int t = -1;
   int z = -1;

   public ElementZoomImage (Image _zoomimage, float _zoomMagnification,
                            boolean pixelRoiSelection) {

      super(_zoomimage, _zoomMagnification, pixelRoiSelection);
			this.setBackground(Color.green);
   }


   public void setT (int _t) {
      t = _t;
   }


   public void setZ (int _z) {
      z = _z;
   }


   public int getT () {
      return t;
   }


   public int getZ () {
      return z;
   }

}
