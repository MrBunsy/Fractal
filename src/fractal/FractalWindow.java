/*
 * Copyright Luke Wallin 2012
 */
package fractal;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

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
//        super("Fullscreen");
         fractal=_fractal;
//        
//        getContentPane().setPreferredSize( Toolkit.getDefaultToolkit().getScreenSize());
//        pack();
//        setResizable(false);
//        show();
       
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(width, height));
        setResizable(false);
        
        addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                mouseWheel(evt);
            }
        });
        
        addKeyListener(new java.awt.event.KeyAdapter() {
//            public void keyPressed(java.awt.event.KeyEvent evt) {
//                keyDown(evt);
//            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                key(evt);
            }
        });
        
//        while(getBufferStrategy() == null){
//            createBufferStrategy(2);
//        }
    }

    private void mouseWheel(java.awt.event.MouseWheelEvent evt) {
        double scroll = evt.getPreciseWheelRotation();
        fractal.scroll(scroll);
        repaint();
    }

     private void key(java.awt.event.KeyEvent evt) {                         
        int key = evt.getKeyCode();
//        int x=0;
//        int y=0;
        //TODO WASD too?
        switch(key){
            case java.awt.event.KeyEvent.VK_KP_DOWN:
            case java.awt.event.KeyEvent.VK_DOWN:
                //down
                //y+=1;
                fractal.move(0, 1);
                break;
            case java.awt.event.KeyEvent.VK_LEFT:
            case java.awt.event.KeyEvent.VK_KP_LEFT:
                //x-=1;
                fractal.move(-1, 0);
                break;
            case java.awt.event.KeyEvent.VK_RIGHT:
            case java.awt.event.KeyEvent.VK_KP_RIGHT:
                //x+=1;
                fractal.move(1, 0);
                break;
            case java.awt.event.KeyEvent.VK_UP:
            case java.awt.event.KeyEvent.VK_KP_UP:
                //y-=1;
                fractal.move(0, -1);
                break;
            case java.awt.event.KeyEvent.VK_ADD:
                fractal.changeDetail(true);
                break;
            case java.awt.event.KeyEvent.VK_SUBTRACT:
                fractal.changeDetail(false);
                break;
            case java.awt.event.KeyEvent.VK_PRINTSCREEN:
                fractal.save();
                break;
        }
//        if(x!=0 && y!=0){
//            fractal.move(x, y);
//        }
        repaint();
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
