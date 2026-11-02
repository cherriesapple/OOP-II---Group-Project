package bluej_extension;

import java.awt.*;
import java.awt.event.*;

public class KeystrokeEvent implements TelemetryEvent, KeyListener {
    private int numCharacters;
    private long timestamp;

    public KeystrokeEvent (long timestamp){
        this.timestamp = timestamp;
    }
    
    @Override
    public String getEventType(){
        return "KEYSTROKE";
    }

    public long getTimeStamp(){
        return this.timestamp;
    }

    //Needed for interface
    public void keyPressed(KeystrokeEvent event){

    }

    public void keyReleased (KeystrokeEvent event){

    }

    public void keyTyped (KeystrokeEvent event){

    }
}
