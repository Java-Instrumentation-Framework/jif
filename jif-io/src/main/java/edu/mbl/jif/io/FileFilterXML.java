package edu.mbl.jif.io;


// Example for FileFiltering

import java.io.*;

public class FileFilterXML extends javax.swing.filechooser.FileFilter {

  public boolean accept(File f) {
    if (f.getName().endsWith(".xml")) return true;
    else if (f.isDirectory()) return true;
    return false;
  }

  public String getDescription() {
    return "XML files (*.xml)";
  }

}
