package org.simtech.dmtt.ui.comp;

import java.awt.Rectangle;
import java.awt.geom.Point2D;

import javax.swing.tree.TreePath;

import org.simtech.dmtt.ui.app.MapperScrollPane;

public class ConnectedNode {
	
	private MapperScrollPane mPane;
	
	private int paneIndex = -1;
	private String selectedSide = "";
	private int squareIndex = -1;
	private int dummySquareIndex = -1;
	
	private TreePath path;
	private Point2D point;
	private String nodeType;
	
	
	public MapperScrollPane getmPane() {
		return mPane;
	}
	public void setmPane(MapperScrollPane mPane) {
		this.mPane = mPane;
	}
	
	public int getPaneIndex() {
		return paneIndex;
	}
	public void setPaneIndex(int paneIndex) {
		this.paneIndex = paneIndex;
	}
	
	public String getSelectedSide() {
		return selectedSide;
	}
	public void setSelectedSide(String selectedSide) {
		this.selectedSide = selectedSide;
	}
	public int getSquareIndex() {
		return squareIndex;
	}
	public void setSquareIndex(int squareIndex) {
		this.squareIndex = squareIndex;
	}
	public int getDummySquareIndex() {
		return dummySquareIndex;
	}
	public void setDummySquareIndex(int dummySquareIndex) {
		this.dummySquareIndex = dummySquareIndex;
	}
	public TreePath getPath() {
		return path;
	}
	public void setPath(TreePath path) {
		this.path = path;
	}
	public Point2D getPoint() {
		return point;
	}
	public void setPoint(Point2D point) {
		this.point = point;
	}
	public String getNodeType() {
		return nodeType;
	}
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	
	public boolean isEmpty(){
		return path.getLastPathComponent().toString().trim().length() == 0;
	}
}
