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
import java.awt.Graphics;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * Funky fractal generator!
 * 
 * @author Luke
 */
public class Fractal {

    private IFractalWindow window;
    private int width, height, detail, drawDetail,threads;
    private BufferedImage bufferImage;//,finishedImage;
    private int[][] buffer;
    private int minI,maxI,totalIs;
    private int threadsDrawnTo,finishedThreads;
    private double averageI;
    //private Colour black = new Colour(0,0,0);
    private Vector centre, drawCentre;
    private double zoom, drawZoom;
    private double zoomAdjust=0.8;
    
    //how much bigger to make hte big image when saving
    private int upscale;
    
    
    private boolean allowSave,generationInProgress,needReGenerate;//,changingImage;
    
    private boolean saveWhenFinished;
    private String saveAs;
    
    private FractalThread[] fractalThreads;
    private Thread[] threadClasses;

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
        
        try {
            parser.parse(args);
        } catch (CmdLineParser.OptionException e) {
            System.err.println(e.getMessage());
            printUsage();
        }
        
        int width = (Integer) parser.getOptionValue(widthArg, 600);
        int height = (Integer) parser.getOptionValue(heightArg, 600);
        int threads = (Integer) parser.getOptionValue(threadsArg, 2);
        int upscale = (Integer) parser.getOptionValue(upScaleArg,4);
        
