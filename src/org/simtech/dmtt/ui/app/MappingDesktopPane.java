package org.simtech.dmtt.ui.app;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.Map.Entry;

import javax.swing.JDesktopPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.TreePath;

import org.simtech.dmtt.ui.comp.ConnectedLine;
import org.simtech.dmtt.ui.comp.ConnectedNode;
import org.simtech.dmtt.ui.comp.LineKey;
import org.simtech.dmtt.ui.comp.NodeElement;
import org.simtech.dmtt.ui.comp.treetable.JTreeTable;

public class MappingDesktopPane extends JDesktopPane implements MouseListener, MouseMotionListener {

	private List<MapperScrollPane> paneList = new ArrayList<MapperScrollPane>();
	
	private Map<LineKey, Set<ConnectedLine>> startNodes = new HashMap<LineKey, Set<ConnectedLine>>();
	private Map<LineKey, Set<ConnectedLine>> endNodes = new HashMap<LineKey, Set<ConnectedLine>>();
	
	private JTable mappingTable;
	
	private boolean isSquareCur = false;
	private int startPaneIndex = -1;
	private int squareIndex = -1;
	private String side = "";
	
	private boolean isTarSquareCur = false;
	private int tarPaneIndex = -1;
	private int tarSquareIndex = -1;
	private String tarSide = "";
	
	private static final BasicStroke base = new BasicStroke();
	
	private Point2D startDrag;
	private Point2D endDrag;
	private Cursor curCursor;
	private ConnectedLine tempLine;
	private boolean isHandCursor = false;
	
