/*
 * Copyright Luke Wallin 2012
 */
package fractal;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import LukesBits.*;
import java.awt.Point;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Luke
 */
public class Fractal {

    private FractalWindow window;
    private int width, height, detail;
    private BufferedImage outputImage;
    private int[][] buffer;
    private int minI,maxI;
    private double averageI;
    //private Colour black = new Colour(0,0,0);
    private Vector centre;
    private double zoom;
    private double zoomAdjust=0.8;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Fractal f = new Fractal(600, 600);
    }

    public Fractal(int _width, int _height) {
        width = _width;
        height = _height;

        detail = 50;
        
        

        //origin = new Vector((double)width*0.75,(double)height/2.0);
        //zoom = 2.0/(double)width;

        //the length of the real axis which stretches across the screen
        zoom = 3.0;
        //what value on the complex plain is in the centre of the screen
        centre = new Vector(-0.5, 0);


        outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        buffer = new int[width][height];
        
        
        generate();

        window = new FractalWindow(this, width, height);

        window.setVisible(true);
    }

    public Colour iterationToColour(int i) {

        if(i==detail){
            return new Colour(0,0,0);
        }
        
        //all these work:
        //Colour c = Colour.hsvToRgb((double) (i % 50) / 50.0, 0.5, 1.0);
        //Colour c = Colour.hsvToRgb((double) i/detail, 0.5, 1.0);
        //Colour c = Colour.hsvToRgb((double) (i-minI)/(maxI-minI), 0.5, 1.0);
        
        
        //Colour c = Colour.hsvToRgb((double) (i-minI)/averageI, 0.5, 1.0);
        //Colour c = Colour.hsvToRgb((double)i%255/255, 0.5, 1.0);
        Colour c = Colour.hsvToRgb((double) (i-minI)%averageI/averageI, 0.5, 1.0);
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
    
    public void scroll(int scroll) {
        Point m = window.getMousePosition();

        if (m != null) {
            Vector mouseScreen = new Vector(m.x, m.y);

            //mouse position on the complex plain
            Vector mouseComplex = offset().add(mouseScreen.multiply(zoom / (double) width));

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

    private Vector offset() {
        Vector offset = centre.subtract(new Vector(1, 1).multiply(zoom / 2.0));

        return offset;
    }

    public void generate() {
        
        minI=detail;
        maxI=0;
        
        int totalIs=0;
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Colour colour;

                Vector p = new Vector(x, height - y - 1);

                //get p to be relative to offset in the complex plain
                p = p.multiply(zoom / (double) width);

                //offset is the top left on the viewport on the complex plain
                //Vector offset = offset();
                p = p.add(offset());
                //p=p.subtract(new Vector(zoom/2.0,zoom/2.0));//origin.add(


                Complex c = new Complex(p.x, p.y);

                Complex z = new Complex(0, 0);

                int i = 0;

                while (z.magnitudeSqrd() < 4 && i < detail) {
                    z = z.times(z).plus(c);
                    i++;
                }

                if(i<minI){
                    minI=i;
                }
                
                if(i>maxI){
                    maxI=i;
                }
                
                totalIs+=i;
//                if (i >= detail) {
//                    //black for not maxed out yet
//                    colour = new Colour(0, 0, 0);
//
//                } else {
//                    colour = iterationToColour(i);
//                }

                //outputImage.setRGB(x, height - y - 1, colour.toColor().getRGB());
                buffer[x][height - y - 1] = i;
            }
        }
        averageI=(double)totalIs/(double)(width*height);
        bufferToImage();
    }
    
    //take the iteration values in the buffer and create the image;
    private void bufferToImage(){
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                outputImage.setRGB(x, y, iterationToColour(buffer[x][y]).toColor().getRGB());
            }
        }
    }
    
    public void save(){
        try {
            
            String filename = (int) (System.currentTimeMillis() / 1000L)+"";
            
            ImageIO.write(outputImage, "png", new File(filename+".png"));
            
//            PrintWriter out = new PrintWriter(filename+".txt");
//            out.println(infoString());
            
            FileWriter fstream = new FileWriter(filename+".txt");
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(infoString());
            //Close the output stream
            out.close();
            
        } catch (IOException ex) {
            Logger.getLogger(Fractal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String infoString(){
        return "Centre: " + centre + ", Zoom: " + zoom + ", Detail: "+detail;
    }

    public void draw(Graphics g) {//,int width,int height
        //Graphics2D g = (Graphics2D) _g;

        g.drawImage(outputImage, 0, 0, null);

        g.drawString(infoString(), 10, 50);
    }
}
