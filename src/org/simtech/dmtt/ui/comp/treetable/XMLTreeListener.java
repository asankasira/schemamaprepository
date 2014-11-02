package org.simtech.dmtt.ui.comp.treetable;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.simtech.dmtt.ui.app.MappingDesktopPane;

public class XMLTreeListener implements TreeExpansionListener {

	private MappingDesktopPane pane;
	
	public XMLTreeListener(MappingDesktopPane pane){
		
		this.pane = pane;
	}
	
	
	public void treeExpanded(TreeExpansionEvent event) {
//		TreePath path = event.getPath();
		
//		for(Object ob: path.getPath()){
//			DefaultMutableTreeNode node = (DefaultMutableTreeNode)ob;
//			String data = node.getUserObject().toString();
//			System.out.print(data+"/");
//		}
//		System.out.println();
//		
//		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
//		String data = node.getUserObject().toString();
//		System.out.println("Expanded: " + data);
//		pane.repaint();
	}

	public void treeCollapsed(TreeExpansionEvent event) {
//		TreePath path = event.getPath();
//		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
//		String data = node.getUserObject().toString();
//		System.out.println("Collapsed: " + data);
//		pane.repaint();
	}

}
