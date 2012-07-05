/*
 * Copyright Luke Wallin 2012
 */
package fractal;

import java.awt.Graphics;
import java.awt.Point;

/**
 *
 * @author Luke
 */
public class FractalPanel extends javax.swing.JPanel {
    
    private Fractal fractal;
    private Point mouseDown;
    
    public void setFractal(Fractal f){
        fractal=f;
    }
    
    public FractalPanel(Fractal _fractal, int width, int height){
        
        fractal=_fractal;
        
        setSize(new java.awt.Dimension(width, height));
        addMouseWheelListener(new java.awt.event.MouseWheelListener() {

            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                mouseWheel(evt);
            }
        });

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                _mouseReleased(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                _mousePressed(evt);
            }
        });

        
        
        setVisible(true);
    }
    
    private void _mousePressed(java.awt.event.MouseEvent evt) {
        mouseDown = evt.getPoint();
    }

    private void _mouseReleased(java.awt.event.MouseEvent evt) {
        Point mouseUp = evt.getPoint();

        fractal.drag(mouseDown, mouseUp);

        mouseDown = null;
        //repaint();
    }

    private void mouseWheel(java.awt.event.MouseWheelEvent evt) {
        int scroll = evt.getWheelRotation();//.getPreciseWheelRotation();
        fractal.scroll(scroll);
        //stop page from scrolling in applet
        evt.consume();
        //repaint();
    }

    
    
    @Override
    public void paint(Graphics g) {
        fractal.draw(g);
    }
}
