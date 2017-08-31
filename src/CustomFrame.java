

import java.awt.BorderLayout;
import java.awt.event.*;

import javax.swing.*;


public class CustomFrame extends JFrame implements MouseListener, WindowListener {
  
  public CustomFrame()
  {
	  getContentPane().setLayout(new BorderLayout());  
    addWindowListener(this);
  }
  
//MouseListener methods
  public void mouseClicked(MouseEvent me){} 
  public void mouseEntered(MouseEvent me){}
  public void mouseExited(MouseEvent me){} 
  public void mousePressed(MouseEvent me){} 
  public void mouseReleased(MouseEvent me) {}
  
  // WindowListener methods
  public void windowActivated(WindowEvent we){} 
  public void windowClosed(WindowEvent we){} 
  public void windowClosing(WindowEvent we){System.exit(0);} 
  public void windowDeactivated(WindowEvent we){}
  public void windowDeiconified(WindowEvent we){} 
  public void windowIconified(WindowEvent we){}
  public void windowOpened(WindowEvent we){}
}
