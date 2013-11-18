package edu.mbl.jif.imaging.tiff;

//import com.sun.media.jai.codec.TIFFEncodeParam;
//import java.awt.image.renderable.ParameterBlock;
//
//import javax.media.jai.*;

//import com.sun.media.jai.codec.TIFFEncodeParam;

/** A utility application used to create a tiled tiff from any original image
 * source that is supported from JAI. Run TiledTiff --help for usage
 information.
 */
public class TiledTiff {

//  public static void printUsage() {
//    System.out.println("<program> [--help] [--info <image>] [--create <out_image> <in_image> <tile_width> <tile_height>]\n");
//    System.out.println("  --help    Display this usage information.");
//    System.out.println("  --info    Display info about the given tiff file.");
//    System.out.println(
//        "  --create  Create a tiled tiff image from the given source image.");
//  }
//
//  public static void main(String[] args) {
//    // Setup the arguments
//    boolean info = false;
//    String inFileName = null;
//    String outFileName = null;
//    int tileHeight = 2;
//    int tileWidth = 2;
//
//    for (int i = 0; i < args.length; ++i) {
//      String arg = args[i];
//
//      if (arg.equals("--help")) {
//        printUsage();
//        System.exit(0);
//      } else if (arg.equals("--info") && (i + 1 < args.length)) {
//        info = true;
//        inFileName = args[++i];
//      } else if (!info && arg.equals("--create") && (i + 4 < args.length)) {
//        outFileName = args[++i];
//        inFileName = args[++i];
//        tileWidth = Integer.parseInt(args[++i]);
//        tileHeight = Integer.parseInt(args[++i]);
//      } else {
//        System.out.println("Invalid argument: " + arg + "\n");
//        printUsage();
//        System.exit( -1);
//      }
//    }
//
//    // Create the tiled tiff object
//    TiledTiff tiledTiff = new TiledTiff();
//
//    if (info) {
//      // Display the info
//      tiledTiff.printInfo(inFileName);
//    } else {
//      tiledTiff.createTiff(outFileName, inFileName, tileWidth, tileHeight);
//    }
//
//  }
//
//  /**
//   * Method createTiff.
//   * @param outFileName
//   * @param inFileName
//   */
//  private void createTiff(String outFileName, String inFileName, int tileWidth,
//
//      int tileHeight) {
//    // Open the original image
//    PlanarImage source = JAI.create("fileload", inFileName);
//
//    System.out.println("Creating tiled tiff (" + tileWidth + ", " + tileHeight
//
//        + ")...");
//
//    // Create the Tile image
//    TiledImage ti = new TiledImage(source, tileWidth, tileHeight);
//
//    // Create the params that will be used by the tiff encoder
//    TIFFEncodeParam param = new TIFFEncodeParam();
//    param.setWriteTiled(true);
//    param.setTileSize(tileWidth, tileHeight);
//    param.setCompression(TIFFEncodeParam.COMPRESSION_DEFLATE);
//
//    // Now save the tiled image
//    ParameterBlock pb = new ParameterBlock();
//    pb.addSource(ti);
//    pb.add(outFileName);
//    pb.add("tiff");
//    pb.add(param);
//    JAI.create("filestore", pb);
//
//    printInfo(outFileName);
//  }
//
//  /**
//   * Method printInfo.
//   * @param inFileName
//   */
//  private void printInfo(String inFileName) {
//    PlanarImage image = JAI.create("fileload", inFileName);
//
//    System.out.println("Image information for " + inFileName);
//    System.out.println("  Num X Tiles: " + image.getNumXTiles());
//    System.out.println("  Num Y Tiles: " + image.getNumYTiles());
//    System.out.println("  Num Sources: " + image.getNumSources());
//    System.out.println("  Num Bands  : " + image.getNumBands());
//    System.out.println("  Width      : " + image.getWidth());
//    System.out.println("  Height     : " + image.getHeight());
//  }

}
