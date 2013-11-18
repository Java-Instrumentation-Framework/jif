package edu.mbl.jif.gui.imaging.player;

/**
 * <p>Title: SeriesViewer</p>
 *
 * <p>Description: Interface for component that can be attached to SeriesPlayerController
 * so as to display a images from a series.</p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */


public interface SeriesViewer
{
   //public PanelAnalysis analysisPanel = null;
   //public void deactivateOverhead();
   public void showImage (int n);
   //public void reactivateOverhead();
}
