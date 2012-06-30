/*
 * Copyright Luke Wallin 2012
 */
package fractal;

import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author Luke
 */
public class FractalWindow extends javax.swing.JFrame {

    private Fractal fractal;
    
    /**
     * Creates new form FractalWindow
     */
    public FractalWindow(Fractal _fractal, int width, int height) {
        fractal=_fractal;
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(width, height));
        setResizable(false);
        
//        while(getBufferStrategy() == null){
//            createBufferStrategy(2);
//        }
    }



     public void paint(Graphics g){
        //Graphics _g = getBufferStrategy().getDrawGraphics();
        
        //Dimension d = getSize();
        
        fractal.draw(g);//,d.width,d.height);
//        getBufferStrategy().show();
//        _g.dispose();
        
   }
    
    // Variables declaration - do not modify
    // End of variables declaration
}
