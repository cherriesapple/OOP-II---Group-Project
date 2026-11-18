package bluej_extension;
import java.sql.Timestamp;

import bluej.extensions2.event.ClassEvent;
import bluej.extensions2.event.ClassListener;
import bluej_extension.SessionModel.ClassLifecycleEvent;

//Need to add code for a class being created

public class ClassTracker implements ClassListener {

    private int numDeletedClasses = 0;   
    private ClassLifecycleEvent myEvent;
    private final SessionModel session;
    
    public ClassTracker (SessionModel session){
        this.session = session;
    }

    private ClassLifecycleEvent createClassLifecycleEvent(String name, String event, Timestamp time){
        
        myEvent = new ClassLifecycleEvent();
        myEvent.className = name;
        myEvent.timestamp = time;
        myEvent.event = event;
        return myEvent;
    }
    
    @Override 
    public void classNameChanged(ClassEvent event){
        Timestamp time = new Timestamp(System.currentTimeMillis());

        String eventDescription = "NAME_CHANGED [New Name: " + event.getBClass().getName() + "]";
        ClassLifecycleEvent classEvent = createClassLifecycleEvent(event.getOldName(), eventDescription, time);
        session.classLifecycle.add(classEvent);  

        System.out.println( event.getOldName()+ " changed to " + event.getBClass().getName());

    }

    @Override
    public void classRemoved(ClassEvent event){
        Timestamp time = new Timestamp(System.currentTimeMillis());

        ClassLifecycleEvent classEvent = createClassLifecycleEvent(event.getBClass().getName(), "REMOVED", time);
        session.classLifecycle.add(classEvent);  

        this.numDeletedClasses++;

        System.out.println(event.getBClass().getName() + " was deleted");
    }

    @Override
    public void classStateChanged(ClassEvent event){
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String eventDescription = "MODIFIED";
        String className = event.getBClass().getName();

        if (event.isClassCompiled()){
             System.out.println( "An attempt to compile " + className + " was made");
             eventDescription+= "[Compilation Attempt]";
        }
          
        else if (event.getOldName() == null){
             System.out.println(className + " was created");
             eventDescription = "CLASS_CREATED";
        }
           
        else{
            System.out.println(className + " was modified | Waiting to compile");
        }
            

        ClassLifecycleEvent classEvent = createClassLifecycleEvent(event.getBClass().getName(), eventDescription, time);
        session.classLifecycle.add(classEvent); 

    }

    public int getNumDeletedClasses() {
        return numDeletedClasses;
    }
}
