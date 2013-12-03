package edu.mbl.jif.imaging.nav;
// edu.mbl.jif.imaging.nav.ImageNavigator

import edu.mbl.jif.imaging.nav.dirtree.DirectoryTree;
import edu.mbl.jif.imaging.nav.dirtree.PopupMenuTree;
import edu.mbl.jif.imaging.nav.util.AbbreviatedFilePathLabel;
import edu.mbl.jif.imaging.nav.util.FavoritePathsComboBox;
import edu.mbl.jif.imaging.nav.util.IconUtils;
import edu.mbl.jif.imaging.nav.util.PropsWassup;
import edu.mbl.jif.imaging.nav.util.TextWindow;
import edu.mbl.jif.utils.Prefs;
import edu.mbl.jif.utils.StaticSwingUtils;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.imageio.spi.IIORegistry;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import net.trevize.galatee.Galatee;

/**
 *
 * ImageNavigator.java
 *
 *
 * @author Grant B. Harris
 */

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
 *
 * Open directory in tree for the selected image, right-click, OpenDirectory
 * ! Tree hunt takes forever
 *    only search down from current dir. ?
 * 
 * On change Dir, blank table and show 'Working...'
 * 
 * ### File System/Tree Navigation
 * Allow or not navigating UP in file system.
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

      
 * 
 */
public class ImageNavigator {

   private String topPath = null;
   //private boolean deep = false;
   private JFrame mainFrame;
   JPanel thumbsPanel;
   JPanel viewPanel;
   JPanel dirPanel;
   ImageNavCreator imgNavCreator;
   private DirectoryTree dirTree;

   AbbreviatedFilePathLabel labelDir;

   JToggleButton columnButton;
   JCheckBox subDirsCheck;
   JCheckBox firstOnlyChk;
   boolean allowNavUp = true;
   private PopupMenuTree pMenu;
   private Galatee currentGalatee;
   private JLabel selectedFileLabel;
   //
   PrefsDialog prefsDialog;
   private static final String PREF_STARTDIR = "startDir";
   private static final String PREF_LASTDIR = "lastDir";
   private static final String PREF_THUMBSIZE = "thumbSize";
   private static final String PREF_FAVPATHS = "favoritePaths";
   private static final String PREF_EQUALIZE = "equalize";

   private boolean isPlugin = false;  // was launched as a plugin, so don't exit on close

   static {
      //Thread.currentThread().setContextClassLoader(IJ.getClassLoader());
      //
      // ImageIO Tiff initialization...
      IIORegistry registry = IIORegistry.getDefaultInstance();
      registry.registerServiceProvider(
              new com.sun.media.imageioimpl.plugins.tiff.TIFFImageWriterSpi());
      registry.registerServiceProvider(
              new com.sun.media.imageioimpl.plugins.tiff.TIFFImageReaderSpi());
      ImageIO.scanForPlugins();
   }

   public ImageNavigator(String topPath, boolean allowNavUp) {
      this.topPath = topPath;
      this.allowNavUp = allowNavUp;
      this.isPlugin = false;
      init();
   }

   public ImageNavigator(boolean plugin, String topPath, boolean allowNavUp) {
      this.isPlugin = plugin;
      this.topPath = topPath;
      this.allowNavUp = allowNavUp;
      init();
   }

   public ImageNavigator() {
      init();
   }

