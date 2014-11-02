package org.simtech.dmtt.ui.comp.treetable.models;

import org.simtech.dmtt.ui.comp.treetable.AbstractTreeTableModel;
import org.simtech.dmtt.ui.comp.treetable.TreeTableModel;

public class DefaultTreeModel extends AbstractTreeTableModel{

	public DefaultTreeModel(Object root) {
		super(root);
	}

	public int getColumnCount() {
		
		return 1;
	}

	public String getColumnName(int column) {

		return "";
	}

	public Class<?> getColumnClass(int column) {

		return TreeTableModel.class;
	}

	public Object getValueAt(Object node, int column) {

		return "";
	}

	public Object getChild(Object parent, int index) {
		
		return "";
	}

	public int getChildCount(Object parent) {

		return 0;
	}
}
