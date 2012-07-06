/*
 * Copyright Luke Wallin 2012
 */
package fractal;

import LukesBits.Complex;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import javax.swing.JPanel;

/**
 *
 * @author Luke
 * 
 * for selecting a point on the mandelbrot
 * 
 */
public class JuliaSelectPanel extends JPanel implements IFractalWindow{
    
    private Fractal fractal;
    private int width,height;
    private CustomJuliaDialogue dialogue;
    
    public JuliaSelectPanel(int _width, int _height,CustomJuliaDialogue _dialogue){
        fractal=new Fractal(_width, _height);
        width=_width;
        height=_height;
        
        fractal.setWindow(this);
        fractal.reset();
        fractal.getFunctionOfZ().setCycleMultiplier(0);
        
        dialogue=_dialogue;
        
        setPreferredSize(new Dimension(width,height));
        
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                _mousePressed(evt);
            }
        });
        
        addMouseWheelListener(new java.awt.event.MouseWheelListener() {

            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                mouseWheel(evt);
            }
        });
    }
    
    private void _mousePressed(java.awt.event.MouseEvent evt){
        Point p = evt.getPoint();
        
        Complex mu = fractal.pixelToComplex(p.x, p.y);
        
        dialogue.setMu(mu);
        
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

    @Override
    public void saving(int progress) {
        
    }
    
    public void reset(){
        fractal.reset();
        fractal.setDetail(100);
        fractal.getFunctionOfZ().setCycleMultiplier(0);
    }
}
