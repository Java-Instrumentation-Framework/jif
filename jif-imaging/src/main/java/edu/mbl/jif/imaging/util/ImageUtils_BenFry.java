package edu.mbl.jif.imaging.util;


import java.awt.*;
import java.awt.image.*;
import java.io.*;

/** @todo Consolidate this with other Imaging utils...
 * */

/**
 * (c) Copyright 1998-2001 Ben Fry, fry@media.mit.edu<BR>
 * If you use this code, you must give credit appropriately.
 * @version 1.01
 * <P>
 * This is a bushel of utility routines to make is easier to
 * deal with images and do image processing. The meat of the
 * routines convert an image into a pixel array, or vice versa,
 * making it easy to manipulate the data and convert it back
 * into an image to be shown on-screen or saved.
 * <P>
 * I've found the standard JDK ImageFilter classes are almost
 * completely a dead-end, and not very useful. Especially for
 * people just getting started with Java, ImageProducers and
 * ImageConsumers are a little bit much, nevermind the other
 * landmines and bugs present in JDK 1.1.
 * <PRE>
 * This is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <PRE>
 * For more information about the license, visit www.gnu.org
 */
public class ImageUtils_BenFry
{
    /**
     * Convert an image object into an array of pixel data.
     * This returns an array of int[] that is width*height large.
     * One int per pixel, so to get the red, green, blue, and
     * alpha values for each pixel, use the following:
     * <PRE>
     * int myData[] = ImageUtils.imageToArray(myImage);
     * int pixel = myData[10]; // get the tenth pixel
     * int alpha = (pixel >> 24) & 0xff;
     * int red = (pixel >> 16) & 0xff;
     * int green = (pixel >> 8) & 0xff;
     * int blue = pixel & 0xff;
     * </PRE>
     */
    static public int[] imageToArray(Image img) {
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        int pix[] = new int[w * h];
        PixelGrabber pg =
            new PixelGrabber(img, 0, 0, w, h, pix, 0, w);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
        }
        return pix;
    }

    /**
     * Turns things back the other way. If you want to modify
     * a pixel (ala the example above) you can reassign the pixel
     * using the following:
     * <PRE>
     * // no transparency
     * int myData[10] = 0xff000000 | (red << 16) | (green < 8) | blue;
     * // ..or with an alpha channel
     * int myData[10] = (alpha << 24) | (red << 16) | (green < 8) | blue;
     * </PRE>
     */
    static public Image arrayToImage(int pix[], int w) {
        Toolkit tk = Toolkit.getDefaultToolkit();
        int h = pix.length / w;
        return tk.createImage(new MemoryImageSource(w, h, pix, 0, w));
    }

    /**
     * Turns an image into a standard offscreen image. This
     * is because an image taken from a URL can't be drawn
     * into using getGraphics(), so the remedy is to make
     * an offscreen image, draw the original image into that,
     * and then continue drawing..
     */
    static public Image makeOffscreenImage(Image img, Component comp) {
        int width = img.getWidth(null);
        int height = img.getHeight(null);
        Image output = comp.createImage(width, height);
        Graphics g = output.getGraphics();
        g.drawImage(img, 0, 0, null);
        return output;
    }

    /**
     * Same as above, but destination image can be a different size
     */
    static public Image makeOffscreenImage(Image img, Component comp,
                                           int wide, int high) {
        int width = img.getWidth(null);
        int height = img.getHeight(null);
        Image output = comp.createImage(wide, high);
        Graphics g = output.getGraphics();
        g.drawImage(img, (wide-width)/2, (high-height)/2, null);
        return output;
    }

    /**
     * Create a blank offscreen image without a component.
     * This is a hack and should be used only at a minimum.
     * I won't bother explaining what's going on here.
     */
    static public Image createOffscreenImage(int width, int height) {
        Frame comp = new Frame();
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        comp.setLocation(size.width + 10, 10);
        comp.setVisible(true);
        Image output = comp.createImage(width, height);
        comp.dispose();
        return output;
    }

    static public int[] imageToSquareArray(Image inputImage) {
        // make a square image of the max size
        // max size is the diameter of the image, which makes a circle
        int rawData[] = ImageUtils_BenFry.imageToArray(inputImage);
        int rawWidth = inputImage.getWidth(null);
        int rawHeight = rawData.length / rawWidth;
        int diameter = (int)Math.ceil(Math.sqrt(rawWidth * rawWidth +
                                                rawHeight * rawHeight));
        int imageData[] = new int[diameter * diameter];
        int offsetX = (diameter - rawWidth) / 2;
        int offsetY = (diameter - rawHeight) / 2;
        for (int y = 0; y < rawHeight; y++) {
            for (int x = 0; x < rawWidth; x++) {
                imageData[offsetY * diameter + offsetX] =
                    rawData[y * rawWidth + x];
                offsetX++;
            }
            offsetX -= rawWidth;
            offsetY++;
        }
        return imageData;
    }
}

