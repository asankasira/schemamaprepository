package samples;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;
import java.util.Map.Entry;

public class TextAreaRenderer extends JTextArea implements TableCellRenderer {
	
  private final DefaultTableCellRenderer adaptee =
      new DefaultTableCellRenderer();
  /** map from table to map of rows to map of column heights */
  private final Map<JTable, Map<Integer, Map<Integer, Integer>>> cellSizes = new HashMap();

  public TextAreaRenderer() {
    setLineWrap(true);
    setWrapStyleWord(true);
  }

  public Component getTableCellRendererComponent(//
      JTable table, Object obj, boolean isSelected,
      boolean hasFocus, int row, int column) {
    // set the colours, etc. using the standard for that platform
    adaptee.getTableCellRendererComponent(table, obj,
        isSelected, hasFocus, row, column);
    setForeground(adaptee.getForeground());
    setBackground(adaptee.getBackground());
    setBorder(adaptee.getBorder());
    setFont(adaptee.getFont());
    setText(adaptee.getText());
    
    // This line was very important to get it working with JDK1.4
    TableColumnModel columnModel = table.getColumnModel();
    setSize(columnModel.getColumn(column).getWidth(), 100000);
    int height_wanted = (int) getPreferredSize().getHeight();
    addSize(table, row, column, height_wanted);
    height_wanted = findTotalMaximumRowSize(table, row);
    if (height_wanted != table.getRowHeight(row)) {
      table.setRowHeight(row, height_wanted);
    }
    return this;
  }

  private void addSize(JTable table, int row, int column,
                       int height) {
    Map<Integer, Map<Integer, Integer>> rows = cellSizes.get(table);
    if (rows == null) {
      cellSizes.put(table, rows = new HashMap());
    }
    Map<Integer, Integer> rowheights = rows.get(row);
    if (rowheights == null) {
      rows.put(row, rowheights = new HashMap());
    }
    rowheights.put(column, height);
  }

  /**
   * Look through all columns and get the renderer.  If it is
   * also a TextAreaRenderer, we look at the maximum height in
   * its hash table for this row.
   */
  private int findTotalMaximumRowSize(JTable table, int row) {
    int maximum_height = 0;
    Enumeration columns = table.getColumnModel().getColumns();
    while (columns.hasMoreElements()) {
      TableColumn tc = (TableColumn) columns.nextElement();
      TableCellRenderer cellRenderer = tc.getCellRenderer();
      if (cellRenderer instanceof TextAreaRenderer) {
        TextAreaRenderer tar = (TextAreaRenderer) cellRenderer;
        maximum_height = Math.max(maximum_height,
            tar.findMaximumRowSize(table, row));
      }
    }
    return maximum_height;
  }

  private int findMaximumRowSize(JTable table, int row) {
    Map<Integer, Map<Integer, Integer>> rows = cellSizes.get(table);
    if (rows == null) return 0;
    Map<Integer, Integer> rowheights = rows.get(row);
    if (rowheights == null) return 0;
    int maximum_height = 0;
    for (Iterator<Entry<Integer, Integer>> it = rowheights.entrySet().iterator();
         it.hasNext();) {
      Map.Entry<Integer, Integer> entry = it.next();
      int cellHeight = entry.getValue();
      maximum_height = Math.max(maximum_height, cellHeight);
    }
    return maximum_height;
  }
}