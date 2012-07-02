/*
 * Copyright Luke Wallin 2012
 */
package fractal;

import LukesBits.Complex;
import java.awt.Color;

/**
 *
 * @author Luke
 */
public class BurningShip implements FunctionOfZ{
    private double cycleMultiplier;
    
    public BurningShip(double _cycleMultiplier){
        cycleMultiplier=_cycleMultiplier;
    }
    
    @Override
    public Color iterations(Complex z, Complex c, int detail) {
        int i=0;
        while (z.magnitudeSqrd() < 4 && i < detail) {
            Complex q = new Complex(Math.abs(z.re()), Math.abs(z.im()));
            z = q.times(q).plus(c);
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
        return "f(z) = (|Re(z)| + i|Im(z)|)^2 + c, CycleMultiplier: "+cycleMultiplier;
    }
}
