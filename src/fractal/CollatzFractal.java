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
 * http://en.wikipedia.org/wiki/Collatz_fractal#Collatz_fractal
 */
public class CollatzFractal extends Mandelbrot{
    public CollatzFractal(){
        super(false);
        defaultCycleMultiplier = 1;
        cycleMultiplier=defaultCycleMultiplier;
    }
    
//    protected Complex newZ(Complex z, Complex c){
//        
//        //f(z) = 0.5*z*cos^2(z*pi/2) + 0.5*(3*z + 1 )*sin^2(z*pi/2);
//        
//        //c = c.times(0.5).times(  c.times(Math.PI/2).cos().square()  ).plus(  (c.times(3).plus(new Complex(1, 0))).times(0.5).times(  c.times(Math.PI/2).sin().square()  )  );
//        
//        //z = 0.25(1 + 4z - (1 + 2z)*cos(pi*z) )
//        
//        //c = c.times(Math.PI).cos().times( c.times(2).plus(new Complex(1, 0))  ).plus( c.times(4).plus(new Complex(1,0)) ).times(0.25);
//        
//        z = z.times(Math.PI).cos().times( z.times(5).plus(new Complex(2, 0))  ).minus(z.times(7).plus(new Complex(2, 0))).times(0.25);
//        
//        return z;
//    }
//    

    
    public int iterations(Complex z, Complex c, int detail){
        int i=0;
        while (c.magnitudeSqrd() <  (double)detail && i< detail) {//100000 && i< 
            
            //works
            c = (    ( (c.times(7)).plus(new Complex(2, 0))  ).minus(  (c.times(5).plus(new Complex(2, 0))).times( (c.times(Math.PI)).cos() ) )    ).times(0.25);
            
            
            //c = c.times(0.5).times(  c.times(Math.PI/2).cos().square()  ).plus(  (c.times(3).plus(new Complex(1, 0))).times(0.5).times(  c.times(Math.PI/2).sin().square()  )  );
            //c = c.times(Math.PI).cos().times( c.times(2).plus(new Complex(1, 0))  ).plus( c.times(4).plus(new Complex(1,0)) ).times(0.25);
            i++;
        }
        
        return i;
    }


    
    @Override
    public Color getColourFor(Complex z, Complex c, int detail) {

        int i=iterations(z, c, detail);
        
        
        if(i==detail){
            //escaped
            return new Color(0,0,0);
        }
        
        double s = (double)i;
        
        double cycleSize = Math.log(detail) * cycleMultiplier;
        //double s = (double)i + 1.0 - Math.log(Math.log(z.abs()))/Math.log(2.0);
        //double colour = (double) i % cycleSize / cycleSize;
        double colour =  (s + cycleOffset*cycleSize) % cycleSize / cycleSize;
        //return Colour.hsvToRgb(colour, 0.8, 1.0);
        return Color.getHSBColor((float)colour, 0.8f, 1.0f);
    }

}
