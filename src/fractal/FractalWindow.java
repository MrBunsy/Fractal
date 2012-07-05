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

import LukesBits.Vector;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.*;
import javax.swing.border.BevelBorder;

/**
 *
 * @author Luke
 */
public class FractalWindow extends javax.swing.JFrame implements IFractalWindow {
    
    private Fractal fractal;
    private FractalPanel panel;
    private JPanel progressPanel;
    //private JDialog progDialog;
    //private JProgressBar progBar;
    private ProgressMonitor progressMonitor;
    //private JMenuBar menuBar;
    //private JMenu fractalMenu,colourMenu,exportMenu;
    //private JMenuItem fractalMenu_mandelbrot,fractalMenu_julia,fractalMenu_customMandelbrot,fractalMenu_customJulia;
    private JLabel statusLabel;
    //private Dimension oldDims;
    
    private int width,height;
    
    private int xPadding,yPadding;
    
    private ProgressMonitor progMon;
    
    private JMenu exportMenu;
    private JMenuBar menuBar;
    
    private final JFrame thisPanel = this;
    
    public FractalWindow(Fractal _fractal, int _width, int _height){
        panel = new FractalPanel(_fractal, _width, _height);
        fractal=_fractal;
        
        width=_width;
        height=_height;
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
        //setResizable(false);
        
        
        
        
        setupMenus();
        
        //height+=menuBar.getHeight();
        
        add(panel);
        //getContentPane().add(panel);
        
        
//        //http://stackoverflow.com/questions/3035880/how-can-i-create-a-bar-in-the-bottom-of-a-java-app-like-a-status-bar
//        // create the status bar panel and shove it down the bottom of the frame
//        JPanel statusPanel = new JPanel();
//        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
//        add(statusPanel, BorderLayout.SOUTH);
//        statusPanel.setPreferredSize(new Dimension(width, 20));
//        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
//        statusLabel = new JLabel("status");
//        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
//        statusPanel.add(statusLabel);
        addStatusPanel();
        //to make up for the status bar at the bottom
        //height+=20;
        //height+=getHeight();
        
        //setupProgressPanel();
        //setupProgressDialogue();
        
//        Dialog d = new Dialog(this);
//        d.setVisible(true);
        
        //content pane has prefered size
        getContentPane().setPreferredSize(new java.awt.Dimension(width, height));
        //so when pack is called, everything else fits around it :D
        pack();
        //setSize(new java.awt.Dimension(width, height));
        setVisible(true);
        
        
        //do this last
        Dimension d = getSize();
        xPadding=d.width-width;
        yPadding=d.height-height;
        //progMon.setProgress(2);
        
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                key(evt);
            }
        });
        
        
        addComponentListener(new ComponentAdapter() {
        public void componentResized(ComponentEvent e){
            beenResized(e);
        }});
    }
    
    private void beenResized(ComponentEvent e){
        FractalSettings s = fractal.exportSettings();
        fractal.cancelGenerate();

        Dimension d = getSize();
        
        width=d.width-xPadding;
        height = d.height-yPadding;
        
        fractal = new Fractal(width,height, true, fractal.getThreads());
        fractal.setWindow(this);



        fractal.loadSettings(s);

        //panel.setPreferredSize(new Dimension(width,height));
        panel.setFractal(fractal);
        
        menuBar.remove(exportMenu);
        
        setupExportMenu();
        
        menuBar.add(exportMenu, 3);
        
        
////                
////                pack();
    }
    
    @Override
    public Point getMousePosition(boolean children){
        //so that scrolling takes into acount the mouse in the right place
        return panel.getMousePosition();
    }
    
//    private void setupProgressDialogue(){
//        progDialog = new JDialog(this);
//        progBar = new javax.swing.JProgressBar();
//        progDialog.add(progBar);
//        progDialog.setVisible(true);
//    }
    
