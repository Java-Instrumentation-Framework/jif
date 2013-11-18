package net.trevize.galatee;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

/**
 *
 *
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]] Galatee.java - Apr
 * 23, 2009
 *
 * Might fix this sometime...
 * http://www.objectdefinitions.com/odblog/2011/how-to-fix-right-click-selection-and-jpopupmenu-so-your-jtree-feels-native/
 * protected void setSelectedItemsOnPopupTrigger(MouseEvent e) { int row =
 * table.rowAtPoint(e.getPoint()); boolean selected =
 * table.getSelectionModel().isSelectedIndex(row); if ( ! selected ) {
 * table.getSelectionModel().setSelectionInterval(row, row); } }
 * @author Grant B. Harris 2013
 */
public class Galatee extends JPanel implements
        ListSelectionListener, MouseListener, ComponentListener, KeyListener {

   private JTable table;
   private _TableModel model;
   private _TableCellRenderer renderer;
   private _TableCellEditor editor;
   private Vector<GItem> gitems;
   private int nbcol;
   private int selectedrow, selectedcol;
   private ImageLoaderThread ilt;
   public static BufferedImage imageLoadingError;
   private Vector<URI> v_uri;
   private Vector<Vector<Object>> data;
   private EventQueue eq;
   private Vector<GListener> listeners;
   private JScrollPane sp;
   //if customized imageLoadingError. (not used by default).
   private String imageLoadingErrorPath = "./gfx/imageLoadingError.jpg";
   //if customized imageLoadingError and application packed in an executable JAR.
   private URL imageLoadingErrorUrl = getClass().getClassLoader().getResource(
           imageLoadingErrorPath);
   //default value for the image thumbnail dimension.
   private Dimension gitemImageDimension = new Dimension(GalateeProperties
           .getImage_width(), GalateeProperties
           .getImage_height());
   //default value for the text description width.
   private int gitemDescriptionWidth = GalateeProperties
           .getDescription_width();
   private HashMap<String, URI> uri_index;
   //private SearchPanel search_panel;
   private boolean keep_selected_cell_visible;
   private JPopupMenu popup_menu = new JPopupMenu();
   private boolean description_visible;
   public PreferencesDialog preferences_dialog;
   private final boolean equalizeHisto;

   public Galatee(Vector<URI> v_uri, Vector<Vector<Object>> data,
           Dimension imagesDim,
           int descriptionWidth,
           int nbcol) {
      this(v_uri, data, imagesDim, descriptionWidth, nbcol, true, false);
   }

   public Galatee(Vector<URI> v_uri, Vector<Vector<Object>> data,
           Dimension imagesDim,
           int descriptionWidth,
           int nbcol, boolean showDescription, boolean equalizeHisto) {

      super();

      /*
       * If using a customized image for the imageLoadingError, use the following code.
       * However, this class is primarily coded to be used from a packed .jar,
       * (a library), and accessing images in this case is tricky, so
       * it's better to not use a real image and rather a built image in
       * java, see the net.trevize.ImageLoadingError class.
       */

      //to lauch directly (not from an executable jar).
      /*
       try {
       imageLoadingError = ImageIO.read(new File(imageLoadingErrorPath));
       } catch (IOException e) {
       e.printStackTrace();
       }
       */

      //for launching from an executable jar.
	/*
       try {
       imageLoadingError = ImageIO.read(imageLoadingErrorUrl);
       } catch (IOException e) {
       e.printStackTrace();
       }
       */

      /*
       * if build a BufferedImage for the imageLoadingError.
       */

      imageLoadingError = ImageLoadingError.getImageLoadingError(imagesDim);

      this.v_uri = v_uri;
      this.data = data;
      this.equalizeHisto = equalizeHisto;
      gitemImageDimension = imagesDim;
      gitemDescriptionWidth = descriptionWidth;

      setNumberOfColumns(nbcol);

      setLayout(new BorderLayout());
      setBorder(new EmptyBorder(0, 0, 0, 0));

      //setting the jtable.
      table = new JTable();
      model = new _TableModel();

      // filling the model.
      gitems = new Vector<GItem>();
      fillModel();
      //setting the model.
      table.setModel(model);
      //setting the renderer and the editor.
      renderer = new _TableCellRenderer(this, gitemImageDimension, gitemDescriptionWidth);
      editor = new _TableCellEditor(this, gitemImageDimension, gitemDescriptionWidth);

      table.setTableHeader(null); //no table header.
      table.setCellSelectionEnabled(true); //authorize cell selection.
      // table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
      table.setDragEnabled(false); //dragging GItem is not authorized.
      /*
       * When the setShowGrid(false) is noticed, the pixels reserved for the grid is still displayed,
       * but not colored with the grid's color, these pixels are actually pixels of the JTable's background.
       * 
       * Furthermore, the completely surprising thing is the way used to paint the grid: each vertical line of 
       * the grid is a rigth-border of a column, i.e. there are pixels reserved for the grid at the last right column,
       * it's very ugly...  
       * 
       * The solution to not have a ugly pixel after the last column is to put the intercellspacing to zero.
       */
      table.setShowGrid(false);
      table.setIntercellSpacing(new Dimension(0, 0));
      table.setRowHeight(renderer.getItemHeight());
      table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
      // at starting time there is any selected cells.
      selectedrow = selectedcol = -1;
      // setting the selection model and the selection mode
      table.setSelectionModel(new DefaultListSelectionModel());
      table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      table.getColumnModel().setSelectionModel(new DefaultListSelectionModel());
      table.getColumnModel().getSelectionModel().
              setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      //setting the JTable's listeners.
      table.getSelectionModel().addListSelectionListener(this);
      table.getColumnModel().getSelectionModel().addListSelectionListener(this);
      table.addMouseListener(this);
      //
      setRenderOfColumns();
      //
      // authorize the creation of events.
      enableEvents(GEvent.GALATEE_EVENT);
      eq = Toolkit.getDefaultToolkit().getSystemEventQueue();
      listeners = new Vector<GListener>();

      //creating and starting the ImageLoaderThread.
      ilt = new ImageLoaderThread(this, gitemImageDimension.width, gitemImageDimension.height);
      ilt.start();

      //and the JTable in a JScrollPane, and the JScrollPane into this.
      sp = new JScrollPane(table);
      sp.setBorder(new LineBorder(null, 0));
      sp.setViewportView(table);
      sp.getVerticalScrollBar().setUnitIncrement(
              GalateeProperties.getVertical_scrollbar_unit_increment());
      sp.getHorizontalScrollBar().setUnitIncrement(
              GalateeProperties.getHorizontal_scrollbar_unit_increment());
      add(sp, BorderLayout.CENTER);
      mapKeys();
      sp.addComponentListener(this);
      addScrollpaneMouseListeners(sp);
//		popup_menu = new JPopupMenu();
//		popup_menu.setBorder(new MatteBorder(0, 15, 0, 0, Color.BLUE));
      setDescriptionVisible(showDescription);
      preferences_dialog = new PreferencesDialog(this);
   }  // constructor

   public void setNumberOfColumns(int numColumns) {
      /* :!: WARNING :!:
       * the indicated nbcol given in parameter to the constructor
       * is not respected if it is greater than the number of results.
       */
      if (v_uri.size() < numColumns) {
         this.nbcol = v_uri.size();
         if(this.nbcol < 1) this.nbcol = 1;
      } else {
         this.nbcol = numColumns;
      }
   }

   public void setRenderOfColumns() {
      for (int i = 0; i < nbcol; ++i) {
         TableColumn c = table.getColumnModel().getColumn(i);
         c.setCellRenderer(renderer);
         c.setCellEditor(editor);
         c.setPreferredWidth(renderer.getItemWidth());
         c.setWidth(renderer.getItemWidth());
      }
   }

   public boolean isEqualizeHisto() {
      return equalizeHisto;
   }
   
   public void enableSearchFunctionality() {
   }

   public JTable getTable() {
      return table;
   }

   public void updateGItemDimension(Dimension gitemImageDimension,
           int description_width) {
      this.gitemImageDimension = gitemImageDimension;

      //update the renderer and the editor.
      if (description_visible) {
         renderer.updateDimension(gitemImageDimension, description_width);
         editor.updateDimension(gitemImageDimension, description_width);
      } else {
         renderer.updateDimension(gitemImageDimension, 0);
         editor.updateDimension(gitemImageDimension, 0);
      }

      //update the image loader thread.
      ilt.setImageWidth(gitemImageDimension.width);
      ilt.setImageHeight(gitemImageDimension.height);

      //erase the list of ever loaded images.
      for (GItem gitem : gitems) {
         gitem.setImage(null);
      }
   }

   public ImageLoaderThread getImageLoaderThread() {
      return ilt;
   }

   public boolean isKeep_selected_cell_visible() {
      return keep_selected_cell_visible;
   }

   public void setKeep_selected_cell_visible(boolean keepSelectedCellVisible) {
      keep_selected_cell_visible = keepSelectedCellVisible;
   }

   private void fillModel() {
      //create columns in the JTable model.
      for (int i = 0; i < nbcol; ++i) {
         model.addColumn(new Vector<GItem>());
      }
      //use a local model, affect it to the JTable later.
      Vector<Vector<GItem>> columns = new Vector<Vector<GItem>>();
      for (int i = 0; i < nbcol; ++i) {
         columns.add(new Vector<GItem>());
      }
      for (int i = 0; i < v_uri.size(); ++i) {
         //loading the GItem.
         GItem gi = new GItem(v_uri.get(i));
         if (data != null) {
            //setting the data.
            gi.setData(data.get(i));
            //setting the description. <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
            String desc = data.get(i).get(0).toString();
            // 
            gi.setText(desc);
         }
         gitems.add(gi);
         //add the GItem in the model.
         columns.get(i % nbcol).add(gi);
      }
      //setting the JTable model with our local model.
      for (int i = 0; i < nbcol; ++i) {
         model.setColumn(i, columns.get(i));
      }
      Runtime.getRuntime().gc();
   }

   //Change # columns...
   private void changeModelAddColumn() {
      /*
       * we add the following condition for not adding an invisible
       * column, i.e. not adding a column if we have no data to
       * put in it.
       */
      if (nbcol == v_uri.size()) {
         return;
      }
      //save the previous nbcol for updating the selectedrow and selectedcol 
      //in the new model.
      int prev_nbcol = nbcol;
      int prev_selectedrow = table.getSelectedRow();
      int prev_selectedcol = table.getSelectedColumn();
      //adding a column to the JTable model.
      model.addColumn(new Vector<GItem>());
      //update the nbcol variable.
      nbcol++;
      //clearing the model content.
      ((Vector<Vector<GItem>>) model.getDataVector()).clear();
      //using a local model, affect it to the JTable later.
      Vector<Vector<GItem>> columns = new Vector<Vector<GItem>>();
      for (int i = 0; i < nbcol; ++i) {
         columns.add(new Vector<GItem>());
      }
      //insert data in the model.
      for (int i = 0; i < v_uri.size(); ++i) {
         GItem gi = gitems.get(i);
         columns.get(i % nbcol).add(gi);
      }
      //affect the model to the JTable model.
      for (int i = 0; i < nbcol; ++i) {
         model.setColumn(i, columns.get(i));
      }
      setRenderOfColumns();

      model.fireTableDataChanged();
      Runtime.getRuntime().gc();
      //updating the selectedrow and selectedcol.
      if (prev_selectedrow != -1 && prev_selectedcol != -1) {
         int n = prev_selectedcol + prev_nbcol * prev_selectedrow;
         table.changeSelection(n / nbcol, n % nbcol, false, false);
      }
   }

   private void changeModelRemoveColumn() {
      //we can't remove all columns.
      if (nbcol == 1) {
         return;
      }
      //save the previous nbcol for updating the selectedrow and selectedcol 
      //in the new model.
      int prev_nbcol = nbcol;
      int prev_selectedrow = table.getSelectedRow();
      int prev_selectedcol = table.getSelectedColumn();
      //remove a column to the JTable model.
      table.removeColumn(table.getColumnModel().getColumn(nbcol - 1));
      //update the nbcol variable.
      nbcol--;
      //clearing the model content.
      ((Vector<Vector<GItem>>) model.getDataVector()).clear();
      //it's mandatory to remove also the column id.
      Vector v = new Vector();
      for (int i = 0; i < nbcol; ++i) {
         v.add(model.getColumnName(i));
      }
      model.setDataVector(((Vector<Vector<GItem>>) model.getDataVector()), v);
      //using a local model, affect it to the JTable later.
      Vector<Vector<GItem>> columns = new Vector<Vector<GItem>>();
      for (int i = 0; i < nbcol; ++i) {
         columns.add(new Vector<GItem>());
      }
      //insert data in the model.
      for (int i = 0; i < v_uri.size(); ++i) {
         GItem gi = gitems.get(i);
         columns.get(i % nbcol).add(gi);
      }
      //affect the model to the JTable model.
      for (int i = 0; i < nbcol; ++i) {
         model.setColumn(i, columns.get(i));
      }
      setRenderOfColumns();

      model.fireTableDataChanged();
      Runtime.getRuntime().gc();
      //updating the selectedrow and selectedcol.
      if (prev_selectedrow != -1 && prev_selectedcol != -1) {
         int n = prev_selectedcol + prev_nbcol * prev_selectedrow;
         table.changeSelection(n / nbcol, n % nbcol, false, false);
      }
   }

   // ++ GBH --------------------------------------------------------------------
   public void setView(int numcolumns, boolean showDescription) {
      //save the previous nbcol for updating the selectedrow and selectedcol 
      //in the new model.
      int prev_nbcol = nbcol;
      int prev_selectedrow = table.getSelectedRow();
      int prev_selectedcol = table.getSelectedColumn();
      setNumberOfColumns(numcolumns);
      model = new _TableModel();
      fillModel();
      table.setModel(model);
      setRenderOfColumns();
      model.fireTableDataChanged();
      Runtime.getRuntime().gc();
      //updating the selectedrow and selectedcol.
      if (prev_selectedrow != -1 && prev_selectedcol != -1) {
         int n = prev_selectedcol + prev_nbcol * prev_selectedrow;
         table.changeSelection(n / nbcol, n % nbcol, false, false);
      }
      setDescriptionVisible(showDescription);
   }

   //------------------------------------------------------------------
   public void scrollToItem(String item_uri_value) {
      int item_number = v_uri.indexOf(uri_index.get(item_uri_value));
      scrollToItem(item_number);
   }

   public void scrollToItem(int item_number) {
      keep_selected_cell_visible = true;
      int r = item_number / nbcol;
      int c = item_number % nbcol;
      /*
       * THE NEXT LINES ARE A TEMPORARY SOLUTION, THE TRICK WILL NOT WORK ON SLOW CPUs AND HUGE IMAGES.
       * we push the gitem that contains the image on which we want to scroll,
       * and we wait some milliseconds for the ImageLoaderThread.
       */
      ilt.pushItem((GItem) table.getValueAt(r, c));
      try {
         Thread.sleep(100);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
      table.changeSelection(r, c, false, false);
   }

   /**
    * *************************************************************************
    * Managing GalateeListener.
    * ************************************************************************
    */
   /**
    * for adding listener to the Galatee
    *
    * @param listener
    */
   public void addGalateeListener(GListener listener) {
      listeners.add(listener);
   }

   /**
    * this method is mandatory for event/listening mechanisms
    */
   @Override
   public void processEvent(AWTEvent e) {
      if (e instanceof GEvent) {
         GEvent gevent = (GEvent) e;
         if (gevent.getType().equals(GEvent.E_ITEM_DOUBLECLICKED)) {
            for (GListener l : listeners) {
               l.itemDoubleClicked((GEvent) e);
            }
         } else if (gevent.getType().equals(GEvent.E_SELECTION_CHANGED)) {
            for (GListener l : listeners) {
               l.selectionChanged((GEvent) e);
            }
         }
      }
   }

   //
   public void setPopup_menu(JPopupMenu jPopupMenu) {
      this.popup_menu = jPopupMenu;
   }

   public JPopupMenu getPopup_menu() {
      return popup_menu;
   }

   /**
    * *************************************************************************
    * implementation of MouseListener.
    * ************************************************************************
    */
   @Override
   public void mouseClicked(MouseEvent e) {

      if (e.getButton() == MouseEvent.BUTTON1) {
         Point p = e.getPoint();

         if (p != null) {
            int r = table.rowAtPoint(p);
            int c = table.columnAtPoint(p);
            GItem gitem = (GItem) table.getModel().getValueAt(r, c);

            if (gitem != null) {
               //managing the double click.
               if (e.getClickCount() > 1) {
                  //updating the focused cell and the selected cells.
                  table.setRowSelectionInterval(r, r);
                  table.setColumnSelectionInterval(c, c);

                  eq.postEvent(new GEvent(GEvent.E_ITEM_DOUBLECLICKED,
                          this, (GItem) model.getValueAt(table
                          .getSelectedRow(), table
                          .getSelectedColumn())));
               } else { // single click
               }

            }//end the cell is not empty.
         }//end the point is in the JTable.

      }//end if MouseEvent.BUTTON1

   }

   @Override
   public void mouseEntered(MouseEvent e) {
   }

   @Override
   public void mouseExited(MouseEvent e) {
   }

   @Override
   public void mousePressed(MouseEvent e) {
      keep_selected_cell_visible = false;

      if (e.getButton() == MouseEvent.BUTTON1) {
         popup_menu.setVisible(false);
      } else if (e.getButton() == MouseEvent.BUTTON3) {
         //we select the item under the mouse pointer like for a MouseEvent.BUTTON1
         Point p = e.getPoint();

         if (p != null) {
            int r = table.rowAtPoint(p);
            int c = table.columnAtPoint(p);
            GItem gitem = (GItem) table.getModel().getValueAt(r, c);

            if (gitem != null) {
               //updating the focused cell and the selected cells.
               table.setRowSelectionInterval(r, r);
               table.setColumnSelectionInterval(c, c);

               eq.postEvent(new GEvent(GEvent.E_SELECTION_CHANGED, this,
                       (GItem) model.getValueAt(table.getSelectedRow(),
                       table.getSelectedColumn())));
            }//end the cell is not empty.
         }//end the point is in the JTable.

         //we display the JPopupMenu.
         if (popup_menu == null) {
            return;
         }
         popup_menu.show(table, e.getX(), e.getY());
      }
   }

   @Override
   public void mouseReleased(MouseEvent e) {
   }

   /**
    * *************************************************************************
    * keystrokes actions. ***********************************************************************
    */
   private void addScrollpaneMouseListeners(JScrollPane sp) {
      sp.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            table.requestFocus();
         }
      });
      sp.getVerticalScrollBar().addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            keep_selected_cell_visible = false;
         }
      });
      sp.getHorizontalScrollBar().addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            keep_selected_cell_visible = false;
         }
      });
      sp.addMouseWheelListener(new MouseWheelListener() {
         @Override
         public void mouseWheelMoved(MouseWheelEvent e) {
            keep_selected_cell_visible = false;
         }
      });
   }

   // +GBH moved from constructor
   private void mapKeys() {
      ((GalateeAction) action_vk_up).setEventSource(this);
      table.getActionMap().put("action_vk_up", action_vk_up);
      table.getInputMap().put(
              KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false),
              "action_vk_up");

      ((GalateeAction) action_vk_right).setEventSource(this);
      table.getActionMap().put("action_vk_right", action_vk_right);
      table.getInputMap().put(
              KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false),
              "action_vk_right");

      ((GalateeAction) action_vk_down).setEventSource(this);
      table.getActionMap().put("action_vk_down", action_vk_down);
      table.getInputMap().put(
              KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false),
              "action_vk_down");

      ((GalateeAction) action_vk_left).setEventSource(this);
      table.getActionMap().put("action_vk_left", action_vk_left);
      table.getInputMap().put(
              KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false),
              "action_vk_left");

      ((GalateeAction) action_vk_enter).setEventSource(this);
      table.getActionMap().put("action_vk_enter", action_vk_enter);
      table.getInputMap().put(
              KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false),
              "action_vk_enter");

      ((GalateeAction) action_vk_plus).setEventSource(this);
      table.getActionMap().put("action_vk_plus", action_vk_plus);
      table.getInputMap().put(KeyStroke.getKeyStroke('+'), "action_vk_plus");

      ((GalateeAction) action_vk_minus).setEventSource(this);
      table.getActionMap().put("action_vk_minus", action_vk_minus);
      table.getInputMap().put(KeyStroke.getKeyStroke('-'), "action_vk_minus");

      ((GalateeAction) action_vk_page_up).setEventSource(this);
      table.getActionMap().put("action_vk_page_up", action_vk_page_up);
      table.getInputMap().put(
              KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0, false),
              "action_vk_page_up");

      ((GalateeAction) action_vk_page_down).setEventSource(this);
      table.getActionMap().put("action_vk_page_down", action_vk_page_down);
      table.getInputMap().put(
              KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0, false),
              "action_vk_page_down");

      ((GalateeAction) action_vk_home).setEventSource(this);
      table.getActionMap().put("action_vk_home", action_vk_home);
      table.getInputMap().put(
              KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0, false),
              "action_vk_home");

      ((GalateeAction) action_vk_end).setEventSource(this);
      table.getActionMap().put("action_vk_end", action_vk_end);
      table.getInputMap().put(
              KeyStroke.getKeyStroke(KeyEvent.VK_END, 0, false),
              "action_vk_end");

      ((GalateeAction) action_vk_tab).setEventSource(this);
      table.getActionMap().put("action_vk_tab", action_vk_tab);
      table.getInputMap().put(
              KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0, false),
              "action_vk_tab");

      table.getActionMap().put("action_preferences_dialog",
              action_preferences_dialog);
      table.getInputMap().put(
              KeyStroke.getKeyStroke(KeyEvent.VK_P,
              java.awt.event.InputEvent.CTRL_MASK, false),
              "action_preferences_dialog");