        Fractal f = new Fractal(width, height,true,threads);
        f.setUpscale(upscale);
        FractalWindow w = new FractalWindow(f,width, height);
        f.setWindow(w);
    }
    
    public void key(int key){
        
//        int x=0;
//        int y=0;
        //TODO WASD too?
        switch(key){
            case java.awt.event.KeyEvent.VK_KP_DOWN:
            case java.awt.event.KeyEvent.VK_DOWN:
                //down
                //y+=1;
                move(0, 1);
                break;
            case java.awt.event.KeyEvent.VK_LEFT:
            case java.awt.event.KeyEvent.VK_KP_LEFT:
                //x-=1;
                move(-1, 0);
                break;
            case java.awt.event.KeyEvent.VK_RIGHT:
            case java.awt.event.KeyEvent.VK_KP_RIGHT:
                //x+=1;
                move(1, 0);
                break;
            case java.awt.event.KeyEvent.VK_UP:
            case java.awt.event.KeyEvent.VK_KP_UP:
                //y-=1;
                move(0, -1);
                break;
            case java.awt.event.KeyEvent.VK_ADD:
                changeDetail(true);
                break;
            case java.awt.event.KeyEvent.VK_SUBTRACT:
                changeDetail(false);
                break;
            case java.awt.event.KeyEvent.VK_PRINTSCREEN:
                save();
                break;
        }
//        if(x!=0 && y!=0){
//            fractal.move(x, y);
//        }
        window.repaint();
    }

    public void setWindow(IFractalWindow _window){
        window=_window;
        generate();
    }
    
    public void saveWhenFinished(String _saveAs){
        saveWhenFinished=true;
        saveAs=_saveAs;
    }
    
    public void loadSettings(Vector _centre,double _zoom, int _detail){
        centre=_centre;
        zoom=_zoom;
        detail=_detail;
    }
    
    public Fractal(int _width, int _height, boolean _allowSave, int _threads) {
        width = _width;
        height = _height;
        
        allowSave=_allowSave;

        upscale=4;
        
        detail = 50;
        
        threads=_threads;
        
        System.out.println("Threads: "+threads);
        
        fractalThreads = new FractalThread[threads];
        threadClasses = new Thread[threads];
        
        //origin = new Vector((double)width*0.75,(double)height/2.0);
        //zoom = 2.0/(double)width;

        //the length of the real axis which stretches across the screen
        zoom = 3.0;
        //what value on the complex plain is in the centre of the screen
        centre = new Vector(-0.5, 0);


        bufferImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //finishedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        buffer = new int[width][height];
        
        //changingImage=false;
        generationInProgress=false;
        needReGenerate=false;
        //generate();

        //window = new FractalWindow(this, width, height);

        
    }

    public void setUpscale(int _upscale){
        upscale=_upscale;
    }
    
    public boolean ready(){
        return !generationInProgress;
    }
    
    public Colour iterationToColour(int i) {

        if(i==drawDetail){
            return new Colour(0,0,0);
        }
        
        //all these work:
        //Colour c = Colour.hsvToRgb((double) (i % 50) / 50.0, 0.5, 1.0);
        //Colour c = Colour.hsvToRgb((double) i/detail, 0.5, 1.0);
        //Colour c = Colour.hsvToRgb((double) (i-minI)/(maxI-minI), 0.5, 1.0);
        
        //idea - look at iteration range and work it out so that it repeats x times across that range?
        
        //double cycles = 1;
        
        //double cycleSize = (double)(maxI-minI)/cycles;
        //double cycleSize = (double)(averageI-minI)/cycles;
        double cycleSize = Math.log(maxI)*30;
        
        //Colour c = Colour.hsvToRgb((double) (i-minI)/averageI, 0.5, 1.0);
        //Colour c = Colour.hsvToRgb((double)i%255/255, 0.5, 1.0);
        //Colour c = Colour.hsvToRgb((double) (i-minI)%averageI/averageI, 0.5, 1.0);
        //subtract something other than minI to change the colour start?
        Colour c = Colour.hsvToRgb((double) (i-minI)%cycleSize/cycleSize, 0.8, 1.0);
        return c;
    }

    //taking ints where they are either -1,0 or 1, so the current zoom level is taken into account
    public void move(int x, int y) {
        centre = centre.add(new Vector(x, y), (zoom * 0.1));
        generate();
    }

    public void changeDetail(boolean more){
        if(more){
            detail*=5;
        }else{
            detail/=5;
        }
        
        generate();
    }
    
    public void drag(Point down, Point up){
        if(down!=null){
            Vector difference = new Vector(up.x - down.x , up.y - down.y);
            
            centre=centre.subtract(difference,zoom/(double)width);
            
            generate();
        }
    }
    
    public void scroll(int scroll) {
        Point m = window.getMousePosition();

        if (m != null) {
            Vector mouseScreen = new Vector(m.x, m.y);

            //mouse position on the complex plain
            Vector mouseComplex = offset(centre,zoom).add(mouseScreen.multiply(zoom / (double) width));

            //mouseComplex = offset + mouseScreen*zoomAdjust
            //re-arrange for offset, then deal with change in zoom
            //offset = mouseComplex - mouseScreen*newZoomAdjust
            //could do this to revolve around centre, not offset, but this was easier to think about

            updateZoom(scroll);
            
            Vector newOffset = mouseComplex.subtract(mouseScreen.multiply(zoom / (double) width));
            centre = newOffset.add(new Vector(zoom / 2.0, zoom / 2.0));
        } else {
            updateZoom(scroll);
        }
        generate();

    }
    
    private void updateZoom(int scroll){
        if (scroll < 0) {
                zoom *= zoomAdjust;
            } else {
                zoom /= zoomAdjust;
            }
    }

    private Vector offset(Vector _centre, double _zoom) {
        Vector offset = _centre.subtract(new Vector(1, 1).multiply(_zoom / 2.0));

        return offset;
    }

    //set max/min and add up totals
    private synchronized void setIStuff(int i){
        if(i<minI){
            minI=i;
        }

        if(i>maxI){
            maxI=i;
        }

        totalIs+=i;
    }
    
    public void generateStrip(int x1,int x2){
        for (int x = x1; x < x2; x++) {
            for (int y = 0; y < height; y++) {
                //Colour colour;

                Vector p = new Vector(x, height - y - 1);

                //get p to be relative to offset in the complex plain
                p = p.multiply(drawZoom / (double) width);

                //offset is the top left on the viewport on the complex plain
                //Vector offset = offset();
                p = p.add(offset(drawCentre,drawZoom));
                //p=p.subtract(new Vector(zoom/2.0,zoom/2.0));//origin.add(


                Complex c = new Complex(p.x, p.y);

                Complex z = new Complex(0, 0);

                int i = 0;

                while (z.magnitudeSqrd() < 4 && i < drawDetail) {
                    z = z.times(z).plus(c);
                    i++;
                }

                setIStuff(i);
                
                buffer[x][height - y - 1] = i;
            }
        }
    }
    
    public synchronized void generate() {
        
        if(!generationInProgress){
            generationInProgress=true;
            needReGenerate=false;
            minI=detail;
            maxI=0;

            drawDetail=detail;
            drawCentre=centre.copy();
            drawZoom=zoom;
            
            totalIs=0;

            finishedThreads=0;

            //what xcoord has been drawn up to
            threadsDrawnTo=0;

            for(int t=0;t<threads;t++){
                fractalThreads[t]=new FractalThread(this,threadsDrawnTo, threadsDrawnTo+1, t);
                
                threadClasses[t]=new Thread(fractalThreads[t]);
                threadClasses[t].start();
                
                threadsDrawnTo++;
            }
        }else{
            needReGenerate=true;
        }
        
    }
    
    public synchronized void threadFinished(int id){
        if(threadsDrawnTo < width){
            //fractalThreads[id]=new FractalThread(this,threadsDrawnTo, threadsDrawnTo+1, id);
            
            fractalThreads[id].newXs(threadsDrawnTo, threadsDrawnTo+1);
            
            threadClasses[id] = new Thread(fractalThreads[id]);
            
            threadClasses[id].start();
            
            threadsDrawnTo++;
        }else{
            //finished!
            finishedThreads++;
        }
        
        if(finishedThreads>=threads){
            //all of them finished!
            averageI=(double)totalIs/(double)(width*height);
            bufferToImage();
            if(window!=null){
                window.repaint();
            }
            generationInProgress=false;
            
            if(saveWhenFinished){
                save(saveAs,false);
            }
            
            if(needReGenerate){
                generate();
            }
        }
    }
    
    //take the iteration values in the buffer and create the image;
    private synchronized void bufferToImage(){
        //changingImage=true;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bufferImage.setRGB(x, y, iterationToColour(buffer[x][y]).toColor().getRGB());
            }
        }
        //changingImage=false;
    }
    
    public void save(){
        save((int) (System.currentTimeMillis() / 1000L)+"",true);
    }
    
    public void save(String filename, boolean bigToo){
        if(allowSave){
            try {
                if(bigToo){
                    //BufferedImage bigImage = new BufferedImage(width*upscale,height*upscale,BufferedImage.TYPE_INT_RGB);
                    Fractal bigFractal = new Fractal(width*upscale, height*upscale, allowSave, threads);

                    bigFractal.loadSettings(centre, zoom, detail);
                    bigFractal.saveWhenFinished(filename+"_big");
                    bigFractal.setUpscale(upscale);

                    bigFractal.generate();
                }
                
                //wait till ready
//                while(!bigFractal.ready()){
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(Fractal.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
               
                
//                Graphics bigGraphics = bigImage.getGraphics();
//                
//                bigFractal.draw(bigGraphics);
                
                //String filename = (int) (System.currentTimeMillis() / 1000L)+"";

                ImageIO.write(bufferImage, "png", new File(filename+".png"));
                
                if(!bigToo){
                    //this is the big image
                    //BufferedImage aaImage = new BufferedImage(width/upscale,height/upscale,BufferedImage.TYPE_INT_RGB);
                    //aaImage = Image.getScaledInstance(bufferImage, width, height, RenderingHints.VALUE_INTERPOLATION_BICUBIC, true);
                    BufferedImage aaImage = Image.getScaledInstance(bufferImage, width/upscale,height/upscale, RenderingHints.VALUE_INTERPOLATION_BICUBIC, true);
                    
                    ImageIO.write(aaImage, "png", new File(filename+"_aa.png"));
                }else{
                    //this is NOT the big image
                    FileWriter fstream = new FileWriter(filename+".txt");
                    BufferedWriter out = new BufferedWriter(fstream);
                    out.write(infoString());
                    //Close the output stream
                    out.close();
                }
                
                //ImageIO.write(bigImage, "png", new File(filename+"_big.png"));

    //            PrintWriter out = new PrintWriter(filename+".txt");
    //            out.println(infoString());

                

            } catch (IOException ex) {
                Logger.getLogger(Fractal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private String infoString(){
        return "Centre: " + centre + ", Zoom: " + zoom + ", Detail: "+detail;
    }

    public synchronized  void draw(Graphics g) {//,int width,int height
        //Graphics2D g = (Graphics2D) _g;
        
        g.drawImage(bufferImage, 0, 0, null);

        g.drawString(infoString(), 10, 50);
    }
    
    
}


class FractalThread implements Runnable{
    private int x1,x2,id;
    private Fractal f;
    
    public FractalThread(Fractal _f,int _x1,int _x2, int _id){
        f=_f;
        x1=_x1;
        x2=_x2;
        id=_id;
    }

    public void newXs(int _x1,int _x2){
        x1=_x1;
        x2=_x2;
    }
    
    @Override
    public void run() {
        f.generateStrip(x1, x2);
        f.threadFinished(id);
    }
    
    
}