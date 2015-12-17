package edu.mbl.jif.imaging.nav;
// edu.mbl.jif.imaging.nav.ImageNavigator

import edu.mbl.jif.gui.DialogBox;
import edu.mbl.jif.imaging.nav.dirtree.DirectoryTree;
import edu.mbl.jif.imaging.nav.dirtree.PopupMenuTree;
import edu.mbl.jif.imaging.nav.util.AbbreviatedFilePathLabel;
import edu.mbl.jif.imaging.nav.util.ArrayStackOfStrings;
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
import java.io.IOException;
import java.util.ArrayList;
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
public class ImageNavigator {

   // startup options...
   private String startPath = null;
   private String topPath = null;
   private boolean allowNavUp = true;
   private boolean isPlugin = false;  // was launched as a plugin, so don't exit on close
   private boolean doNotOpen = false; // instead use FileChoosenListener on 'open'
   private List<FileChoosenListener> fileChoosenListeners
           = new ArrayList<FileChoosenListener>();
   //private boolean deep = false;
   // UI components
   private JFrame mainFrame;
   JPanel thumbsPanel;
   JPanel viewPanel;
   JPanel dirPanel;
   JLabel labelNoImages = new JLabel("  No images");
   JLabel labelWorking = new JLabel("  Working...");

   private DirectoryTree dirTree;
   AbbreviatedFilePathLabel labelDir;
   JToggleButton columnButton;
   JCheckBox subDirsCheck;
   JCheckBox firstOnlyChk;
   private PopupMenuTree pMenu;
   private JLabel selectedFileLabel;
   FavoritePathsComboBox favsCombo;
   //
   ImageNavCreator imgNavCreator;
   private Galatee currentGalatee;
   //
   private List<DirectoryChoosenListener> directoryChoosenListeners
           = new ArrayList<DirectoryChoosenListener>();
   //
   ArrayStackOfStrings pathStack = new ArrayStackOfStrings(10);
   // Preferences - persisted values
   PrefsDialog prefsDialog;
   private static final String PREF_STARTDIR = "startDir";
   private static final String PREF_LASTDIR = "lastDir";
   private static final String PREF_THUMBSIZE = "thumbSize";
   private static final String PREF_FAVPATHS = "favoritePaths";
   private static final String PREF_EQUALIZE = "equalize";

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

   public ImageNavigator(String startPath, boolean allowNavUp) {
      this.startPath = startPath;
      this.allowNavUp = allowNavUp;
      this.isPlugin = false;
      init();
      show();
   }

   public ImageNavigator(boolean plugin, String startPath, boolean allowNavUp) {
      this.isPlugin = plugin;
      this.startPath = startPath;
      this.allowNavUp = allowNavUp;
      init();
      show();
   }

   public ImageNavigator(boolean plugin, String startPath,
           boolean allowNavUp, FileChoosenListener fileListener) {
      this.isPlugin = plugin;
      this.startPath = startPath;
      this.allowNavUp = allowNavUp;
      this.doNotOpen = true;
      this.addFileChoosenListener(fileListener);
      init();
      show();
   }

   public ImageNavigator(boolean plugin,
           String startPath,
           boolean allowNavUp,
           boolean subDirs,
           FileChoosenListener fileListener,
           DirectoryChoosenListener dirListener) {

      this.isPlugin = plugin;
      this.startPath = startPath;
      this.allowNavUp = allowNavUp;
      if (fileListener != null) {
         this.doNotOpen = true;
         this.addFileChoosenListener(fileListener);
      }
      if (dirListener != null) {
         this.addDirectoryChoosenListener(dirListener);
      }
      init();
      if (subDirs) {
         subDirsCheck.setSelected(true);
         imgNavCreator.setRecurseSubDirs(true);
         firstOnlyChk.setEnabled(true);
         // imgNavCreator.updateImageNav();
      }
      show();
   }

   public ImageNavigator() {
      allowNavUp = true;
      init();
      show();
   }

