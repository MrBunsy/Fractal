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

import LukesBits.Complex;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

/**
 *
 * @author Luke
 */
public class FractalApplet extends JApplet implements IFractalWindow,KeyListener{//,

    Fractal fractal;
    private Point mouseDown;
    private FractalPanel panel;
    private JLabel statusLabel;
    /**
     * Initialization method that will be called after the applet is loaded into
     * the browser.
     */
    public void init() {
        Dimension d =  getSize();
        
        
//        
        //FunctionOfZ fz = new Mandelbrot(30,false);
        FunctionOfZ fz = new Julia(new Complex(0,1), Julia.ColourType.COSINE);
        fractal = new Fractal(d.width, d.height,false,2,fz);
        fractal.setWindow(this);
        
       
        
        panel = new FractalPanel(fractal, d.width-10, d.height-10);
        
        add(panel);
        
        setupMenus();
        addStatusPanel();
        
//        addMouseWheelListener(this);
//        addMouseListener(this);
        //think this is needed for key stuff?
        setFocusable(true);
        addKeyListener(this);
    }
    
    private void addStatusPanel(){
        //http://stackoverflow.com/questions/3035880/how-can-i-create-a-bar-in-the-bottom-of-a-java-app-like-a-status-bar
        // create the status bar panel and shove it down the bottom of the frame
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        add(statusPanel, BorderLayout.SOUTH);
        statusPanel.setPreferredSize(new Dimension(getWidth(), 20));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusLabel = new JLabel("status");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);
    }
    
    private void setupMenus(){
        JMenuBar menuBar=new JMenuBar();
        
        JMenu fractalMenu = new JMenu("Fractal");
        JMenu colourMenu = new JMenu("Colour");
        JMenu exportMenu = new JMenu("Export");
        
        //option to load hte default mandelbrot
        JMenuItem loadMandelbrot = new JMenuItem("Mandelbrot");
        fractalMenu.add(loadMandelbrot);
        loadMandelbrot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fractal.loadMandelbrot();
            }
        });
        
        JMenuItem loadJulia = new JMenuItem("Julia Quadratic");
        fractalMenu.add(loadJulia);
        loadJulia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fractal.loadJuliaQuadratic();
            }
        });
        
        
        
        menuBar.add(fractalMenu);
        menuBar.add(colourMenu);
        menuBar.add(exportMenu);
        
        setJMenuBar(menuBar);
    }
    
    // TODO overwrite start(), stop() and destroy() methods
    public void keyPressed(KeyEvent e) {                       
        fractal.key(e.getKeyCode());
        
    }
    public void paint(Graphics g){
      //  fractal.draw(g);
        super.paint(g);
        if(fractal.ready()){
            statusLabel.setText(fractal.statusText());
        }else{
            statusLabel.setText("Generating...");
        }
    }
    
    public void update(Graphics g){
        paint(g);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        fractal.key(key);
        e.consume();
    }
}
