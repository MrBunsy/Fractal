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
 */
package fractal;

import java.awt.Dimension;
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
        Dimension d =  getSize();
        fractal = new Fractal(d.width, d.height,false,2);
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
