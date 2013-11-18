package net.trevize.galatee;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * _TableCellRenderer.java - May 17, 2009
 */

public class _TableCellRenderer implements TableCellRenderer {

	private Galatee galatee;

	private GCellPanel cp;

	private JPanel emptyCell;

	private Dimension gitemImageDimension;

	private int gitemDescriptionWidth;

	public _TableCellRenderer(Galatee galatee, Dimension gitemImageDimension,
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
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
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

}
