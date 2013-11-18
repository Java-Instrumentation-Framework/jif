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
public class SetOfPositionsMmgr implements SetOfPositions {

	ArrayList<SeriesOfImages> positions;
			
	@Override
	public void setPositions(ArrayList<SeriesOfImages> pos) {
		this.positions = pos;
	}

	@Override
	public BufferedImage getImage(int p, int n) {
		return positions.get(p).getImage(n);
	}

	@Override
	public BufferedImage getImage(int p, int c, int z, int t) {
		return positions.get(p).getImage(c, z, t);
	}	
}