//    private void setupProgressPanel(){
////        progressPanel = new JPanel();
////        progressBar = new javax.swing.JProgressBar();
////        progressPanel.add(progressBar,BorderLayout.CENTER);
////        
////        add(progressPanel,BorderLayout.CENTER);
////        
////        progressPanel.setPreferredSize(new Dimension(200,50));
////        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
////        progressPanel.setVisible(true);
////        progressPanel = new JPanel();
////        progressPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
////        //add(progressPanel, BorderLayout.SOUTH);
////        
////        getLayeredPane().add(progressPanel,new Integer(300));
////        
////        progressPanel.setPreferredSize(new Dimension(200, 500));
////        //progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));
////        progBar = new javax.swing.JProgressBar();
////        progBar.setPreferredSize(new Dimension(200,20));
////        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
////        progressPanel.add(progBar);
////        
////        progressPanel.setVisible(true);
//        
//        
//        progBar = new javax.swing.JProgressBar();
//        
//        progBar.setBounds(getWidth()/3, getHeight()/3, 2*getWidth()/3, 2*getHeight()/3);
//        
//        getLayeredPane().add(progBar,new Integer(300));
//        
//    }
    
    private void addStatusPanel(){
        //http://stackoverflow.com/questions/3035880/how-can-i-create-a-bar-in-the-bottom-of-a-java-app-like-a-status-bar
        // create the status bar panel and shove it down the bottom of the frame
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        
        //getLayeredPane().add(statusPanel,new Integer(300));
        
        add(statusPanel, BorderLayout.SOUTH);
        
        
        statusPanel.setPreferredSize(new Dimension(getWidth(), 20));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusLabel = new JLabel("status");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);
    }
    
    private void setupMenus(){
        
        
        menuBar=new JMenuBar();
        
        JMenu fractalMenu = new JMenu("Fractal");
        JMenu colourMenu = new JMenu("Colours");
//        exportMenu = new JMenu("Export");
        JMenu controlMenu = new JMenu("Controls");
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
        
        JMenuItem loadShip = new JMenuItem("Burning Ship");
        fractalMenu.add(loadShip);
        loadShip.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fractal.loadBurningShip();
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
        
        // ------------------- Colour Menu -------------------
        
        
        // ------------------- Control Menu -------------------
        JMenuItem reset = new JMenuItem("Reset");
        controlMenu.add(reset);
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //fractal.loadSettings(new Vector(0,0,0), 3, 50);
                fractal.reset();
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
                        "\n+/- keys or the Control menu can change the detail level.\n Higher detail levels take longer to render."
                        , "Help - JavaFractal", JOptionPane.INFORMATION_MESSAGE);
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
        
        setupExportMenu();
        
        menuBar.add(fractalMenu);
        menuBar.add(colourMenu);
        menuBar.add(controlMenu);
        menuBar.add(exportMenu);
        menuBar.add(helpMenu);
        
        
        
        setJMenuBar(menuBar);
    }
    
    private void setupExportMenu(){
        
        exportMenu = new JMenu("Export");
        
        // ------------------- Export Menu -------------------
        
        JMenuItem standardExport = new JMenuItem("Info + Preview");
        exportMenu.add(standardExport);
        standardExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fractal.save();
            }
        });
        
        JMenuItem aa4Export = new JMenuItem("4xAA - "+(width)+"x"+(height));
        exportMenu.add(aa4Export);
        aa4Export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                String filename = fractal.getFileName()+"_4aa";
                progressMonitor = new ProgressMonitor(thisPanel, "Exporting to "+filename+".png", null, 0, width+1);
                progressMonitor.setMillisToDecideToPopup(0);
                fractal.saveBig(filename, 4, true,progressMonitor);
            }
        });
        
        JMenuItem aa8Export = new JMenuItem("8xAA - "+(width)+"x"+(height));
        exportMenu.add(aa8Export);
        aa8Export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                String filename = fractal.getFileName()+"_8aa";
                progressMonitor = new ProgressMonitor(thisPanel, "Exporting to "+filename+".png", null, 0, width+1);
                progressMonitor.setMillisToDecideToPopup(0);
                fractal.saveBig(filename, 8, true,progressMonitor);
            }
        });
        
        JMenuItem bigaa4Export = new JMenuItem("4xAA - "+(width*2)+"x"+(height*2));
        exportMenu.add(bigaa4Export);
        bigaa4Export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                String filename = fractal.getFileName()+"_4aa";
                progressMonitor = new ProgressMonitor(thisPanel, "Exporting to "+filename+".png", null, 0, width+1);
                progressMonitor.setMillisToDecideToPopup(0);
                fractal.saveBig(filename, 8,4, true,progressMonitor);
            }
        });
        
        JMenuItem bigaa8Export = new JMenuItem("8xAA - "+(width*2)+"x"+(height*2));
        exportMenu.add(bigaa8Export);
        bigaa8Export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                String filename = fractal.getFileName()+"_8aa";
                progressMonitor = new ProgressMonitor(thisPanel, "Exporting to "+filename+".png", null, 0, width+1);
                progressMonitor.setMillisToDecideToPopup(0);
                fractal.saveBig(filename, 16,8, true,progressMonitor);
            }
        });
        
        JMenuItem hugeExport = new JMenuItem((width*16)+"x"+(height*16));
        exportMenu.add(hugeExport);
        hugeExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                String filename = fractal.getFileName()+"_huge";
                progressMonitor = new ProgressMonitor(thisPanel, "Exporting to "+filename+".png", null, 0, width+1);
                progressMonitor.setMillisToDecideToPopup(0);
                fractal.saveBig(filename, 16, false,progressMonitor);
            }
        });
    }
    
    public void paint(Graphics g){
        super.paint(g);
        if(fractal.ready()){
            statusLabel.setText(fractal.statusText());
        }else{
            statusLabel.setText("Generating...");
        }
    }
    
    private void key(java.awt.event.KeyEvent evt) {
        int key = evt.getKeyCode();
        fractal.key(key);
    }
    
//    @Override
//    public void validate(){
//        super.validate();
////        if(oldDims!=null){
////            Dimension dims = getSize();
////            int dx = dims.width - oldDims.width;
////            int dy = dims.height - oldDims.height;
////
////            width+=dx;
////            height+=dy;
////            if(dx !=0 && dy!=0){
////                FractalSettings s = fractal.exportSettings();
////                fractal.cancelGenerate();
////
////
////                fractal = new Fractal(width, height, true, fractal.getThreads());
////                fractal.setWindow(this);
////
////
////
////                fractal.loadSettings(s);
////
////                panel.setPreferredSize(new Dimension(width,height));
////                panel.setFractal(fractal);
////                
////                pack();
////
////                oldDims=dims;
////            }
////        }
//        
//        
//    }

    @Override
    public void saving(int progress) {
//        if(progress==0){
//            progMon = new ProgressMonitor(this, "Saving...", "", 0, width);
//        }
//        progMon.setProgress(progress);
//        System.out.println(progress);
    }
}