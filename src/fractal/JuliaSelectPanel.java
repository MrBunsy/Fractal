/*
 * Copyright Luke Wallin 2012
 */
package fractal;

import LukesBits.Complex;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import javax.swing.BorderFactory;
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
    private JuliaSelectDialogue dialogue;
    private Point mouseDown;
    
    public JuliaSelectPanel(int _width, int _height,JuliaSelectDialogue _dialogue){
        fractal=new Fractal(_width, _height,_dialogue.getThreads());
        width=_width;
        height=_height;
        
        fractal.setWindow(this);
        fractal.reset();
        fractal.getFunctionOfZ().setCycleMultiplier(0);
        //fractal.getFunctionOfZ().setCycleOffset(0.5);
        
        dialogue=_dialogue;
        
        //setBorder(BorderFactory.createLineBorder(Color.black));
        
        setPreferredSize(new Dimension(width,height));
        
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                _mousePressed(evt);
            }
            
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                _mouseReleased(evt);
            }
            
            public void mouseClicked(java.awt.event.MouseEvent evt){
                _mouseClicked(evt);
            }
        });
        
        addMouseWheelListener(new java.awt.event.MouseWheelListener() {

            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                mouseWheel(evt);
            }
        });
    }
    
    //selecting a value for mu
    private void _mouseClicked(java.awt.event.MouseEvent evt){
        Point p = evt.getPoint();
        
        Complex mu = fractal.pixelToComplex(p.x, p.y);
        
        dialogue.setMu(mu);
        
    }
    
    private void _mousePressed(java.awt.event.MouseEvent evt) {
        mouseDown = evt.getPoint();
    }
    

    private void _mouseReleased(java.awt.event.MouseEvent evt) {
        Point mouseUp = evt.getPoint();

        fractal.drag(mouseDown, mouseUp);

        mouseDown = null;
    }
    
     private void mouseWheel(java.awt.event.MouseWheelEvent evt) {
        int scroll = evt.getWheelRotation();//.getPreciseWheelRotation();
        fractal.scroll(scroll);
        //stop page from scrolling in applet
        
        double zoom = fractal.getZoom();
        //good rough detail level for the zoom
        //int detail = (int)Math.round(100d/Math.sqrt(zoom));
        int detail = (int)Math.round(100d/Math.pow(zoom,0.3));
        
        fractal.setDetail(detail);
        
        evt.consume();
        //repaint();
    }
    
    @Override
    public void paint(Graphics g) {
        fractal.draw(g);
        g.setColor(Color.LIGHT_GRAY);
        g.drawRect(0, 0, width-1, height-1);
    }

    @Override
    public void saving(int progress) {
        
    }
    
    public void reset(){
        fractal.reset();
        fractal.setDetail(100);
        fractal.getFunctionOfZ().setCycleMultiplier(0);
        //fractal.getFunctionOfZ().setCycleOffset(0.5);
    }
}
