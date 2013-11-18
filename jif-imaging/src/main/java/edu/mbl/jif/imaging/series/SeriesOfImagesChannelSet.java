/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.imaging.series;

import edu.mbl.jif.imaging.api.SeriesOfImages;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

/**
 *
 * @author GBH
 */
public class SeriesOfImagesChannelSet implements SeriesOfImages {

    BufferedImage[] images;

    public SeriesOfImagesChannelSet(BufferedImage[] buffImgs) {
        this.images = buffImgs;
    }

    @Override
    public String getFilename() {
        return "";
    }

    @Override
    public BufferedImage getImage(int n) {
        return images[n];
    }

    @Override
    public BufferedImage getImage(int channel, int zSections, int timePoints) {
        return images[channel];
    }

    @Override
    public BufferedImage getAsThumbnail(int n, int sampling) {
        // TODO
            return images[n];
    }

    @Override
    public Dimension getImageDimensions() {
        return new Dimension(images[0].getWidth(), images[0].getHeight());
    }

    @Override
    public int getNumImages() {
        return images.length;
    }

    @Override
    public int getChannels() {
        return images.length;
    }

    @Override
    public int getTimeIntervals() {
        return 1;
    }

    @Override
    public int getZSections() {
        return 1;
    }
}
