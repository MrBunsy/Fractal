/*
 * Copyright Luke Wallin 2012
 */
package fractal;

import LukesBits.Complex;
import LukesBits.Vector;

/**
 *
 * @author Luke
 */
public class CustomFunction extends Mandelbrot{
    public CustomFunction(){
        super(30,false);
        defaultCycleMultiplier = 15;
        defaultCycleOffset = 0.65;
        resetColour();
    }
    
    @Override
     protected Complex newZ(Complex z, Complex c){
         
        //Complex q = z.sin().plus(z.square());
        
        //f(z) = (z+c)(z+c)
        //Complex q = z.plus(c).times(z.plus(c));
        
        return (z.square().plus(c)).divides(z.minus(c));
        
        //return z.exp().minus(c);
        
        //Z^3 / (1 + CZ^2)
        //return z.power(3).divides((z.square().times(c)).plus(new Complex(1,0)));
         
         
         //return q;//.plus(c);
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
        return new Vector(0.4, 0.3);
    }
    
    @Override
    public FractalSettings defaultSettings() {
        return new FractalSettings(defaultZoom(), defaultDetail(), defaultCentre(), this, defaultSamples());
    }
}