	public MappingDesktopPane(){
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void paintComponent(Graphics g){
		
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		g2.setStroke(base);
		drawTableRowNodes(g2);
		drawConnectingLines(g2);
		setCursor(curCursor);
	}

	public void reset(){
		isSquareCur = false;
		startPaneIndex = -1;
		squareIndex = -1;
		side = "";
	}
	
	public void targetReset(){
		isTarSquareCur = false;
		tarPaneIndex = -1;
		tarSquareIndex = -1;
		tarSide = "";
	}

	public void resetConnectedLines(boolean isPath){
		
		for(Set<ConnectedLine> s: startNodes.values()){
			
			for(ConnectedLine l: s){
				
				if(!isPath)
					l.setCurrentPath(null);
				
				l.setStartNodeChanged(false);
				l.setEndNodeChanged(false);
				l.setHasStartNode(true);
			}
		}
	}
	
	public void drawTableRowNodes(Graphics2D g2){
		
		for(MapperScrollPane sp: paneList){
				
			resetConnectedLines(false);
			
			int pane = sp.getPaneIndex();
			JTreeTable tb = sp.getJTreeTable();
			JTree tree = tb.getTree(); 
			
			int firstRow = sp.getTopRow();
			int visibleRows = sp.getVisibleRows();
			int lastRow = firstRow + visibleRows;
			
			Map<Integer, NodeElement> map = sp.getDrawMap();

			Rectangle r = null;
			Rectangle left = null;
    		Rectangle right = null;
    		
    		Rectangle frameRect = sp.getFrame().getBounds();
    		
		    for(int i = firstRow; i < lastRow; i++){
		    		
				NodeElement de = new NodeElement();
					
		    	r = tb.getCellRect(i, 0, false);
		    	r = SwingUtilities.convertRectangle(tb, r, this);
		    	
		    	r.setSize(frameRect.width, r.height);
		    	
		    	de.setIndex(i);
		    	de.setValue(tb.getValueAt(i, 0).toString());
		    			
		    	right = new Rectangle((int)frameRect.getMaxX() - 1 , (int)r.getCenterY() - 4 , 6, 6);
		        left = new Rectangle(((int)frameRect.getMinX() - 6),(int)r.getCenterY() -4, 6, 6);
		        	
		        de.setLeft(left);
		        de.setRight(right);
				
		        
		        TreePath path = tree.getPathForRow(i);
		        Set<ConnectedLine> s = null;
		        Set<ConnectedLine> s1 = null;
//		        boolean isStartNode = true;
		        
		        if(path != null){
		        	 
		        	 s = startNodes.get(new LineKey(path));
		        	 if(s == null){
		        		 s = endNodes.get(new LineKey(path));	
		        		 if(s != null){
			        		 for(ConnectedLine ln: s){
			        			 ln.setHasStartNode(false); 
			        		 }
		        		 }
//		        		 isStartNode = false;
		        	 }
		        	 else{
		        		 for(ConnectedLine ln: s){
		        			 ln.setHasStartNode(true); 
		        		 }
		        		 s1 = endNodes.get(new LineKey(path));	
		        		 if(s1 != null){
		        			 for(ConnectedLine ln: s1){
			        			 ln.setHasStartNode(false); 
			        		 }
		        			 s = new HashSet(s);
		        			 s.addAll(s1);
		        		 }
		        	 }
		        		 
		        	 
		        	 if(s != null){
					     for(ConnectedLine ln: s){
					        	
					        Line2D l = ln.getLine();
					        	
					        ln.setCurrentPath(path);
					       
						        if(ln.hasStartNode() && !ln.isStartNodeChanged()){
						        		
						        	ConnectedNode startNode = ln.getStartNode();
						        	Point2D start = null;
						        							
									Rectangle rec1 = null;
									start = l.getP1();
		
									if("R".equals(startNode.getSelectedSide())){
										rec1 = right;									
										start.setLocation(rec1.getMaxX(), rec1.getCenterY());
									}
									else{
										rec1 = left;					
										start.setLocation(rec1.getMinX(), rec1.getCenterY());
									}
											
									startNode.setSquareIndex(i);
									startNode.setDummySquareIndex(i);
									
									l.setLine(start, l.getP2());
									ln.setStartNodeChanged(true);
									g2.setPaint(new Color(0, 150, 0));
									g2.fill(rec1);
						        }
						        else if(!ln.isEndNodeChanged()){
						        	
						        	ConnectedNode endNode = ln.getEndNode();
						            Point2D end = null;
						        		
						        	Rectangle rec2 = null;
									end = l.getP2();
						        		
						        	if("R".equals(endNode.getSelectedSide())){
										rec2 = right;			
										end.setLocation(rec2.getMaxX(), rec2.getCenterY());
									}
									else{
										rec2 = left;									
										end.setLocation(rec2.getMinX(), rec2.getCenterY());
									}
						        	
						        	endNode.setSquareIndex(i);
						        	endNode.setDummySquareIndex(i);
						        	
						        	l.setLine(l.getP1(), end);
						        	ln.setEndNodeChanged(true);
						        	g2.setPaint(new Color(0, 150, 0));
						        	g2.fill(rec2);
						        }
						        
								g2.setPaint(Color.BLACK);	       
					     }
		        	 }
		        }
		       		    	
		        g2.draw(left);
	        	g2.draw(right);	   
	        	
    	        map.put(i, de);
			}
		    		   
		    
        	int rowIndex = -1;
        	int y = 0;
	        
        	resetConnectedLines(true);
        	
	        for(Set<ConnectedLine> s: startNodes.values()){
	        	
	        	for(ConnectedLine cl: s){
	          	
		        	if(cl.getCurrentPath() != null)
		        		continue;
		        	
		        	ConnectedNode stNode = cl.getStartNode();
		        	ConnectedNode eNode = cl.getEndNode();        	
		        	Line2D l = cl.getLine();
		        	Point2D start = l.getP1();
		        	Point2D end  = l.getP2();
		        	
		        	if(pane == stNode.getPaneIndex() && !cl.isStartNodeChanged()){
		        		
		        		if(stNode.getDummySquareIndex() < firstRow){
		        			rowIndex = firstRow;
		        			y = -25;
		        		}
		        		else if(stNode.getDummySquareIndex() > lastRow - 1){
		        			rowIndex = lastRow - 1;
		        			y = 25;
		        		}
		        		else{
		        			rowIndex = firstRow;
		        			y = -25;
		        		}
		        		
		    		  	r = tb.getCellRect(rowIndex, 0, false);
		    	    	r = SwingUtilities.convertRectangle(tb, r, this);
		    	    	
		    	    	r.setSize(frameRect.width, r.height);
		    	    			
		    	    	right = new Rectangle((int)frameRect.getMaxX() - 1 , (int)r.getCenterY() - 4 , 6, 6);
		    	        left = new Rectangle(((int)frameRect.getMinX() - 6),(int)r.getCenterY() -4, 6, 6);
		        		
		        		if("R".equals(stNode.getSelectedSide()))
		        			start.setLocation(right.getMinX(), right.getCenterY()+y);
		        	    else
		        			start.setLocation(left.getMaxX(), left.getCenterY()+y);
		        		
		        		stNode.setSquareIndex(-1);
		        	}
		        	else if(pane == eNode.getPaneIndex() && !cl.isEndNodeChanged()){
		        		
		        		if(eNode.getDummySquareIndex() < firstRow){
		        			rowIndex = firstRow;
		        			y = -25;
		        		}
		        		else if(eNode.getDummySquareIndex() > lastRow -1){
		        			rowIndex = lastRow - 1;
		        			y = 25;
		        		}
		        		else{
		        			rowIndex = firstRow;
		        			y = -25;
		        		}
		        		
		    		  	r = tb.getCellRect(rowIndex, 0, false);
		    	    	r = SwingUtilities.convertRectangle(tb, r, this);
		    	    	
		    	    	r.setSize(frameRect.width, r.height);
		    	    			
		    	    	right = new Rectangle((int)frameRect.getMaxX() - 1 , (int)r.getCenterY() - 4 , 6, 6);
		    	        left = new Rectangle(((int)frameRect.getMinX() - 6),(int)r.getCenterY() -4, 6, 6);
		        		
		        		if("R".equals(eNode.getSelectedSide()))
		        			end.setLocation(right.getMinX(), right.getCenterY()+y);
		        	    else
		        			end.setLocation(left.getMaxX(), left.getCenterY()+y);
		        		
		        		eNode.setSquareIndex(-1);
		        	}
		        	
		        	l.setLine(start, end);
	        	}	
	        }
		}	
		
			
		for(Set<ConnectedLine> s: startNodes.values()){

			for(ConnectedLine ln : s){
	//			g2.setPaint(new Color(150, 130, 15));
				g2.setPaint(Color.DARK_GRAY);
				g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
				
				if(ln.getLineColor() != null){
	
					ConnectedNode stNode = ln.getStartNode();
					ConnectedNode eNode = ln.getEndNode();
					
					MapperScrollPane sp = paneList.get(stNode.getPaneIndex());
					int index = stNode.getSquareIndex();
					
					if(index > -1)
						sp.getJTreeTable().setRowSelectionInterval(index, index);
					
					sp = paneList.get(eNode.getPaneIndex());
					index = eNode.getSquareIndex();
					
					if(index > -1)
						sp.getJTreeTable().setRowSelectionInterval(index, index);	
									
					g2.setPaint(ln.getLineColor());
					g2.setComposite(AlphaComposite.SrcOver);
				}
				
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		        g2.setStroke(base);
		        g2.draw(ln.getLine());
			}
		}
		
		 g2.setPaint(Color.BLACK);
		 g2.setComposite(AlphaComposite.SrcOver);
	}

	public Rectangle getSelectedNode(){
		MapperScrollPane sp = paneList.get(startPaneIndex);
		NodeElement de = sp.getDrawMap().get(squareIndex);
	
		Rectangle rec = null;
		
		if("R".equals(side)){
			rec = de.getRight();
			startDrag = new Point2D.Double(rec.getMaxX(), rec.getCenterY());
		}
		else if("L".equals(side)){
			rec = de.getLeft();
			startDrag = new Point2D.Double(rec.getMinX(), rec.getCenterY());
		}
		
		return rec;
	}
	
	public void drawConnectingLines(Graphics2D g2){
		
		if(isSquareCur && squareIndex > -1 && startPaneIndex > -1 ){
			getSquareFocus(g2);
		}
			
		if(isTarSquareCur && tarSquareIndex > -1 && tarPaneIndex > -1 ){
			getTargetSquareFocus(g2);
		}
		
		if(startDrag != null && endDrag != null) {
			drawDottedLines(g2);
		}	
	}
	
	public void getSquareFocus(Graphics2D g2){
		
	    MapperScrollPane sp = paneList.get(startPaneIndex);
		NodeElement de = sp.getDrawMap().get(squareIndex);
	
		Rectangle rec = null;
		
		if("R".equals(side))
			rec = de.getRight();
		else if("L".equals(side))
			rec = de.getLeft();
			
		g2.setStroke(new BasicStroke(3));
		g2.draw(rec);
			
		int i = de.getIndex();
			
		sp.getJTreeTable().setRowSelectionInterval(i, i);	
	}
	
	public void getTargetSquareFocus(Graphics2D g2){
		
	    MapperScrollPane sp = paneList.get(tarPaneIndex);
		NodeElement de = sp.getDrawMap().get(tarSquareIndex);
	
		Rectangle rec = null;
		
		if("R".equals(tarSide))
			rec = de.getRight();
		else if("L".equals(tarSide))
			rec = de.getLeft();
			
		g2.setStroke(new BasicStroke(3));
		g2.draw(rec);
			
		int i = de.getIndex();
			
		sp.getJTreeTable().setRowSelectionInterval(i, i);	
	}
	
	public void drawDottedLines(Graphics2D g2){
		
		g2.setPaint(new Color(50, 180, 200));
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Line2D l = new Line2D.Double(startDrag, endDrag);
        g2.setStroke (new BasicStroke(
        	      1.7f, 
        	      BasicStroke.CAP_ROUND, 
        	      BasicStroke.JOIN_ROUND, 
        	      0, 
        	      new float[] {0.05f, 4f}, 
        	      0));
        g2.draw(l);	
	}
	
	public List<MapperScrollPane> getPaneList() {
		return paneList;
	}

	public void setPaneList(List<MapperScrollPane> paneList) {
		this.paneList = paneList;
	}

	public JTable getMappingTable() {
		return mappingTable;
	}

	public void setMappingTable(JTable mappingTable) {
		
		mappingTable.addMouseListener(new MapperTableClick());
		mappingTable.addKeyListener(new MapperTableKey());
		this.mappingTable = mappingTable;
	}

	public void mouseClicked(MouseEvent e) {}


	public void mousePressed(MouseEvent e) {
		
		startDrag = null;
		
		if(isSquareCur && squareIndex > -1 && startPaneIndex > -1 ){
			
			getSelectedNode();
		}
		
		if(isHandCursor && tempLine != null){
			
			ConnectedNode st = tempLine.getStartNode();
			ConnectedNode en = tempLine.getEndNode();
			
			Set<ConnectedLine> s1 = startNodes.get(new LineKey(st.getPath()));
			Set<ConnectedLine> s2 = endNodes.get(new LineKey(en.getPath()));
			
			if(s1 != null && !s1.isEmpty())
				s1.remove(tempLine);
			if(s2 != null && !s2.isEmpty())
				s2.remove(tempLine);
			
			if(s1.isEmpty())
				startNodes.remove(new LineKey(st.getPath()));
			if(s2.isEmpty())
				endNodes.remove(new LineKey(en.getPath()));
			
			curCursor = Cursor.getDefaultCursor();
			
			MapperScrollPane sp = paneList.get(st.getPaneIndex());
			sp.getJTreeTable().clearSelection();
			
			sp = paneList.get(en.getPaneIndex());
			sp.getJTreeTable().clearSelection();
			
			int row = tempLine.getMappingRow();
			
			for(Set<ConnectedLine> s: startNodes.values()){			
				for(ConnectedLine ln: s){
					if(ln.getMappingRow() > row){
						ln.setMappingRow(ln.getMappingRow() -1);
					}	
				}
			}
			
			DefaultTableModel model = (DefaultTableModel)mappingTable.getModel();
			model.removeRow(row);
			
			repaint();
		}
	}



	public void mouseReleased(MouseEvent e) {
		
		Line2D line = null;
		ConnectedNode startNode = null;
		ConnectedNode endNode = null;
		
		o:
		if(startDrag != null && endDrag != null && tarPaneIndex > -1 && tarSquareIndex > -1 ){
			
			line = new Line2D.Double(startDrag, endDrag);
			startNode = getCNode(startPaneIndex, squareIndex, side, startDrag);
			endNode = getCNode(tarPaneIndex, tarSquareIndex, tarSide, endDrag);
			
			ConnectedLine ln = new ConnectedLine();
			ln.setLine(line);
			ln.setStartNode(startNode);
			ln.setEndNode(endNode);
			
			Set<ConnectedLine> s1 = startNodes.get(new LineKey(endNode.getPath()));
			Set<ConnectedLine> s2 = endNodes.get(new LineKey(startNode.getPath()));
			
			if(s1 != null && s2 != null){
				if(s1.contains(ln) && s2.contains(ln))
					break o;
			}
			
			s1 = startNodes.get(new LineKey(startNode.getPath()));
			s2 = endNodes.get(new LineKey(endNode.getPath()));
			
			if(s1 != null && s2 != null){
				if(s1.contains(ln) && s2.contains(ln))
					break o;
			}	
			if(s1 == null){
				s1 = new HashSet<ConnectedLine>();
			}
			if(s2 == null){
				s2 = new HashSet<ConnectedLine>();
			}
			s1.add(ln);
			s2.add(ln);
			
			startNodes.put(new LineKey(startNode.getPath()), s1);
			endNodes.put(new LineKey(endNode.getPath()), s2);

			
			DefaultTableModel model = (DefaultTableModel)mappingTable.getModel();
			TreePath p1 = null;
			TreePath p2 = null;
			
			if(startPaneIndex < 2 && tarPaneIndex < 2){
				if(startPaneIndex == 0){
					p1 = startNode.getPath();
					p2 = endNode.getPath();
				}
				else{
					p1 = endNode.getPath();
					p2 = startNode.getPath();
				}
				
				model.addRow(new Object[]{p1, "", p2});
				
				ln.setMappingRow(mappingTable.getRowCount() -1);
			}
			
			MapperScrollPane mp = null;
			
			if(startPaneIndex > 1 ){
				
				mp = startNode.getmPane();
				
				if(tarPaneIndex == 0){		
					mp.getSource().put(mp.getSource().size(), endNode.getPath());
				}
				if(tarPaneIndex == 1){
					char c = mp.getDefChar();
					mp.getTarget().put(c, endNode.getPath());
					mp.setDefChar(++c);
				}	
				
				addTableRow(mp, model);
				
			}
			else if(tarPaneIndex > 1){
				
				mp = endNode.getmPane();
				
				if(startPaneIndex == 0){		
					mp.getSource().put(mp.getSource().size(), startNode.getPath());
				}
				if(startPaneIndex == 1){
					char c = mp.getDefChar();
					mp.getTarget().put(c, startNode.getPath());
					mp.setDefChar(++c);
				}	
				
				addTableRow(mp, model);
			}	
		}
		
		startDrag = null;
		endDrag = null;
		targetReset();
		
		repaint();
	}

	public ConnectedNode getCNode(int pane, int square, String side, Point2D p){
		
		ConnectedNode n = null;
		
		if(pane > -1 && square > -1){
			n  = new ConnectedNode();
			n.setPaneIndex(pane);
			n.setSelectedSide(side);
			n.setSquareIndex(square);
			n.setPoint(p);
			
			MapperScrollPane sp = paneList.get(pane);	
			TreePath path = sp.getJTreeTable().getTree().getSelectionPath();
			n.setPath(path);
			n.setmPane(sp);
			n.setNodeType(sp.getPaneName());
		}
		
		return n;
	}
	
	public void addTableRow(MapperScrollPane mp, DefaultTableModel model){
		
		StringBuilder source = new StringBuilder("");
		StringBuilder target = new StringBuilder("");
		StringBuilder sMap = new StringBuilder("");
		StringBuilder tMap = new StringBuilder("");
		String mapping = null;
		
		for(Iterator<Integer> lt = mp.getSource().keySet().iterator(); lt.hasNext();){
			
			Integer i = lt.next();
			TreePath p = mp.getSource().get(i);
			
			if(lt.hasNext()){
				source.append(i+1+". "+ p + "\n");
				sMap.append(i+1 +" + ");
			}
			else{
				source.append(i+1+". "+ p);
				sMap.append(i+1);
			}
		}
		for(Iterator<Character> lt = mp.getTarget().keySet().iterator(); lt.hasNext();){
			
			Character c = lt.next();
			TreePath p = mp.getTarget().get(c);
			
			if(lt.hasNext()){
				target.append(c+". "+ p + "\n");
				tMap.append(c+" + ");
			}
			else{
				target.append(c+". "+ p);
				tMap.append(c);
			}
		}
		
		if(sMap.toString().trim().length() > 0 && tMap.toString().trim().length() > 0){
			mapping = sMap.toString() + " ---> " + tMap.toString();
		}
		else if(sMap.toString().trim().length() > 0){
			mapping = sMap.toString();
		}
		else if(tMap.toString().trim().length() > 0){
			mapping = tMap.toString();
		}
		
		int row = mp.getRowIndex();
		if(row < 0){
			model.addRow(new Object[]{source, mapping, target});
			mp.setRowIndex(mappingTable.getRowCount() -1);
		}
		else{
			model.setValueAt(source, row, 0);
			model.setValueAt(mapping, row, 1);
			model.setValueAt(target, row, 2);
		}
		
	}
	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}


