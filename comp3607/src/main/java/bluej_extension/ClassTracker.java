package bluej_extension;
import bluej.extensions2.event.ClassEvent;
import bluej.extensions2.event.ClassListener;

public class ClassTracker implements ClassListener {

    private int numDeletedClasses = 0;

    @Override 
    public void classNameChanged(ClassEvent event){
        System.out.println(event.getOldName() + " changed to " + event.getBClass().getName());
    }

    @Override
    public void classRemoved(ClassEvent event){

        numDeletedClasses++;
        System.out.println(event.getBClass().getName() + " was deleted");
    }

    @Override
    public void classStateChanged(ClassEvent event){
        if (event.getEventType() == ClassEvent.EventType.STATE_CHANGED){
            System.out.print(event.getBClass().getName());
            if (event.isClassCompiled())
                System.out.println(" was successfully compiled");

            else if (event.hasError())
                System.out.println(" has errors. Compilation failed");

            else 
                System.out.println(" was modified | Waiting to compile");

        }
    }
    public int getNumDeletedClasses(){return this.numDeletedClasses;}
}
