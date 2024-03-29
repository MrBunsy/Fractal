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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
    //the label with the text
    private JLabel statusLabel;
    //the panel that status resides inside
    private JPanel statusPanel;
    //private Dimension oldDims;
    
    private int width,height;
    
    private int xPadding,yPadding;
    
    private ProgressMonitor progMon;
    
    private JMenu exportMenu;
    private JMenuBar menuBar;
    
    private final FractalWindow thisPanel = this;
    private boolean fullscreen;
    
    public FractalWindow(Fractal _fractal, int _width, int _height, boolean _fullscreen){
        panel = new FractalPanel(_fractal, _width, _height);
        fractal=_fractal;
        
        width=_width;
        height=_height;
        
        fullscreen=_fullscreen;
        
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        add(panel);
        
        //set an initial size for the panel
        setPanelSize();
        
        //both of these will call pack();
        if(fullscreen){
            setupFullscreen();
        }else{
            setupWindow();
        }
        
        //now the real window size will be a little different and we can owrk out
        //the padding that the menus added
        //bit of a bodge, but it works
        calculatePadding();
        
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
    
    private void setPanelSize(){
        //content pane has prefered size
        getContentPane().setPreferredSize(new java.awt.Dimension(width, height));
    }
    
    private void calculatePadding(){
        //do this last
        Dimension d = getSize();
        xPadding=d.width-width;
        yPadding=d.height-height;
    }
    
    private void setupFullscreen(){
        
        
        setUndecorated(true);
        setResizable(false);
        validate();
        pack();
        
        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(this);            
        beenResized(null);
    }
    
    private void setupWindow(){
        setupMenus();
        addStatusPanel();
        //so when pack is called, everything else fits around it :D
        pack();
        setVisible(true);
    }
    
    private void toggleFullscreen(){
        if(fullscreen){
            //go back to window
            GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(null);
            //calling dispose means the window can't be displayed, so undecorated can be altered
            dispose();
            setUndecorated(false);
            setResizable(true);
            
            
            //todo put this code and code in constructor in smae place
//            width=800;
//            height=640;
//            xPadding=0;
//            yPadding=0;
            resizeWindow(800, 640);
            
            setupWindow();
            
            
            calculatePadding();
            
            //beenResized(null);
            
            
            fullscreen=false;
        }else{
            
            dispose();
            
            //remove the menu bar and status bar
            setJMenuBar(null);
            remove(statusPanel);
            
            
            xPadding=0;
            yPadding=0;
            
            //add(panel);
            setupFullscreen();
            
            
            
            fullscreen=true;
        }
        
        
    }
    
    private void beenResized(ComponentEvent e){
        
        Dimension d = getSize();
        
        int w=d.width-xPadding;
        int h = d.height-yPadding;

        if(w!=width || h!=height || e==null){
            //only resize if an actual change occured or called manually
            resizeFractal(w,h);
        }

    }
    
    public void resizeWindow(int w, int h){
        resizeFractal(w, h);
        //panel.setPreferredSize(new Dimension(w, h));
        getContentPane().setPreferredSize(new java.awt.Dimension(width, height));
        pack();
    }
    
    public void resizeFractal(int w, int h){
        width=w;
        height=h;
        FractalSettings s = fractal.exportSettings();
        fractal.cancelGenerate();

        
        fractal = new Fractal(w,h, true, fractal.getThreads());
        fractal.setWindow(this);



        fractal.loadSettings(s);
        panel.setFractal(fractal);
        if(!fullscreen){
            menuBar.remove(exportMenu);
            setupExportMenu();
            menuBar.add(exportMenu, 4);
        }
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
        statusPanel = new JPanel();
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
        JMenu windowMenu = new JMenu("Window");
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
        
        JMenuItem loadMandelbrotBlue = new JMenuItem("Mandelbrot (blue)");
        fractalMenu.add(loadMandelbrotBlue);
        loadMandelbrotBlue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fractal.loadMandelbrotBlue();
            }
        });
        
        JMenuItem loadMandelbrot4 = new JMenuItem("'Mandelbrot' with x^n");
        fractalMenu.add(loadMandelbrot4);
        loadMandelbrot4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //fractal.loadMandelbrot(4);
                try{
                    String nString = JOptionPane.showInputDialog(rootPane, "f(x) = x^n + c, where n = (can be non-int)", "4");
                    if(nString!=null){
                        fractal.loadMandelbrot(Double.parseDouble(nString));
                    }
                }catch(NumberFormatException er){
                    JOptionPane.showMessageDialog(rootPane, "Invalid inputs", "Error",JOptionPane.ERROR_MESSAGE);
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
//        
//        JMenuItem loadCustom = new JMenuItem("Test");
//        fractalMenu.add(loadCustom);
//        loadCustom.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                fractal.loadCustomFunction();
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
        
        JMenuItem collatz = new JMenuItem("Collatz");
        fractalMenu.add(collatz);
        collatz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fractal.loadCollatz();
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
        
        JMenuItem loadCustomJulia = new JMenuItem("Custom Julia Quadratic");
        fractalMenu.add(loadCustomJulia);
        loadCustomJulia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JuliaSelectDialogue d = new JuliaSelectDialogue(fractal,thisPanel);
                d.setVisible(true);
            }
        });
        
        JMenuItem quit = new JMenuItem("Exit");
        fractalMenu.add(quit);
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shutdown();
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
                ColourDialogue d = new ColourDialogue(fractal.getFunctionOfZ(),fractal,thisPanel);
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
                GoToDialogue d = new GoToDialogue(fractal, thisPanel);
                d.setVisible(true);
            }
        });
        
        JMenuItem zoomIn = new JMenuItem("Zoom In");
        controlMenu.add(zoomIn);
        zoomIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fractal.scrollNoMouse(-1);
            }
        });
        
        JMenuItem zoomOut = new JMenuItem("Zoom Out");
        controlMenu.add(zoomOut);
        zoomOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fractal.scrollNoMouse(1);
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
        
        JMenuItem moreSamples = new JMenuItem("Increase Samples");
        controlMenu.add(moreSamples);
        moreSamples.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fractal.changeSubPixelSamples(true);
            }
        });
        
        JMenuItem lessSamples = new JMenuItem("Decrease Samples");
        controlMenu.add(lessSamples);
        lessSamples.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fractal.changeSubPixelSamples(false);
            }
        });
        // ------------------- Window Menu -------------------
        
        
        JMenuItem goFullscreen = new JMenuItem("Toggle Fullscreen (F11)");
        windowMenu.add(goFullscreen);
        goFullscreen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleFullscreen();
            }
        });
        
        JMenuItem resize600 = new JMenuItem("600x600 (1:1)");
        windowMenu.add(resize600);
        resize600.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thisPanel.resizeWindow(600, 600);
            }
        });
        
        JMenuItem resize1024 = new JMenuItem("1024x768 (4:3)");
        windowMenu.add(resize1024);
        resize1024.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thisPanel.resizeWindow(1024, 768);
            }
        });
        
        JMenuItem resize1280_9 = new JMenuItem("1280x720 (16:9)");
        windowMenu.add(resize1280_9);
        resize1280_9.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thisPanel.resizeWindow(1280, 720);
            }
        });
        
        JMenuItem resize1280 = new JMenuItem("1280x800 (16:10)");
        windowMenu.add(resize1280);
        resize1280.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thisPanel.resizeWindow(1280, 800);
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
                        "\nReleased under LGPL"+
                        "\nwww.lukewallin.co.uk/graphics/fractals"+
                        "\nluke.wallin@gmail.com", "JavaFractal revision 56", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
         
        
        setupExportMenu();
        
        menuBar.add(fractalMenu);
        menuBar.add(colourMenu);
        menuBar.add(controlMenu);
        menuBar.add(windowMenu);
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
        
        JMenuItem customExport = new JMenuItem("Custom");
        exportMenu.add(customExport);
        customExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CustomExport d = new CustomExport(fractal,thisPanel);
                d.setVisible(true);
            }
        });
        
