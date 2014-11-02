package org.simtech.dmtt.ui.comp;

import java.awt.Rectangle;

public class DrawingElement {
	
	private Rectangle left;
	private Rectangle right;
	private int index;
	private String value;
	
	public Rectangle getLeft() {
		return left;
	}
	public void setLeft(Rectangle left) {
		this.left = left;
	}
	public Rectangle getRight() {
		return right;
	}
	public void setRight(Rectangle right) {
		this.right = right;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
