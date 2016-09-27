
package gTester;

import java.io.UnsupportedEncodingException;

/**
 * Decodes the serial packet into average hourly radiation dose and 
 * per second radiation dose values.
 * 
 * @author Mike Fouche
 */

public class DecodeGamm 
{
    // String to hold the entire data set - the average hourly 
    private String hexChar;
    // Char array to hold the above - it's used to for the search for each
    // data set (the hourly and per second values).  
    private char[] aa;
    // String that holds the Hex value.
    private String hourlyAverageDose;
    // String that holds the Hex value.
    private String secondDose;
    // If true then the slot in the char[] aa array is a white space.
    private boolean charSpace;
    // String that holds the decimal value of the average hourly radiation dose.
    // oneHex -> first Hex value
    private String oneHex;
    // String that holds the decimal value of the per second radiation does.
    // twoHex -> second Hex value
    private String twoHex;
    // 4-element String that holds the values of:
    // - hourlyAverageDose
    // - secondDose
    // - oneHex
    // - twoHex
    private final String[] radValues;
    
    /**
     * Constructor.
     */
    public DecodeGamm()
    {
        hexChar = "";

        hourlyAverageDose = "";
        secondDose = "";
        
        charSpace = true;
        
        oneHex = "";
        twoHex = "";
        
        radValues = new String[4];
    }
    
    /**
     * 
     * @param buff The 15 byte array that contains the received Gamma sensor
     * data (via serial port).
     * 
     * @return The String array which contains the decimal and Hex values of the
     * average hourly radiation dose and per second dose.
    */
    public String[] gammData(byte[] buff)
    {
        try
        {
            // Pull in the byte array as Hex.
            hexChar = new String(buff, "US-ASCII");
            // Now load to the Char array.
            aa = hexChar.toCharArray();
            // Set the first part of the Hex value.
            hourlyAverageDose = "0x";
            // Default is true.
            charSpace = true;
            // Cycle through elements 1 through 8 to obtain the average hourly
            // radiation dose value (8 bytes).  Element 0 holds the "." if it's
            // sent.
            for(int ii = 1; ii < 9; ii++)
            {
                // If the Char character is not white space or the bolean
                // is false then load the value.  After the first number has
                // been encountered, zeroes are counted as values.  Zeroes
                // prior are counted as white space.
                if(  (byte)aa[ii] != 0 || charSpace == false )
                {
                    hourlyAverageDose += aa[ii];
                    charSpace = false;
                }
            }    

            // Set the first part of the Hex value.
            secondDose = "0x";
            charSpace = true;
            // Cycle through elements 9 through 12 to obtain the per second
            // radiation dose value (4 bytes).  Elements 13 and 14 hold the
            // carriage return and linefeed characters.
            for(int ii = 9; ii < 13; ii++)
            {
                // If the Char character is not white space or the bolean
                // is false then load the value.  After the first number has
                // been encountered, zeroes are counted as values.  Zeroes
                // prior are counted as white space.
                if( (byte)aa[ii] != 0 || charSpace == false)
                {
                    secondDose += aa[ii];
                    charSpace = false;
                }    
            }    

            // String that holds the decimal value of the average hourly radiation dose. 
            oneHex = Long.decode(hourlyAverageDose).toString();
            // String that holds the decimal value of the per second radiation does.
            twoHex = Long.decode(secondDose).toString();

            radValues[0] = hourlyAverageDose;
            radValues[1] = secondDose;
            radValues[2] = oneHex;
            radValues[3] = twoHex;
        }
        catch(UnsupportedEncodingException | NumberFormatException e)
        {
            System.out.println("Exception in Gamma byte decoding: "+e);
        }
        
        return radValues;
    } 
    
} // end of class DecodeGamm
