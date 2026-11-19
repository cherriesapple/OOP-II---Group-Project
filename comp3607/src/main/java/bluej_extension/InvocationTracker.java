package bluej_extension;
import java.sql.Timestamp;

import bluej.extensions2.event.InvocationFinishedEvent;
import bluej.extensions2.event.InvocationFinishedListener;
import bluej_extension.SessionModel.TelemetryEvent;

/**
 * This class is used by the extension to listen to all the finished invocations executed within the bluej environment
 * 
 * @author Shurona Jackson
 */
public class InvocationTracker implements InvocationFinishedListener{

    private int numExecutions = 0;
    private TelemetryEvent myEvent;
    private final SessionModel session;

    /**
     * Creates an invocation finished tracker.
     * 
     * @param session is used to store telemetry data.
     */
    public InvocationTracker(SessionModel session){
        this.session = session;
    }

    /**
     * Adds the telemetry event to the session with its relevant data including its timestamp, type and additional information;
     * 
     */
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

    /**
     * Method provided by bluej API that checks if an invocation was completed.
     * 
     */
    @Override
    public void invocationFinished(InvocationFinishedEvent event){
        Timestamp time = new Timestamp(System.currentTimeMillis());
        storeTelemetryEvent(event.getEventType(), time);

    }

    /**
     * @return Returns the number of executions initatied within the bluej environment. 
     */
    public int getNumExecutions(){return this.numExecutions;}
}
