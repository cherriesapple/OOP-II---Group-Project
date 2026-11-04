package bluej_extension;
import java.sql.Timestamp;

import bluej.extensions2.event.ClassListener;
import bluej.extensions2.event.CompileListener;

public class CompileEvent implements TelemetryEvent{
    private String eventType;
    private Timestamp timestamp;
    private boolean status;
    private String message;
    private int warnings;

    public CompileEvent(String eventType, String message,boolean status){
        this.eventType = eventType;
        this.message = message;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.status = status;
        this.warnings = 0;
    }

    public String getEventType(){
        if (this.status)
            return "SUCCESSFUL_COMPILE";
        else 
            return "FAILED_COMPILE";
    }

    public Timestamp getTimeStamp(){return this.timestamp;}



}
