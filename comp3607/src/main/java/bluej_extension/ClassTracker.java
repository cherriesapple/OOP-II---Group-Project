package bluej_extension;
import java.sql.Timestamp;
import java.util.ArrayList;

import bluej.extensions2.BClass;
import bluej.extensions2.event.ClassEvent;
import bluej.extensions2.event.ClassListener;
import bluej_extension.SessionModel.ClassLifecycleEvent;

public class ClassTracker implements ClassListener {

    private int numDeletedClasses = 0;   
    private ClassLifecycleEvent myEvent;
    private final SessionModel session;
    private final ArrayList<BClass> classes = new ArrayList<>();
    
    public ClassTracker (SessionModel session){
        this.session = session;
    }

    public String formatStateChangeEvent(boolean isCompiled, boolean isNewClass, String className) {
        String eventDescription = "MODIFIED";

        if (isCompiled)
            eventDescription += "[Compilation Attempt] " + "ClassName: " + className;
        
        if (isNewClass)
            eventDescription = "CLASS_CREATED";
        else
            eventDescription += " " + className + " was modified | Waiting to compile";
        
        return eventDescription;
    }

    public void storeTelemetryEvents(String name, String event, Timestamp time){
        
        myEvent = new ClassLifecycleEvent();
        myEvent.className = name;
        myEvent.timestamp = time;
        myEvent.event = event;
        session.classLifecycle.add(myEvent);  
    }

    public boolean isNewClass(ClassEvent event){
        
        if (classes.isEmpty()){
            try{
            for (BClass b : event.getPackage().getClasses()){
                classes.add(b);
                }
            }catch(Exception e){e.getMessage();}
        }

        if (!classes.contains(event.getBClass())){
            classes.add(event.getBClass());
            return true;
        }

        return false;
       
    }
    
    @Override 
    public void classNameChanged(ClassEvent event){

        Timestamp time = new Timestamp(System.currentTimeMillis());
        String eventDescription = "NAME_CHANGED [New Name: " + event.getBClass().getName() + "]\n[Old Name: " + event.getOldName() + "]";
        storeTelemetryEvents(event.getOldName(), eventDescription, time);
    }

    @Override
    public void classRemoved(ClassEvent event){
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String eventDescription = "[CLASS_REMOVED]" + event.getBClass().getName(); 

        this.numDeletedClasses++;
        storeTelemetryEvents(event.getBClass().getName(), eventDescription, time);
    }

    @Override
    public void classStateChanged(ClassEvent event){
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String className = event.getBClass().getName();
        String eventDescription = formatStateChangeEvent(event.isClassCompiled(), isNewClass(event), className);
        storeTelemetryEvents(className, eventDescription, time);

    }

    public int getNumDeletedClasses(){return this.numDeletedClasses;}
}
