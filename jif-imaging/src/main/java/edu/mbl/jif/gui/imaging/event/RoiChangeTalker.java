package edu.mbl.jif.gui.imaging.event;

public interface RoiChangeTalker
  {
    public  void addRoiChangeListener(RoiChangeListener listener);
    
    public  void removeRoiChangeListener(RoiChangeListener listener);
    
    public  void fireRoiChange(RoiChangeEvent evnt);
  }
