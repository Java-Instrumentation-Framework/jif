package edu.mbl.jif.imaging.tiff;

/**
 * File Name:   tiffwrite.java
 *
 * Description:	store 2D array of double or integer values
 *              to a TIFF file
 *
 * @author	Liya Thomas
 * @Date        November, 2001
 * @version
 */

// Java packages
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.lang.Math.*;

// class tiffwrite
public class TiffWrite_Liya {
 public static final short MAXROWS = 3000;    // maximum # of rows
 public static final short MAXCOLUMNS = 1400; // maximum # of columns
 private FileOutputStream fileOut;  		  // output data stream pointer
 private DataOutputStream dataOut;  		  // output file stream pointer

 // class constructor
 public TiffWrite_Liya() {  }

 // initialize variables

 // Convert a file name from "*.txt" to "*.tif"
 String CreateTiffFileName(String fileName) {
  StringBuffer tbuf;
  String tempname;

  tbuf = new StringBuffer(" ");

  tbuf.setLength(0);
  tbuf.append(fileName.substring(0,(fileName.length() - 2)));
  tbuf.append("if");
  return tbuf.toString();
  }

 // Create TIFF image of integer array
 void WriteIntValues(int rows, int columns, int inputImageInt[][],
                     String outputFileName) {

   if (rows<0 || rows>MAXROWS || columns<0 || columns>MAXCOLUMNS)
     //errorMessage.PrintErrorMessage("Invalid # rows and # columns!");

   System.out.println("Begin to write "+outputFileName+"...");

   // offset to the end of data (gray values) in file
   int pos = 8 + rows*columns;

   System.out.println("pos = "+pos);

   try {
      fileOut = new FileOutputStream(outputFileName);
      dataOut = new DataOutputStream(fileOut);

                 /*
                 *  Write the header
                 */
                   short i, j;
                i = (short) 'I';
                j = (short) (i * 256 + i);
                fputword(j);
                fputword((short)42);
                fputlong(pos);

                 /*
                 * Write the bitmap
                 */
                for (i=0; i < rows; i++)
                    for (j=0; j < columns; j++)
                       dataOut.writeByte((byte)inputImageInt[i][j]);

                System.out.println("Image bits wrote");

                /*
                 * Write the tags
                 */

                fputword((short)8);										// # of tags
                writetiftag(SubFileType, TIFFshort, 1, 1);
                writetiftag(ImageWidth, TIFFshort, 1,  columns);
                writetiftag(ImageLength, TIFFshort, 1, rows);
                writetiftag(BitsPerSample, TIFFshort, 1, 8);
                writetiftag(Compression, TIFFshort, 1, 1);
                writetiftag(PhotoMetricInterp, TIFFshort, 1, 1);  		// for gray values only
                writetiftag(StripOffsets, TIFFlong, 1, 8);				// beginning of image data
                writetiftag(PlanarConfiguration, TIFFshort, 1, 1);
                System.out.println("Tags Wrote");

                fputlong(0);

      fileOut.close();

      } catch (java.io.IOException read) {
          System.out.println("Error occured while writing output file."); }

     System.out.println("File "+outputFileName+ " output successful!");

   }

/*
 * write one TIFF tag to the IFD
 */
void writetiftag(short tag, short type, int length, int offset)
{
  fputword(tag);
  fputword(type);
  fputlong(length);
  fputlong(offset);
} /* writetiftag */

/*
 * function: fputword
 */
void fputword(short n)
{
   try {
      dataOut.writeByte((byte)n);
      dataOut.writeByte((byte) (n >> 8));
      } catch (java.io.IOException read) {
          System.out.println("Error occured while writing output file."); }

} /* fputword */

/*
 * function: fputlong
 */
void fputlong(int n)
{
   try {
      dataOut.writeByte((byte)n);
      dataOut.writeByte((byte) (n >> 8));
      dataOut.writeByte((byte) (n >> 16));
      dataOut.writeByte((byte) (n >> 24));
      } catch (java.io.IOException read) {
          System.out.println("Error occured while writing output file."); }

} /* fputlong */

