package net.trevize.galatee;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

//import net.trevize.tinker.ImageUtils;
import com.davidsoergel.conja.Function;
import com.davidsoergel.conja.Parallel;
import com.sun.media.jai.codec.SeekableStream;
import edu.mbl.jif.imaging.nav.FileOpener;
import edu.mbl.jif.imaging.nav.util.Histogram;

/**
 * This class is used for the Galatee image viewer.
 *
 * In this implementation, severals locks are in use: 1. a lock on the instance of the class (this):
 * running/sleeping 2. a lock on the variable stopIt: a boolean that notice if the end of the thread
 * is asked by the GUI or not. 3. a lock on the variable loading: an ArrayList<GalleryItem> which
 * contains GalleryItem that are in a loading state (i.e. not ever in the queue). 4. a lock on the
 * variable files: an ArrayList<File> which contains File instances regarding to each GalleryItem.
 * This is the process queue.
 *
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * ImageLoaderThread.java - Mar 25, 2009
 */
public class ImageLoaderThread extends Thread {

   private Galatee galatee;
   private int imageWidth;
   private int imageHeight;
   private GItemList gi_list;

   public ImageLoaderThread(Galatee galatee, int imageWidth, int imageHeight) {
      super("ImgLoader");
      this.galatee = galatee;
      this.imageWidth = imageWidth;
      this.imageHeight = imageHeight;
      gi_list = new GItemList();
   }

   /**
    * For adding the loading of an image. The GalleryItem given in parameter is put in the first
    * place queue, i.e. at the next iteration of the main loop in the run() method, it will be this
    * GalleryItem that will be loaded.
    *
    * @param file
    * @param item
    */
   public void pushItem(GItem item) {
      gi_list.add(item);
      //notify this because he's maybe in a "waiting" state. 
      synchronized (this) {
         this.notify();
      }
   }

   public void run() {
      while (!stop) { //main loop.
         //define the function for the Parallel.forEach(...).
         Function<GItem, Void> loadImageFunction = new Function<GItem, Void>() {
            public Void apply(GItem gitem) {
               BufferedImage image = null;
               String metaDataString = null;
               if (gitem.getUri().getScheme().equals("tar")) {
                  // special behavior: is the URI schema is tar, we access the file using Apache Common VFS.
                  InputStream gitem_is = gitem.getInputStream();
                  image = ImageUtils.JAI_loadAndResizeImage(imageWidth,
                          imageHeight, SeekableStream.wrapInputStream(
                                  gitem_is, true), true);
                  try {
                     gitem_is.close();
                  } catch (IOException e) {
                     e.printStackTrace();
                  }
               } else {  // from local file
                  //======================================================================
                  // TODO GBH here is where we will read metadata...
                  // if it is a MM dataset...
                  // Open it and get summary metadata...
                  //======================================================================▐
                  boolean isMMDataset = FileOpener.isMicroManagerType(gitem.getLocalFilepath());
//                  if (isMMDataset) {
//                     try {
//                        String[] paths = DatasetUtils.parsePath(gitem.getLocalFilepath());
//                        MMgrDatasetAccessor mmdp = new MMgrDatasetAccessor(paths[0], paths[1],
//                                true, false);
//                        //JSONObject sumMD = mmdp.getImageCache().getSummaryMetadata();
//                        String comment = mmdp.getImageCache().getComment();
//                        if (comment != null) {
//                           metaDataString = "\n" + comment;
//                        }
//                     } catch (Exception ex) {
//                        System.err.println("Cannot open dataset: " + gitem.getLocalFilepath());
//                     }
//                     try {
//                        image = ImageUtils.JAI_loadAndResizeImage(imageWidth,
//                                imageHeight, gitem.getLocalFilepath(), true);
//                     } catch (Exception e) {
//                        // exception may be thrown here, but no problem,
//                        // as it is mlib accelerator for JAI...
//                        // e.printStackTrace();
//                     }
//
//                  } else {
                     try {
                        image = ImageUtils.JAI_loadAndResizeImage(imageWidth,
                                imageHeight, gitem.getLocalFilepath(), true);
                     } catch (Exception e) {
                        // exception may be thrown here, but no problem,
                        // as it is mlib accelerator for JAI...
                        // e.printStackTrace();
                     }
                  //}
               }
               if (image == null) {
                  image = Galatee.imageLoadingError;
               }
               // after HERE we assume the image is loaded.
               // If Enhance checked...
               if (galatee.isEqualizeHisto()) {
                  if (image.getType() == 10 || image.getType() == 11) {
                     // Equalize the contrast
                     Histogram hist = new Histogram(image);
                     hist.equalizeHistogram();
                     gitem.setImage(hist.getImage());
                  } else {
                     gitem.setImage(image);
                  }
               } else {
                  gitem.setImage(image);
               }
               // Add to description ========================================================
               if (!gitem.wasMetadataAdded()) {  // prevent adding it more than once.
                  if (metaDataString != null) {
                     gitem.setText(gitem.getText() + metaDataString);
                  }
                  gitem.setMetadataAdded(true);
               }
               // ===========================================================================
               galatee.getTable().repaint(galatee.getTable().getVisibleRect());
               // if needed we keep the selected cell still in the visible rectangle of the JTable.
               synchronized (galatee) {
                  int selected_row = galatee.getTable().getSelectedRow();
                  int selected_column = galatee.getTable().getSelectedColumn();
                  if (galatee.isKeep_selected_cell_visible()
                          && selected_row != -1 && selected_column != -1) {
                     galatee.getTable().scrollRectToVisible(
                             galatee.getTable().getCellRect(selected_row, selected_column, true));
                  }
               }
               gi_list.processingEndedFor(gitem);
               return null;
            }
         };
         //launch the Parallel.forEach(...).
         
         try {
            Parallel.forEach(gi_list, loadImageFunction);
            Parallel.emergencyAbort();
         } catch (Exception e) {
            e.printStackTrace();
         }
         //waiting to be notified that there's stuff again in the queue.
         synchronized (this) {
            try {
               //System.out.println("iltpt is waiting.");
               this.wait();
            } catch (InterruptedException e) {
               //e.printStackTrace();
               //System.out.println("Image loader Thread terminated... at wait");
            }
         }
      }//end while(true)
   }//end run() method

   boolean stop = false;
   public void terminate() {
      stop = true;
      this.interrupt();
   }

   /**
    * *************************************************************************
    * getters and setters. ************************************************************************
    */
   public int getImageWidth() {
      return imageWidth;
   }

   public int getImageHeight() {
      return imageHeight;
   }

   public void setImageWidth(int imageWidth) {
      this.imageWidth = imageWidth;
   }

   public void setImageHeight(int imageHeight) {
      this.imageHeight = imageHeight;
   }
   
}
