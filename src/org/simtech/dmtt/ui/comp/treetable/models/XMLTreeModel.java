package org.simtech.dmtt.ui.comp.treetable.models;

import javax.swing.table.AbstractTableModel;
import javax.swing.tree.DefaultMutableTreeNode;

import org.simtech.dmtt.ui.comp.treetable.AbstractTreeTableModel;
import org.simtech.dmtt.ui.comp.treetable.TreeTableModel;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xerces.internal.dom.ChildNode;
import com.sun.org.apache.xerces.internal.dom.ElementImpl;
import com.sun.org.apache.xerces.internal.dom.NodeImpl;
import com.sun.org.apache.xerces.internal.dom.ParentNode;


public class XMLTreeModel extends AbstractTreeTableModel{

	public XMLTreeModel(DefaultMutableTreeNode root) {
		super(root);
	}

	public int getColumnCount() {
	
		return 1;
	}


	public String getColumnName(int column) {

		return "";
	}

	 public boolean isLeaf(Object node) {
		 
		 DefaultMutableTreeNode dm = (DefaultMutableTreeNode)node;
		 return dm.isLeaf();
	 }

	public Object getValueAt(Object node, int column) {
		
		DefaultMutableTreeNode dm = (DefaultMutableTreeNode)node;
		return dm.getUserObject().toString();
	}


	public Object getChild(Object parent, int index) {
		
		DefaultMutableTreeNode dm = (DefaultMutableTreeNode)parent;
		
		if(!dm.isLeaf()){
			
			return dm.getChildAt(index);
		}
		
		return null;
	}


	public int getChildCount(Object parent) {
		
		DefaultMutableTreeNode dm = (DefaultMutableTreeNode)parent;
		return dm.getChildCount();
	}

	public Class<?> getColumnClass(int column) { return TreeTableModel.class; }
}
