package bluej_extension;

public class ClassEvent implements TelemetryEvent{
    private String eventType;
    private String className;
    private long timestamp;

    public ClassEvent(String eventType, String className, long timestamp){
        this.eventType = eventType;
        this.className = className;
        this.timestamp = timestamp;
    }

    @Override 
    public String getEventType(){return this.eventType;}

    public String getClassName(){return this.className;}

    @Override
    public long getTimeStamp(){return this.timestamp;}


}