   public static void main(String args[]) {
      // final String pathStart = args[0];
      final String pathStart = null; // "C:/MicroManagerData/";
      //String path = "/_Dev/_Dev_Data/TestImages/imagesIJ";
      //String path = //"C:\\_Dev\\_Dev_Data\\TestImages\\*.tif";
      //"C:/MicroManagerData/project/testdata/SMT_2012_0821_1808_1/*retardance*.tif";
      // to show only the retardance image, for columnButton page tiff datasets
      //		"C:/MicroManagerData/project/testdata/2012_08_27/*retardance*.tif";
      // =================
      // To start in specified path:

      if (pathStart != null) {
         SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               ImageNavigator explorer1 = new ImageNavigator(pathStart, true);
            }
         });
      } else {
         SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               ImageNavigator explorer = new ImageNavigator();
               // showProperties();
            }
         });
      }
   }

   public static void showProperties() {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            StringBuffer sBuff = new StringBuffer();
            TextWindow tf = new TextWindow("Properties & Preferences");
            tf.setSize(600, 600);
            tf.setLocation(200, 20);
            tf.setVisible(true);
            tf.set("PROPERTIES ------------------------\n");
            tf.append(PropsWassup.displayAllProperties("\n"));
            tf.append("\n\nEnvironment Variables -------------------------\n");
            Map<String, String> variables = System.getenv();
            for (Map.Entry<String, String> entry : variables.entrySet()) {
               String name = entry.getKey();
               String value = entry.getValue();
               tf.append(name + "=" + value + "\n");
            }
         }
      });
   }

   private void init() {
      // setting the system look and feel
//      try {
//         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//      } catch (ClassNotFoundException e) {
//         e.printStackTrace();
//      } catch (InstantiationException e) {
//         e.printStackTrace();
//      } catch (IllegalAccessException e) {
//         e.printStackTrace();
//      } catch (UnsupportedLookAndFeelException e) {
//         e.printStackTrace();
//      }

      try {
         for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
               UIManager.setLookAndFeel(info.getClassName());
               break;
            }
         }
      } catch (Exception e) {
         // If Nimbus is not available, you can set the GUI to another look and feel.
      }
      //Prefs.put(ImageNavigator.class, "key", "Value");
      System.out.println("*** ImageNavigator started ***");
      System.out.println("Initializing...");
      Prefs.setDefaultClass(ImageNavigator.class);

      // set Directory to open
      if (topPath == null) {  // User has control...
         topPath = Prefs.get(ImageNavigator.class, PREF_STARTDIR, "C:/");
         allowNavUp = true;
      }
      System.out.println("path=[" + topPath + "]");
      //
//      int numColumns = 1; //initial number of columns.
//      System.out.println("display in " + numColumns + " columns");
      imgNavCreator = new ImageNavCreator(this);
      imgNavCreator.setThumbnailSize(Prefs.getInt(ImageNavigator.class, PREF_THUMBSIZE, 64));
      pMenu = new PopupMenuTree(this);
      StaticSwingUtils.dispatchToEDTWait(new Runnable() {

         public void run() {

            // Create frame...
            mainFrame = new JFrame("Image Navigator");
            mainFrame.setIconImage(
                    IconUtils.getImageIcon("edu/mbl/jif/imaging/nav/icons/", "compass.png").getImage());
            if (isPlugin) {
               // Don't exit on close if launched as plugin...
               mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            } else {
               mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
            //  this will contain the thumbnails
            thumbsPanel = new JPanel();
            thumbsPanel.setLayout(new BorderLayout());
            thumbsPanel.setBorder(new EmptyBorder(3, 3, 3, 3));
            //
            // create Directory Tree
            dirTree = new DirectoryTree();
            dirTree.setStartIn(topPath);
            dirTree.addListener(imgNavCreator);
            dirTree.setPopup_menu(pMenu);
            // create Panel containing DirTree
            dirPanel = new JPanel();
            dirPanel.setLayout(new BorderLayout());
            dirPanel.add(dirTree, BorderLayout.CENTER);
            // add FavoritePathsComboBox
            createFavoritePathsComboBox();
            // create the thumbnails

            buildViewPanel();
            thumbsPanel.add(viewPanel, BorderLayout.SOUTH);
            //
            JSplitPane mainSplitPane = new JSplitPane();
            mainSplitPane.setLeftComponent(dirPanel);
            mainSplitPane.setRightComponent(thumbsPanel);
            mainSplitPane.setDividerLocation(250);
            mainFrame.getContentPane().add(mainSplitPane, BorderLayout.CENTER);
            //
            // add TopPanel
            JPanel topPanel = buildTopPanel();
            mainFrame.getContentPane().add(topPanel, BorderLayout.NORTH);
            //
            // TODO add gCreator.terminate();
            mainFrame.setSize(884, 750);
            mainFrame.setLocationRelativeTo(null);
            mainFrame.setVisible(true);

         }

      });
      updateComponentSizes();
      imgNavCreator.updateImageNav();
//      imgNavCreator.setColumnsAndWidth();
//      Galatee g = imgNavCreator.createNewImageNav(topPath, false);
//      if (g.getTable().getRowCount() == 0) {
//         JLabel label = new JLabel("No images");
//         thumbsPanel.add(label, BorderLayout.CENTER);
//      } else {
//         thumbsPanel.add(g, BorderLayout.CENTER);
//      }
      //
   }

   // states...
   public DirectoryTree getDirTree() {
      return dirTree;
   }

   //
   JLabel labelNoImages = new JLabel("  No images");
   JLabel labelWorking = new JLabel("  Working...");

   public void setLabelWorking() {
      thumbsPanel.removeAll();
      thumbsPanel.add(labelWorking, BorderLayout.CENTER);
   }

   void replaceGalatee(Galatee newGalatee) {
//      if (currentGalatee != null) {
//         thumbsPanel.remove(currentGalatee);
//      }
//      currentGalatee = null;
      currentGalatee = newGalatee;
      thumbsPanel.removeAll();
      if (newGalatee.getTable().getRowCount() == 0) {
         thumbsPanel.add(labelNoImages, BorderLayout.CENTER);
      } else {
         thumbsPanel.add(newGalatee, BorderLayout.CENTER);
      }
      thumbsPanel.add(viewPanel, BorderLayout.SOUTH);
//      if (currentGalatee.getTable().getRowCount() == 0) {
//         thumbsPanel.add(labelNoImages, BorderLayout.CENTER);
//      } else {
//         thumbsPanel.add(currentGalatee, BorderLayout.CENTER);
//      }
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {
            thumbsPanel.revalidate();
            thumbsPanel.repaint();
         }
      });
   }

   public void setStartDir(String dir) {
      Prefs.put(ImageNavigator.class, PREF_STARTDIR, dir);
   }

   public void setCurrentDirLabel(String dir) {
      labelDir.setText(dir);
      if(dirHasSubDirs(dir)){
         this.subDirsCheck.setEnabled(true);
         this.firstOnlyChk.setEnabled(true);                 
      } else {
         this.subDirsCheck.setEnabled(false);
         this.firstOnlyChk.setEnabled(false);                 
      }
      //single.setSelected(true);
      Prefs.put(ImageNavigator.class, PREF_LASTDIR, dir);
   }
   
   private boolean dirHasSubDirs(String dir) {
      File f = new File(dir);
      File[] files = f.listFiles();
      for (File file : files) {
         if(file.isDirectory()) return true;
      }   
      return false;
   }

   public void setSelectedFileLabel(String file) {
      this.selectedFileLabel.setText(file);
   }
   //<editor-fold defaultstate="collapsed" desc="Progress Dialog">
   //ModalProgressMonitor dialog;

