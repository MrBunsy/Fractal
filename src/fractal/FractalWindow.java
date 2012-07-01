/*
 * Copyright Luke Wallin 2012
 */
package fractal;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;

/**
 *
 * @author Luke
 */
public class FractalWindow extends javax.swing.JFrame implements IFractalWindow {

    private Fractal fractal;
    private Point mouseDown;

    /**
     * Creates new form FractalWindow
     */
    public FractalWindow(Fractal _fractal, int width, int height) {
//        super("Fullscreen");
        fractal = _fractal;
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

        addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                _mouseReleased(evt);
            }

            public void mousePressed(java.awt.event.MouseEvent evt) {
                _mousePressed(evt);
            }
        });

//        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
//            public void mouseDragged(java.awt.event.MouseEvent evt) {
//                mouseDragged(evt);
//            }
//        });

//        while(getBufferStrategy() == null){
//            createBufferStrategy(2);
//        }

        setVisible(true);
    }

//    private void mouseDragged(java.awt.event.MouseEvent evt) {
//    }
    private void _mousePressed(java.awt.event.MouseEvent evt) {
        mouseDown = evt.getPoint();
    }

    private void _mouseReleased(java.awt.event.MouseEvent evt) {
        Point mouseUp = evt.getPoint();

        fractal.drag(mouseDown, mouseUp);

        mouseDown = null;

        repaint();
    }

    private void mouseWheel(java.awt.event.MouseWheelEvent evt) {
        int scroll = evt.getWheelRotation();//.getPreciseWheelRotation();
        fractal.scroll(scroll);
        repaint();
    }

    private void key(java.awt.event.KeyEvent evt) {
        int key = evt.getKeyCode();
        fractal.key(key);
    }

    public void paint(Graphics g) {
        //Graphics _g = getBufferStrategy().getDrawGraphics();

        //Dimension d = getSize();

        fractal.draw(g);//,d.width,d.height);
//        getBufferStrategy().show();
//        _g.dispose();

    }
    // Variables declaration - do not modify
    // End of variables declaration
}
