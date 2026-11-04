package bluej_extension;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class ClassEvent implements TelemetryEvent{
    private String eventType;
    private String className;
    Timestamp timestamp; 
    private int numStateChanges;

    //I'm not sure if i want this to be static, but I want to keep track of the number of changes made to each class
    private Map<String, Integer> classes = new TreeMap<>(); 


    public ClassEvent(String eventType, String className){
        this.eventType = eventType;
        this.className = className;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    @Override 
    public String getEventType(){return this.eventType;}

    public String getClassName(){return this.className;}

    @Override
    public Timestamp getTimeStamp(){return this.timestamp;}

    public int getStateChanges(){return this.numStateChanges;}


}
