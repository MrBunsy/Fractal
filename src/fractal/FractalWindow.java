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
public class FractalWindow extends javax.swing.JFrame implements IFractalWindow {
    
    private Fractal fractal;
    private FractalPanel panel;
    
    public FractalWindow(Fractal _fractal, int width, int height){
        panel = new FractalPanel(_fractal, width, height);
        fractal=_fractal;
        
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        setResizable(false);
        
        //add(panel);
        getContentPane().add(panel);
        
        pack();
        setSize(new java.awt.Dimension(width, height));
        setVisible(true);
        
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                key(evt);
            }
        });
    }
    
    private void key(java.awt.event.KeyEvent evt) {
        int key = evt.getKeyCode();
        fractal.key(key);
    }
}