/*
 * Copyright Luke Wallin 2012
 */
package fractal;

import java.awt.Component;
import java.awt.Dialog;
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
    
    public ColourDialogue(FunctionOfZ _fz,Fractal _fractal, Window window){
        super(window);
        fz=_fz;
        fractal=_fractal;
        
        setTitle("Colour Settings");
        
        GridLayout g = new GridLayout(3,3,10,10);
        
        //add space around the edge of the window
        //http://www.velocityreviews.com/forums/t135834-gridlayout-does-not-leave-room-between-edge-of-window-and-objects.html
        JPanel panel = (JPanel) getContentPane();
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        //rows, cols, spacing
        setLayout(g);
        
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
        //setAlwaysOnTop(true);
        setLocationRelativeTo(window);
        
        //show make it act like a normal dialogue: user can't click behind it and it can't dissapear behind the applet/jframe
         setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
         
         //for helping with applets
         setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
    }
    
    public void open(){
        reset();
        setVisible(true);
    }
    
    private void use(){
        try{
            fz.setCycleMultiplier(Double.parseDouble(cycleMultiplierField.getText()));
            fz.setCycleOffset(Double.parseDouble(cycleOffsetField.getText()));
            setVisible(false);
            fractal.generate();
        }catch(NumberFormatException er){
            JOptionPane.showMessageDialog(rootPane, "Invalid inputs", "Error",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void reset(){
        cycleMultiplierField.setText(fz.getCycleMultiplier()+"");
        cycleOffsetField.setText(fz.getCycleOffset()+"");
    }
    
    
}
