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
public class Julia implements FunctionOfZ{

    private Complex mu;
    private ColourType colour;
    //private Colour background;
    //private 
    
    public static enum ColourType{
        COSINE,NONE
    }
            
    public Julia(Complex _mu, ColourType _colour){//, Colour _background){
        mu=_mu;
        colour=_colour;
    }
    
    @Override
    public Color iterations(Complex z, Complex c, int detail) {
        
        Complex oldC = c;
        
        int i=0;
        while (c.magnitudeSqrd() < 4 && i < detail) {
            oldC=c;
            c = c.times(c).plus(mu);
            i++;
        }
        
//        if(c.magnitudeSqrd() >= 4){
//            //outside julia set
//            i=0;
//        }
        
        //cosine colouring - see http://eldar.mathstat.uoguelph.ca/dashlock/ftax/CosineCol.html
        switch(colour){
            
            case COSINE:
                //vector from final valid point to final invalid point
                Complex p = c.minus(oldC);

                double angle = Math.atan2(p.im(),p.re());

                //square, so force range to be +ve
                //angle *= angle;
                if(angle < 0){
                    angle+=Math.PI;
                }

                //bodge to get colouring mechanism to work right with only using ints
                //i = (int)Math.round(angle * Integer.MAX_VALUE);
                //return Colour.hsvToRgb(angle, 0.8, 1.0).toColor();
                //return Color.getHSBColor((float)angle, 0.8f, 1.0f);
                if(c.magnitudeSqrd() >= 4){
                    //escaped - colour like the mandelbrot
                    //return Colour.red.dim(1-angle).toColor();
                    //return new Colour(255,255,255).toColor();
                    c = c.times(c).plus(mu);i++;
                    c = c.times(c).plus(mu);i++;
                    
                     double s=(double)i - Math.log(Math.log(c.abs()))/Math.log(2.0);// + 1.0
                    
                     double cycleSize = Math.log(detail) * 50;

                     double co = ( s + cycleSize/2) % cycleSize / cycleSize;
                    
                     return Color.getHSBColor((float)co, 0.5f, 1.0f);
                     //return Colour.red.dim(s).toColor();
                }else{
                    //non escaped
                    return Colour.blue.dim(1.0-angle/Math.PI).toColor();
                    //return Color.getHSBColor((float)(angle/Math.PI), 0.8f, 1.0f);
                }
                
            case NONE:
            default:
                if(c.magnitudeSqrd() >= 4){
                    //outside julia set
                    return new Colour(255,255,255).toColor();
                }
                return new Colour(0,0,0).toColor();
        }
        
    }
    
    @Override
    public String toString(){
        return "f(z) = z^2 + "+mu;
    }
    
}
