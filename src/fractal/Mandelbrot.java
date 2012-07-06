/*
 * Copyright Luke Wallin 2012
 */
package fractal;

import LukesBits.Colour;
import LukesBits.Complex;
import LukesBits.Vector;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author Luke
 */
public class Mandelbrot implements FunctionOfZ{

    protected double cycleMultiplier;
    protected double cycleOffset;
    protected boolean smoothColour;
    //protected ColourPane colourChooser;
    protected Fractal fractal;
    
    public double defaultCycleMultiplier = 30;
    public double defaultCycleOffset = 0;
    public Vector defaultCentre = new Vector(-0.5, 0);
    
    protected double k;
    
    public Mandelbrot(boolean _smoothColour){
        k=2;
        smoothColour=_smoothColour;
        cycleOffset=defaultCycleOffset;
        cycleMultiplier=defaultCycleMultiplier;
    }
    
    public Mandelbrot(double _k){
        
        if(_k>2){
            defaultCycleMultiplier=5;
            defaultCentre = new Vector(0, 0);
        }
        
        cycleMultiplier=defaultCycleMultiplier;
        cycleOffset=defaultCycleOffset;
        
        smoothColour=true;
        
        k=_k;
    }
    
    public Mandelbrot(double _cycleMultiplier,boolean _smoothColour){//, Fractal _fractal){
        k=2;
        cycleMultiplier=_cycleMultiplier;
        cycleOffset=0;
        smoothColour=_smoothColour;
        
        //not entirely sure if having this reference back again is a good thing, but it's needed atm for the colour stuff
        //fractal=_fractal;
        
//        colourChooser=new JDialog((Dialog)null, "Colour Options");
//        colourChooser.add(new ColourPane(this));
//        
//        colourChooser.pack();
        
        //colourChooser=new ColourPane(this);
    }
    
    @Override
    public void resetColour(){
        cycleMultiplier=defaultCycleMultiplier;
        cycleOffset=defaultCycleOffset;
    }
    
    protected Complex newZ(Complex z, Complex c){
        //TODO potentially see if all the prime factors are 2 and then use only powers of 2.
        switch((int)k){
            case 8:
                z=z.times(z);
            case 4:
                z=z.times(z);
            case 2:
                z=z.times(z);
                break;
            default:
                //this is VERY slow
                z = z.power(k);
                break;
        }
        return z.plus(c);
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
    
//    public int iterations(Complex z, Complex c, int detail){
//        int i=0;
//        while (z.magnitudeSqrd() < 4 && i < detail) {
//            //oldZ=z;
//            z = newZ(z,c);
//            i++;
//            //this is for smooth colouring - http://www.hiddendimension.com/FractalMath/Divergent_Fractals_Main.html
//            //does slow things down a lot though
////            if(smoothColour){
////                s = s + Math.exp(-z.abs());
////            }
//        }
//        
//        return i;
//    }
    
    @Override
    public Color getColourFor(Complex z, Complex c, int detail) {
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
        }else if(cycleMultiplier==0){
            return new Color(255,255,255);
        }
        
        if(!smoothColour){
            //just using iteration, not the funky (but slow) smooth colour value
            s = (double)i;
        }else{
            // extra incrementations seems to help the smoothness - http://www.codeproject.com/Articles/18361/Mandelbrot-Set-with-Smooth-Drawing
            //don't know why :/
            //TODO work out why?
            //http://www.vb-helper.com/howto_net_mandelbrot_smooth.html just says that it's not exact and using a later z reduces the error.  seems to work.
            if(k==2){
                z = newZ(z,c);// i++;
                z = newZ(z,c);// i++;
                z = newZ(z,c);// i++;
                z = newZ(z,c);
                z = newZ(z,c);
            }
            s=(double)i +1 - Math.log(Math.log(z.abs()))/Math.log((double)k);// + 1.0
        }
        
        //if(true){
            //return Color.getHSBColor((float)s, 0.8f, 1.0f);
        //}else{
        
            double cycleSize = Math.log(detail) * cycleMultiplier;
            //double s = (double)i + 1.0 - Math.log(Math.log(z.abs()))/Math.log(2.0);
            //double colour = (double) i % cycleSize / cycleSize;
            double colour =  (s + cycleOffset*cycleSize) % cycleSize / cycleSize;
            //return Colour.hsvToRgb(colour, 0.8, 1.0);
            return Color.getHSBColor((float)colour, 0.8f, 1.0f);
       // }
    }
    
    public String toString(){
        return toString(true);
    }
    
    public String toString(boolean detailed){
        return "f(z) = z^"+k+" + c"
                + (detailed ? ", CycleMultiplier: "+cycleMultiplier+ ", CycleOffset: "+cycleOffset : "");
    }

    @Override
    public Vector defaultCentre() {
        return defaultCentre;
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

//    public void openColourDialogue() {
//        //colourChooser.setVisible(true);
//        colourChooser.open();
//    }

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
    
}