	public void mouseDragged(MouseEvent e) {
		
		targetReset();
		Line2D l = null;
		
		if(startDrag != null){
			
			endDrag = new Point2D.Double(e.getX(), e.getY());
			l = new Line2D.Double(startDrag, endDrag);
			
		outer:	for(int i=0; i < paneList.size(); i++){
				
				if(i == startPaneIndex)
					continue;
				
				MapperScrollPane sp = paneList.get(i);
				int firstRow = sp.getTopRow();
				int visibleRows = sp.getVisibleRows();
				int lastRow = firstRow + visibleRows;
				
				Map<Integer, NodeElement> map = sp.getDrawMap();

				for(int j = firstRow; j < lastRow; j++){
					
					NodeElement de = map.get(j);
					
					Rectangle r1 = de.getLeft();
					Rectangle r2 = de.getRight();
					
					boolean bl = l.intersects(r1);
					boolean br = l.intersects(r2);
									
					
					if( bl || br ){
						isTarSquareCur = true;
						tarPaneIndex = i;
						tarSquareIndex = de.getIndex();
						tarSide = bl ? "L" : "R";
						
						if(bl)
							endDrag = new Point2D.Double(r1.getMinX(), r1.getCenterY());
						else 
							endDrag = new Point2D.Double(r2.getMaxX(), r2.getCenterY());
						
						break outer;
					}
				}
			}
		}
		
		repaint();
	}


