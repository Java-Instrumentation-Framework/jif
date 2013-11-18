package org.dart.imagej;

import java.io.File;
import java.io.IOException;

//import com.rbnb.api.Directory;
/**
 *
 * @author Tony O'Hagan
 */
public interface IJClient {

    /**
     * Attempt to quit ImageJ
     */
    void quit();


    /**
     * Sets the default working directory for ImageJ
     * @param dir
     * @throws IOException
     */
    public void setDefaultDirectory(File dir) throws IOException;


    /**
     * Open an image file
     * @param file
     */
    void openImage(File file) throws IOException;


    /**
     * Execute an ImageJ menu command
     * @param macro
     * @param macroArg
     */
    void runMenuCommand(String menuCommand) throws IOException;


    /**
     * Evaluates a macro string
     * @param macro
     */
    void runMacro(String macro) throws IOException;


    /**
     * Evaluates a macro string
     * @param macro
     * @param macroArg
     */
    void runMacro(String macro, String macroArg) throws IOException;


    /**
     * Executes a macro loaded from a file
     * @param file
     */
    void runMacroFile(File file) throws IOException;


    /**
     * Executes a macro loaded from a file with an option string argument
     * @param file
     * @param arg
     * @throws IOException
     */
    void runMacroFile(File file, String arg) throws IOException;


}
