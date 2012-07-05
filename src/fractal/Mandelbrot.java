/*
 * Copyright Luke Wallin 2012
 */
package fractal;

import LukesBits.Colour;
import LukesBits.Complex;
import LukesBits.Vector;
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
    
    public static boolean isPointIn(Complex c){
        return isPointIn(c, 1000000);
    }
    
    //is a point in the mandelbrot set?
    public static boolean isPointIn(Complex c, int detail){
        int i=0;
        Complex z = new Complex(0,0);
        
        while (z.magnitudeSqrd() < 4 && i < detail) {
            z = z.times(z).plus(c);
            i++;
        }
        //if i is less than detail then we escaped, if not we're assuming we won't escape
        return i<detail;
    }
    
    @Override
    public Color iterations(Complex z, Complex c, int detail) {
        int i=0;
        double s=0;
        
        //Complex oldZ=z;
        
        while (z.magnitudeSqrd() < 4 && i < detail) {
            //oldZ=z;
            z = newZ(z,c);
            i++;
            //this is for smooth colouring - http://www.hiddendimension.com/FractalMath/Divergent_Fractals_Main.html
            //does slow things down a lot though
//            if(smoothColour){
//                s = s + Math.exp(-z.abs());
//            }
        }
        
        
        
        if(i==detail){
            //escaped
            return new Color(0,0,0);
        }
        
        if(!smoothColour){
            //just using iteration, not the funky (but slow) smooth colour value
            s = (double)i;
        }else{
            // extra incrementations seems to help the smoothness - http://www.codeproject.com/Articles/18361/Mandelbrot-Set-with-Smooth-Drawing
            //don't know why :/
            //TODO work out why?
            z = newZ(z,c); i++;
            z = newZ(z,c); i++;
            z = newZ(z,c); i++;
            s=(double)i - Math.log(Math.log(z.abs()))/Math.log(2.0);// + 1.0
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

    @Override
    public Vector defaultCentre() {
        return new Vector(-0.5, 0);
    }

    @Override
    public int defaultDetail() {
        return 50;
    }

    @Override
    public double defaultZoom() {
        return 3.0;
    }

    @Override
    public FractalSettings defaultSettings() {
        return new FractalSettings(defaultZoom(), defaultDetail(), defaultCentre(), this);
    }
    
}
