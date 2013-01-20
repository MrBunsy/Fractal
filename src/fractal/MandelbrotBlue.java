/*
 * Copyright Luke Wallin 2012
 */
package fractal;

import LukesBits.Colour;
import LukesBits.Complex;
import java.awt.Color;

/**
 *
 * @author Luke
 */
public class MandelbrotBlue extends Mandelbrot{
    
    public MandelbrotBlue(double k){
        super(k);
        
    }
    
    private final Colour colour1 = new Colour(0,0, 255);//blue
    private final Colour colourMid = new Colour(255,255, 255);//white
    private final Colour colour2 = new Colour(255, 215, 0);//gold
    
    public Color colourLogic(int i, int detail,double s,Complex z, Complex c){
        
        if(i==detail){
            //escaped
            return new Color(0,0,0);
        }else{
            
            //0 to 1
            //s = (double)i/(double)detail;
            if(k==2){
                z = newZ(z,c);// i++;
                z = newZ(z,c);// i++;
                z = newZ(z,c);// i++;
//                z = newZ(z,c);
//                z = newZ(z,c);
            }
            s=(double)i +1 - Math.log(Math.log(z.abs()))/Math.log((double)k);
            
//            Colour finished = colour1.add(colour2.dim(s));//.dim(s-1)
//            
//            return finished.toColor();
            
            
            double cycleSize = Math.log(detail) * cycleMultiplier;
            double colour =  ((s + cycleOffset*cycleSize) % cycleSize);// / cycleSize;
//            if(colour<0){
//                colour=0;
//            }
//            if(colour>1){
//                colour=1;
//            }
            //System.out.println(colour);
            //return new Color(255,215,(int)Math.round(colour*250));
            
            //double colour = Math.pow((double)s,2f)/(double)detail;
//            double  colour = s/(double)detail;
            //double colour = Math.log(s)/Math.log(detail);
            
            //blue -> white -> yellow
            //the white bit is where you've got lots of blue and yellow - some sort of adding and averaging?
            
            //Colour finished = colour1.dim(Math.pow(1-colour,2)).add(colour2.dim(Math.pow(colour,0.5)));//.add(colourMid.dim(colour-0.5));//.dim(1-colour)
            
            //multiplier
           // double m=500;
            
          //  s*=m;
            
            double r,g,b;
            
            /*
             * The colour cycle is broken into 4 parts
             * 
             * first - red rises from 0 to 255, green rises to 128
             * second - r stays at 255, blue rises from 0 to 255, green rises from 128 to 255
             * third - blue stays steady, r+g drop
             * forth - blue drops
             * 
             * 
             * note 0 < colour < cycleSize
             * 
             * 
             * idea: abstract this allowing schemes for each of the colours to be customised?
             * 
             */
            
            colour = cycleSize - colour;
            
            double cycle2 = cycleSize/2;
            double cycle4 = cycleSize/4;
            double cycle8 = cycleSize/8;
            
            if(colour < cycle4){
                //first  chunk
                r = 255*colour/cycle4;
                g = 255*colour/cycle2;
                b = 0;
            }else if(colour < cycle2){
                //second chunk
                r = 255;
                g = 255*colour/cycle2;
                b = 255*(colour-cycle4)/cycle4;
            }else if(colour < 3*cycle4){
                //third chunk
                r = g = 255 * (1 - (colour-cycle4*2)/cycle8);
                b = 255;
            }else{
                //forth chunk
                r = g = 0;
                b = 255 * (1 - (colour-cycle4*3)/cycle4);
            }
            
            //g*=0.7;
//            r =  colour < cycle4 ? colour/cycle4 : 
//            g = Math.min(s,255);
//            b = Math.min( s<m/2 ? 0 : s-m/2   ,255);
            
            
            Colour finished = new Colour(r, g, b );
            
            return finished.toColor();
            
        }
    }
}
