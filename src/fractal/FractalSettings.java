/*
 * Copyright Luke Wallin 2012
 */
package fractal;

import LukesBits.Vector;

/**
 *
 * @author Luke
 */
public class FractalSettings {
    public double zoom;
    public int detail;
    public Vector centre;
    public FunctionOfZ fz;
    public int samples;
    
    public FractalSettings(double _zoom,int _detail, Vector _centre, FunctionOfZ _fz, int _samples){
        zoom=_zoom;
        detail=_detail;
        centre=_centre;
        fz=_fz;
        samples = _samples;
    }
}