	public void mouseMoved(MouseEvent e) {
		
		reset();
		
		int pane = -1;
				
outer: for(MapperScrollPane sp: paneList){
			
			pane++;
			Map<Integer, NodeElement> map = sp.getDrawMap();
			
			sp.getJTreeTable().clearSelection();
			
			int firstRow = sp.getTopRow();
			int visibleRows = sp.getVisibleRows();
			int lastRow = firstRow + visibleRows;
			
			for(int i = firstRow; i < lastRow; i++){
				
				NodeElement de = map.get(i);
				
				if(de == null)
					continue;
				
				Rectangle r1 = de.getLeft();
				Rectangle r2 = de.getRight();
				
				boolean bl = r1.contains(e.getX(), e.getY());
				boolean br = r2.contains(e.getX(), e.getY());
				
				if( bl || br ){
					isSquareCur = true;
					startPaneIndex = pane;
					squareIndex = de.getIndex();
					side = bl ? "L" : "R";
					break outer;
				}
			}
		}
		
		curCursor = Cursor.getDefaultCursor();
		isHandCursor = false;
		tempLine = null;
		boolean selected = false;	
		mappingTable.clearSelection();
		
		for(Set<ConnectedLine> s: startNodes.values()){
			
			for(ConnectedLine cLine: s){
			
				Line2D l = cLine.getLine();		
				cLine.setLineColor(null);
				
				if(!isSquareCur && !selected && l.ptSegDist(e.getX(), e.getY()) < 3){
					
//					cLine.setLineColor(new Color(20, 170, 190));
					cLine.setLineColor(Color.RED);
					selected = true;
					tempLine = cLine;
					curCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
					isHandCursor = true;
/***************************************************************************************/			
/*					int index = cLine.getMappingRow();
					mappingTable.setRowSelectionInterval(index, index);*/
				}
			}	
		}

		repaint();
	}
	
	
	public void highlight(){
		
		int row = mappingTable.getSelectedRow();
		for(Set<ConnectedLine> s: startNodes.values()){
			
			for (ConnectedLine l: s){
			
				if(row == l.getMappingRow()){
					l.setLineColor(Color.RED);
				}
				else
					l.setLineColor(null);
			}
		}
		
		repaint();	
	}
	
