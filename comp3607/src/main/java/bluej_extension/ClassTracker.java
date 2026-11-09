package bluej_extension;
import java.sql.Timestamp;

import bluej.extensions2.event.ClassEvent;
import bluej.extensions2.event.ClassListener;

//Need to add code for a class being created

public class ClassTracker implements ClassListener {

    private int numDeletedClasses = 0;
    private Timestamp classEventTime;       
   
    @Override 
    public void classNameChanged(ClassEvent event){

        System.out.println(event.getOldName() + " changed to " + event.getBClass().getName());
    }

    @Override
    public void classRemoved(ClassEvent event){
        numDeletedClasses++;
        this.classEventTime = new Timestamp(System.currentTimeMillis());
        System.out.println(event.getBClass().getName() + " was deleted");
    }

    @Override
    //This does not cater for a new classEvent being created
    public void classStateChanged(ClassEvent event){

        String className = event.getBClass().getName();

        if (event.isClassCompiled())
           System.out.println( "An attempt to compile " + className + " was made");
        else if (event.getOldName() == null)
            System.out.println(className + " was created");
        else
            System.out.println(className + " was modified | Waiting to compile");

    }

    public int getNumDeletedClasses(){return this.numDeletedClasses;}
    public Timestamp classEventTime(){return this.classEventTime;}
}
