package edu.mbl.jif.imaging.nav;

import java.io.File;

/**
 *
 * @author GBH
 */
public interface DatasetOpener {
   String description = "";
   
   // Actions (operations)   
   // List<Action) actions;
      
   // Image/Dataset Menu Item
   
   // Directory tree Menu Item
   
    void openDataset(File[] files) throws Exception;
    

}
