package bluej_extension;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bluej.extensions2.event.InvocationFinishedEvent;

public class InvocationTrackerTest {
    
    private SessionModel session;
    private InvocationTracker invocationTracker;


    @BeforeEach 
    void setUp(){
        session = new SessionModel();
        invocationTracker = new InvocationTracker(session);
    }

    @Test
    void checkInstantiations(){
        assertNotNull(session);
        assertNotNull(invocationTracker);
    }

    @Test 
    void testUnknownExit(){
        Timestamp time = new Timestamp(System.currentTimeMillis());
        invocationTracker.storeTelemetryEvent(InvocationFinishedEvent.EventType.UNKNOWN_EXIT, time);

        assertEquals(1, session.events.size());
        assertEquals(1, invocationTracker.getNumExecutions());
        assertEquals(" Unknown Exit", session.events.get(0).data.get("STATUS:"));
    }

    @Test 
    void testNormalExit(){
        Timestamp time = new Timestamp(System.currentTimeMillis());
        invocationTracker.storeTelemetryEvent(InvocationFinishedEvent.EventType.NORMAL_EXIT, time);

        assertEquals(1, session.events.size());
        assertEquals(1, invocationTracker.getNumExecutions());
        assertEquals(" Finished Normally", session.events.get(0).data.get("STATUS:"));
    }

    @Test
    void testExceptionExit(){
        Timestamp time = new Timestamp(System.currentTimeMillis());
        invocationTracker.storeTelemetryEvent(InvocationFinishedEvent.EventType.EXCEPTION_EXIT, time);

        assertEquals(1, session.events.size());
        assertEquals(1, invocationTracker.getNumExecutions());
        assertEquals("Terminated due to an Exception", session.events.get(0).data.get("STATUS:"));
    }

    @Test 
    void testTerminatedExit(){
        Timestamp time = new Timestamp(System.currentTimeMillis());
        invocationTracker.storeTelemetryEvent(InvocationFinishedEvent.EventType.TERMINATED_EXIT, time);

        assertEquals(1, session.events.size());
        assertEquals(1, invocationTracker.getNumExecutions());
        assertEquals("Terminated by User", session.events.get(0).data.get("STATUS:"));
    }



}
