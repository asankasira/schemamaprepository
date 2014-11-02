package org.simtech.dmtt.ui.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultDesktopManager;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.simtech.dmtt.ui.borders.OvalBorder;

import samples.TextAreaRenderer;


/**
 * Internal Frames Demo
 * 
 * @version $Id: JIFrameDemo.java,v 1.4 2003/07/15 01:46:47 ian Exp $
 */
public class SchemaMatcherApp {

	
  private JFrame frame;
	
  private InternaNodeFrame source;
  private InternaNodeFrame target;
  private MappingDesktopPane desktopPane;
  private MapperScrollPane pane;
  
  private List<MapperScrollPane> paneList = new ArrayList<MapperScrollPane>(); 
  private Color activeColor;

  private JTable table;
 
	
  /* Main View */
  public static void main(String[] a) {
	
	  try {

          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
          
      } catch (ClassNotFoundException ex) {
      } catch (InstantiationException ex) {
      } catch (IllegalAccessException ex) {
      } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      }
	  
	  new SchemaMatcherApp().startApplication();
  }
  
 
  public void startApplication(){
	  
	  	createMainFrame();
	  	createDesktopPane();
	  	
	    initSourceFrame();
	    initTargetFrame();
	    
	    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, desktopPane, createMappingTable());
	    splitPane.setResizeWeight(0.7);
	    splitPane.setContinuousLayout(true);
	    JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JPanel(), splitPane);
	    sp.setResizeWeight(0.2);
	    sp.setContinuousLayout(true);
	    
	    desktopPane.add(source);
	    desktopPane.add(target);
	    desktopPane.setPaneList(paneList); 
	    desktopPane.setMappingTable(table);
	    
	    InputStream in = URLClassLoader.class.getResourceAsStream("/images/1.png");
	    Image image = null;
	    try {
			image = ImageIO.read(in);
			image = image.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    Action a = new AbstractAction("", new ImageIcon(image)) {
			
			public void actionPerformed(ActionEvent e) {
				
				pane = new MapperScrollPane(paneList.size());
			  	pane.setPane(desktopPane);
			  	pane.setPaneName("C");
			  	paneList.add(pane);
				
				InternaNodeFrame frame = new InternaNodeFrame("C");
//				frame.setResizable(true);
				BasicInternalFrameUI bUI = (BasicInternalFrameUI)frame.getUI();
//				bUI.setNorthPane(null);
//				BasicInternalFrameTitlePane tp = (BasicInternalFrameTitlePane) bUI.getNorthPane();
//				tp.setPreferredSize(new Dimension(0, 0));
//				
				UIManager.put("InternalFrame.titlePaneHeight", new Integer(12));
				Font f = source.getFont();
				UIManager.put("InternalFrame.titleFont", new Font(f.getName(), f.getStyle(), f.getSize() - 2));
//				
//				frame.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
//				frame.putClientProperty("JInternalFrame.isPalette", Boolean.TRUE);
//				UIManager.put("InternalFrame.paletteTitleHeight", 10);
				frame.updateUI();
		
				frame.setBounds(400, 10, 50, 40);
				frame.setBorder(new OvalBorder(5, 5, activeColor, activeColor));
				desktopPane.add(frame);
				pane.setFrame(frame);
				frame.setContentPane(pane.getScPane());
				frame.setFrameIcon(null);
				frame.setVisible(true);
			}
		};
	   
		a.putValue(Action.SHORT_DESCRIPTION, "Concat");
	    
	    JToolBar bar = new JToolBar();
	    bar.add(a);
	    
//	    frame.setContentPane(desktopPane);
	    frame.add(bar, BorderLayout.NORTH);
	    frame.add(sp, BorderLayout.CENTER);
	    frame.validate();
  }
  
  public void initSourceFrame(){
	  
	  	pane = new MapperScrollPane(paneList.size());
	  	pane.setPane(desktopPane);
	  	paneList.add(pane);
	   
	    source = new InternaNodeFrame("Source", true, false, false, false);
//	    source.setPaneTest(pane);
	    pane.setFrame(source);
	    
	    source.setContentPane(pane.getScPane());
	    source.setBorder(new OvalBorder(5, 5, activeColor, activeColor));
	    source.setSize(230, 260);
	    source.setLocation(80, 50);
	    source.setVisible(true);
	    source.getContentPane().setBackground(Color.WHITE);
	    source.setFrameIcon(null);   
  }
  
  public void initTargetFrame(){
	  
	  	pane = new MapperScrollPane(paneList.size());
	  	pane.setPane(desktopPane);
	  	paneList.add(pane);
	  
	    target = new InternaNodeFrame("Target", true, false, false, false);
	    
//	    target.setPaneTest(pane);
	    pane.setFrame(target);
	    
	    target.setBorder(new OvalBorder(5, 5, activeColor, activeColor));
	    target.setContentPane(pane.getScPane());
	    target.setSize(230, 260);
	    target.setLocation(650, 120);
	    target.setVisible(true);    
	    target.setFrameIcon(null);
  }
  
  public void createMainFrame(){
	 
	    frame = new JFrame("Schema Matching");
	    ImageIcon ic = new ImageIcon("images.jpg");
	    frame.setIconImage(ic.getImage());
	    
	    activeColor = UIManager.getColor("activeCaption");
	    
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    screenSize.width -= 90;
	    screenSize.height -= 90;
	    frame.setSize(screenSize);
	    frame.setLocation(20, 20);

	    JMenuBar mb = new JMenuBar();
	    frame.setJMenuBar(mb);
	    JMenu fm = new JMenu("File");
	    mb.add(fm);
	    JMenuItem mi;
	    fm.add(mi = new JMenuItem("Exit"));
	    mi.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        System.exit(0);
	      }
	    });
	    
	    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	    frame.setVisible(true);
	    frame.addWindowListener(new WindowAdapter() {
	      public void windowClosing(WindowEvent e) {
	    	frame.setVisible(false);
	    	frame.dispose();
	        System.exit(0);
	      }
	    }); 
  }
  
  public void createDesktopPane(){
	 
	  	desktopPane = new MappingDesktopPane();
	    
	    desktopPane.setBackground(frame.getBackground());
	    desktopPane.setDesktopManager(new DefaultDesktopManager(){
	    	public void dragFrame(JComponent f, int newX, int newY) {
	    		super.dragFrame(f, newX, newY);	    		
	    		desktopPane.reset();
	    		desktopPane.repaint();
	    	}
	    	
	    	public void resizeFrame(JComponent f, int newX, int newY, int newWidth, int newHeight) {
	    		super.resizeFrame(f, newX, newY, newWidth, newHeight);
	    		desktopPane.reset();
	    		desktopPane.repaint();
	    	}
	    		    	  
	    	public void activateFrame(JInternalFrame f) {
	    
	    	}
	    });
  }
  
  public JScrollPane createMappingTable(){
	  
	  DefaultTableModel model = new DefaultTableModel(){
		  
		  public boolean isCellEditable(int row, int column) {
		        return false;
		  }
	  };
	  
	  table = new JTable(model);
	  table.getTableHeader().setReorderingAllowed(false);
	  table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	  table.setSelectionBackground(new Color(250, 250, 150));
	  table.setSelectionForeground(table.getForeground());
	  table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	  
	  TableColumnModel colModel = table.getColumnModel();
	  
	  model.addColumn("Source");
	  model.addColumn("Mapping");
	  model.addColumn("Target");
	  
	  colModel.getColumn(0).setPreferredWidth(455);
	  colModel.getColumn(1).setPreferredWidth(162);
	  colModel.getColumn(2).setPreferredWidth(455);
	  
	  ((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer())
	    .setHorizontalAlignment(JLabel.CENTER);
	  
	  TextAreaRenderer textAreaRenderer = new TextAreaRenderer();
	  
	  colModel.getColumn(0).setCellRenderer(textAreaRenderer);
	  colModel.getColumn(1).setCellRenderer(textAreaRenderer);
	  colModel.getColumn(2).setCellRenderer(textAreaRenderer);
	  
	  JScrollPane scrollPane = new JScrollPane(table);
	  scrollPane.setPreferredSize(new Dimension(scrollPane.getWidth(), scrollPane.getHeight()));
//	  Rectangle rect = scrollPane.getBounds();
//	  rect.setSize(rect.width, rect.height - 100);
//	  scrollPane.setBounds(rect);
//	  panel.add(scrollPane, BorderLayout.CENTER);
	  
	  return scrollPane;
  }
}