//   public void showBusyDialog() {
//      System.out.println("showBusyDialog, EDT: " + SwingUtilities.isEventDispatchThread());
//      dialog = new ModalProgressMonitor((Component) mainFrame, "Message", "Note",
//              0, 100);
//      dialog.setIndeterminate(true);
//      dialog.setProgress(1);
//   }
//
//   public void closeBusyDialog() {
//      java.awt.EventQueue.invokeLater(new Runnable() {
//         public void run() {
//            dialog.close();
//         }
//      });
//   }
   //</editor-fold>
   // Build the top panel that has the Sub-Dir, firstOnly,
   private JPanel buildTopPanel() {
      JPanel topPanel = new JPanel();
      topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
      topPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
      if (allowNavUp) {
         JButton buttonDiskdrive = new JButton();
         buttonDiskdrive.setPreferredSize(new Dimension(28, 28));
         buttonDiskdrive.setIcon(IconUtils.getImageIcon("edu/mbl/jif/imaging/nav/icons/",
                 "diskdrive.png"));
         buttonDiskdrive.setToolTipText("Select a different disk / volume.");
         buttonDiskdrive.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               selectDiskdrive();
            }
         });
         topPanel.add(buttonDiskdrive);
         topPanel.add(Box.createRigidArea(new Dimension(8, 0)));
         JButton buttonUpFolder = new JButton();
         buttonUpFolder.setPreferredSize(new Dimension(28, 28));
         buttonUpFolder.setToolTipText("Go up one level");
         buttonUpFolder.setIcon(IconUtils.getImageIcon("edu/mbl/jif/imaging/nav/icons/",
                 "folderUp.png"));
         buttonUpFolder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               goUpOneFolderLevel();
            }
         });
         topPanel.add(buttonUpFolder);
         topPanel.add(Box.createRigidArea(new Dimension(8, 0)));

      }
      //labelDir = new JLabel(this.topPath);
      labelDir = new AbbreviatedFilePathLabel(this.topPath, 40);
      labelDir.setPreferredSize(new Dimension(460, 32));
      topPanel.add(labelDir);
      //topPanel.add(Box.createRigidArea(new Dimension(8, 0)));
      topPanel.add(Box.createHorizontalGlue());
      //
      // ImageType CommboBox
