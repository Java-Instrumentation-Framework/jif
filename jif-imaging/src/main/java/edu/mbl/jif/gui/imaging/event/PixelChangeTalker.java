package edu.mbl.jif.gui.imaging.event;

import edu.mbl.jif.gui.imaging.*;

public interface PixelChangeTalker
  {
    public  void addPixelChangeListener(PixelChangeListener listener);
    
    public  void removePixelChangeListener(PixelChangeListener listener);
    
    public  void firePixelChange(PixelChangeEvent evnt);
  }
