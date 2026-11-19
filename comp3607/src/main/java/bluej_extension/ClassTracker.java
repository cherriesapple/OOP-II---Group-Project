package bluej_extension;
import java.sql.Timestamp;
import java.util.ArrayList;

import bluej.extensions2.BClass;
import bluej.extensions2.event.ClassEvent;
import bluej.extensions2.event.ClassListener;
import bluej_extension.SessionModel.ClassLifecycleEvent;

/**
 * This class is used by the extension to listen to all the class level changes made within the bluej environment.
 * 
 * Utlises the observer design pattern as it implements the Classlistener interface provided by the bluejAPI.
 * 
 * @author Shurona Jackson
 */
public class ClassTracker implements ClassListener {

    private int numDeletedClasses = 0;   
    private String eventDescription; 
    private ClassLifecycleEvent myEvent;
    private final SessionModel session;
    private final ArrayList<BClass> classes = new ArrayList<>();
    
    /**
     * Creates a class tracker.
     * 
     * @param session Session is used to store telemetry data.
     */
    public ClassTracker (SessionModel session){
        this.session = session;
    }

    /**
     * Formats the state change event description based on what change within the class. 
     * 
     * Whether it was compiled, had a name change or was newly created.
     * @return An event description string that will be stored in the session class. 
     */
    public String formatStateChangeEvent(boolean isCompiled, boolean isNewClass, String className) {
        eventDescription = "MODIFIED";

        if (isCompiled)
            eventDescription += "[Compilation Attempt] " + "ClassName: " + className;
        
        if (isNewClass)
            eventDescription = "CLASS_CREATED";
        else
            eventDescription += " " + className + " was modified | Waiting to compile";
        
        return eventDescription;
    }

    /**
     * Populates the telemetry event (class lifecycle event) data then adds it to the session model. 
     */
    public void storeTelemetryEvents(String name, String event, Timestamp time){
        
        myEvent = new ClassLifecycleEvent();
        myEvent.className = name;
        myEvent.timestamp = time;
        myEvent.event = event;
        session.classLifecycle.add(myEvent);  
    }

    /**
     * Checks to see if a new class was created
     * 
     * @return true if class name does not exist in the array list of package classes. Class is newly created
     */
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
    
    /**
     * Method provided by bluej API that checks to see whether the class name is changed. 
     * 
     * @param event This is an event that is triggered when a user interactions with a class in the bluej environment. 
     */
    @Override 
    public void classNameChanged(ClassEvent event){

        Timestamp time = new Timestamp(System.currentTimeMillis());
        eventDescription = "NAME_CHANGED [New Name: " + event.getBClass().getName() + "]\n[Old Name: " + event.getOldName() + "]";
        storeTelemetryEvents(event.getOldName(), eventDescription, time);
    }

    /**
     * Method provided by bluej that checks whether a class is removed. 
     * 
     * @param event This is an event that is triggered when a user interactions with a class in the bluej environment. 
     */
    @Override
    public void classRemoved(ClassEvent event){
        Timestamp time = new Timestamp(System.currentTimeMillis());
        eventDescription = "[CLASS_REMOVED]" + event.getBClass().getName(); 

        this.numDeletedClasses++;
        storeTelemetryEvents(event.getBClass().getName(), eventDescription, time);
    }

    /**
     * Method provided by bluej that checks whether a class' state is changed. 
     * 
     * @param event This is an event that is triggered when a user interactions with a class in the bluej environment. 
     */

    @Override
    public void classStateChanged(ClassEvent event){
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String className = event.getBClass().getName();
        eventDescription = formatStateChangeEvent(event.isClassCompiled(), isNewClass(event), className);
        storeTelemetryEvents(className, eventDescription, time);

    }

    /**
     * This method can be called by other classes the require the number of classes deleted. 
     * 
     * @return Returns the number of deleted classes
     */
    public int getNumDeletedClasses(){return this.numDeletedClasses;}
}
