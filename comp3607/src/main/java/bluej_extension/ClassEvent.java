package bluej_extension;

import bluej.extensions2.event.ClassListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class ClassEvent implements TelemetryEvent, ClassListener {
    private String eventType;
    private String className;
    private long timestamp;
    private int numStateChanges;

    //I'm not sure if i want this to be static, but I want to keep track of the number of changes made to each class
    private static Map<String, Integer> classes = new TreeMap<>(); 


    private ArrayList<ClassEvent> deletedClasses;

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

    public int getStateChanges(){return this.numStateChanges;}

    
    public void classNameChanged(ClassEvent event){
        this.className = event.className;
    }

    public void classRemoved(ClassEvent event){
        deletedClasses.add(event);
    }

    public void classStateChanged (ClassEvent event){
        

    }


}
