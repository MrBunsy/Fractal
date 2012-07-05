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

import LukesBits.Colour;
import LukesBits.Complex;
import LukesBits.Image;
import LukesBits.Vector;
import jargs.gnu.CmdLineParser;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ProgressMonitor;

/**
 *
 * Funky fractal generator!
 *
 * @author Luke
 *
 *
 * TODO - MASSIVELY tidy up the saving mechanism, so I can get feedback and it
 * makes sense. -done :D
 *
 * TODO - cancel a generation and start again if something changes? DONE
 *
 * TODO check that images folder exists and create if not
 *
 * TODO put reset info in FunctionOfZ so each fractal can reset properly
 *
 */
public class Fractal {

    private IFractalWindow window;
    private int width, height, detail, drawDetail, threads;
    private BufferedImage bufferImage;//,finishedImage;
    //private int[][] buffer;
    //private Colour[][] buffer;
    //private int minI, maxI, totalIs;
    private int threadsDrawnTo, finishedThreads;
    //private double averageI;
    //private Colour black = new Colour(0,0,0);
    private Vector centre, drawCentre;
    private double zoom, drawZoom;
    private double zoomAdjust = 0.8;
    //how much bigger to make hte big image when saving
    private int upscale;
    private boolean allowSave, generationInProgress, needReGenerate, cancelGeneration;
    private boolean saveWhenFinished, aa;
    private String saveAs;
    private FractalThread[] fractalThreads;
    private Thread[] threadClasses;
    private FunctionOfZ functionOfZ;
    private ProgressMonitor progressMonitor;
    //parameter used for juliet sets
    private Complex juliaMu;

