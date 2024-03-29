/*
 * Copyright Luke Wallin 2012
 */
package fractal;

import LukesBits.Vector;
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
 * 
 * Dialogue box to choose where on the fractal to view, specifying x,y and zoom level.  
 * When dialogue opens it defaults to the default centre of the fractal being viewed and the current zoom level
 * Might change those defaults, not sure.
 */
public class GoToDialogue extends JDialog{
    
    private Window parent;
    private Fractal fractal;
    private JTextField x,y,zoom;
    
    public GoToDialogue(Fractal _fractal, Window _parent){
        super(_parent);
        fractal=_fractal;
        parent=_parent;
        
        setTitle("Viewport Settings");
        
        //add space around the edge of the window
        //http://www.velocityreviews.com/forums/t135834-gridlayout-does-not-leave-room-between-edge-of-window-and-objects.html
        JPanel panel = (JPanel) getContentPane();
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        setLayout(new GridLayout(5, 2, 10, 5));
        
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
        zoom = new JTextField(fractal.getZoom()+"");
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
        //setAlwaysOnTop(true);
        
        //show make it act like a normal dialogue: user can't click behind it and it can't dissapear behind the applet/jframe
         setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
         //for helping with applets
         setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
    }
    /**
     * Action of the use button - makes the user's input so.
     */
    public void use(){
        try{
            FractalSettings s = fractal.exportSettings();
            s.zoom=Double.parseDouble(zoom.getText());
            s.centre = new Vector(Double.parseDouble(x.getText()),Double.parseDouble(y.getText()));
            fractal.loadSettings(s);
            setVisible(false);
        }catch(NumberFormatException e){
            JOptionPane.showMessageDialog(rootPane, "Invalid inputs", "Error",JOptionPane.ERROR_MESSAGE);
        }
        
    }
}