//        JMenuItem hugeExport = new JMenuItem((width*16)+"x"+(height*16));
//        exportMenu.add(hugeExport);
//        hugeExport.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                
//                String filename = fractal.getFileName()+"_huge";
//                progressMonitor = new ProgressMonitor(thisPanel, "Exporting to "+filename+".png", null, 0, width+1);
//                progressMonitor.setMillisToDecideToPopup(0);
//                fractal.saveBig(filename, 16, false,progressMonitor);
//            }
//        });
        
        JMenuItem aa4Export = new JMenuItem(""+(width)+"x"+(height));
        exportMenu.add(aa4Export);
        aa4Export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                String filename = fractal.getFileName()+"_"+(width)+"x"+(height);
                progressMonitor = new ProgressMonitor(thisPanel, "Exporting to "+filename+".png", null, 0, width+1);
                progressMonitor.setMillisToDecideToPopup(0);
                fractal.saveBig(filename, 1, false,progressMonitor, 3);
            }
        });
        
//        JMenuItem bigaa4Export = new JMenuItem("4xAA - "+(width*2)+"x"+(height*2));
//        exportMenu.add(bigaa4Export);
//        bigaa4Export.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                
//                String filename = fractal.getFileName()+"_4aa";
//                progressMonitor = new ProgressMonitor(thisPanel, "Exporting to "+filename+".png", null, 0, width+1);
//                progressMonitor.setMillisToDecideToPopup(0);
//                fractal.saveBig(filename, 8,4, true,progressMonitor);
//            }
//        });
        
        JMenuItem myRezaa4Export = new JMenuItem("1680x1050");
        exportMenu.add(myRezaa4Export);
        myRezaa4Export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                String filename = fractal.getFileName()+"_1680x1050";
                progressMonitor = new ProgressMonitor(thisPanel, "Exporting to "+filename+".png", null, 0, width+1);
                progressMonitor.setMillisToDecideToPopup(0);
                fractal.saveCertainRez(filename, 1680, 1050, 1, progressMonitor, 3);
            }
        });
        
        JMenuItem hdaa4Export = new JMenuItem("1920x1080 (HD)");
        exportMenu.add(hdaa4Export);
        hdaa4Export.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                String filename = fractal.getFileName()+"_hd";
                progressMonitor = new ProgressMonitor(thisPanel, "Exporting to "+filename+".png", null, 0, width+1);
                progressMonitor.setMillisToDecideToPopup(0);
                fractal.saveCertainRez(filename, 1920, 1080, 1, progressMonitor, 3);
            }
        });
        
        
