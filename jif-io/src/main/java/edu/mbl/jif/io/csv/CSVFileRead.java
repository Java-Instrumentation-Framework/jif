/*
 * TextFile.java
 *
 * Created on March 13, 2007, 11:15 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package edu.mbl.jif.io.csv;

import com.infomata.data.CSVFormat;
import com.infomata.data.DataFile;
import com.infomata.data.DataFileFactory;
import com.infomata.data.DataRow;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author GBH
 */
public class CSVFileRead {

    DataRow row;
    DataFile read;


    public CSVFileRead(String filepath) {
        /*    Creating a reader for CSV file using ISO-8859-1    */
        try {
            read = DataFileFactory.createReader("8859_1");
            read.setDataFormat(new CSVFormat());
            // first line is column header
            read.containsHeader(true);
            read.open(new File(filepath));
        } catch(Exception e){}
    }


    public void readRow(Object item1, Object item2) {
//        for (row = read.next(); row != null; row = read.next()) {
//            //String text = row.getString(0);
//            // retrieval using column header
//            //int number1 = row.getInt("FIRST_NUMBER", 0);
//            int number1 = row.getInt(1);
//            double number2 = row.getDouble(2);
//            // use the retrieved data ...
//        }
    }


    public Object[] readRow() {
        return null;
    }


    public void close() {
        try {
            read.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) {

    }


}
