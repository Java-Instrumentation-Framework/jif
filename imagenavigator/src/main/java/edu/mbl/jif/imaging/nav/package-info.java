
package edu.mbl.jif.imaging.nav;


/*
 * ========== To Do: ==============================================
 * TODO: 
 * MMgr Plugin
 * Packaging
 * 
 * 
 * Launcher, shortcut
 * Dependencies
 *   (without JAI support)
 * 
    [ ] What if Startup path doesn't exist?

 ?? How to prevent endless hunt when SubDirs is on and the dir level is too high....
 ?? Limit hunt depth

 *  Ellimate Galatee.properties.
 *  Prefs: Persist these: (uses Prefs from IJ2)
 *   frame bound 
 *   Start in root or default dir.
 *   Persist last used dir for restart
 *   Enhance, View, SubDirs. firstOnly, thumbSize  
 * 
 *  [ ] Remember frame size

 * Add ProgressMonitor... 
 * 
 * [ ] Set # of columns in grid view based on panel width, update on change of size.

 * [ ] Popup on file, add copy path, copy, delete
 Popup Menu on Image - copy path to sys. clipboard
 *
 * Open directory in tree for the selected image, right-click, OpenDirectory
 * ! Tree hunt takes forever
 *    only search down from current dir. ?
 * 
 * On change Dir, blank table and show 'Working...'
 * 
 * ### File System/Tree Navigation
 Allow or not navigating UP in file system. (GoUpOneFolderLevel)
 SubDirs is disabled on GoUpOneFolderLevel

   
 *    .. Button, roots Button
 * [x] Show path relative to current dir. for children (when recursive)
 * [ ] Allow Select DiskDrive (root) selection not done.
 * [ ]+ Open Explorer/FileFinder here...
 * Sorting - by Name or Date of file - not gonna do.
 * Filtering for both Dir and Files with wildcards 
 * Choose Image Types: All Images, .tif, .ome.tif
 *
 [ }+ Drag-and-drop from DirTree... ?
 *

 * ### Opening Images
 *    If MMgr, try to open with Micro-Manager (assuming it is running.)
 *    Open MMgr Datasets, isMMDataset() - presence of metadata.txt or file with .ome.tif 
 *    Otherwise, uses ImageJ.
 *    If all else fails, trys with ImageIO.
 * 
 [ }+ Add option to run a plugin after opening a certain type.

 * Default ImageOpener 
 *    Bundle ImageIO for Tiff
 * 
 * Popup for Opening with spec. app is not implemented
 * Add other app. launchers for other file types ...
 * 
 
 * 
 * If "Find Micro-Manager Datasets",  * Open dataset, get summary metadata and firstOnlyChk image.
 * Add 'Show Metadata' checkbox
 *    Read OME metadata...
 * ++ Copy/Move with anticedents...
 * 
 * 
 * Add acknowlegement to Galatee
 *
 * Later... 
 * Add dragAndDrop
 * Add Checkboxes to choose multiple images to operate on 
 *
 * ========== Done: ==============================================
 * On selection of an image/item, show the path and filename...
 * -- Set Size of thumbnails32,64,128,256
 *      (In Grid view, number of columns fits in horiz space)
 * 
 * -- PopupMenu on Directory Tree, includes Copy Path (to sys. clipboard)
 
 * -- SubDirectories (recursive) checkbox
 * -- First-only checkbox (default on)
 
 * -- Views - toggle from List to Grid
 *    1) Single column with thumbnail and description (path & filename)
 *    2) Grid/Table View of thumbnails in N columns and no description.
 * 
 * 
 * -- Thumbnail display options: (eg. 16 bit) EnhanceContrast (equalize) (byte & short)
 * 
 * -- Directory/File Watching
 * Checkbox to turn on/off Watching of dir.  If Sub-Dirs selected, it also recurses.
 * PathMonitor - watch current dir. for changes (esp. during acquisition)
 * PathWatcher - While running an acquisition, it is helpful to have the ImageNavigator 
 * update the images when a file is created or deleted.
 * On Windows, it can allow watch a directory and it's subdirectories (recursive)
 * [ ] Need to test.

     
Callbacks: Pass in an implementation of 
interface FileChoosenListener or interface DirectoryChoosenListener

[ ] + Open in External Viewer (e.g. Irfanview)
 * 
Keep a history/breadcrumbs, add GoBack button.

SubDirs is turned OFF on 
   - up-level
   - change Drive

[x] Initial open, ckecking subdirs does not update the list
[x] ability to set SubDirs on initialization

[ ] ability to set notAboveDirectory, when allowNavUp 

[ ] Repaints sometimes incomplete...

And yet more features that would be nice to add (May 14):

[ ] add right-click to 'Inspect' the image/file properties, metadata, whatever...

[ ] In not Sub-dirs and no images, display number of images under this dir, without displaying, rather than just "No Images"
Aug 14

[ ] handle multi-position MMgr datasets -- needs dataset lib

[ ] Don't remove the up level button, but limit to top-level dir.

 */
