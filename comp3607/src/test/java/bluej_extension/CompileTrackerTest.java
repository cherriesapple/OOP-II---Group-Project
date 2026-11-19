package bluej_extension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class CompileTrackerTest {

    @Test
    void successfulCompileCounters() {
        SessionModel session = new SessionModel();
        CompileTracker compTracker = new CompileTracker(session);
        compTracker.compileStarted(null);
        compTracker.compileSucceeded(null);
        compTracker.populateSession();

        assertEquals(1, session.totalCompilations);
        assertEquals(1, session.sessionData.successfulCompilations);
        assertEquals(1.0, session.derived.compileSuccessRate, 0.001);
    }

    @Test
    void failedCompileCounters() {
        SessionModel session = new SessionModel();
        CompileTracker compTracker = new CompileTracker(session);
        compTracker.compileStarted(null);
        compTracker.compileFailed(null);
        compTracker.populateSession();

        assertEquals(1, session.sessionData.failedCompilations);
        assertEquals(0.0, session.derived.compileSuccessRate, 0.001);
    }

    @Test
    void startedEventRecorded() {
        SessionModel session = new SessionModel();
        CompileTracker compTracker = new CompileTracker(session);
        compTracker.compileStarted(null);

        assertEquals(1, session.events.size());
        assertEquals("STARTED", session.events.get(0).data.get("status"));
    }

    @Test
    void populateSessionIsIdempotent() {
        SessionModel session = new SessionModel();
        CompileTracker compTracker = new CompileTracker(session);
        compTracker.compileStarted(null);
        compTracker.compileSucceeded(null);
        compTracker.populateSession();
        compTracker.populateSession();
        assertEquals(1, session.totalCompilations);
    }

}
