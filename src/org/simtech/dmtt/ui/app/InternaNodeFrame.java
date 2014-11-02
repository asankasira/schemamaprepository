package org.simtech.dmtt.ui.app;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JInternalFrame;

public class InternaNodeFrame extends JInternalFrame {

//	private MapperScrollPane paneTest;
	
	public InternaNodeFrame(String title, boolean resizable, boolean closable, 
            boolean maximizable, boolean iconifiable){
		
		super(title, resizable, closable, maximizable, iconifiable);
	}
	
	public InternaNodeFrame(String title){
		super(title);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
	}

//	public MapperScrollPane getPaneTest() {
//		return paneTest;
//	}
//
//	public void setPaneTest(MapperScrollPane paneTest) {
//		this.paneTest = paneTest;
//	}
}
