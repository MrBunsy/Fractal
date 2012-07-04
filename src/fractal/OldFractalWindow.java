/*
 *  Fractal - Java fractal generator
    Copyright (C) 2012 Luke Wallin

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * 
 * idea - have a menu bar at the top with options like in the graph plotter:
 * choosing type of fractal
 * maybe even some way of choosing the equation?
 * setting julia mu
 * changing zoom and everything with dialogue boxes
 * 
 */
package fractal;

import java.awt.Graphics;
import java.awt.Point;

/**
 *
 * @author Luke
 */
public class OldFractalWindow extends javax.swing.JFrame {//implements IFractalWindow 

    private Fractal fractal;
    private Point mouseDown;
    private javax.swing.JProgressBar progressBar;
    /**
     * Creates new form FractalWindow
     */
    public OldFractalWindow(Fractal _fractal, int width, int height) {
//        super("Fullscreen");
        fractal = _fractal;
//        
//        getContentPane().setPreferredSize( Toolkit.getDefaultToolkit().getScreenSize());
//        pack();
//        setResizable(false);
//        show();

        progressBar=new javax.swing.JProgressBar();
        
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

        setVisible(true);
        
//        while(getBufferStrategy() == null){
//            createBufferStrategy(2);
//        }

        
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

        //repaint();
    }

    private void mouseWheel(java.awt.event.MouseWheelEvent evt) {
        int scroll = evt.getWheelRotation();//.getPreciseWheelRotation();
        fractal.scroll(scroll);
        //repaint();
    }

    private void key(java.awt.event.KeyEvent evt) {
        int key = evt.getKeyCode();
        fractal.key(key);
    }

    public void paint(Graphics g) {
        //Graphics _g = getBufferStrategy().getDrawGraphics();

        //Dimension d = getSize();

        fractal.draw(g);//,d.width,d.height);
        //getBufferStrategy().show();
        //_g.dispose();

    }
    // Variables declaration - do not modify
    // End of variables declaration
}
