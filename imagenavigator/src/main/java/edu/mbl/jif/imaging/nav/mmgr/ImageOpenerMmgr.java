package edu.mbl.jif.imaging.nav.mmgr;

import edu.mbl.jif.imaging.dataset.viewer.DatasetHyperstack;
import edu.mbl.jif.imaging.nav.DatasetOpener;
import java.io.File;
import java.lang.reflect.Method;
import org.dart.imagej.IJClient;
import org.dart.imagej.IJClientFactory;

/**
 *
 * @author GBH
 */
public class ImageOpenerMmgr implements DatasetOpener {

   @Override
   public void openDataset(File[] files)
           throws Exception {

      IJClient ijClient = IJClientFactory.getIJClient(false);
      for (File file : files) {
         file = file.getParentFile();
         String rootDir = file.getAbsolutePath();
         String name = file.getName();
         rootDir = rootDir.substring(0, rootDir.length() - (name.length() + 1));
         new DatasetHyperstack(rootDir, name).createImagePlus().show();
      }

   }
//   @Override
//   public void openDataset(File[] files) throws Exception {
//      try {
//         MMStudioMainFrame mmf = getMMStudioMainFrameInstance();
//         for (File file : files) {
//            String dir = file.getParent();
//            mmf.openAcquisitionData(dir, false, true);
//         }
//      } catch (Exception exception) {
//         throw new Exception("Micro-Manager not available.");
//      }
//   }
//   private MMStudioMainFrame getMMStudioMainFrameInstance() throws Exception {
//      try {
//         ClassLoader l = Thread.currentThread().getContextClassLoader();
//         Class cls = l.loadClass("MMStudioMainFrame");
//         Method mainMethod = cls.getDeclaredMethod("getInstance");
//         MMStudioMainFrame mmMainFrame = (MMStudioMainFrame) mainMethod.invoke(null);
//         return mmMainFrame;
//      } catch (Exception ex) {
//         //ex.printStackTrace();  // remove after debugging
//         throw ex;
//      }
//   }
}
