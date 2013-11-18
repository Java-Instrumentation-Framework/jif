package net.trevize.galatee;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * _TableModel.java - May 17, 2009
 */

public class _TableModel extends DefaultTableModel {
	@Override
	public boolean isCellEditable(int r, int c) {
		return true;
	}

	// the management 'line by line' of the DefaultTableModel complicate a little the process
	public void setColumn(int c, Vector<GItem> v) {
		Vector<Vector<GItem>> data = (Vector<Vector<GItem>>) getDataVector();
		int nbr = data.size();
		int i = 0;
		while (i < nbr && i < v.size()) {
			((Vector<GItem>) data.get(i)).set(c, v.get(i));
			++i;
		}
		if (nbr < v.size()) {
			while (i < v.size()) {
				Vector<GItem> r = new Vector<GItem>();
				for (int j = 0; j < getColumnCount(); ++j) {
					r.add(null);
				}
				r.set(c, v.get(i));
				data.add(r);
				++i;
			}
		}
		fireTableDataChanged();
	}
}