   // Launched from command line...
   public static void main(String args[]) {
      // final String pathStart = args[0];
      //final String pathStart = null;
      final String pathStart = "C:/MicroManagerData/";
      //String path = //"C:\\_Dev\\_Dev_Data\\TestImages\\*.tif";
      //"C:/MicroManagerData/project/testdata/SMT_2012_0821_1808_1/*retardance*.tif";
      // to show only the retardance image, for columnButton page tiff datasets
      //		"C:/MicroManagerData/project/testdata/2012_08_27/*retardance*.tif";
      // =================
      // To start in specified path:
      if (pathStart != null) {
         SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               //ImageNavigator nav = new ImageNavigator(pathStart, true);
               ImageNavigator nav = new ImageNavigator(false, pathStart, true, true, null, null);
            }
         });
      } else {
         SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               ImageNavigator nav = new ImageNavigator();

               // showProperties();
            }
         });
      }
   }

   public void init() {
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
      if (!isPlugin) {
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
      }
      //Prefs.put(ImageNavigator.class, "key", "Value");
      System.out.println("*** ImageNavigator started ***");
      System.out.println("Initializing...");
      Prefs.setDefaultClass(ImageNavigator.class);

      // set Directory to open
      if (startPath == null) {  // User has control...
         startPath = Prefs.get(ImageNavigator.class, PREF_STARTDIR, "C:/");
         //   allowNavUp = true;
      }
      System.out.println("path=[" + startPath + "]");
      //
//      int numColumns = 1; //initial number of columns.
//      System.out.println("display in " + numColumns + " columns");
      imgNavCreator = new ImageNavCreator(this, doNotOpen);
      imgNavCreator.setThumbnailSize(Prefs.getInt(ImageNavigator.class, PREF_THUMBSIZE, 64));
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
      dirTree.setStartIn(startPath);
      dirTree.addListener(imgNavCreator);
      // Add popup menu
      pMenu = new PopupMenuTree(ImageNavigator.this);
      dirTree.setPopup_menu(pMenu);
      // create Panel containing DirTree
      dirPanel = new JPanel();
      dirPanel.setLayout(new BorderLayout());
      dirPanel.add(dirTree, BorderLayout.CENTER);
      // add FavoritePathsComboBox
      createFavoritePathsComboBox();
      // add the view control panel
      viewPanel = buildViewPanel();
      thumbsPanel.add(viewPanel, BorderLayout.SOUTH);
      //
      JSplitPane mainSplitPane = new JSplitPane();
      mainSplitPane.setLeftComponent(dirPanel);
      mainSplitPane.setRightComponent(thumbsPanel);
      mainSplitPane.setDividerLocation(250);
      mainFrame.getContentPane().add(mainSplitPane, BorderLayout.CENTER);
      // add TopPanel
      JPanel topPanel = buildTopPanel();
      mainFrame.getContentPane().add(topPanel, BorderLayout.NORTH);
      //
      // TODO add gCreator.terminate();
      mainFrame.setSize(884, 750);
      mainFrame.setLocationRelativeTo(null);
   }

   public void show() {
      StaticSwingUtils.dispatchToEDTWait(new Runnable() {
         public void run() {
            mainFrame.setVisible(true);
            imgNavCreator.directorySelected(new File(startPath));
         }
      });
      updateComponentSizes();
   }

   // Build the top panel that has the Sub-Dir, firstOnly,
   private JPanel buildTopPanel() {
      JPanel topPanel = new JPanel();
      topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
      topPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
      if (allowNavUp) {
         // Disk drive select button
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
         // Up Level button
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
         // Go Back button
         JButton goBackButton = new JButton();
         goBackButton.setPreferredSize(new Dimension(28, 28));
         goBackButton.setToolTipText("Go back to prior tree");
         goBackButton.setIcon(IconUtils.getImageIcon("edu/mbl/jif/imaging/nav/icons/",
                 "goback.png"));
         goBackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               goBackToPriorTreeRoot();
            }
         });
         topPanel.add(goBackButton);
         topPanel.add(Box.createRigidArea(new Dimension(8, 0)));

      }
      //labelDir = new JLabel(this.topPath);
      labelDir = new AbbreviatedFilePathLabel(this.startPath, 40);
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
      // SubDirectories Checkbox...
      subDirsCheck = new JCheckBox("Sub-directories");
      subDirsCheck.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent e) {
            imgNavCreator.setRecurseSubDirs(subDirsCheck.isSelected());
            firstOnlyChk.setEnabled(subDirsCheck.isSelected());
            imgNavCreator.updateImageNav();
         }
      });
      topPanel.add(subDirsCheck);
      topPanel.add(Box.createRigidArea(new Dimension(8, 0)));
      //
      // First Only Checkbox...
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
      // Watch... Watch changes in Path Checkbox
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
      // Preferences Dialog button
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
      JPanel viewPanel = new JPanel();
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

   public DirectoryTree getDirTree() {
      return dirTree;
   }

   //
   public void setLabelWorking() {
      thumbsPanel.removeAll();
      thumbsPanel.add(labelWorking, BorderLayout.CENTER);
   }

   public void setCurrentDirLabel(String dir) {
      labelDir.setText(dir);
      if (dirHasSubDirs(dir)) {
         subDirsCheck.setEnabled(true);
         if (subDirsCheck.isSelected()) {
            firstOnlyChk.setEnabled(true);
         }
      } else {
         subDirsCheck.setEnabled(false);
         firstOnlyChk.setEnabled(false);
      }
      //single.setSelected(true);
      Prefs.put(ImageNavigator.class, PREF_LASTDIR, dir);
   }

   public void setSelectedFileLabel(String file) {
      this.selectedFileLabel.setText(file);
   }

   //<editor-fold defaultstate="collapsed" desc="Path Changing Stuff">
   private void goUpOneFolderLevel() {
      if (startPath == null) {
         return;
      }
      // TODO Don't allow nav above topPath...
      File file = new File(startPath);
      if (file.getParentFile() == null) {
         return;
      }
      //if(isRoot(file.getParentFile())) return ;
      String parent = file.getParent();
      subDirsCheck.setSelected(false);
//      imgNavCreator.setRecurseSubDirs(deep.isSelected());
//            firstOnlyChk.setEnabled(deep.isSelected());
      setDefaultTopPath(parent, true);
      //dirTree.setStartIn(parent);
      //topPath = parent;
   }

   private void goBackToPriorTreeRoot() {
      if (!pathStack.isEmpty()) {
         String path = pathStack.pop();
         setDefaultTopPath(path, true);
      }
   }

   private void selectDiskdrive() {
      File[] drives = File.listRoots();
      String[] drvs = new String[drives.length];
      for (int i = 0; i < drives.length; i++) {
         System.out.println(drives[i]);
         try {
            drvs[i] = drives[i].getCanonicalPath();
         } catch (IOException ex) {
            ex.printStackTrace();
         }
      }
      String newDrive = DialogBox.boxSelectFromList(this.mainFrame, "Select disk", "Disk", drvs);
      subDirsCheck.setSelected(false);
      setDefaultTopPath(newDrive, true);
   }

   public void setDefaultTopPath(String path, boolean pushToPathStack) {
      dirTree.setStartIn(path);
      if (pushToPathStack) {
         pathStack.push(startPath);
      }
      startPath = path;
      setStartDir(path);
   }

   private boolean dirHasSubDirs(String dir) {
      File f = new File(dir);
      File[] files = f.listFiles();
      try {
         for (File file : files) {
            if (file.isDirectory()) {
               return true;
            }
         }
      } catch (Exception e) {
      }
      return false;
   }

   public void createFavoritePathsComboBox() {
      List<String> favPaths = Prefs.getList(ImageNavigator.class, PREF_FAVPATHS);
      favsCombo = new FavoritePathsComboBox(favPaths, 30);
      dirPanel.add(favsCombo, BorderLayout.SOUTH);
      favsCombo.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            FavoritePathsComboBox cb = (FavoritePathsComboBox) e.getSource();
            String path = (String) cb.getSelectedItem();
            setDefaultTopPath(path, true);
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

//</editor-fold>
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

   //<editor-fold defaultstate="collapsed" desc="Directory/File Choosen Listeners">
   // Directory and File Choosen Listener Management
   public void addFileChoosenListener(FileChoosenListener l) {
      fileChoosenListeners.add(l);
   }

   protected void notifyFileChoosenListeners(File file) {
      for (FileChoosenListener fileChoosenListener : fileChoosenListeners) {
         fileChoosenListener.fileChoosen(file);
      }
   }

   public void addDirectoryChoosenListener(DirectoryChoosenListener l) {
      directoryChoosenListeners.add(l);
   }

   protected void notifyDirectoryChoosenListeners(File file) {
      for (DirectoryChoosenListener directoryChoosenListener : directoryChoosenListeners) {
         directoryChoosenListener.directoryChoosen(file);
      }
   }
   //</editor-fold>

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
}
