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

    private double cycleMultiplier;
    
    public Mandelbrot(double _cycleMultiplier){
        cycleMultiplier=_cycleMultiplier;
    }
    
    @Override
    public Color iterations(Complex z, Complex c, int detail) {
        int i=0;
        while (z.magnitudeSqrd() < 4 && i < detail) {
            z = z.times(z).plus(c);
            i++;
        }
        
        if(i==detail){
            return new Color(0,0,0);
        }
        
        double cycleSize = Math.log(detail) * cycleMultiplier;

        double colour = (double) i % cycleSize / cycleSize;
        //return Colour.hsvToRgb(colour, 0.8, 1.0);
        return Color.getHSBColor((float)colour, 0.8f, 1.0f);
    }
    
    public String toString(){
        return "f(z) = z^2 + c, CycleMultiplier: "+cycleMultiplier;
    }
    
}
