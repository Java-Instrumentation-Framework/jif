/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.imaging.series;

import edu.mbl.jif.imaging.api.SeriesOfImages;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author GBH
 */
public interface SetOfPositions {
	
	void setPositions(ArrayList<SeriesOfImages> pos);
	
	BufferedImage getImage(int p, int n);

	BufferedImage getImage(int p, int c, int z, int t);
	
}
