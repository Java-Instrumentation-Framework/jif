package net.trevize.galatee;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;

/**
 * This class has been written by Louis Cova, see [[http://louis.cova.neuf.fr/blocs-notes/page11.html]]
 */

/**
 * The <code>XGridBag</code> helps to use the GridBagConstraints.<br>
 * This class defines styles reusable by differents components in a grid.
 * 
 * To create a layout do the following step:<br> - create a
 * <code>GridBagLayout</code> and a XGridBag</code>.<br> - prepare the
 * components size (min/prefered/max).<br> - create one or more style using
 * <code>CellStyle.getStyle</code> method<br> - add components to the grid
 * using the <code>XGridBag.add</code> method.<br>
 * <br>
 * Example:<br>
 * <code> GridBagLayout layout = new GridBagLayout();<br>
 * panel.setLayout(layout);<br>
 * XGridBag grid = new XGridBag(panel);<br>
 * <br>
 * CellStyle style1 = new CellStyle(1.0, 0.0,<br>
 * GridBagConstraints.WEST, GridBagConstraints.VERTICAL, insets, 4, 0);<br>
 * ...<br>
 * grid.add(comp1, style1, 0, 0);<br>
 * grid.add(comp2, style1, 0, 1);<br>
 * grid.add(comp3, style2, 1, 0);<br> ..<br>
 * </code>
 * 
 * @see CellStyle
 * @see java.awt.GridBagConstraints
 * @see java.awt.Insets
 */

public class XGridBag extends GridBagConstraints {

	public Container container;

	/**
	 * Add a component to the container. The component occupy only one cell
	 * width and heigth.
	 * 
	 * @param c
	 *            component to add
	 * @param style
	 *            cell style
	 * @param row
	 *            first Y cell
	 * @param col
	 *            first X cell
	 * @see CellStyle
	 */
	public void add(Component c, CellStyle style, int row, int col) {
		add(c, style, row, col, 1, 1);
	}

	/**
	 * Add a component to the container.
	 * 
	 * @param c
	 *            component to add
	 * @param style
	 *            cell style
	 * @param row
	 *            first Y cell
	 * @param col
	 *            first X cell
	 * @param rowHeight
	 *            number of cells in a column (use GridBagConstraints.REMAINDER
	 *            to fill out all rows to the end).
	 * @param colWidth
	 *            number of cells in a row (use GridBagConstraints.REMAINDER to
	 *            fill out all columns to the end).
	 * @see CellStyle
	 */
	public void add(Component c, CellStyle style, int row, int col,
			int rowHeight, int colWidth) {
		this.gridx = col;
		this.gridy = row;
		this.gridwidth = colWidth;
		this.gridheight = rowHeight;
		this.fill = style.fill;
		this.ipadx = style.ipadx;
		this.ipady = style.ipady;
		this.insets = style.insets;
		this.anchor = style.anchor;
		this.weightx = style.weightx;
		this.weighty = style.weighty;
		container.add(c, this);
	}

	/**
	 * XGridBag constructor.
	 * 
	 * @param container
	 */
	public XGridBag(Container container) {
		super();
		this.container = container;
	}

}
