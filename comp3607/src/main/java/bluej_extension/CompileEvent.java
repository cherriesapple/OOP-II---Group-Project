package bluej_extension;

import bluej.extensions2.event.ClassListener;
import bluej.extensions2.event.CompileListener;

public class CompileEvent implements TelemetryEvent, CompileListener{
    private String eventType;
    private long timestamp;
    private boolean status;
    private String message;
    private int warnings;

    //Need to be static???
    private int numFailedCompiles;
    private int numSuccessfulCompiles;

    public CompileEvent(String eventType, String message, long timestamp, boolean status){
        this.eventType = eventType;
        this.message = message;
        this.timestamp = timestamp;
        this.status = status;
        this.warnings = 0;
    }

    public String getEventType(){
        if (this.status)
            return "SUCCESSFUL_COMPILE";
        else 
            return "FAILED_COMPILE";
    }

    public long getTimeStamp(){return this.timestamp;}

    public void compileError (CompileEvent event){
       

    }

    public void compileFailed (CompileEvent event){
        event.numFailedCompiles++;
    }

    public void compileStarted (CompileEvent event){

    }

    public void compileSucceeded (CompileEvent event){
        event.numSuccessfulCompiles++;
    }

    public void compileWarning (CompileEvent event){
        event.warnings++;
    }


}
