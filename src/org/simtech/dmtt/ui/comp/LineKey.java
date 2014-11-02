package org.simtech.dmtt.ui.comp;

import javax.swing.tree.TreePath;

public class LineKey {

	private TreePath path;
//	private int pane;
	
//	public LineKey(TreePath path, int pane){
//		
//		this.path = path;
//		this.pane = pane;
//	}
	
	public LineKey(TreePath path){
		
		this.path = path;
	}
/*	public boolean equals(Object o){
		
		if(o instanceof LineKey){
			
			LineKey key = (LineKey)o;
			return (key.path == path && key.pane == pane);
		}
		
		return false;
	}
	
	public int hashCode(){
		return path.hashCode() + pane;
	}*/
	
	public boolean equals(Object o){
		
		if(o instanceof LineKey){
			
			LineKey key = (LineKey)o;
			return (key.path == path);
		}
		
		return false;
	}
	
	public int hashCode(){
		return path.hashCode();
	}
}