    public static void printUsage() {
        System.out.println("Usage: "
                + "-w --width Image width (pixels)\n"
                + "-h --height Image height (pixels)\n"
                + "-t --threads Number of threads\n");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        CmdLineParser parser = new CmdLineParser();

        CmdLineParser.Option widthArg = parser.addIntegerOption('w', "width");
        CmdLineParser.Option heightArg = parser.addIntegerOption('h', "height");
        CmdLineParser.Option threadsArg = parser.addIntegerOption('t', "threads");
        CmdLineParser.Option upScaleArg = parser.addIntegerOption('u', "upscale");
        CmdLineParser.Option animationArg = parser.addBooleanOption('a', "animation");

        try {
            parser.parse(args);
        } catch (CmdLineParser.OptionException e) {
            System.err.println(e.getMessage());
            printUsage();
        }

        int width = (Integer) parser.getOptionValue(widthArg, 600);
        int height = (Integer) parser.getOptionValue(heightArg, 600);
        int threads = (Integer) parser.getOptionValue(threadsArg, Runtime.getRuntime().availableProcessors());
        int upscale = (Integer) parser.getOptionValue(upScaleArg, 4);
        boolean animation = (Boolean) parser.getOptionValue(animationArg, false);

        if (animation) {

            int upTo = 1000;
            int skip = 500;

            String folderName = (int) (System.currentTimeMillis() / 1000L) + "";
            new File("images/" + folderName).mkdir();
            //Vector c = new Vector(-1.11,-0.26,0.0);
            //Vector c = new Vector(-1.10958164277272, -0.27535734081757735, 0.0);
            Vector c = new Vector(-0.5699636380381331, -0.5617647004128065, 0);
            double z = 3.0;
            for (int i = 0; i < skip; i++) {
                z *= 0.95;
            }
            for (int i = skip; i < upTo; i++) {

                //FunctionOfZ fz = new Julia(new Complex(0.36237,0.32),Julia.ColourType.COSINE);
                FunctionOfZ fz = new Mandelbrot(30, true);

                //Fractal f = new Fractal(width * upscale, height * upscale, true, threads, FractalType.JULIA,fz);
                Fractal f = new Fractal(width * upscale, height * upscale, true, threads);//, fz
                f.loadSettings(c, z, 1000);
                //f.setUpscale(upscale);
                f.saveWhenFinished("/" + folderName + "/" + i);
                f.generate();
                f.setAA(false);
                //f.setOnlyAA(true);

                //f.setBackground(new Colour(255,255,255));

                z *= 0.95;
                try {
                    //massive bodge to get it to wait till the image has been written
                    while (!f.ready()) {
                        Thread.sleep(100);
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(Fractal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            Complex mu = new Complex(0.36237, 0.32);
            //Complex mu = new Complex(0.285,0.01);
            //Complex mu = new Complex(0.8,0.156);
            //Complex mu = new Complex(-0.726895347709114071439, 0.188887129043845954792);//very pretty - top of wiki page
            //Complex mu = new Complex(-0.8,0.156);
            //Complex mu = new Complex(-0.74543,0.11301);
            //Complex mu = new Complex(-0.4,0.6);
            //Complex mu = new Complex(0,1);//lightning!

//            Random random = new Random(9323222);
//            
//            Complex mu;
//            
//            //find a random point in the mandelbrot set
//            do{
//                mu = new Complex(random.nextDouble()*4d-2d, random.nextDouble()*4d-2d);
//            }
//            while(!Mandelbrot.isPointIn(mu));


            FunctionOfZ fz = new Julia(mu, Julia.ColourType.COSINE);
            //FunctionOfZ fz = new Mandelbrot(30,true);
            //FunctionOfZ fz = new BurningShip(30,true);

            //Fractal f = new Fractal(width * upscale, height * upscale, true, threads, FractalType.JULIA,fz);
            //Fractal f = new Fractal(width, height, true, threads, fz);
            Fractal f = new Fractal(width, height, true, threads);
            //f.setJulia(new Complex(0.36237,0.32), 10);

            //f.setCycleMultiplier(50);
            f.setUpscale(upscale);
            FractalWindow w = new FractalWindow(f, width, height);
            f.setWindow(w);
            //f.setBackground(new Colour(255,255,255));
        }
    }

    //this is for a fractal that will be saved as soon as it's generated
    public Fractal(int _width, int _height, int _threads, FunctionOfZ _functionOfZ, int _detail, double _zoom, Vector _centre, String _saveAs, boolean _aa) {
        this(_width, _height, _threads, _functionOfZ, _detail, _zoom, _centre, _saveAs, _aa, null);
    }

    public Fractal(int _width, int _height, int _threads, FunctionOfZ _functionOfZ, int _detail, double _zoom, Vector _centre, String _saveAs, boolean _aa, ProgressMonitor _progMon) {
        width = _width;
        height = _height;
        threads = _threads;
        functionOfZ = _functionOfZ;
        detail = _detail;
        zoom = _zoom;
        centre = _centre;

        saveWhenFinished = true;
        saveAs = _saveAs;
        aa = _aa;

        progressMonitor = _progMon;

        init();

        generate();
    }

    public Fractal(int _width, int _height, int _threads, FunctionOfZ _functionOfZ, int _detail, double _zoom, Vector _centre) {
        width = _width;
        height = _height;
        threads = _threads;
        functionOfZ = _functionOfZ;
        detail = _detail;
        zoom = _zoom;
        centre = _centre;

        saveWhenFinished = false;
        aa = false;

        init();
    }

    public Fractal(int _width, int _height, boolean _allowSave, int _threads) {
        width = _width;
        height = _height;
        allowSave = _allowSave;
        threads = _threads;

//        aa=true;
//        
//        detail=50;
//        //just load a default fractal
//        functionOfZ = new Mandelbrot(30,true);
//        centre = new Vector(-0.5, 0);
//        zoom = 3.0;

        init();
        loadMandelbrot();


    }

    //TODO, cull this later?
//    public Fractal(int _width, int _height, boolean _allowSave, int _threads, FunctionOfZ _functionOfZ) {
//        width = _width;
//        height = _height;
//        allowSave = _allowSave;
//        //upscale = 4;
//        detail = 50;
//        functionOfZ=_functionOfZ;
//
//        aa = true;
//        threads = _threads;
//        
//        //the length of the real axis which stretches across the screen
//        zoom = 3.0;
//        //what value on the complex plain is in the centre of the screen
//        centre = new Vector(-0.5, 0);
//
//        init();
//    }
    //set up lots of stuff
    private void init() {
        bufferImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        generationInProgress = false;
        needReGenerate = false;

        fractalThreads = new FractalThread[threads];
        threadClasses = new Thread[threads];

        upscale = 4;

        cancelGeneration = false;
    }

    //load the standard mandelbrot
    public void loadMandelbrot() {
        functionOfZ = new Mandelbrot(30, true);
        reset();
//        generate();
//        if (window != null) {
//            window.repaint();
//        }
    }
    
    public void reset(){
        centre = functionOfZ.defaultCentre();
        zoom = functionOfZ.defaultZoom();
        detail = functionOfZ.defaultDetail();
        generate();
    }

    public void loadBurningShip() {
        functionOfZ = new BurningShip(30, true);
        reset();
//        generate();
//        if (window != null) {
//            window.repaint();
//        }
    }

    //load a pretty Julia set
    public void loadJuliaQuadratic() {
        functionOfZ = new Julia(new Complex(-0.726895347709114071439, 0.188887129043845954792), Julia.ColourType.COSINE);
        reset();
//        generate();
//        if (window != null) {
//            window.repaint();
//        }
    }

    public void key(int key) {
        //TODO WASD too?
        switch (key) {
            case java.awt.event.KeyEvent.VK_KP_DOWN:
            case java.awt.event.KeyEvent.VK_DOWN:
                move(0, 1);
                break;
            case java.awt.event.KeyEvent.VK_LEFT:
            case java.awt.event.KeyEvent.VK_KP_LEFT:
                move(-1, 0);
                break;
            case java.awt.event.KeyEvent.VK_RIGHT:
            case java.awt.event.KeyEvent.VK_KP_RIGHT:
                move(1, 0);
                break;
            case java.awt.event.KeyEvent.VK_UP:
            case java.awt.event.KeyEvent.VK_KP_UP:
                move(0, -1);
                break;
            case java.awt.event.KeyEvent.VK_ADD:
                changeDetail(true);
                break;
            case java.awt.event.KeyEvent.VK_SUBTRACT:
                changeDetail(false);
                break;
            case java.awt.event.KeyEvent.VK_PRINTSCREEN:
                if (allowSave) {
                    save();
                }
                break;
        }
        window.repaint();
    }

    public void setWindow(IFractalWindow _window) {
        window = _window;
        generate();
    }

    public void saveWhenFinished(String _saveAs) {
        saveWhenFinished = true;
        saveAs = _saveAs;
    }

//    public void setCycleMultiplier(double m){
//        cycleMultiplier=m;
//    }
    public void loadSettings(Vector _centre, double _zoom, int _detail) {
        centre = _centre;
        zoom = _zoom;
        detail = _detail;
        generate();
    }

    public void setAA(boolean _aa) {
        aa = _aa;
    }

//    public void setOnlyAA(boolean _aa) {
//        onlyAA = _aa;
//    }
    public void setUpscale(int _upscale) {
        upscale = _upscale;
    }

    public boolean ready() {
        return !generationInProgress;
    }

//    public void setBackground(Colour bg){
//        background=bg;
//    }
    public void setProgressMonitor(ProgressMonitor pm) {
        progressMonitor = pm;
    }

    //taking ints where they are either -1,0 or 1, so the current zoom level is taken into account
    public void move(int x, int y) {
        centre = centre.add(new Vector(x, y), (zoom * 0.1));
        generate();
    }

    public void changeDetail(boolean more) {

        double m = 5;

        if (more) {
            detail *= m;
        } else {
            detail /= m;
        }

        generate();
    }

    public void drag(Point down, Point up) {
        if (down != null) {
            Vector difference = new Vector(up.x - down.x, up.y - down.y);

            centre = centre.subtract(difference, zoom / (double) width);

            window.repaint();
            generate();
        }
    }

    public void scroll(int scroll) {
        Point m = window.getMousePosition();

        if (m != null) {
            Vector mouseScreen = new Vector(m.x, m.y);

            //mouse position on the complex plain
            Vector mouseComplex = offset(centre, zoom).add(mouseScreen.multiply(zoom / (double) width));

            //mouseComplex = offset + mouseScreen*zoomAdjust
            //re-arrange for offset, then deal with change in zoom
            //offset = mouseComplex - mouseScreen*newZoomAdjust
            //could do this to revolve around centre, not offset, but this was easier to think about

            updateZoom(scroll);

            //.add(new Vector(0,1),_zoom*(double)(width-height)/(2.0*(double)width)) is a bodge to get it to work when the aspect ratio isn't 1
            //I'm not entirely sure what's happenning, but it works!
            Vector newOffset = mouseComplex.subtract(mouseScreen.multiply(zoom / (double) width)).subtract(new Vector(0, 1), zoom * (double) (width - height) / (2.0 * (double) width));
            centre = newOffset.add(new Vector(zoom / 2.0, zoom / 2.0));
        } else {
            updateZoom(scroll);
        }
        generate();
        window.repaint();
    }

    private void updateZoom(int scroll) {
        if (scroll < 0) {
            zoom *= zoomAdjust;
        } else {
            zoom /= zoomAdjust;
        }
    }

    private Vector offset(Vector _centre, double _zoom) {
        Vector offset = _centre.subtract(new Vector(1, 1).multiply(_zoom / 2.0));//(double)height/(double)width

        offset = offset.add(new Vector(0, 1), _zoom * (double) (width - height) / (2.0 * (double) width));

        return offset;
    }

    public void generateStrip(int x1, int x2) {
        Vector offset = offset(drawCentre, drawZoom);
        double adjustedZoom = drawZoom / (double) width;
        for (int x = x1; x < x2; x++) {
            for (int y = 0; y < height; y++) {
                Vector p = new Vector(x, height - y - 1);
                //get p to be relative to offset in the complex plain
                p = p.multiply(adjustedZoom);
                //offset is the top left on the viewport on the complex plain
                p = p.add(offset);

                Complex c = new Complex(p.x, p.y);
                Complex z = new Complex(0, 0);
                Color colour = functionOfZ.iterations(z, c, drawDetail);
                bufferImage.setRGB(x, height - y - 1, colour.getRGB());
            }
        }
    }

    public synchronized void generate() {

        if (!generationInProgress) {
            generationInProgress = true;
            needReGenerate = false;
            drawDetail = detail;
            drawCentre = centre.copy();
            drawZoom = zoom;
            finishedThreads = 0;

            if (progressMonitor != null) {
                //extra one if we're using AA
                //now an extra 2, because everything has an extra 1 for "saving image"
                progressMonitor.setMaximum(width + (aa ? 2 : 1));
                progressMonitor.setNote("Generating Image");
            }

            //what xcoord has been drawn up to
            threadsDrawnTo = 0;

            for (int t = 0; t < threads; t++) {
                int drawTo = threadsDrawnTo + 2;
                if (drawTo > width) {
                    drawTo = width;
                }
                fractalThreads[t] = new FractalThread(this, threadsDrawnTo, drawTo, t);
                threadClasses[t] = new Thread(fractalThreads[t]);
                threadClasses[t].start();
                threadsDrawnTo += 2;
            }
        } else {
            needReGenerate = true;
            cancelGeneration = true;
        }

    }

    public synchronized void threadFinished(int id, FractalThread t) {
        //this is so any threads that were killed off by a cancelation can't then spread artifacts around
        if (t.stopped()) {
            return;
        }

        if (progressMonitor != null && progressMonitor.isCanceled()) {
            cancelGeneration = true;
        }

        if (cancelGeneration) {
            //need to stop generating this fractal

            //count how many other threads are left
            //int threadsStillGoing=0;
            for (int i = 0; i < threads; i++) {
//                if(threadClasses[i].isAlive()){
//                    //threadsStillGoing++;
//                }
                fractalThreads[i].stop();
            }
            //System.out.println(threadsStillGoing);
            //if none, we've successfully canceled
            //if(threadsStillGoing==1){
            cancelGeneration = false;
            generationInProgress = false;
            if (needReGenerate) {
                generate();
            }
            //  }

        } else if (threadsDrawnTo < width) {
            //continue generation
            int drawTo = threadsDrawnTo + 2;
            if (drawTo > width) {
                drawTo = width;
            }

            fractalThreads[id].newXs(threadsDrawnTo, drawTo);
            threadClasses[id] = new Thread(fractalThreads[id]);
            threadClasses[id].start();
            threadsDrawnTo++;
        } else {
            //finished!
            finishedThreads++;
        }
        if (window != null) {
            //saving a fractal doesn't necessarily mean it had a window - the big version to be AA for example
            window.repaint();
        }

        if (progressMonitor != null) {
            progressMonitor.setProgress(threadsDrawnTo);
        }

        if (finishedThreads >= threads) {
            //all of them finished!
            if (window != null) {
                window.repaint();
            }
            generationInProgress = false;

            if (saveWhenFinished) {
                //if saving when we've finished, we don't need the info string - it's either for an animation or an AA image
                save(saveAs, aa, false);
            }

            if (needReGenerate) {
                generate();
            }
        }
    }

    //get a default file name
    public String getFileName() {
        //make images folder if it doens't exist
        //TODO put this bodge somewhere better
        new File("images/").mkdir();
        return "images/" + (int) (System.currentTimeMillis() / 1000L);
    }

    //saves buffer and info
    public void save() {
        String filename = getFileName();

        save(filename, false, true);
        //the larger image
        //saveBig(filename,upscale,false);
        //the larger image AA
        //saveBig(filename+"_aa",upscale,true,null);
    }

    public void saveBig(String filename) {
        saveBig(filename, upscale, true, null);
    }

    //this assumes we're just AA the image at the same rez
    public void saveBig(String filename, int scale, boolean _aa, ProgressMonitor pm) {
        saveBig(filename, scale, scale, _aa, pm);
    }

    //can also make the image bigger as well as AA it!
    public void saveBig(String filename, int scaleUp, int scaleDown, boolean _aa, ProgressMonitor pm) {
        //this should be auto-saved
        Fractal f = new Fractal(width * scaleUp, height * scaleUp, threads, functionOfZ, detail, zoom, centre, filename, _aa, pm);
        //f.setProgressMonitor(progressMonitor);
        f.setUpscale(scaleDown);
    }

    public void save(String filename, boolean aa, boolean info) {
        try {

            if (aa) {
                if (progressMonitor != null) {
                    progressMonitor.setNote("Resizing Image");
                }
                //rescaled image
                BufferedImage aaImage = Image.getScaledInstance(bufferImage, width / upscale, height / upscale, RenderingHints.VALUE_INTERPOLATION_BICUBIC, true);
                if (progressMonitor != null) {
                    progressMonitor.setNote("Saving Image");
                    progressMonitor.setProgress(width + 1);
                }
                ImageIO.write(aaImage, "png", new File(filename + ".png"));
                if (progressMonitor != null) {
                    progressMonitor.setProgress(width + 2);
                }
            } else {
                if (progressMonitor != null) {
                    progressMonitor.setNote("Saving Image");
                }
                //just the straight buffer
                ImageIO.write(bufferImage, "png", new File(filename + ".png"));
                if (progressMonitor != null) {
                    progressMonitor.setProgress(width + 1);
                }
            }

            if (info) {
                //store a text file too
                FileWriter fstream = new FileWriter(filename + ".txt");
                BufferedWriter out = new BufferedWriter(fstream);
                out.write(infoString(true));
                //Close the output stream
                out.close();
            }

        } catch (IOException ex) {
            Logger.getLogger(Fractal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String statusText() {
        return infoString(false);
    }

    private String infoString(boolean detailed) {

        return "Centre: " + (detailed ? "(" + centre.x + "," + centre.y + ")" : centre) + ", "
                + "Zoom: " + zoom + ", "
                + "Detail: " + detail + ", "
                + //"Type: "+fractalType.toString()+", "+
                "Function: " + functionOfZ.toString();
        //(detailed ? "ColourCycleMultiplier: "+cycleMultiplier : "");
    }

    public synchronized void draw(Graphics g) {//,int width,int height
        //Graphics2D g = (Graphics2D) _g;

        g.drawImage(bufferImage, 0, 0, null);

        //g.drawString(infoString(false), 5, height-10);
    }
}

class FractalThread implements Runnable {

    private int x1, x2, id;
    private Fractal f;
    private boolean stop;

    public FractalThread(Fractal _f, int _x1, int _x2, int _id) {
        f = _f;
        x1 = _x1;
        x2 = _x2;
        id = _id;
        stop = false;
    }

    public void newXs(int _x1, int _x2) {
        x1 = _x1;
        x2 = _x2;
    }

    public void stop() {
        stop = true;
    }

    public boolean stopped() {
        return stop;
    }

    @Override
    public void run() {
        f.generateStrip(x1, x2);
        //not bothering to stop it mid-strip, but can stop it interfeering with anything else
        if (!stop) {
            f.threadFinished(id, this);
        }
    }
}