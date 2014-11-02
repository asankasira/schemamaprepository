package org.simtech.dmtt.ui.comp;

import java.awt.Color;
import java.awt.geom.Line2D;

import javax.swing.tree.TreePath;

public class ConnectedLine {

	private ConnectedNode startNode;
	private ConnectedNode endNode;
	private Line2D line;
	private TreePath currentPath;
	private Color lineColor;
	private int mappingRow = -1;
	private boolean isStartNodeChanged;
	private boolean isEndNodeChanged;
	
	private boolean hasStartNode = false;
	
	public ConnectedNode getStartNode() {
		return startNode;
	}
	public void setStartNode(ConnectedNode startNode) {
		this.startNode = startNode;
	}
	public ConnectedNode getEndNode() {
		return endNode;
	}
	public void setEndNode(ConnectedNode endNode) {
		this.endNode = endNode;
	}
	
	public Line2D getLine() {
		return line;
	}
	public void setLine(Line2D line) {
		this.line = line;
	}
	
	public TreePath getCurrentPath() {
		return currentPath;
	}
	public void setCurrentPath(TreePath currentPath) {
		this.currentPath = currentPath;
	}
	public Color getLineColor() {
		return lineColor;
	}
	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
	}
	public int getMappingRow() {
		return mappingRow;
	}
	public void setMappingRow(int mappingRow) {
		this.mappingRow = mappingRow;
	}
	public boolean isStartNodeChanged() {
		return isStartNodeChanged;
	}
	public void setStartNodeChanged(boolean isStartNodeChanged) {
		this.isStartNodeChanged = isStartNodeChanged;
	}
	public boolean isEndNodeChanged() {
		return isEndNodeChanged;
	}
	public void setEndNodeChanged(boolean isEndNodeChanged) {
		this.isEndNodeChanged = isEndNodeChanged;
	}
	public boolean hasStartNode() {
		return hasStartNode;
	}
	public void setHasStartNode(boolean hasStartNode) {
		this.hasStartNode = hasStartNode;
	}
	
	public boolean equals(Object ob){
		
		if(ob instanceof ConnectedLine){
			
			ConnectedLine ln = (ConnectedLine)ob;
			Line2D l = ln.getLine();
			return(l.ptSegDistSq(startNode.getPoint()) == 0 && l.ptSegDistSq(endNode.getPoint()) == 0);	
		}
		
		return false;
	}
	
	public int hashCode(){
		
		return (int)(startNode.getPoint().distanceSq(endNode.getPoint()));
	}
}
