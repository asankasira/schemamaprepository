package org.simtech.dmtt.ui.app;
 
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.*;  
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

import javax.swing.*;  
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.simtech.dmtt.ui.comp.NodeElement;
import org.simtech.dmtt.ui.comp.treetable.JTreeTable;
import org.simtech.dmtt.ui.comp.treetable.TreeTableModel;
import org.simtech.dmtt.ui.comp.treetable.models.*;
import org.simtech.dmtt.ui.utils.XMLParseUtil;
import org.w3c.dom.Node;
   
public class MapperScrollPane extends ComponentAdapter implements AdjustmentListener,
	MouseMotionListener
{  
	private MappingDesktopPane pane;
	private InternaNodeFrame frame;
	
    private JTreeTable treeTable;
    private JScrollPane scrollPane;  
    private JScrollBar scrollBar;
    private JLabel label;  
    private int topRow;
    private int visibleRows;
    
    private int paneIndex = -1;
    private String paneName;
    
    private Map<Integer, TreePath> source = new TreeMap();
    private Map<Character, TreePath> target = new TreeMap();
    private char defChar = 'a';
    private int rowIndex = -1;
    
    
    private Map<Integer, NodeElement> drawMap = new HashMap<Integer, NodeElement>();
    
    public MapperScrollPane(int index)  
    {  
       this.paneIndex = index;
       init();
    }  
    
    public void init(){
    	  createTable(); 
//          createLabel();
          
          scrollPane = new JScrollPane(treeTable){
          	public void paintComponent(Graphics g){
          		super.paintComponent(g);
          		Graphics2D g2D = (Graphics2D)g;
          		g2D.setStroke(new BasicStroke());
          	}
          };  
          scrollPane.addComponentListener(this); 
          scrollPane.addMouseMotionListener(this);
          
          scrollBar =  scrollPane.getVerticalScrollBar();
          scrollBar.addAdjustmentListener(this);  
          
    }
    
    public void componentResized(ComponentEvent e)  
    {  
    	
    }  
   
    public JScrollPane getScPane(){
    	return scrollPane;
    }
    
    public JTreeTable getJTreeTable(){
    	return treeTable;
    }
    
    public JLabel getJLabel() {
		return label;
	}
    
    public JScrollBar getScrollBar() {
		return scrollBar;
	}
    
    private JTable createTable()  
    {  
    	TreeTableModel treeModel = null;
    	int colWidth = 0;
    	
    	if(paneIndex == 0 || paneIndex == 1){
    		
        	String fileName = null;
        	XMLParseUtil util = new XMLParseUtil();
        	
        	if(paneIndex == 0)
        		fileName = "/Excel.xsd";
        	else
        		fileName = "/Excel.xsd";
        	
        	InputStream in = URLClassLoader.class.getResourceAsStream(fileName);
        	File file = util.getFileFromInputStream(in);

//        	Node node = util.parseXml(in);
//        	DefaultMutableTreeNode treeNode = util.createTreeNode(node);
        	DefaultMutableTreeNode treeNode = util.createXsdTreeNode(file.getAbsolutePath());
        	treeModel = new XMLTreeModel(treeNode);
        	colWidth = 250;
    	}
    	else{
    		treeModel = new DefaultTreeModel("");
    		colWidth = 10;
    	}
    	treeTable = new JTreeTable(treeModel);
        
        treeTable.setTableHeader(null);
        treeTable.setShowGrid(false);
        treeTable.setColumnSelectionAllowed(false);
        treeTable.setRowSelectionAllowed(true);
        
        for (int i = 0; i < treeTable.getTree().getRowCount(); i++) {
        	treeTable.getTree().expandRow(i);
        }
        
        treeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        treeTable.getColumnModel().getColumn(0).setPreferredWidth(colWidth);    
        treeTable.addMouseMotionListener(this);
        
        return treeTable;
    }  
   
    private JLabel createLabel()  
    {  
        label = new JLabel();  
        label.setHorizontalAlignment(JLabel.CENTER);  
        Dimension d = label.getPreferredSize();  
        d.height = 25;  
        label.setPreferredSize(d);  
        return label;  
    }  
   
/*    public static void main(String[] args)  
    {  
        MapperScrollPane ts = new MapperScrollPane(10, 0);  
        
        JFrame f = new JFrame();  
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        f.getContentPane().add(ts.getScPane());  
        f.getContentPane().add(ts.getJLabel(), "South");  
        f.setSize(400,400);  
        f.setLocation(200,200);  
        f.setVisible(true); 
    }*/

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
				
		paneRepaint();
		displayRows();
	}  
	
	public void displayRows(){
       
		JViewport viewport = scrollPane.getViewport(); 
        Dimension extentSize = viewport.getExtentSize();     
        Rectangle viewRect = viewport.getViewRect();         
        Rectangle visibleRect = viewport.getVisibleRect();    

        Point position = viewport.getViewPosition();          
        int topRow = treeTable.rowAtPoint(position);  
        Rectangle l = treeTable.getCellRect(topRow, 0, false);
        
        boolean next = false;
        
        
        if(position.y > (l.getCenterY() + l.getMinY())/2){	
        	topRow++;
        	next = true;
        }
        if(position.y > (l.getCenterY() + l.getMaxY())/2){
        	next = false;
        }
        

        String item = (String)treeTable.getValueAt(topRow, 0);  
        float ratio = (float)extentSize.height/treeTable.getRowHeight();
        int visibleRows = 0;
        if(ratio - (int)ratio > 0.8)
        	visibleRows = Math.round(ratio); 
        else
        	visibleRows = (int)ratio;      
        
        if(next)
        	visibleRows--;
        
        if(visibleRows > treeTable.getRowCount())
        	visibleRows = treeTable.getRowCount();
        
        setTopRow(topRow);
        setVisibleRows(visibleRows);
	}

	public int getTopRow() {
		return topRow;
	}

	public void setTopRow(int topRow) {
		this.topRow = topRow;
	}

	public int getVisibleRows() {
		return visibleRows;
	}

	public void setVisibleRows(int visibleRows) {
		this.visibleRows = visibleRows;
	}

	public JDesktopPane getPane() {
		return pane;
	}

	public void setPane(MappingDesktopPane pane) {
		this.pane = pane;
//		XMLTreeListener tl = new XMLTreeListener(pane);
//		treeTable.getTree().addTreeExpansionListener(tl);
	}

	public Map<Integer, NodeElement> getDrawMap() {
		return drawMap;
	}

	public void setDrawMap(Map<Integer, NodeElement> drawMap) {
		this.drawMap = drawMap;
	}

	public int getPaneIndex() {
		return paneIndex;
	}

	public void setPaneIndex(int paneIndex) {
		this.paneIndex = paneIndex;
	}

	public InternaNodeFrame getFrame() {
		return frame;
	}

	public void setFrame(InternaNodeFrame frame) {
		this.frame = frame;
	}
	
	public String getPaneName() {
		return paneName;
	}

	public void setPaneName(String paneName) {
		this.paneName = paneName;
	}
	
	public Map<Integer, TreePath> getSource() {
		return source;
	}

	public void setSource(Map<Integer, TreePath> source) {
		this.source = source;
	}

	public Map<Character, TreePath> getTarget() {
		return target;
	}

	public void setTarget(Map<Character, TreePath> target) {
		this.target = target;
	}

	public char getDefChar() {
		return defChar;
	}

	public void setDefChar(char defChar) {
		this.defChar = defChar;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public void paneRepaint(){
		if(pane != null){	
    		pane.reset();
    		pane.repaint();
		}
	}
	
	public void mouseMoved(MouseEvent e) {
		paneRepaint();
		treeTable.clearSelection();
	}
	
	public void mouseDragged(MouseEvent e){
		paneRepaint();
	}
}  
