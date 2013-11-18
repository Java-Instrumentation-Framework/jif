/*
 * TextFile.java
 *
 * Created on March 13, 2007, 11:15 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.mbl.jif.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author GBH
 */
public class TextFile {
    
    BufferedReader reader;
    File toFile;
    BufferedWriter writer;
    
    /** Creates a new instance of TextFile */
    public TextFile(String filepath) {
        try {
            toFile = new File(filepath);
            if(toFile == null) {
                System.err.println(filepath + " could not be created");
                return;
            }
            writer = new BufferedWriter(new FileWriter(toFile));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
    
    public void writeln(String line) {
        if(writer !=null) {
            try {
                writer.write(line);
                writer.newLine();   // Write system dependent end of line.
                writer.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void close() {
        try {
            writer.close();  // Close to unlock and flush to disk.
        } catch (IOException ex) {
            ex.printStackTrace();
        }  // Close to unlock and flush to disk.
    }
    
    public static void main(String[] args) {
        TextFile tf = new TextFile("test.txt");
        tf.writeln("whatdafuc over?");
        tf.close();
    }
    
    }
