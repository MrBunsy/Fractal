/*
 * Copyright Luke Wallin 2012
 */
package fractal;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import LukesBits.*;

/**
 *
 * @author Luke
 */
public class Fractal {

    private FractalWindow window;
    private int width,height, detail;
    private BufferedImage buffer;
    //private Colour black = new Colour(0,0,0);
    private Vector centre;
    private double zoom;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Fractal f = new Fractal(600,600);
    }
    
    public Fractal(int _width, int _height){
        width=_width;
        height = _height;
        
        detail=50;
        
        //origin = new Vector((double)width*0.75,(double)height/2.0);
        //zoom = 2.0/(double)width;
        
        //the length of the real axis which stretches across the screen
        zoom = 3.0;
        //what value on the complex plain is in the centre of the screen
        centre = new Vector(-0.5,0);
        
        
        buffer =  new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        
        generate();
        
        window = new FractalWindow(this, width, height);
        
        window.setVisible(true);
    }
    
    public Colour iterationToColour(int i){

        Colour c = Colour.hsvToRgb((double)(i%50)/50.0, 0.5, 1.0);
        return c;
    }
    
    //taking ints where they are either -1,0 or 1, so the current zoom level is taken into account
    public void move(int x, int y){
        centre=centre.add(new Vector(x,y),(zoom*0.1));
        generate();
    }
    
    public void scroll(double scroll){
        //zoom+=scroll/500;
        if(scroll < 0){
            zoom*=0.9;
        }else{
            zoom/=0.9;
        }
        generate();
        
    }
    
    public void generate(){
        for(int x=0;x<width;x++){
            for(int y=0;y<height;y++){
                Colour colour;
                
                Vector p = new Vector(x,height-y-1);
                
                //get p to be relative to offset in the complex plain
                p=p.multiply(zoom/(double)width);
                
                //offset is the top left on the viewport on the complex plain
                Vector offset = centre.subtract(new Vector(1,1).multiply(zoom/2.0));
                p=p.add(offset);
                //p=p.subtract(new Vector(zoom/2.0,zoom/2.0));//origin.add(
                
                
                Complex c = new Complex(p.x,p.y);
                
                Complex z = new Complex(0,0);
                
                int i=0;
                
                while(z.magnitudeSqrd() < 4 && i<detail){
                    z = z.times(z).plus(c);
                    i++;
                }
                
                if(i>=detail){
                    //black for not maxed out yet
                    colour=new Colour(0,0,0);
                    
                }else{
                    colour = iterationToColour(i);
                }
                
                buffer.setRGB(x, height-y-1, colour.toColor().getRGB());
            }
        }
    }
    
    public void draw(Graphics g){//,int width,int height
        //Graphics2D g = (Graphics2D) _g;
        
        g.drawImage(buffer, 0, 0, null);
        
        g.drawString("Centre: "+centre+" Zoom: "+zoom, 10, 50);
    }
}
