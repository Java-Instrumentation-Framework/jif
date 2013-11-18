/*
ImageViewerPlugin
 */

package edu.mbl.jif.gui.imaging;

import java.util.List;
import javax.swing.AbstractButton;

/**
 *
 * @author GBH
 */
public interface ImageViewerPlugin {
    
    List<AbstractButton> getButtons();
    
    GraphicOverlay getOverlay();

}
