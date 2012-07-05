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
    
    public FractalSettings defaultSettings();
    
    public void resetColour();
    
    //public void openColourDialogue();
    
    public double getCycleMultiplier();
    public double getCycleOffset();
    public double getDefaultCycleOffset();
    public double getDefaultCycleMultiplier();
    
    public void setCycleMultiplier(double _cycleMultiplier);
    public void setCycleOffset(double _cycleOffset);
    
    @Override
    public String toString();
}
