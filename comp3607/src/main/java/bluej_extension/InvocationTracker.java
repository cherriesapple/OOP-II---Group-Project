package bluej_extension;
import java.sql.Timestamp;

import bluej.extensions2.event.InvocationFinishedEvent;
import bluej.extensions2.event.InvocationFinishedListener;
import bluej_extension.SessionModel.TelemetryEvent;

public class InvocationTracker implements InvocationFinishedListener{

    private int numExecutions = 0;
    private TelemetryEvent myEvent;
    private final SessionModel session;

    public InvocationTracker(SessionModel session){
        this.session = session;
    }

    @Override
    public void invocationFinished(InvocationFinishedEvent event){
        myEvent = new TelemetryEvent();
        myEvent.timestamp = new Timestamp(System.currentTimeMillis());
        myEvent.type = "EXECUTION_INITIATED";
        
        numExecutions++;

        System.out.println("Program execution finished | Status: ");
        
        if (event.getEventType() == InvocationFinishedEvent.EventType.UNKNOWN_EXIT){
            myEvent.data.put("STATUS:", " Unknown Exit");
            System.out.println("Unknown Termination Cause");
        }
           

        if (event.getEventType() == InvocationFinishedEvent.EventType.NORMAL_EXIT){
            myEvent.data.put("STATUS:", " Finished Normally");
            System.out.println("Finished Normally");
        }
            

        if (event.getEventType() == InvocationFinishedEvent.EventType.EXCEPTION_EXIT){
            myEvent.data.put("STATUS:", "Terminated due to Exception");
            System.out.println("Terminated due to an Exception");
        }
            

        if (event.getEventType() == InvocationFinishedEvent.EventType.TERMINATED_EXIT){
            myEvent.data.put("STATUS:", "Terminated by User");
            System.out.println("Terminated by User");
        }

        session.events.add(myEvent);
           
    }

    public int getNumExecutions(){return this.numExecutions;}
}
