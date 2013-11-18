package edu.mbl.jif.io.xml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


/**
 * Stores and retreives serializable objects in XML files
 * using XStream from com.thoughtworks.xstream
 * 
 * Usage:
 *      MyObject myObject;
        ObjectStoreXML.write(myObject, "filename");
        myObject = (MyObject) ObjectStoreXML.read("filename");
 */

public class ObjectStoreXML {
    
    private ObjectStoreXML() {}
    
    
    static XStream xstream = new XStream(new DomDriver());
    // does not require XPP3 library, uses JAXP DOM
    
    public static void write(Object o, String filename) throws Throwable {
        // Serializing an object to XML
        String xml = xstream.toXML(o);
        //System.out.println(xml);
        try {
            saveTxtFile(filename, xml, false);
        } catch (IOException ex) {
            //ex.printStackTrace();
            throw ex;
        }
    }
    
    
    //
    //---------------------------------------------------------------------
    public static Object read(String filename) throws Throwable  {
        
        // Deserialize object back from XML
        Object obj = null;
        String xml = null;
        try {
            xml = readTxtFile(filename);
            obj = xstream.fromXML(xml);
        } catch (Exception ex) {
            //ex.printStackTrace();
            throw ex;
        }
        return obj;
    }
    
    
    /**
     * Save a string to a text file
     */
    private static void saveTxtFile(String pathname, String data, boolean append) throws
            IOException {
        saveTxtFile(new File(pathname), data, append);
    }
    
    
    /**
     * Save a string to a text file
     */
    private static void saveTxtFile(File f, String data, boolean append) throws
            IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(f, append));
        out.write(data);
        out.close();
    }
    
    
    /**
     * Read a text file into a string
     */
    private static String readTxtFile(String pathname) throws IOException {
        return (readTxtFile(new File(pathname)));
    }
    
    
    /**
     * Read a text file into a string
     */
    private static String readTxtFile(File f) throws IOException {
        FileReader read = new FileReader(f);
        BufferedReader in = new BufferedReader(read);
        String result = "";
        String str = null;
        while ((str = in.readLine()) != null) {
            result += str;
            result += "\n";
        }
        in.close();
        return (result);
    }
    
    
//---------------------------------------------------------------------
//    public static void main(String[] args) throws Throwable {
//        ArrayList stuff = new ArrayList();
//        
//        CameraModelZ z = new CameraModelZ(null);
//        stuff.add(z);
//        System.out.println("Before writing: " +
//                stuff.get(0).getClass()
//                );
//        
//        ObjectStoreXML.write(stuff, "stuffTest");
//        stuff.clear();
//        stuff = (ArrayList) ObjectStoreXML.read("stuffTest");
//        System.out.println("After clearing and reading: " + 
//                stuff.get(0).getClass()
//                );
//        Prefs.initialize("/psj");
//        Object[][] dataObjectArray = Prefs.getPrefsUsrObjects();
//        ObjectStoreXML.write(dataObjectArray, "prefstest");        
//        
//    }
}
