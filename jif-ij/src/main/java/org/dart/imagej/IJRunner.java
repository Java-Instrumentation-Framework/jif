package org.dart.imagej;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author GBH
 */
public class IJRunner {

    public static void runImageJ(File[] files, String macro) {

        try {
            // Get either current JVM or external JVM connection to ImageJ.
            // Creates an instance of ImageJ if none exists in current JVM.
            IJClient ijClient = IJClientFactory.getIJClient(false);

            // Load up image files
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file != null) {
                    if (!file.exists()) {
                        //log.error(file + ": Image file not found");
                        System.err.println(file + ": Image file not found");
                    } else {
                        //log.debug("open: " + file.getPath());
                        System.err.println("opening: " + file.getPath());
                        ijClient.openImage(file);
                    }
                }
            }

            // Execute the optional macro
            if (macro != null && macro.trim().length() > 0) {
                //log.debug("macro: " + macro);
                ijClient.runMacro(macro);
            }
            
        } catch (IOException e) {
            //log.error("Failed to connect to or execute ImageJ commands.  " + e.getMessage());
            System.err.println("Failed to connect to or execute ImageJ commands.  " + e.getMessage());

        }
    }


    public static void main(String[] args) {
        File[] files = new File[1];
        runImageJ(files, null);
    }


}
