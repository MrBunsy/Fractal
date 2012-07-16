/*
 * Copyright Luke Wallin 2012
 */
package fractal;

import LukesBits.Complex;

/**
 *
 * @author Luke
 */
public class CustomFunction2 extends Julia{
    public CustomFunction2(){
        super(new Complex(-0.726895347709114071439, 0.188887129043845954792), Julia.ColourType.COSINE);
    }
    
    public Complex newC(Complex z){
        //return c.times(c).plus(mu);
        Complex c = mu;
        
        return (z.square().plus(c)).divides(z.minus(c));
    }
}
