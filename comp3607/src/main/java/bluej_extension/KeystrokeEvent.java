package bluej_extension;

public class KeystrokeEvent implements TelemetryEvent {
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
}
