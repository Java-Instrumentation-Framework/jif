/*
 * Added by GBH March '13
 * Galatee is a Java library for exploring and searching in large image collection where images are annotated. The library is mainly developed for browsing-searching image collections and annotating images, and it is used in the DatasetExplorer project http://code.google.com/p/datasetexplorer/ .

Please send me an email at nicolas.james@gmail.com if you are using or you are interested by this library. The code is under GPL license, you can freely reusing it under the terms of the GPL license but I hope to get feedbacks.

The Galatee library provides:

- visualization of a list of images (with associated metadata): images are referenced by a URI object. Schemes of the URI can be file for a local image file, or http for an image file accessible via HTTP
* 
- textual search in the image list (based on the value of the image URI and on the image description) using a Lucene in-memory index
- downloading, in-memory loading and resizing of an image is made only when it's necessary (something like a load when you see)
- multi-threading for the downloading, loading, resizing of the images
- configuration via a Galatee.properties file:
- customize the item visualization (image size, text area size)
- cache directory for downloaded files
- implementing its own item visualization is easy (just a class to extend, the GCellPanel class)
* 
* Also see: http://njames.trevize.net/wiki/projects:galatee
* 
* Used by http://code.google.com/p/datasetexplorer/
* also here http://njames.trevize.net/wiki/projects:datasetexplorer
* 
========================================
* Galatee Keys

up:
down:
left:
right:
pg-up:
pg-dn:
home: go to first
end: go to last
enter: same as double-click

minus: reduce # columns in table
plus: increase # columns in table
*
*: Hides description
tab: nothing


ctrl-F: search
ctrl-P: Prefs

//===========================================
// Values in Galatee.properties file:
IMAGE_WIDTH=109
CELL_OUTER_HEIGHT=3
HORIZONTAL_SCROLLBAR_UNIT_INCREMENT=42
VERTICAL_SCROLLBAR_UNIT_INCREMENT=42
NUMBER_OF_COLUMN=1
CELL_OUTER_WIDTH=3
CELL_PADDING_WIDTH=1
AUTHORIZED_FILENAME_EXTENSIONS=jpg,jpeg,png,gif,svg,bmp,ppm,pgm,tif
IMAGE_DESCRIPTION_SPACER=2
UNSELECTED_ITEM_BACKGROUND_COLOR=\#EDE9E3
IMAGE_ERROR_FILE_PATH=./gfx/imageLoadingError.jpg
CELL_PADDING_HEIGHT=1
IMAGE_HEIGHT=109
TEMPORARY_DIRECTORY=/TEMP
SELECTED_ITEM_BACKGROUND_COLOR=\#9acd32
DESCRIPTION_WIDTH=400
  */
package net.trevize.galatee;
