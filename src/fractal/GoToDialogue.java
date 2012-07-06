/*
 * Copyright Luke Wallin 2012
 */
package fractal;

import LukesBits.Vector;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author Luke
 */
public class GoToDialogue extends JDialog{
    
    private Component parent;
    private Fractal fractal;
    private JTextField x,y,zoom;
    
    public GoToDialogue(Fractal _fractal, Component _parent){
        fractal=_fractal;
        parent=_parent;
        
        setTitle("Viewport Settings");
        
        setLayout(new GridLayout(5, 2, 5, 5));
        
        JLabel centreText = new JLabel("Centre = ");
        add(centreText);
        JLabel centreText2 = new JLabel("x + iy");
        add(centreText2);
        
        JLabel xText = new JLabel("x = ");
        add(xText);
        x = new JTextField(fractal.getFunctionOfZ().defaultCentre().x+"");
        add(x);
        
        JLabel yTeyt = new JLabel("y = ");
        add(yTeyt);
        y = new JTextField(fractal.getFunctionOfZ().defaultCentre().y+"");
        add(y);
        
        JLabel zoomText = new JLabel("Zoom = ");
        add(zoomText);
        zoom = new JTextField(fractal.getFunctionOfZ().defaultZoom()+"");
        add(zoom);
        
        JButton cancel = new JButton("Cancel");
        add(cancel);
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        
        JButton ok = new JButton("Use");
        add(ok);
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                use();
            }
        });
        
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }
    
    public void use(){
        setVisible(false);
        
        FractalSettings s = fractal.exportSettings();
        s.zoom=Double.parseDouble(zoom.getText());
        s.centre = new Vector(Double.parseDouble(x.getText()),Double.parseDouble(y.getText()));
        
        fractal.loadSettings(s);
    }
}