	class MapperTableClick extends  MouseAdapter{
		
		public void mouseClicked(MouseEvent e){
			
			highlight();
		}
	}
	
	class MapperTableKey extends KeyAdapter{
		
		public void keyReleased(KeyEvent e){
			
			int keyCode = e.getKeyCode();
			if(keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_UP 
					|| keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_PAGE_UP
					|| keyCode == KeyEvent.VK_PAGE_DOWN){
				
				highlight();		
			}		
		}
		
		public void keyPressed(KeyEvent e){
			
			int keyCode = e.getKeyCode();
			boolean deleted = false;

			if(keyCode == KeyEvent.VK_DELETE){
				
				int row = mappingTable.getSelectedRow();
				
				Iterator<Entry<LineKey, Set<ConnectedLine>>> lt =	startNodes.entrySet().iterator();
				
				while(lt.hasNext()){
					
					Map.Entry<LineKey, Set<ConnectedLine>> pair = lt.next();
					
					for(ConnectedLine l: pair.getValue()){
					
						if(row == l.getMappingRow()){
							
							ConnectedNode st = l.getStartNode();
							ConnectedNode en = l.getEndNode();
								
							lt.remove();
//							startNodes.remove(new LineKey(st.getPath()));	
							endNodes.remove(new LineKey(en.getPath()));
							
							MapperScrollPane sp = paneList.get(st.getPaneIndex());
							sp.getJTreeTable().clearSelection();
							
							sp = paneList.get(en.getPaneIndex());
							sp.getJTreeTable().clearSelection();
							
							DefaultTableModel model = (DefaultTableModel)mappingTable.getModel();
							model.removeRow(row);
							
							deleted = true;
							break;
						}
					}
				}
				
				for(Set<ConnectedLine> s: startNodes.values()){
					
					for(ConnectedLine l: s){
						
						if(deleted && l.getMappingRow() > row){
							l.setMappingRow(l.getMappingRow() - 1);
						}
					}
				}
				
				repaint();
			}	
		}
	}
}