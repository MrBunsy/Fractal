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
public class Mandelbrot implements FunctionOfZ{

    protected double cycleMultiplier;
    protected boolean smoothColour;
    
    public Mandelbrot(double _cycleMultiplier,boolean _smoothColour){
        cycleMultiplier=_cycleMultiplier;
        smoothColour=_smoothColour;
    }
    
    protected Complex newZ(Complex z, Complex c){
        return z.times(z).plus(c);
    }
    
    @Override
    public Color iterations(Complex z, Complex c, int detail) {
        int i=0;
        double s=0;
        while (z.magnitudeSqrd() < 4 && i < detail) {
            z = newZ(z,c);
            i++;
            //this is for smooth colouring - http://www.hiddendimension.com/FractalMath/Divergent_Fractals_Main.html
            //does slow things down a lot though
            if(smoothColour){
                s = s + Math.exp(-z.abs());
            }
        }
        
        if(i==detail){
            //escaped
            return new Color(0,0,0);
        }
        
        if(!smoothColour){
            //just using iteration, not the funky (but slow) smooth colour value
            s = (double)i;
        }
        
        //if(true){
            //return Color.getHSBColor((float)s, 0.8f, 1.0f);
        //}else{
        
            double cycleSize = Math.log(detail) * cycleMultiplier;
            //double s = (double)i + 1.0 - Math.log(Math.log(z.abs()))/Math.log(2.0);
            //double colour = (double) i % cycleSize / cycleSize;
            double colour =  s % cycleSize / cycleSize;
            //return Colour.hsvToRgb(colour, 0.8, 1.0);
            return Color.getHSBColor((float)colour, 0.8f, 1.0f);
       // }
    }
    
    public String toString(){
        return "f(z) = z^2 + c, CycleMultiplier: "+cycleMultiplier;
    }
    
}