//        
//        JMenuItem aa8Export = new JMenuItem("8xAA - "+(width)+"x"+(height));
//        exportMenu.add(aa8Export);
//        aa8Export.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                
//                String filename = fractal.getFileName()+"_8aa";
//                progressMonitor = new ProgressMonitor(thisPanel, "Exporting to "+filename+".png", null, 0, width+1);
//                progressMonitor.setMillisToDecideToPopup(0);
//                fractal.saveBig(filename, 8, true,progressMonitor);
//            }
//        });
//        
//        
//        
//        JMenuItem bigaa8Export = new JMenuItem("8xAA - "+(width*2)+"x"+(height*2));
//        exportMenu.add(bigaa8Export);
//        bigaa8Export.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                
//                String filename = fractal.getFileName()+"_8aa";
//                progressMonitor = new ProgressMonitor(thisPanel, "Exporting to "+filename+".png", null, 0, width+1);
//                progressMonitor.setMillisToDecideToPopup(0);
//                fractal.saveBig(filename, 16,8, true,progressMonitor);
//            }
//        });
//        
//        JMenuItem hdaa8Export = new JMenuItem("8xAA - 1920x1080 (HD)");
//        exportMenu.add(hdaa8Export);
//        hdaa8Export.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                
//                String filename = fractal.getFileName()+"_hd_8aa";
//                progressMonitor = new ProgressMonitor(thisPanel, "Exporting to "+filename+".png", null, 0, width+1);
//                progressMonitor.setMillisToDecideToPopup(0);
//                fractal.saveCertainRez(filename, 1920, 1080, 8, progressMonitor);
//            }
//        });
//        
//        JMenuItem myRezaa8Export = new JMenuItem("8xAA - 1680x1050");
//        exportMenu.add(myRezaa8Export);
//        myRezaa8Export.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                
//                String filename = fractal.getFileName()+"_1680x1050_8aa";
//                progressMonitor = new ProgressMonitor(thisPanel, "Exporting to "+filename+".png", null, 0, width+1);
//                progressMonitor.setMillisToDecideToPopup(0);
//                fractal.saveCertainRez(filename, 1680, 1050, 8, progressMonitor);
//            }
//        });
        
        
    }
    
    public void paint(Graphics g){
        super.paint(g);
        if(!fullscreen){
            if(fractal.ready()){
                statusLabel.setText(fractal.statusText());
            }else{
                statusLabel.setText("Generating...");
            }
        }
    }
    
    private void shutdown(){
        dispose();
        System.exit(0);
    }
    
    private void key(java.awt.event.KeyEvent evt) {
        int key = evt.getKeyCode();
        switch(key){
            //something purely for the window?
            case java.awt.event.KeyEvent.VK_F11:
                //toggle fullscreen
                toggleFullscreen();
                break;
            case java.awt.event.KeyEvent.VK_ESCAPE:
                shutdown();
                break;
            default:
                fractal.key(key);
                break;
        }
        
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