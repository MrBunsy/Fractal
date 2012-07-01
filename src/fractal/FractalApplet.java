/*
 * Copyright Luke Wallin 2012
 */
package fractal;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.*;
import javax.swing.JApplet;

/**
 *
 * @author Luke
 */
public class FractalApplet extends JApplet implements IFractalWindow, MouseWheelListener,MouseListener,KeyListener{

    Fractal fractal;
    private Point mouseDown;
    /**
     * Initialization method that will be called after the applet is loaded into
     * the browser.
     */
    public void init() {
        fractal = new Fractal(600, 600,false,1);
        fractal.setWindow(this);
        
        addMouseWheelListener(this);
        addMouseListener(this);
        addKeyListener(this);
    }
    // TODO overwrite start(), stop() and destroy() methods
    public void keyPressed(KeyEvent e) {                       
        fractal.key(e.getKeyCode());
        
    }
    public void paint(Graphics g){
        fractal.draw(g);
    }
    
    public void update(Graphics g){
        paint(g);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        fractal.scroll(e.getWheelRotation());
        //repaint();
        e.consume();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
         mouseDown = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Point mouseUp = e.getPoint();

        fractal.drag(mouseDown, mouseUp);

        mouseDown = null;

        //repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
