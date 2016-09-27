
package gTester;

import java.io.IOException;
import java.io.InputStream;

import gnu.io.SerialPort;

/**
 * Retrieves the buffered data from the RS232 serial port, and decodes the data 
 * into the appropriate values - hourly dose (first part) and per second dose
 * (second part).
 * 
 * @author Mike Fouche
 */

public class GammRecv implements Runnable 
{
    private SerialPort serialPort;
    private ThreadQueue tQ;
    private DataObject dataObject;
    
    private InputStream in;
    // Byte array that holds the 15-byte data packet.
    private final byte[] buffer;
    // Byte array that receives the serial data.
    private final byte[] buffFinal;
    // Number of bytes in serial buffer.
    private int len; 
    // Number of bytes that have been loaded into byte array, buffFinal.
    private int buffctr;

    /**
     * Constructor
     * 
     * @param sPort The Serial port object.
     * @param tQueue The Thread communications object.
     * @param dObj The Data exchange object.
     */
    public GammRecv ( SerialPort sPort, ThreadQueue tQueue, DataObject dObj )
    {
        this.serialPort = sPort;
        this.tQ = tQueue;
        this.dataObject = dObj;

        // Set up inputstream object.
        try
        {
            in = serialPort.getInputStream();
        } 
        catch ( IOException e )
        {
            dataObject.setCLogDisplayText("Exception: "+e+", in GammRecv");
        }
        
        buffer = new byte[1024]; 
        
        len = 1;
        // Counts the number of bytes read from the serial port
        buffctr = 0;
        // The byte array that is loaded with the incoming serial data packet.
        buffFinal = new byte[15];
    }
    
    //--------------------------------------------------------------------------
    @Override
    public void run ()
    {
        String[] radValues = new String[4];
        
        String displayData = "";
        
        // Create instance of Gamma sensor data decoder.  This contains the
        // method for decoding the data packet from the serial port.
        DecodeGamm dgam = new DecodeGamm();

        try
        {
            displayData = "Waiting on data ...\n\n";
            dataObject.setCLogDisplayText(displayData);
            
            // If len = -1 then there is no data in the buffer.
            while ( (len = in.read(buffer)) > -1 && tQ.getSimStatus() )
            {
                // If the number of bytes in the buffer is greater than zero
                // then begin processing them
                if ( len > 0 )
                {    
                    for (int i = 0; i < len; i++)
                    {    
                        buffFinal[buffctr] = buffer[i];
                        
                        // If 15 bytes have been retrieved from the serial
                        // buffer, then the full attitude state can be decoded.
                        // 0 through 14 = 15 bytes.
                        if (buffctr == 14)
                        {
                            // Decode the data packet and return the hourly 
                            // average and per second radiation dosage values
                            // in decimal and Hex format.
                            radValues = dgam.gammData(buffFinal);

                            // Display the values in the text window.
                            displayData = "Avg Radiation Dose per hour = "+radValues[2]+
                                                                " uR, Hex value = "+radValues[0];
                            dataObject.setCLogDisplayText(displayData);
                            displayData = "Radiation Dose per second = "+radValues[3]+
                                                                " uR, Hex value = "+radValues[1]+"\n";
                            dataObject.setCLogDisplayText(displayData);

                            // Reset the byte counter
                            buffctr = 0;

                        } // end of loading data
                        else
                        {
                            // Increment the byte counter.
                            buffctr++;
                        }   
                        
                    } // end of processing through bytes
                
                } // end of if-statement len > 0 
            
            } // end while loop
            
            // Close the serial port.
            serialPort.close();
        } 
        catch ( IOException e )
        {
            dataObject.setCLogDisplayText("Exception: "+e+", in GammRecv");
        }

        // Notify user that the thread has finished.
        displayData = "Exiting Gamma Sensor Tester thread ...\n";
        dataObject.setCLogDisplayText(displayData+"\n");
    } 
    
} // end of class GammRecv



