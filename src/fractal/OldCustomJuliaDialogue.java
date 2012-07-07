/*
 * Copyright Luke Wallin 2012
 */
package fractal;

import LukesBits.Complex;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author Luke
 */
public class OldCustomJuliaDialogue extends JDialog{
    
    private Fractal fractal;
    private JTextField re,im;
    //private JButton okay,cancel;
    private JuliaSelectPanel p;
    
    public OldCustomJuliaDialogue(Fractal _fractal, Component window){
        fractal=_fractal;
        
        setTitle("Custom Quadratic Julia Set");
        
        
        // 3 wide:
        // Equation
        // Mu = [ real box]
        // + i [img box]
        // |-----------|
        // | mandelbrot|
        // |-----------|
        // Cancel Reset Use
        
         setLayout(new GridBagLayout());
         GridBagConstraints c = new GridBagConstraints();
         
         c.gridx=0;
         c.gridy=0;
         c.gridwidth=3;
         JLabel l = new JLabel("f(z) = z^2 + Mu");
                 l.setPreferredSize(new Dimension(100,30));
         add(l,c);
         
         JLabel mu = new JLabel("Mu = ");
         //mu.setPreferredSize(new Dimension(50,30));
         c.gridx=0;
         c.gridy=1;
         c.gridwidth=1;
         add(mu,c);
         
         re=new JTextField(0.36237+"");
         re.setPreferredSize(new Dimension(150,30));
         c.gridx=1;
         c.gridy=1;
         c.gridwidth=2;
         add(re,c);
         
         c.gridx=0;
         c.gridy=2;
         c.gridwidth=1;
         add(new JLabel("    + i "),c);
         
         im=new JTextField(0.32+"");
         im.setPreferredSize(new Dimension(150,30));
         c.gridx=1;
         c.gridy=2;
         c.gridwidth=2;
         c.ipady=5;
         add(im,c);
         
         //setPreferredSize(new Dimension(200,100));
         
         c.gridx=0;
         c.gridy=3;
         c.gridwidth=3;
         add(new JLabel("Click the Mandelbrot to select a value for mu:"),c);
         
         
         //p = new JuliaSelectPanel(200, 150, this);
         
         c.gridx=0;
         c.gridy=4;
         c.gridwidth=3;
         c.anchor=GridBagConstraints.CENTER;
         c.ipady=5;
         //c.gridheight=3;
         add(p,c);
         
         JButton cancel = new JButton("Cancel");
         cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
         c.gridx=0;
         c.gridwidth=1;
         c.gridy=5;
         add(cancel,c);
         //I've just switched this buttons around - for soem reason when trying to do it properly the gridbag goes wrong
         JButton reset = new JButton("Reset");
         reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                p.reset();
            }
        });
         c.gridx=1;
         c.gridy=5;
         c.anchor=GridBagConstraints.LINE_END;
         add(reset,c);
         
         JButton okay = new JButton("Draw");
         okay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fractal.loadCustomJuliaQuadratic(new Complex(Double.parseDouble(re.getText()),Double.parseDouble(im.getText())));
                setVisible(false);
            }
        });
         c.gridx=2;
         c.gridy=5;
         add(okay,c);
         
         
         
         pack();
         
         setResizable(false);
         setLocationRelativeTo(window);
         
         //show make it act like a normal dialogue: user can't click behind it and it can't dissapear behind the applet/jframe
         setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
         
         //setAlwaysOnTop(true);
    }
    
    public int getThreads(){
        return fractal.getThreads();
    }
    
    public void setMu(Complex mu){
        re.setText(String.valueOf(mu.re()));
        im.setText(String.valueOf(mu.im()));
    }
}
