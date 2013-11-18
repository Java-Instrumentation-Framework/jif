package net.trevize.galatee;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.AbstractCellEditor;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * _TableCellEditor.java - Feb 2, 2010
 */

public class _TableCellEditor extends AbstractCellEditor implements
		TableCellEditor {

	private Galatee galatee;

	private GCellPanel cp;

	private JPanel emptyCell;

	private Dimension gitemImageDimension;

	private int gitemDescriptionWidth;

	public _TableCellEditor(Galatee galatee, Dimension gitemImageDimension,
			int gitemDescriptionWidth) {
		this.galatee = galatee;
		this.gitemImageDimension = gitemImageDimension;
		this.gitemDescriptionWidth = gitemDescriptionWidth;

		cp = new GCellPanel(gitemImageDimension, gitemDescriptionWidth);

		emptyCell = new JPanel();
		emptyCell.setBackground(Color.WHITE);
		emptyCell.setSize(cp.getMinimumSize());
	}

	public void updateDimension(Dimension gitemImageDimension,
			int gitemDescriptionWidth) {
		this.gitemImageDimension = gitemImageDimension;
		this.gitemDescriptionWidth = gitemDescriptionWidth;

		cp = new GCellPanel(gitemImageDimension, gitemDescriptionWidth);
		emptyCell.setSize(cp.getMinimumSize());
	}

	public int getItemWidth() {
		return cp.getItemWidth();
	}

	public int getItemHeight() {
		return cp.getItemMinHeight();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		GItem gitem = (GItem) value;

		if (gitem == null) {
			return emptyCell;
		}

		cp.setGItem(gitem);

		if (gitem.getImage() == null) {
			galatee.getImageLoaderThread().pushItem(gitem);
		}

		if (gitemDescriptionWidth != 0) {
			String text;
			if (gitem.getText() != null) {
				text = gitem.getText();
			} else {
				text = "";
			}

			cp.setText(text);
		}

		cp.setSelected(isSelected);

		/*
		 * update the row height.
		 */
		if (gitemDescriptionWidth == 0) {
			if (table.getRowHeight(row) != cp.getItemMinHeight()) {
				table.setRowHeight(row, cp.getItemMinHeight());
			}
		}

		else

		if (table.getRowHeight(row) < cp.getItemMinHeight()
				|| (table.getRowHeight() > cp.getItemMinHeight() && table
						.getRowHeight() > cp.getPreferredHeight())) {
			table.setRowHeight(row, cp.getItemMinHeight());
		}

		else

		if (table.getRowHeight(row) < cp.getPreferredHeight()) {
			table.setRowHeight(row, cp.getPreferredHeight());
		}

		/*
		 * update the column width.
		 */
		if (table.getColumnModel().getColumn(column).getPreferredWidth() != cp
				.getItemWidth()) {
			table.getColumnModel().getColumn(column).setPreferredWidth(
					cp.getItemWidth());
		}

		return cp;
	}

	@Override
	public Object getCellEditorValue() {
		int row = galatee.getTable().getEditingRow();
		int col = galatee.getTable().getEditingColumn();
		GItem gitem = (GItem) galatee.getTable().getModel()
				.getValueAt(row, col);
		return gitem;
	}

}
