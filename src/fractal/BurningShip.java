/*
 * Copyright Luke Wallin 2012
 */
package fractal;

import LukesBits.Complex;
import LukesBits.Vector;
import java.awt.Color;

/**
 *
 * @author Luke
 * 
 * everything except f(z) is the same as the mandelbrot, so this just extends it and overrides newZ
 * 
 */
public class BurningShip extends Mandelbrot{
    
    public BurningShip(double _cycleMultiplier,boolean _smoothColour){
        super(_cycleMultiplier, _smoothColour);
    }
    
    @Override
     protected Complex newZ(Complex z, Complex c){
         Complex q = new Complex(Math.abs(z.re()), Math.abs(z.im()));
         return q.times(q).plus(c);
    }
    
    @Override
    public String toString(){
        return "f(z) = (|Re(z)| + i|Im(z)|)^2 + c, CycleMultiplier: "+cycleMultiplier;
    }
    
    @Override
    public double defaultZoom(){
        return 3.5;
    }
    
    @Override
    public int defaultDetail(){
        return 50;
    }
    
    @Override
    public Vector defaultCentre(){
        return new Vector(-0.4, -0.5);
    }
    
    @Override
    public FractalSettings defaultSettings() {
        return new FractalSettings(defaultZoom(), defaultDetail(), defaultCentre(), this);
    }
}
