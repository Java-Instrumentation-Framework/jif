/*
 * IntensityWatcherInterface.java
 * Created on March 8, 2007, 5:16 PM
 */

package edu.mbl.jif.gui.imaging;

import edu.mbl.jif.gui.imaging.event.PixelChangeListener;
import edu.mbl.jif.gui.imaging.event.RoiChangeListener;

/**
 * @author GBH
 */
public interface IntensityWatcherInterface extends PixelChangeListener, RoiChangeListener {
    
    float getMean();

    void setMeasurementFreq(int freq);

}
