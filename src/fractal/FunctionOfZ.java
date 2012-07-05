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
 */
public interface FunctionOfZ {
    //for mandelbrot, z is the previous iteration and c is x + iy
    //for julia, z is the 
    public Color iterations(Complex z,Complex c, int detail);
    
    public Vector defaultCentre();
    
    public int defaultDetail();
    
    public double defaultZoom();
    
    @Override
    public String toString();
}