//      ((GalateeAction) action_changeToGrid).setEventSource(this);
//      table.getActionMap().put("action_changeToGrid",
//              action_changeToGrid);
//      table.getInputMap().put(
//              KeyStroke.getKeyStroke(KeyEvent.VK_G,
//              java.awt.event.InputEvent.CTRL_MASK, false),
//              "action_changeToGrid");

      ((GalateeAction) action_asterisk).setEventSource(this);
      table.getActionMap().put("action_asterisk", action_asterisk);
      table.getInputMap().put(KeyStroke.getKeyStroke('*'), "action_asterisk");

   }

   private abstract class GalateeAction extends AbstractAction {

      protected Object source;

      public void setEventSource(Object source) {
         this.source = source;
      }
   }
   private Action action_vk_up = new GalateeAction() {
      public void actionPerformed(ActionEvent e) {
         //System.out.println("up");

         //if any gitem has been selected yet.
         if (table.getSelectedRow() == -1 || table.getSelectedColumn() == -1) {
            //updating selected item.
            table.setRowSelectionInterval(0, 0);
            table.setColumnSelectionInterval(0, 0);
         } else if (table.getSelectedRow() > 0) { //if we don't go out the table.
            //updating selected item.*
            table.setRowSelectionInterval(table.getSelectedRow() - 1, table
                    .getSelectedRow() - 1);
         } else {

            //scrolling the table to view the selected cell.
            table.scrollRectToVisible(table.getCellRect(table
                    .getSelectedRow(), table.getSelectedColumn(), true));

            return;
         }
      }
   };
   private Action action_vk_down = new GalateeAction() {
      public void actionPerformed(ActionEvent e) {
         System.out.println("down");

         //if any gitem has been selected yet.
         if (table.getSelectedRow() == -1 || table.getSelectedColumn() == -1) {
            //updating selected item.
            table.setRowSelectionInterval(0, 0);
            table.setColumnSelectionInterval(0, 0);
         } else if (table.getSelectedRow() < table.getRowCount() - 1) { //if we don't go out the table.
            if (table.getModel().getValueAt(table.getSelectedRow() + 1,
                    table.getSelectedColumn()) == null) { //if the cell at the bottom is empty.

               //scrolling the table to view the selected cell.
               table
                       .scrollRectToVisible(table.getCellRect(table
                       .getSelectedRow(), table
                       .getSelectedColumn(), true));

               return;
            }

            //updating selected cell.
            table.setRowSelectionInterval(table.getSelectedRow() + 1, table
                    .getSelectedRow() + 1);
         } else {

            //scrolling the table to view the selected cell.
            table.scrollRectToVisible(table.getCellRect(table
                    .getSelectedRow(), table.getSelectedColumn(), true));

            return;
         }
      }
   };
   private Action action_vk_right = new GalateeAction() {
      public void actionPerformed(ActionEvent e) {
         //System.out.println("right");

         //if any gitem has been selected yet.
         if (table.getSelectedRow() == -1 || table.getSelectedColumn() == -1) {
            //updating selected item.
            table.setRowSelectionInterval(0, 0);
            table.setColumnSelectionInterval(0, 0);
         } else if (table.getSelectedColumn() < nbcol - 1) { //if we don't go out the table. 
            if (table.getModel().getValueAt(table.getSelectedRow(),
                    table.getSelectedColumn() + 1) == null) { //if the cell at the right is empty.

               //scrolling the table to view the selected cell.
               table
                       .scrollRectToVisible(table.getCellRect(table
                       .getSelectedRow(), table
                       .getSelectedColumn(), true));

               return;
            }
            //updating the selected cell.
            table.setColumnSelectionInterval(table.getSelectedColumn() + 1,
                    table.getSelectedColumn() + 1);
         } else {

            //scrolling the table to view the selected cell.
            table.scrollRectToVisible(table.getCellRect(table
                    .getSelectedRow(), table.getSelectedColumn(), true));

            return;
         }
      }
   };
   private Action action_vk_left = new GalateeAction() {
      public void actionPerformed(ActionEvent e) {
         //System.out.println("left");

         //if any gitem has been selected yet.
         if (table.getSelectedRow() == -1 || table.getSelectedColumn() == -1) {
            //updating selected item.
            table.setRowSelectionInterval(0, 0);
            table.setColumnSelectionInterval(0, 0);
         } else if (table.getSelectedColumn() > 0) { //if we don't go out the table.
            //updating selected item.
            table.setColumnSelectionInterval(table.getSelectedColumn() - 1,
                    table.getSelectedColumn() - 1);
         } else {

            //scrolling the table to view the selected cell.
            table.scrollRectToVisible(table.getCellRect(table
                    .getSelectedRow(), table.getSelectedColumn(), true));

            return;
         }
      }
   };
   private Action action_vk_enter = new GalateeAction() {
      public void actionPerformed(ActionEvent e) {
         //System.out.println("enter");
         if (table.getSelectedRow() >= 0 && table.getSelectedColumn() >= 0) {
            eq.postEvent(new GEvent(GEvent.E_ITEM_DOUBLECLICKED, source,
                    (GItem) model.getValueAt(table.getSelectedRow(), table
                    .getSelectedColumn())));
         }
      }
   };
   private Action action_vk_plus = new GalateeAction() {
      public void actionPerformed(ActionEvent e) {
         //System.out.println("plus");
         changeModelAddColumn();
      }
   };
   private Action action_vk_minus = new GalateeAction() {
      public void actionPerformed(ActionEvent e) {
         //System.out.println("minus");
         changeModelRemoveColumn();
      }
   };
   private Action action_vk_tab = new GalateeAction() {
      public void actionPerformed(ActionEvent e) {
         //System.out.println("tab");
      }
   };
   private Action action_vk_page_up = new GalateeAction() {
      public void actionPerformed(ActionEvent e) {
         //System.out.println("page_up");

         //getting the pixel coordinates of the gitem at the corner left in the viewport.
         Point gitem_at_left_bottom_corner_in_spviewport = new Point(1, sp
                 .getViewport().getHeight() - 1);

         //transforming these coordinates in coordinates for the jtable.
         Point gitem_at_left_bottom_corner_in_table = SwingUtilities
                 .convertPoint(sp.getViewport(),
                 gitem_at_left_bottom_corner_in_spviewport, table);

         //getting the row and the column for the found coordinates.
         int gitem_bottom_row = table
                 .rowAtPoint(gitem_at_left_bottom_corner_in_table);
         int gitem_left_col = table
                 .columnAtPoint(gitem_at_left_bottom_corner_in_table);

         //get the number of gitems vertically displayed in the JScrollPane.
         int verticallyDisplayableGItems = sp.getViewport().getHeight()
                 / gitemImageDimension.height;

         //get the row number of the new first row (i.e. after page_up).
         int new_first_displayed_row = gitem_bottom_row
                 - verticallyDisplayableGItems * 2 + 2; //the +1 is for keeping the first gitem displayed.
         if (new_first_displayed_row < 0) {
            new_first_displayed_row = 0;
         }

         //do the scroll.
         table.scrollRectToVisible(table.getCellRect(
                 new_first_displayed_row, gitem_left_col, true));
      }
   };
   private Action action_vk_page_down = new GalateeAction() {
      public void actionPerformed(ActionEvent e) {
         //System.out.println("page_down");

         //getting the pixel coordinates of the gitem at the corner left in the viewport.
         Point gitem_at_left_bottom_corner_in_spviewport = new Point(1, sp
                 .getViewport().getHeight() - 1);

         //transforming these coordinates in coordinates for the jtable.
         Point gitem_at_left_bottom_corner_in_table = SwingUtilities
                 .convertPoint(sp.getViewport(),
                 gitem_at_left_bottom_corner_in_spviewport, table);

         //getting the row and the column for the found coordinates.
         int gitem_bottom_row = table
                 .rowAtPoint(gitem_at_left_bottom_corner_in_table);
         int gitem_left_col = table
                 .columnAtPoint(gitem_at_left_bottom_corner_in_table);

         //get the number of gitems vertically displayed in the JScrollPane.
         int verticallyDisplayableGItems = sp.getViewport().getHeight()
                 / gitemImageDimension.height;

         //get the row number of the new first row (i.e. after page_down).
         int new_last_displayed_row = gitem_bottom_row
                 + verticallyDisplayableGItems - 2; //the -2 is for keeping the last gitem displayed.
         if (new_last_displayed_row > (table.getRowCount() - 1)) {
            new_last_displayed_row = table.getRowCount() - 1;
         }

         //do the scroll.
         table.scrollRectToVisible(table.getCellRect(new_last_displayed_row,
                 gitem_left_col, true));
      }
   };
   private Action action_vk_home = new GalateeAction() {
      public void actionPerformed(ActionEvent e) {
         //System.out.println("home");

         //do the scroll.
         table.scrollRectToVisible(table.getCellRect(0, 0, true));
      }
   };
   private Action action_vk_end = new GalateeAction() {
      public void actionPerformed(ActionEvent e) {
         //System.out.println("end");

         //do the scroll.
         table.scrollRectToVisible(table.getCellRect(
                 table.getRowCount() - 1, 0, true));
      }
   };
   private Action action_preferences_dialog = new GalateeAction() {
      public void actionPerformed(ActionEvent e) {
         //System.out.println("preferences");
         preferences_dialog.setVisible(true);
      }
   };
   private Action action_asterisk = new GalateeAction() {
      public void actionPerformed(ActionEvent e) {
         System.out.println("asterisk");
         setDescriptionVisible(!description_visible);
      }
   };
