package edu.mbl.jif.gui.imaging;

import edu.mbl.jif.gui.imaging.event.PixelChangeEvent;
import edu.mbl.jif.gui.imaging.event.PixelChangeListener;
import java.util.ArrayList;
import java.util.Iterator;


// Temp?
public class ImageDisplayModel {
   public ImageDisplayModel() {
   }

   void somethinghappened() {
      synchronized (this) {
         fireFoo(new PixelChangeEvent(this, 1, 100, 120));
      }
   }

   ArrayList listeners = new ArrayList();

   public synchronized void addPixelChangeListener(PixelChangeListener listener) {
      if (listener != null) {
         listeners.add(listener);
      }
   }


   public synchronized void removePixelChangeListener(PixelChangeListener listener) {
      listeners.remove(listener);
   }


   public synchronized void fireFoo(PixelChangeEvent evnt) {
      for (Iterator i = listeners.iterator(); i.hasNext();) {
         ((PixelChangeListener)i.next()).pixelChanged(evnt);
      }
   }
}
