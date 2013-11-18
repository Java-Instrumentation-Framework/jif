package edu.mbl.jif.imaging;

import java.awt.image.BufferedImage;

import java.io.Serializable;

public class ImageObject
      implements Serializable
{
   public String fileName;
   public BufferedImage firstImage;
   public BufferedImage thumbnail;
   public int numberOfPages = 0;
   public int sliceSelected = 0;
   public ImageStackBuffer imgStkBuff = null;

   public ImageObject (String _fileName, BufferedImage _pImage,
            int _numberOfPages, BufferedImage thumbnail, float thumbScale) {
         fileName = _fileName;
         firstImage = _pImage;
         this.thumbnail = thumbnail;
         numberOfPages = _numberOfPages;
         sliceSelected = 0;
         imgStkBuff = null;
   }
   //
//   public ImageObject (String _fileName, BufferedImage _pImage,
//         int _numberOfPages, float thumbScale) {
//      fileName = _fileName;
//      firstImage = _pImage;
//      thumbnail = ImageUtils.makeThumbnail(_pImage, thumbScale);
//      numberOfPages = _numberOfPages;
//      sliceSelected = 0;
//      imgStkBuff = null;
//   }


   public void loadStack (String path) {
      System.out.println("LoadingStack...");
      // checkMemory
      imgStkBuff = new ImageStackBuffer(path + "\\" + fileName + ".tif");
      // System.out.println("imgStkBuff: "+ imgStkBuff);
   }


   public BufferedImage getSlice (int _slice) {
      sliceSelected = _slice;
      return imgStkBuff.getSlice(_slice);
   }
}