//   private Action action_changeToGrid = new GalateeAction() {
//      public void actionPerformed(ActionEvent e) {
//         Galatee.this.changeToGridView(3);
//      }
//   };

   public void setDescriptionVisible(boolean vis) {
      if (!vis) {
         renderer.updateDimension(gitemImageDimension, 0);
         editor.updateDimension(gitemImageDimension, 0);
         table.repaint();
         description_visible = false;
      } else {
         renderer.updateDimension(gitemImageDimension, gitemDescriptionWidth);
         editor.updateDimension(gitemImageDimension, gitemDescriptionWidth);
         table.repaint();
         description_visible = true;
      }
   }

   /**
    * *************************************************************************
    * implementation of ComponentListener.
    * ************************************************************************
    */
   @Override
   public void componentHidden(ComponentEvent e) {
   }

   @Override
   public void componentMoved(ComponentEvent e) {
   }

   @Override
   public void componentResized(ComponentEvent e) {
      JScrollPane sp = (JScrollPane) e.getSource();

      //		if (sp.getViewport().getSize().width > (nbcol + 1) * renderer.getItemWidth()) {
      //			while (sp.getViewport().getSize().width > (nbcol + 1) * renderer.getItemWidth()) {
      //				changeModelAddColumn();
      //			}
      //			return;
      //		}

      //		if (sp.getViewport().getSize().width < (nbcol) * renderer.getItemWidth()) {
      //			while (sp.getViewport().getSize().width < (nbcol) * renderer.getItemWidth()) {
      //				changeModelRetrieveColumn();
      //			}
      //			return;
      //		}
   }

   @Override
   public void componentShown(ComponentEvent e) {
   }

   /**
    * *************************************************************************
    * implementation of ListSelectionListener.
    * ************************************************************************
    */
   @Override
   public void valueChanged(ListSelectionEvent e) {
      synchronized (this) {
         if (!e.getValueIsAdjusting()) {
            /*
             * verifying that it's a true real "new selection".
             * (the contrary could happen because we have a ListSelectionListener
             * for the rows and another one for the columns).
             */
            if (selectedrow == table.getSelectedRow()
                    && selectedcol == table.getSelectedColumn()) {
               return;
            }
            int r = table.getSelectedRow();
            int c = table.getSelectedColumn();

            /*
             * some ListSelectionEvent are thrown with no selection...
             */
            if (r == -1 || c == -1) {
               return;
            }

            //scrolling the table to view the selected cell.
            table.scrollRectToVisible(table.getCellRect(r, c, true));

            /*
             * we edit the current cell (for having the JTextArea in the cell, and thus
             * allowing the selection of the text with the mouse for copy/paste filepath etc.).
             */
            table.editCellAt(r, c);

            //creating the new GEvent.E_SELECTION_CHANGED event.
            eq.postEvent(new GEvent(GEvent.E_SELECTION_CHANGED, this,
                    (GItem) model.getValueAt(r, c)));

            /*
             * updating the selectedcell.
             */
            selectedrow = r;
            selectedcol = c;
         }
      }
   }

   /**
    * *************************************************************************
    * implementation of KeyListener.
    * ************************************************************************
    */
   @Override
   public void keyPressed(KeyEvent e) {
      keep_selected_cell_visible = false;
   }

   @Override
   public void keyReleased(KeyEvent e) {
   }

   @Override
   public void keyTyped(KeyEvent e) {
   }
}
