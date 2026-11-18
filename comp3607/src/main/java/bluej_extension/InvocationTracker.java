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

    public void storeTelemetryEvent(InvocationFinishedEvent.EventType eventType, Timestamp time){
        myEvent = new TelemetryEvent();
        myEvent.timestamp = time; 
        myEvent.type = "EXECUTION_INITIATED";

        numExecutions++;

        if (eventType == InvocationFinishedEvent.EventType.UNKNOWN_EXIT){
            myEvent.data.put("STATUS:", " Unknown Exit");
        }

        else if (eventType == InvocationFinishedEvent.EventType.NORMAL_EXIT){
            myEvent.data.put("STATUS:", " Finished Normally");
        }

        else if (eventType == InvocationFinishedEvent.EventType.EXCEPTION_EXIT){
            myEvent.data.put("STATUS:", "Terminated due to an Exception");
        }

        else if (eventType == InvocationFinishedEvent.EventType.TERMINATED_EXIT){
            myEvent.data.put("STATUS:", "Terminated by User");
        }

        session.events.add(myEvent);

    }

    @Override
    public void invocationFinished(InvocationFinishedEvent event){
        Timestamp time = new Timestamp(System.currentTimeMillis());
        storeTelemetryEvent(event.getEventType(), time);

    }

    public int getNumExecutions(){return this.numExecutions;}
}
