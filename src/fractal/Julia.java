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
public class Julia implements FunctionOfZ{

    protected Complex mu;
    private ColourType colour;
    
    protected double cycleMultiplier;
    protected double cycleOffset;
    protected boolean smoothColour;
    
    public double defaultCycleMultiplier = 50;
    public double defaultCycleOffset = 0.5;
    
    //private Colour background;
    //private 

    @Override
    public Vector defaultCentre() {
        return new Vector(0, 0);
    }

    @Override
    public int defaultDetail() {
        return 1000;
    }

    @Override
    public double defaultZoom() {
        return 3d;
    }
    
    @Override
    public double getCycleMultiplier() {
        return cycleMultiplier;
    }

    @Override
    public double getCycleOffset() {
        return cycleOffset;
    }

    @Override
    public double getDefaultCycleOffset() {
        return defaultCycleOffset;
    }

    @Override
    public double getDefaultCycleMultiplier() {
       return defaultCycleMultiplier;
    }

    @Override
    public void setCycleMultiplier(double _cycleMultiplier) {
        cycleMultiplier=_cycleMultiplier;
    }

    @Override
    public void setCycleOffset(double _cycleOffset) {
        cycleOffset=_cycleOffset;
    }

    @Override
    public FractalSettings defaultSettings() {
        return new FractalSettings(defaultZoom(), defaultDetail(), defaultCentre(), this);
    }

    @Override
    public void resetColour() {
        cycleMultiplier=defaultCycleMultiplier;
        cycleOffset=defaultCycleOffset;
    }

    
    public static enum ColourType{
        COSINE,NONE
    }
            
    public Julia(Complex _mu, ColourType _colour){//, Colour _background){
        mu=_mu;
        colour=_colour;
        
        cycleMultiplier=defaultCycleMultiplier;
        cycleOffset=defaultCycleOffset;
    }
    
    public Complex newC(Complex c){
        return c.times(c).plus(mu);
    }
    
    @Override
    public Color getColourFor(Complex z, Complex c, int detail) {
        
        Complex oldC = c;
        
        int i=0;
        while (c.magnitudeSqrd() < 4 && i < detail) {
            oldC=c;
            c = newC(c);
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
                    
                     double cycleSize = Math.log(detail) * cycleMultiplier;

                     double co = ( s + cycleSize*cycleOffset) % cycleSize / cycleSize;
                    
                     return Color.getHSBColor((float)co, 0.5f, 1.0f);
                     //return Colour.red.dim(s).toColor();
                }else{
                    //non escaped
                    return Colour.blue.dim(1.0-angle/Math.PI).toColor();
                    
                    //return Colour.blue.toColor();
                    
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
    
    public String toString(){
        return toString(true);
    }
    
    @Override
    public String toString(boolean detailed){
        return "f(z) = z^2 + "+mu
                + (detailed ? ", CycleMultiplier:"+cycleMultiplier+ ", CycleOffset: "+cycleOffset : "");
    }
    
}
