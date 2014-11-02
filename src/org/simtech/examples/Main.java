package org.simtech.examples;
/*
 *
 * Copyright (c) 1998 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Main extends JPanel implements MouseWheelListener {
  JTextArea textArea = new JTextArea();

  JScrollPane scrollPane = new JScrollPane(textArea);

  final static String newline = "\n";

  public Main() {
    super(new BorderLayout());
    textArea.setEditable(false);
    
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setPreferredSize(new Dimension(400, 250));
    add(scrollPane, BorderLayout.CENTER);
    textArea.append("This text area displays information " + "about its mouse wheel events."
        + newline);

    textArea.addMouseWheelListener(this);

    setPreferredSize(new Dimension(450, 350));
    setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
  }

  public void mouseWheelMoved(MouseWheelEvent e) {
    String message;
    int notches = e.getWheelRotation();
    if (notches < 0) {
      message = "Mouse wheel moved UP " + -notches + " notch(es)" + newline;
    } else {
      message = "Mouse wheel moved DOWN " + notches + " notch(es)" + newline;
    }
    if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
      message += "    Scroll type: WHEEL_UNIT_SCROLL" + newline;
      message += "    Scroll amount: " + e.getScrollAmount() + " unit increments per notch"
          + newline;
      message += "    Units to scroll: " + e.getUnitsToScroll() + " unit increments" + newline;
      message += "    Vertical unit increment: "
          + scrollPane.getVerticalScrollBar().getUnitIncrement(1) + " pixels" + newline;
    } else { // scroll type == MouseWheelEvent.WHEEL_BLOCK_SCROLL
      message += "    Scroll type: WHEEL_BLOCK_SCROLL" + newline;
      message += "    Vertical block increment: "
          + scrollPane.getVerticalScrollBar().getBlockIncrement(1) + " pixels" + newline;
    }
    saySomething(message, e);
  }

  void saySomething(String eventDescription, MouseWheelEvent e) {
    textArea.append(e.getComponent().getClass().getName() + ": " + eventDescription);
    textArea.setCaretPosition(textArea.getDocument().getLength());
  }
  public static void main(String[] args) {
    JFrame frame = new JFrame("MouseWheelEventDemo");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JComponent newContentPane = new Main();
    newContentPane.setOpaque(true);
    frame.setContentPane(newContentPane);

    frame.pack();
    frame.setVisible(true);
  }
}
