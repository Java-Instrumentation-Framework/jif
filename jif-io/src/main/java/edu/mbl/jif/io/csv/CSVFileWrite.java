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
public class CSVFileWrite {

    DataRow row;
    DataFile write;

    /** Creates a new instance of TextFile */
    public CSVFileWrite(String filepath) {
        try {
            write = DataFileFactory.createWriter("8859_1", false);
            write.setDataFormat(new CSVFormat());
            write.open(new File(filepath));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void writeRow(Object item1, Object item2) {
        if (write != null) {
            try {
                row = write.next();
                row.add(item1);
                row.add(item2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void writeRow(Object item1, Object item2, Object item3) {
        if (write != null) {
            try {
                row = write.next();
                row.add(item1);
                row.add(item2);
                row.add(item3);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void writeRow(Object[] items) {
        if (write != null) {
            try {
                row = write.next();
                for (int i = 0; i < items.length; i++) {
                    row.add(items[i]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        try {
            write.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CSVFileWrite csv = new CSVFileWrite("\\test.csv");
        int n = 2;
        double x = 8889.9;
        String s = "jjjuuu";
        Object[] items = new Object[]{n, x, s};
        csv.writeRow(items);
        csv.close();
    }
}