//      JComboBox fileTypeComboBox = new JComboBox();
//      ComboBoxModel cbModel = new DefaultComboBoxModel(ImageFileTypes.values());
//      fileTypeComboBox.setModel(cbModel);
//      fileTypeComboBox.addActionListener(new ActionListener() {
//         @Override
//         public void actionPerformed(ActionEvent evt) {
//            JComboBox cb = (JComboBox) evt.getSource();
//            ImageFileTypes fileType = (ImageFileTypes) cb.getSelectedItem();
//            switch (fileType) {
//               case OME_TIF:
//               //processing code...
//            }
//         }
//      });
//      fileTypeComboBox.setMaximumSize(new Dimension(90, 24));
//      topPanel.add(fileTypeComboBox);
//      topPanel.add(Box.createRigidArea(new Dimension(8, 0)));
      
      //
      // SubDirectories...
      subDirsCheck = new JCheckBox("Sub-directories");
      subDirsCheck.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent e) {
            imgNavCreator.setRecurseSubDirs(subDirsCheck.isSelected());
            firstOnlyChk.setEnabled(subDirsCheck.isSelected());
            //single.setSelected(true);
            imgNavCreator.updateImageNav();
         }
      });
      topPanel.add(subDirsCheck);
      topPanel.add(Box.createRigidArea(new Dimension(8, 0)));
      //
      // First Only...
      firstOnlyChk = new JCheckBox("First only");
      firstOnlyChk.setSelected(true);
      imgNavCreator.setFirstOnly(firstOnlyChk.isSelected());
      firstOnlyChk.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent e) {
            imgNavCreator.setFirstOnly(firstOnlyChk.isSelected());
            //single.setSelected(true);
            imgNavCreator.updateImageNav();
         }
      });
      firstOnlyChk.setEnabled(false);
      topPanel.add(firstOnlyChk);
      topPanel.add(Box.createRigidArea(new Dimension(8, 0)));
      // Watch...
      final JCheckBox watch = new JCheckBox("Watch");
      watch.setToolTipText("Watch directories for changes");
      watch.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent e) {
            imgNavCreator.setWatchingPath(watch.isSelected());
            imgNavCreator.updateImageNav();
         }
      });
      topPanel.add(watch);
      topPanel.add(Box.createRigidArea(new Dimension(10, 0)));

      //
      prefsDialog = new PrefsDialog(this, mainFrame, true);
      JButton prefsButton = new JButton();
      prefsButton.setIcon(IconUtils.getImageIcon("edu/mbl/jif/imaging/nav/icons/", "settings16.png"));
      prefsButton.setToolTipText("Settings and Preferences");
      topPanel.add(prefsButton);
      prefsButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            //currentGalatee.preferences_dialog.setVisible(true);
            java.awt.EventQueue.invokeLater(new Runnable() {
               public void run() {
                  prefsDialog.setLocationRelativeTo(mainFrame);
                  prefsDialog.setVisible(true);
               }
            });
         }
      });
      topPanel.add(Box.createRigidArea(new Dimension(10, 0)));
      return topPanel;
   }

   // Build the View control panel, goes at the bottom
   private JPanel buildViewPanel() {
      viewPanel = new JPanel();
      viewPanel.setLayout(new BoxLayout(viewPanel, BoxLayout.LINE_AXIS));
      viewPanel.setBorder(BorderFactory.createEtchedBorder());
      //viewPanel.add(Box.createHorizontalGlue());
      // add Equalize Histogram checkbox
      final JCheckBox eqHist = new JCheckBox("Equalize");
      eqHist.setToolTipText("Equalize histogram for thumbnail display ");
      boolean equalize = Prefs.getBoolean(ImageNavigator.class, PREF_EQUALIZE, false);
      eqHist.setSelected(equalize);
      imgNavCreator.setEqualizeHisto(equalize);
      imgNavCreator.setEqualizeHisto(eqHist.isSelected());
      eqHist.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent e) {
            imgNavCreator.setEqualizeHisto(eqHist.isSelected());
            Prefs.put(ImageNavigator.class, PREF_EQUALIZE, eqHist.isSelected());
            imgNavCreator.updateImageNav();
         }
      });
      viewPanel.add(eqHist);
      viewPanel.add(Box.createRigidArea(new Dimension(8, 0)));
      //
      // add Column or Grid toggle
      columnButton = new JToggleButton();
      columnButton.setToolTipText("View as single column with descriptions");
      columnButton.setPreferredSize(new Dimension(32, 24));
      columnButton.setIcon(IconUtils.getImageIcon("edu/mbl/jif/imaging/nav/icons/", "table.png"));
      JToggleButton gridButton = new JToggleButton();
      gridButton.setToolTipText("View as a grid with no descriptions");
      gridButton.setPreferredSize(new Dimension(32, 24));
      gridButton.setIcon(IconUtils.getImageIcon("edu/mbl/jif/imaging/nav/icons/", "imageTable.gif"));
      gridButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            imgNavCreator.setView(ImageNavCreator.GRID);
            Prefs.put(ImageNavigator.class, "view", ImageNavCreator.GRID);
            imgNavCreator.updateImageNav();
         }
      });
      columnButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            imgNavCreator.setView(ImageNavCreator.TABLE);
            Prefs.put(ImageNavigator.class, "view", ImageNavCreator.TABLE);
            imgNavCreator.updateImageNav();
         }
      });
      ButtonGroup buttons = new ButtonGroup();
      buttons.add(gridButton);
      buttons.add(columnButton);
      viewPanel.add(columnButton);
      viewPanel.add(gridButton);
      int view = Prefs.getInt(ImageNavigator.class, "view", ImageNavCreator.TABLE);
      imgNavCreator.setView(view);
      columnButton.setSelected(view == ImageNavCreator.TABLE);
      viewPanel.add(Box.createRigidArea(new Dimension(8, 0)));
      // add Thumbnail Size Combobox
      String[] sizes = {"32", "64", "128", "256"};
      JComboBox thumbsizeComboBox = new JComboBox(sizes);
      thumbsizeComboBox.setMaximumSize(new Dimension(60, 24));
      thumbsizeComboBox.setSelectedItem(Integer.toString(imgNavCreator.getThumbWidth()));
      thumbsizeComboBox.addActionListener(new ImageSizeListener());
      viewPanel.add(thumbsizeComboBox);
      viewPanel.add(Box.createRigidArea(new Dimension(8, 0)));
      //
      selectedFileLabel = new JLabel();
      viewPanel.add(selectedFileLabel);
      viewPanel.add(Box.createRigidArea(new Dimension(8, 0)));
      return viewPanel;
   }

   private void updateComponentSizes() {
      //
      FontMetrics fm = labelDir.getGraphics().getFontMetrics();
      int charWidth = fm.stringWidth("x");
      int width = labelDir.getWidth();
      labelDir.setLength(width / charWidth);

   }

   private void goUpOneFolderLevel() {
      if (topPath == null) {
         return;
      }
      File file = new File(topPath);
      String parent = file.getParent();
      subDirsCheck.setSelected(false);
//      imgNavCreator.setRecurseSubDirs(deep.isSelected());
//            firstOnlyChk.setEnabled(deep.isSelected());
      setDefaultTopPath(parent);
      //dirTree.setStartIn(parent);
      //topPath = parent;
   }

   private void selectDiskdrive() {
      File[] drives = File.listRoots();
      for (int i = 0; i < drives.length; i++) {
         System.out.println(drives[i]);
      }
   }

   public void setDefaultTopPath(String path) {
      dirTree.setStartIn(path);
      topPath = path;
      setStartDir(path);
   }

   FavoritePathsComboBox favsCombo;

   public void createFavoritePathsComboBox() {
      List<String> favPaths = Prefs.getList(ImageNavigator.class, PREF_FAVPATHS);
      favsCombo = new FavoritePathsComboBox(favPaths, 30);
      dirPanel.add(favsCombo, BorderLayout.NORTH);
      favsCombo.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FavoritePathsComboBox cb = (FavoritePathsComboBox) e.getSource();
            String path = (String) cb.getSelectedItem();
            setDefaultTopPath(path);
            imgNavCreator.directorySelected(new File(path));
            // select this node... 
         }
      });
   }

   public void addToFavoritePaths(String str) {
      List<String> favPaths = Prefs.getList(ImageNavigator.class, PREF_FAVPATHS);
      favPaths.add(str);
      Prefs.put(ImageNavigator.class, PREF_FAVPATHS, favPaths);
      dirPanel.remove(favsCombo);
      createFavoritePathsComboBox();
   }

   

   class ImageSizeListener implements ActionListener {

      public void actionPerformed(ActionEvent e) {
         JComboBox cb = (JComboBox) e.getSource();
         String size = (String) cb.getSelectedItem();
         if (size.equalsIgnoreCase("32")) {
            setThumbnailSize(32);
         }
         if (size.equalsIgnoreCase("64")) {
            setThumbnailSize(64);
         }
         if (size.equalsIgnoreCase("128")) {
            setThumbnailSize(128);
         }
         if (size.equalsIgnoreCase("256")) {
            setThumbnailSize(256);
         }
      }
   }

   private void setThumbnailSize(int i) {
      imgNavCreator.setThumbnailSize(i);
      Prefs.put(ImageNavigator.class, PREF_THUMBSIZE, i);
      imgNavCreator.updateImageNav();
   }

}
