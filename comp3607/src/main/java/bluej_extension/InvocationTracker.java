package bluej_extension;

import bluej.extensions2.event.InvocationFinishedEvent;
import bluej.extensions2.event.InvocationFinishedListener;

public class InvocationTracker implements InvocationFinishedListener{

    private int numExecutions = 0;

    @Override
    public void invocationFinished(InvocationFinishedEvent event){
        numExecutions++;

        System.out.println("Program execution finished | Status: ");
        
        if (event.getEventType() == InvocationFinishedEvent.EventType.UNKNOWN_EXIT)
            System.out.println("Unknown Termination Cause");

        if (event.getEventType() == InvocationFinishedEvent.EventType.NORMAL_EXIT)
            System.out.println("Finished Normally");

        if (event.getEventType() == InvocationFinishedEvent.EventType.EXCEPTION_EXIT)
            System.out.println("Terminated due to an Exception");

        if (event.getEventType() == InvocationFinishedEvent.EventType.TERMINATED_EXIT)
            System.out.println("Terminated by User");

    }

    public int getNumExecutions(){return this.numExecutions;}
}
