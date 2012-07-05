/*
 * Copyright Luke Wallin 2012
 */
package fractal;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author Luke
 */
class ColourDialogue extends JDialog{
    
    //private Mandelbrot mandelbrot;
    private FunctionOfZ fz;
    private Fractal fractal;
    private JTextField cycleMultiplierField,cycleOffsetField;
    public final JDialog thisPanel=this;
    
    public ColourDialogue(FunctionOfZ _fz,Fractal _fractal, Component window){
        fz=_fz;
        fractal=_fractal;
        
        setTitle("Colour Settings");
        
        //rows, cols, spacing
        setLayout(new GridLayout(3,3,10,10));
        
        add(new JLabel("Cycle Multiplier:"));
        cycleMultiplierField=new JTextField(fz.getCycleMultiplier()+"");        
        add(cycleMultiplierField);
        add(new JLabel("Default:"+fz.getDefaultCycleMultiplier()));
        
        add(new JLabel("Cycle Offset:"));
        cycleOffsetField=new JTextField(fz.getCycleOffset()+"");        
        add(cycleOffsetField);
        add(new JLabel("Default:"+fz.getDefaultCycleOffset()+" Range:0-1"));
        
        
        
        JButton cancel = new JButton("Cancel");
        add(cancel);
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        
        JButton reset = new JButton("Reset");
        add(reset);
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reset();
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
        
        setLocationRelativeTo(window);
    }
    
    public void open(){
        reset();
        setVisible(true);
    }
    
    private void use(){
        fz.setCycleMultiplier(Double.parseDouble(cycleMultiplierField.getText()));
        fz.setCycleOffset(Double.parseDouble(cycleOffsetField.getText()));
        setVisible(false);
        fractal.generate();
    }
    
    private void reset(){
        cycleMultiplierField.setText(fz.getCycleMultiplier()+"");
        cycleOffsetField.setText(fz.getCycleOffset()+"");
    }
    
    
}
