package bluej_extension;
import java.sql.Timestamp;

import java.awt.*;
import java.awt.event.*;

public class KeystrokeEvent implements TelemetryEvent{
    private int numCharacters;
    private Timestamp timestamp;

    public KeystrokeEvent (){
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }
    
    @Override
    public String getEventType(){
        return "KEYSTROKE";
    }

    public Timestamp getTimeStamp(){
        return this.timestamp;
    }
}
