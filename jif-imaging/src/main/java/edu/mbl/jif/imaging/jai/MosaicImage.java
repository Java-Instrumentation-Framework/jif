package edu.mbl.jif.imaging.jai;

import java.awt.*;
import java.awt.image.*;
import java.awt.image.renderable.*;

//MosaicImage.java
import java.util.*;

import javax.media.jai.*;


/**
 * The MosaicImage class.
 */
public class MosaicImage extends OpImage {
   /**
    * The number of rows in the mosaic.
    */
   private int rows;

   /**
    * The number of columns in the mosaic.
    */
   private int cols;

   /**
    * The image tile width.
    */
   private int tileWidth;

   /**
    * The image tile height.
    */
   private int tileHeight;

   /**
    * The image width.
    */
   private int width;

   /**
    * The image height.
    */
   private int height;

   /**
    * The number of tiles across the image.
    */
   private int tilesX;

   /**
    * The number of tiles down the image.
    */
   private int tilesY;

   /**
    * The width of the source tiles.
    */
   private int majorTileWidth;

   /**
    * The height of the source tiles.
    */
   private int majorTileHeight;

   public MosaicImage(int rows, int cols, Vector sources) {
      super(sources, null, null, true);
      this.rows = rows;
      this.cols = cols;
      if (sources.size() < (rows * cols)) {
         throw new IllegalArgumentException(
               "Not enough sources supplied to MosaicOperator");
      }
      int count = sources.size();
      for (int i = 0; i < count; i++)
         addSource((PlanarImage)sources.elementAt(i));

      PlanarImage baseTile = (PlanarImage)sources.firstElement();
      majorTileWidth = baseTile.getWidth();
      majorTileHeight = baseTile.getHeight();
      tileWidth = baseTile.getTileWidth();
      tileHeight = baseTile.getTileHeight();
      width = cols * majorTileWidth;
      height = rows * majorTileHeight;

      tilesX = ((width + tileWidth) - 1) / tileWidth;
      tilesY = ((height + tileHeight) - 1) / tileHeight;

      SampleModel sampleModel = baseTile.getSampleModel();
      ColorModel  colorModel  = baseTile.getColorModel();

      ImageLayout imageLayout =
         new ImageLayout(0, 0, width, height, 0, 0, tileWidth, tileHeight, sampleModel,
            colorModel);
      setImageLayout(imageLayout);
   }

   public Raster computeTile(int x, int y) {
      DataBuffer dataBuffer = sampleModel.createDataBuffer();
      if ((x < 0) || (x >= tilesX) || (y < 0) || (y >= tilesY)) {
         System.out.println("Error: illegal tile requested from a MosaicOpImage.");
         Raster raster =
            Raster.createRaster(
               sampleModel,
               dataBuffer,
               new Point(x * tileWidth, y * tileHeight));
         return raster;
      }
      try {
         int         tx     = (x * tileWidth) / majorTileWidth;
         int         ty     = (y * tileHeight) / majorTileHeight;
         Rectangle   r      =
            new Rectangle(tileWidth * (x % (majorTileWidth / tileWidth)),
               tileHeight * (y % (majorTileHeight / tileHeight)), tileWidth, tileHeight);
         PlanarImage op     = getSourceImage((ty * cols) + tx);
         Raster      raster = null;
         synchronized (op) {
            raster = op.getData(r);
            raster = raster.createTranslatedChild(x * tileWidth, y * tileHeight);
         }
         return raster;
      } catch (Exception e) {
         e.printStackTrace();
      }
      return Raster.createRaster(
         sampleModel,
         dataBuffer,
         new Point(x * tileWidth, y * tileHeight));
   }


   public Rectangle mapDestRect(Rectangle rectangle, int i) {
      return rectangle;
   }


   public Rectangle mapSourceRect(Rectangle rectangle, int i) {
      return rectangle;
   }


   public static RenderedImage creaMosaico() {
      return null;
   }


   public static void main(String[] argv) {
      String     fileName1 = "YourFile00.tif";
      RenderedOp image1    = JAI.create("fileload", fileName1);
      String     fileName2 = "YourFile01.tif";
      RenderedOp image2    = JAI.create("fileload", fileName2);
      String     fileName3 = "YourFile10.tif";
      RenderedOp image3    = JAI.create("fileload", fileName3);
      String     fileName4 = "YourFile11.tif";
      RenderedOp image4    = JAI.create("fileload", fileName4);
      Vector     sv        = new Vector();
      sv.add(image1);
      sv.add(image2);
      sv.add(image3);
      sv.add(image4);
      RenderedImage mi             = new MosaicImage(2, 2, sv);

      String        fileNameToSafe = "TotalImage.tif";
      System.out.println("Start Save");
      JAI.create("filestore", mi, fileNameToSafe, "TIFF", null);
      System.out.println("End Save");
   }
}
