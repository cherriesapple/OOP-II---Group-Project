package bluej_extension;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * This class is used by the extension to access bluej's console output.
 * 
 * @author Shurona Jackson
 */
public class ConsoleSummary {

    PrintStream originalOutput;
    private int messageCount = 0;

    /**
     * Intercepts the standard system output stream ({@code System.out}) to count console messages.
     *
     * This is done by creating a custom {@link PrintStream} that wraps the original output stream.
     */
    public void captureMessages(){
        originalOutput = System.out;

        PrintStream countingStream = new PrintStream(new OutputStream(){

            /**
             * Writes a byte to the bluej console and increments the messageCount variable and counts the number of lines outputted to the bluej console. 
             */
            public void write(int bytes){
                originalOutput.write(bytes);

                if (bytes=='\n')
                    messageCount++;
                
            }
        });

        System.setOut(countingStream);
    }

    /**
     * Used to generate reports. 
     * 
     * @return Number of outputs sent to the console. 
     */
    public int getMessageCount(){
        System.setOut(originalOutput);
        int totalMessages = messageCount; 
        messageCount = 0;
        return totalMessages;
    }
}