 // Create TIFF image of double-valued array
 void WriteDoubleValues(int rows, int columns, double inputImageDouble[][],
                     String outputFileName) {

   if (rows<0 || rows>MAXROWS || columns<0 || columns>MAXCOLUMNS)
     //errorMessage.PrintErrorMessage("Invalid # rows and # columns!");

   System.out.println("Begin to write "+outputFileName+"...");

   // offset to the end of data (gray values) in file
   int pos = 8 + rows*columns;

   System.out.println("pos = "+pos);

   try {
      fileOut = new FileOutputStream(outputFileName);
      dataOut = new DataOutputStream(fileOut);

                 /*
                 *  Write the header
                 */
                   short i, j;
                i = (short) 'I';
                j = (short) (i * 256 + i);
                fputword(j);
                fputword((short)42);
                fputlong(pos);

                 /*
                 * Write the bitmap
                 */
                for (i=0; i < rows; i++)
                    for (j=0; j < columns; j++)
                       dataOut.writeByte((byte)((int)inputImageDouble[i][j]));

                System.out.println("Image bits wrote");

                /*
                 * Write the tags
                 */

                fputword((short)8);										// # of tags
                writetiftag(SubFileType, TIFFshort, 1, 1);
                writetiftag(ImageWidth, TIFFshort, 1,  columns);
                writetiftag(ImageLength, TIFFshort, 1, rows);
                writetiftag(BitsPerSample, TIFFshort, 1, 8);
                writetiftag(Compression, TIFFshort, 1, 1);
                writetiftag(PhotoMetricInterp, TIFFshort, 1, 1);  		// for gray values only
                writetiftag(StripOffsets, TIFFlong, 1, 8);				// beginning of image data
                writetiftag(PlanarConfiguration, TIFFshort, 1, 1);
                System.out.println("Tags Wrote");

                fputlong(0);

      fileOut.close();

      } catch (java.io.IOException read) {
          System.out.println("Error occured while writing output file."); }

     System.out.println("File "+outputFileName+ " output successful!");

   }

public static final short GOOD_WRITE	=	0;
public static final short BAD_WRITE		=	1;
public static final short BAD_READ		=	2;
public static final short MEMORY_ERROR	=	3;
public static final short WRONG_BITS	=	4;

public static final short RGB_RED		=	0;
public static final short RGB_GREEN		=	1;
public static final short RGB_BLUE		=	2;
public static final short RGB_SIZE		=	3;

/*
 * TIFF object sizes
 */
public static final short TIFFbyte		=	1;
public static final short TIFFascii		=	2;
public static final short TIFFshort		=	3;
public static final short TIFFlong		=	4;
public static final short TIFFrational	=	5;

/*
 * TIFF tag names
 */
public static final short NewSubFile			=	254;
public static final short SubFileType			=	255;
public static final short ImageWidth			=	256;
public static final short ImageLength			=	257;
public static final short RowsPerStrip			=	278;
public static final short StripOffsets			=	273;
public static final short StripByteCounts		=	279;
public static final short SamplesPerPixel		=	277;
public static final short BitsPerSample			=	258;
public static final short Compression			=	259;
public static final short PlanarConfiguration	=	284;
public static final short Group3Options			=	292;
public static final short Group4Options			=	293;
public static final short FillOrder				=	266;
public static final short Threshholding			=	263;
public static final short CellWidth				=	264;
public static final short CellLength			=	265;
public static final short MinSampleValue		=	280;
public static final short MaxSampleValue		=	281;
public static final short PhotoMetricInterp		=	262;
public static final short GrayResponseUnit		=	290;
public static final short GrayResponseCurve		=	291;
public static final short ColorResponseUnit		=	300;
public static final short ColorResponseCurves	=	301;
public static final short XResolution			=	282;
public static final short YResolution			=	283;
public static final short ResolutionUnit		=	296;
public static final short Orientation			=	274;
public static final short DocumentName			=	269;
public static final short PageName				=	285;
public static final short XPosition				=	286;
public static final short YPosition				=	287;
public static final short PageNumber			=	297;
public static final short ImageDescription		=	270;
public static final short Make					=	271;
public static final short Model					=	272;
public static final short FreeOffsets			=	288;
public static final short FreeByteCounts		=	289;
public static final short ColorMap				=	320;
public static final short Artist				=	315;
public static final short DateTime				=	306;
public static final short HostComputer			=	316;
public static final short Software				=	305;

}
