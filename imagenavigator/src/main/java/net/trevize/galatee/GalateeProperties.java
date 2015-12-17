package net.trevize.galatee;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class GalateeProperties {

   private static Properties properties;
   private static final String PROPERTIES_FILEPATH = "./config/Galatee.properties";
   private static final String PROPERTIES_COMMENTS = "This is the properties file of Galatee, a Java library for exploring and searching in large image collection where images are annotated\n#Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]";

   public static final String PROPERTY_IMAGE_WIDTH = "IMAGE_WIDTH";
   public static final String PROPERTY_IMAGE_HEIGHT = "IMAGE_HEIGHT";
   public static final String PROPERTY_VERTICAL_SCROLLBAR_UNIT_INCREMENT = "VERTICAL_SCROLLBAR_UNIT_INCREMENT";
   public static final String PROPERTY_HORIZONTAL_SCROLLBAR_UNIT_INCREMENT = "HORIZONTAL_SCROLLBAR_UNIT_INCREMENT";
   public static final String PROPERTY_DESCRIPTION_WIDTH = "DESCRIPTION_WIDTH";
   public static final String PROPERTY_NUMBER_OF_COLUMN = "NUMBER_OF_COLUMN";
   public static final String PROPERTY_CELL_OUTER_WIDTH = "CELL_OUTER_WIDTH";
   public static final String PROPERTY_CELL_OUTER_HEIGHT = "CELL_OUTER_HEIGHT";
   public static final String PROPERTY_CELL_PADDING_WIDTH = "CELL_PADDING_WIDTH";
   public static final String PROPERTY_CELL_PADDING_HEIGHT = "CELL_PADDING_HEIGHT";
   public static final String PROPERTY_IMAGE_DESCRIPTION_SPACER = "IMAGE_DESCRIPTION_SPACER";
   public static final String PROPERTY_UNSELECTED_ITEM_BACKGROUND_COLOR = "UNSELECTED_ITEM_BACKGROUND_COLOR";
   public static final String PROPERTY_SELECTED_ITEM_BACKGROUND_COLOR = "SELECTED_ITEM_BACKGROUND_COLOR";
   public static final String PROPERTY_TEMPORARY_DIRECTORY = "TEMPORARY_DIRECTORY";
   public static final String PROPERTY_AUTHORIZED_FILENAME_EXTENSIONS = "AUTHORIZED_FILENAME_EXTENSIONS";
   public static final String PROPERTY_IMAGE_ERROR_FILE_PATH = "IMAGE_ERROR_FILE_PATH";

   private static int image_width;
   private static int image_height;
   private static int horizontal_scrollbar_unit_increment;
   private static int vertical_scrollbar_unit_increment;
   private static int description_width;
   private static int number_of_column;
   private static int cell_outer_width;
   private static int cell_outer_height;
   private static int cell_padding_width;
   private static int cell_padding_height;
   private static int image_description_spacer;
   private static String unselected_item_background_color;
   private static String selected_item_background_color;
   private static String temporary_directory;
   private static ArrayList<String> authorized_filename_extensions;
   private static String image_error_file_path;

   private static void loadProperties() {
      image_width = 128;
      image_height = 128;
      horizontal_scrollbar_unit_increment = 42;
      vertical_scrollbar_unit_increment = 42;
      description_width = 400;
      number_of_column = 1;
      cell_outer_width = 5;
      cell_outer_height = 0;
      cell_padding_width = 0;
      cell_padding_height = 0;
      image_description_spacer = 5;
      unselected_item_background_color = "#D4D4D4";
      selected_item_background_color = "#00A9E0";
      temporary_directory = "temp";
      authorized_filename_extensions = new ArrayList<String>();
      authorized_filename_extensions.add("tif");
      authorized_filename_extensions.add("tiff");
      authorized_filename_extensions.add("gif");
      authorized_filename_extensions.add("jpg");
      authorized_filename_extensions.add("png");
      image_error_file_path = "./gfx/imageLoadingError.jpg";
   }

   /* Copy fo the properties file:
         #This is the properties file of Galatee, a Java library for exploring and searching in large image collection where images are annotated
      #Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
      #Wed Apr 10 18:52:48 EDT 2013
      IMAGE_WIDTH=128
      CELL_OUTER_HEIGHT=5
      HORIZONTAL_SCROLLBAR_UNIT_INCREMENT=42
      VERTICAL_SCROLLBAR_UNIT_INCREMENT=42
      NUMBER_OF_COLUMN=1
      CELL_OUTER_WIDTH=5
      AUTHORIZED_FILENAME_EXTENSIONS=jpg,jpeg,png,gif,svg,bmp,ppm,pgm,tif
      IMAGE_DESCRIPTION_SPACER=2
      CELL_PADDING_WIDTH=0
      UNSELECTED_ITEM_BACKGROUND_COLOR=\#D4D4D4
      IMAGE_ERROR_FILE_PATH=./gfx/imageLoadingError.jpg
      CELL_PADDING_HEIGHT=0
      IMAGE_HEIGHT=128
      TEMPORARY_DIRECTORY=/TEMP
      SELECTED_ITEM_BACKGROUND_COLOR=\#00A9E0
      DESCRIPTION_WIDTH=400
   */
   private static void loadPropertiesNOT() {
      if (properties == null) {
         properties = new Properties();
         try {
            properties.load(new FileInputStream(PROPERTIES_FILEPATH));
         } catch (FileNotFoundException e) {
            e.printStackTrace();
         } catch (IOException e) {
            e.printStackTrace();
         }
         image_width = Integer.parseInt(properties
                 .getProperty(PROPERTY_IMAGE_WIDTH));
         image_height = Integer.parseInt(properties
                 .getProperty(PROPERTY_IMAGE_HEIGHT));
         horizontal_scrollbar_unit_increment = Integer.parseInt(properties
                 .getProperty(PROPERTY_HORIZONTAL_SCROLLBAR_UNIT_INCREMENT));
         vertical_scrollbar_unit_increment = Integer.parseInt(properties
                 .getProperty(PROPERTY_VERTICAL_SCROLLBAR_UNIT_INCREMENT));
         description_width = Integer.parseInt(properties
                 .getProperty(PROPERTY_DESCRIPTION_WIDTH));
         number_of_column = Integer.parseInt(properties
                 .getProperty(PROPERTY_NUMBER_OF_COLUMN));
         cell_outer_width = Integer.parseInt(properties
                 .getProperty(PROPERTY_CELL_OUTER_WIDTH));
         cell_outer_height = Integer.parseInt(properties
                 .getProperty(PROPERTY_CELL_OUTER_HEIGHT));
         cell_padding_width = Integer.parseInt(properties
                 .getProperty(PROPERTY_CELL_PADDING_WIDTH));
         cell_padding_height = Integer.parseInt(properties
                 .getProperty(PROPERTY_CELL_PADDING_HEIGHT));
         image_description_spacer = Integer.parseInt(properties
                 .getProperty(PROPERTY_IMAGE_DESCRIPTION_SPACER));
         unselected_item_background_color = properties
                 .getProperty(PROPERTY_UNSELECTED_ITEM_BACKGROUND_COLOR);
         selected_item_background_color = properties
                 .getProperty(PROPERTY_SELECTED_ITEM_BACKGROUND_COLOR);
         temporary_directory = properties
                 .getProperty(PROPERTY_TEMPORARY_DIRECTORY);
         authorized_filename_extensions = new ArrayList<String>(
                 Arrays.asList(properties.getProperty(
                                 PROPERTY_AUTHORIZED_FILENAME_EXTENSIONS).split(",")));
         image_error_file_path = properties
                 .getProperty(PROPERTY_IMAGE_ERROR_FILE_PATH);
      }
   }

   public static void saveProperties() {
      try {
         properties.setProperty(PROPERTY_IMAGE_WIDTH, "" + image_width);

         properties.setProperty(PROPERTY_IMAGE_HEIGHT, "" + image_height);

         properties.setProperty(
                 PROPERTY_HORIZONTAL_SCROLLBAR_UNIT_INCREMENT, ""
                 + horizontal_scrollbar_unit_increment);

         properties.setProperty(PROPERTY_VERTICAL_SCROLLBAR_UNIT_INCREMENT,
                 "" + vertical_scrollbar_unit_increment);

         properties.setProperty(PROPERTY_DESCRIPTION_WIDTH, ""
                 + description_width);

         properties.setProperty(PROPERTY_NUMBER_OF_COLUMN, ""
                 + number_of_column);

         properties.setProperty(PROPERTY_CELL_OUTER_WIDTH, ""
                 + cell_outer_width);

         properties.setProperty(PROPERTY_CELL_OUTER_HEIGHT, ""
                 + cell_outer_height);

         properties.setProperty(PROPERTY_CELL_PADDING_WIDTH, ""
                 + cell_padding_width);

         properties.setProperty(PROPERTY_CELL_PADDING_HEIGHT, ""
                 + cell_padding_height);

         properties.setProperty(PROPERTY_IMAGE_DESCRIPTION_SPACER, ""
                 + image_description_spacer);

         properties.setProperty(PROPERTY_UNSELECTED_ITEM_BACKGROUND_COLOR,
                 unselected_item_background_color);

         properties.setProperty(PROPERTY_SELECTED_ITEM_BACKGROUND_COLOR,
                 selected_item_background_color);

         properties.setProperty(PROPERTY_TEMPORARY_DIRECTORY,
                 temporary_directory);

         StringBuffer sb = new StringBuffer();
         for (int i = 0; i < authorized_filename_extensions.size(); ++i) {
            sb.append(authorized_filename_extensions.get(i));
            if (i != authorized_filename_extensions.size() - 1) {
               sb.append(",");
            }
         }

         FileWriter fw = new FileWriter(PROPERTIES_FILEPATH);
         properties.store(fw, PROPERTIES_COMMENTS);
         fw.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public static int getDescription_width() {
      if (properties == null) {
         loadProperties();
      }
      return description_width;
   }

   public static void setDescription_width(int description_width) {
      if (properties == null) {
         loadProperties();
      }
      GalateeProperties.description_width = description_width;
   }

   public static int getNumber_of_column() {
      if (properties == null) {
         loadProperties();
      }
      return number_of_column;
   }

   public static void setNumber_of_column(int number_of_column) {
      if (properties == null) {
         loadProperties();
      }
      GalateeProperties.number_of_column = number_of_column;
   }

   public static int getCell_outer_width() {
      if (properties == null) {
         loadProperties();
      }
      return cell_outer_width;
   }

   public static void setCell_outer_width(int cell_outer_width) {
      if (properties == null) {
         loadProperties();
      }
      GalateeProperties.cell_outer_width = cell_outer_width;
   }

   public static int getCell_outer_height() {
      if (properties == null) {
         loadProperties();
      }
      return cell_outer_height;
   }

   public static void setCell_outer_height(int cell_outer_height) {
      if (properties == null) {
         loadProperties();
      }
      GalateeProperties.cell_outer_height = cell_outer_height;
   }

   public static int getCell_padding_width() {
      if (properties == null) {
         loadProperties();
      }
      return cell_padding_width;
   }

   public static void setCell_padding_width(int cell_padding_width) {
      if (properties == null) {
         loadProperties();
      }
      GalateeProperties.cell_padding_width = cell_padding_width;
   }

   public static int getCell_padding_height() {
      if (properties == null) {
         loadProperties();
      }
      return cell_padding_height;
   }

   public static void setCell_padding_height(int cell_padding_height) {
      if (properties == null) {
         loadProperties();
      }
      GalateeProperties.cell_padding_height = cell_padding_height;
   }

   public static int getImage_description_spacer() {
      if (properties == null) {
         loadProperties();
      }
      return image_description_spacer;
   }

   public static void setImage_description_spacer(int image_description_spacer) {
      if (properties == null) {
         loadProperties();
      }
      GalateeProperties.image_description_spacer = image_description_spacer;
   }

   public static String getUnselected_item_background_color() {
      if (properties == null) {
         loadProperties();
      }
      return unselected_item_background_color;
   }

   public static void setUnselected_item_background_color(
           String unselected_item_background_color) {
      if (properties == null) {
         loadProperties();
      }
      GalateeProperties.unselected_item_background_color = unselected_item_background_color;
   }

   public static String getSelected_item_background_color() {
      if (properties == null) {
         loadProperties();
      }
      return selected_item_background_color;
   }

   public static void setSelected_item_background_color(
           String selected_item_background_color) {
      if (properties == null) {
         loadProperties();
      }
      GalateeProperties.selected_item_background_color = selected_item_background_color;
   }

   public static int getImage_width() {
      if (properties == null) {
         loadProperties();
      }
      return image_width;
   }

   public static void setImage_width(int image_width) {
      if (properties == null) {
         loadProperties();
      }
      GalateeProperties.image_width = image_width;
   }

   public static int getImage_height() {
      if (properties == null) {
         loadProperties();
      }
      return image_height;
   }

   public static void setImage_height(int image_height) {
      if (properties == null) {
         loadProperties();
      }
      GalateeProperties.image_height = image_height;
   }

   public static int getHorizontal_scrollbar_unit_increment() {
      if (properties == null) {
         loadProperties();
      }
      return horizontal_scrollbar_unit_increment;
   }

   public static void setHorizontal_scrollbar_unit_increment(
           int horizontal_scrollbar_unit_increment) {
      if (properties == null) {
         loadProperties();
      }
      GalateeProperties.horizontal_scrollbar_unit_increment = horizontal_scrollbar_unit_increment;
   }

   public static int getVertical_scrollbar_unit_increment() {
      if (properties == null) {
         loadProperties();
      }
      return vertical_scrollbar_unit_increment;
   }

   public static void setVertical_scrollbar_unit_increment(
           int vertical_scrollbar_unit_increment) {
      if (properties == null) {
         loadProperties();
      }
      GalateeProperties.vertical_scrollbar_unit_increment = vertical_scrollbar_unit_increment;
   }

   public static String getTemporary_directory() {
      if (properties == null) {
         loadProperties();
      }
      return temporary_directory;
   }

   public static void setTemporary_directory(String temporary_directory) {
      if (properties == null) {
         loadProperties();
      }
      GalateeProperties.temporary_directory = temporary_directory;
   }

   public static ArrayList<String> getAuthorized_filename_extensions() {
      if (properties == null) {
         loadProperties();
      }
      return authorized_filename_extensions;
   }

   public static void setAuthorized_filename_extensions(
           ArrayList<String> authorized_filename_extensions) {
      if (properties == null) {
         loadProperties();
      }
      GalateeProperties.authorized_filename_extensions = authorized_filename_extensions;
   }

   public static String getImage_error_file_path() {
      if (properties == null) {
         loadProperties();
      }
      return image_error_file_path;
   }

}
