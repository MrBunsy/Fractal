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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
    private int width,height;
    
    private final JRootPane thisPanel = this.getRootPane();
    
    /**
     * Initialization method that will be called after the applet is loaded into
     * the browser.
     */
    public void init() {
        Dimension d =  getSize();
        
        
//        
        //FunctionOfZ fz = new Mandelbrot(30,false);
        //FunctionOfZ fz = new Julia(new Complex(0,1), Julia.ColourType.COSINE);
        fractal = new Fractal(d.width, d.height,false,Runtime.getRuntime().availableProcessors());//,fz
        fractal.setWindow(this);
        
       width=d.width;
       height=d.height;
        
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
        JMenu colourMenu = new JMenu("Colours");
//        exportMenu = new JMenu("Export");
        JMenu controlMenu = new JMenu("Controls");
        //JMenu windowMenu = new JMenu("Window");
        JMenu helpMenu = new JMenu("Help");
        
        // ------------------- Fractal Menu -------------------
        //option to load hte default mandelbrot
        JMenuItem loadMandelbrot = new JMenuItem("Mandelbrot");
        fractalMenu.add(loadMandelbrot);
        loadMandelbrot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fractal.loadMandelbrot();
            }
        });
        
        JMenuItem loadMandelbrot4 = new JMenuItem("'Mandelbrot' with x^n");
        fractalMenu.add(loadMandelbrot4);
        loadMandelbrot4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //fractal.loadMandelbrot(4);
                String nString = JOptionPane.showInputDialog(rootPane, "f(x) = x^n + c, where n = (can be non-int)", "4");
                if(nString!=null){
                    fractal.loadMandelbrot(Double.parseDouble(nString));
                }
            }
        });
        
//        JMenuItem loadMandelbrot8 = new JMenuItem("Mandelbrot with x^8");
//        fractalMenu.add(loadMandelbrot8);
//        loadMandelbrot8.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                fractal.loadMandelbrot(8);
//            }
//        });
        
        JMenuItem loadShip = new JMenuItem("Burning Ship");
        fractalMenu.add(loadShip);
        loadShip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fractal.loadBurningShip();
            }
        });
        
//        JMenuItem collatz = new JMenuItem("Collatz");
//        fractalMenu.add(collatz);
//        collatz.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                fractal.loadCollatz();
//            }
//        });
        
        JMenuItem loadJulia = new JMenuItem("Julia Quadratic");
        fractalMenu.add(loadJulia);
        loadJulia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fractal.loadJuliaQuadratic();
            }
        });
        
        JMenuItem loadCustomJulia = new JMenuItem("Custom Julia Quadratic");
        fractalMenu.add(loadCustomJulia);
        loadCustomJulia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                JuliaSelectDialogue d = new JuliaSelectDialogue(fractal,SwingUtilities.windowForComponent(thisPanel));
                d.setVisible(true);
            }
        });
        
        // ------------------- Colour Menu -------------------
        JMenuItem resetColour = new JMenuItem("Reset");
        colourMenu.add(resetColour);
        resetColour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //fractal.loadSettings(new Vector(0,0,0), 3, 50);
                fractal.resetColour();
            }
        });
        
        JMenuItem changeColour = new JMenuItem("Custom");
        colourMenu.add(changeColour);
        changeColour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //fractal.loadSettings(new Vector(0,0,0), 3, 50);
                //fractal.openColourDialogue();
                ColourDialogue d = new ColourDialogue(fractal.getFunctionOfZ(),fractal,SwingUtilities.windowForComponent(thisPanel));
                d.open();
            }
        });
        
        
        // ------------------- Control Menu -------------------
        JMenuItem reset = new JMenuItem("Reset View");
        controlMenu.add(reset);
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //fractal.loadSettings(new Vector(0,0,0), 3, 50);
                fractal.reset();
            }
        });
        
        JMenuItem goTo = new JMenuItem("Go To");
        controlMenu.add(goTo);
        goTo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GoToDialogue d = new GoToDialogue(fractal, SwingUtilities.windowForComponent(thisPanel));
                d.setVisible(true);
            }
        });
        
        JMenuItem zoomIn = new JMenuItem("Zoom In");
        controlMenu.add(zoomIn);
        zoomIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fractal.scroll(-1);
            }
        });
        
        JMenuItem zoomOut = new JMenuItem("Zoom Out");
        controlMenu.add(zoomOut);
        zoomOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fractal.scroll(1);
            }
        });
        
        JMenuItem moreDetail = new JMenuItem("Increase Detail");
        controlMenu.add(moreDetail);
        moreDetail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fractal.changeDetail(true);
            }
        });
        
        JMenuItem lessDetail = new JMenuItem("Decrease Detail");
        controlMenu.add(lessDetail);
        lessDetail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fractal.changeDetail(false);
            }
        });
        
        
        // ------------------- Help Menu -------------------
        
        
        JMenuItem help = new JMenuItem("Help");
        helpMenu.add(help);
        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(thisPanel, 
                        "Use the arrow keys or click and drag the cursor to move the viewport."+
                        "\nThe mouse scroll wheel or Control Menu will zoom in and out."+
                        "\n+/- keys or the Control menu can change the detail level.\n Higher detail levels take longer to render."+
                        "\nClick on a point to centre the view around it"
                        , "Help - JavaFractal", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        JMenuItem website = new JMenuItem("Website");
        helpMenu.add(website);
        website.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                //doesn't seem to work in chrome?
                //*sometimes* doesn't work in chrome
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    if (desktop.isSupported(Desktop.Action.BROWSE)) {
                        try {
                            desktop.browse(new URI("http://www.lukewallin.co.uk/graphics/fractals"));
                        }
                        catch(IOException ioe) {
                            ioe.printStackTrace();
                        }
                        catch(URISyntaxException use) {
                            use.printStackTrace();
                        }
                    }
                }
            }
        });
        
        JMenuItem about = new JMenuItem("About");
        helpMenu.add(about);
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(thisPanel, 
                        "JavaFractal is Copyright (c) Luke Wallin 2012"+
                        "\nReleased under LPGL"+
                        "\nwww.lukewallin.co.uk/graphics/fractals"+
                        "\nluke.wallin@gmail.com", "JavaFractal revision 27", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        //setupExportMenu();
        
        menuBar.add(fractalMenu);
        menuBar.add(colourMenu);
        menuBar.add(controlMenu);
        //menuBar.add(windowMenu);
        //menuBar.add(exportMenu);
        menuBar.add(helpMenu);
        
        
        
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

    @Override
    public void saving(int x) {
    }
}
