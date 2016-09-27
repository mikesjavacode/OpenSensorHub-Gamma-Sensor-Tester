
package gTester;

import java.awt.BorderLayout;

import javax.swing.JFrame;

/**
 * This application receives output data from a Model 2070 Gamma Detector
 * Module - made by Health Physics Instruments, a division of Far West Technology, 
 * Inc. - and / or the software sensor simulator, GammaSensorSimulator.
 * 
 * @author Mike Fouche
 */

public class GammaTester 
{
    /**
     * @param args None.
     */
    public static void main(String[] args)
    {
        // Build the JFrame.
        JFrame jf = new JFrame("Gamma Sensor Tester");
        jf.setSize(400,400);
        jf.setResizable(false);
        jf.setLayout(new BorderLayout());
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Build the main JPanel and load it into the JPanel.
        BuildMainPanel bmp = new BuildMainPanel();
        jf.add(bmp);
        
        // Make the JFrame visible.
        jf.setVisible(true);
   }

} // end of class GammaTester

