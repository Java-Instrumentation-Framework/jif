package edu.mbl.jif.gui.imaging.event;

import edu.mbl.jif.gui.imaging.*;
import java.util.EventListener;

public interface PixelChangeListener extends EventListener
  {
    public void pixelChanged(PixelChangeEvent evnt);
  }
