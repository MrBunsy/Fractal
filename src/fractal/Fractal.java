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
    private Colour black = new Colour(0,0,0);
    private Vector origin;
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
        
        detail=500;
        
        origin = new Vector((double)width*0.75,(double)height/2.0);
        zoom = 2.0/(double)width;
        
        
        
        buffer =  new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        
        generate();
        
        window = new FractalWindow(this, width, height);
        
        window.setVisible(true);
    }
    
    public Colour iterationToColour(int i){

        Colour c = Colour.hsvToRgb((double)(i%50)/50.0, 0.5, 1.0);
        return c;
    }
    
    public void generate(){
        for(int x=0;x<this.width;x++){
            for(int y=0;y<this.height;y++){
                Colour colour;
                
                Vector p = new Vector(x,y);
                
                p=p.subtract(this.origin);
                p=p.multiply(this.zoom);
                
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
                
                buffer.setRGB(x, y, colour.toColor().getRGB());
            }
        }
    }
    
    public void draw(Graphics g){//,int width,int height
        //Graphics2D g = (Graphics2D) _g;
        
        g.drawImage(buffer, 0, 0, null);
    }
}
