package edu.mbl.jif.gui.imaging.event;

import java.util.EventListener;

public interface RoiChangeListener extends EventListener
  {
    public void roiChanged(RoiChangeEvent evnt);
  }
