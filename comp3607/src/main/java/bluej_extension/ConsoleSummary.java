package bluej_extension;

import java.io.OutputStream;
import java.io.PrintStream;

public class ConsoleSummary {

    PrintStream originalOutput;
    private int messageCount = 0;

    public void captureMessages(){
        originalOutput = System.out;

        PrintStream countingStream = new PrintStream(new OutputStream(){

            public void write(int b){
                originalOutput.write(b);

                if (b=='\n')
                    messageCount++;
                
            }
        });

        System.setOut(countingStream);
    }

    public int getMessageCount(){
        System.setOut(originalOutput);
        int invocationCount = messageCount; 
        messageCount = 0;
        return invocationCount;
    }
}