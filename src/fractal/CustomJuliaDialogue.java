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
public class CustomJuliaDialogue extends JDialog{
    
    private Fractal fractal;
    private JTextField re,im;
    private JButton okay,cancel;
    
    public CustomJuliaDialogue(Fractal _fractal, Component window){
        fractal=_fractal;
        
        setTitle("Custom Quadratic Julia Set");
        
         setLayout(new GridBagLayout());
         GridBagConstraints c = new GridBagConstraints();
         
         c.gridx=0;
         c.gridy=0;
         c.gridwidth=4;
         JLabel l = new JLabel("f(z) = z^2 + C");
                 l.setPreferredSize(new Dimension(100,30));
         add(l,c);
         
         c.gridx=0;
         c.gridy=1;
         c.gridwidth=1;
         add(new JLabel("C = "),c);
         
         re=new JTextField(0.36237+"");
         re.setPreferredSize(new Dimension(100,30));
         c.gridx=1;
         c.gridy=1;
         c.gridwidth=1;
         add(re,c);
         
         c.gridx=2;
         c.gridy=1;
         c.gridwidth=1;
         add(new JLabel(" + i "),c);
         
         im=new JTextField(0.32+"");
         im.setPreferredSize(new Dimension(100,30));
         c.gridx=3;
         c.gridy=1;
         c.gridwidth=1;
         add(im,c);
         
         //setPreferredSize(new Dimension(200,100));
         
         cancel = new JButton("Cancel");
         cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
         c.gridx=1;
         c.gridy=2;
         add(cancel,c);
         
         okay = new JButton("Draw");
         okay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fractal.loadCustomJuliaQuadratic(new Complex(Double.parseDouble(re.getText()),Double.parseDouble(im.getText())));
                setVisible(false);
            }
        });
         c.gridx=3;
         c.gridy=2;
         add(okay,c);
         
         pack();
         
         setResizable(false);
         setLocationRelativeTo(window);
    }
}
