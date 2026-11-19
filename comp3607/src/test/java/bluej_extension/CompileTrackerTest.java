package bluej_extension;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import bluej.extensions2.event.CompileEvent;

public class CompileTrackerTest {
    private SessionModel session;
    private CompileTracker compTracker;
    private CompileEvent event;

    @BeforeEach 
    void setUp(){
        session = new SessionModel();
        compTracker = new CompileTracker(session);
        event = mock(CompileEvent.class);
        when(event.getFiles()).thenReturn(new File[]{ new File("A.java") });
    }

    @Test 
    void successfulCompUpdatesCount(){
        compTracker.compileStarted(event);
        compTracker.compileSucceeded(event);

        assertEquals(1, session.totalCompilations);
        assertEquals(1, session.sessionData.successfulCompilations);
        assertEquals(0,session.totalErrors);
        assertEquals(0,session.sessionData.failedCompilations);
        assertEquals(1.0, session.derived.compileSuccessRate, 0.001);  
    }

    @Test 
    void successfulCompCreatesTelEvent(){
        compTracker.compileStarted(event);
        compTracker.compileSucceeded(event);

        assertEquals(2, session.events.size());
        assertEquals("COMPILE", session.events.get(0).type);
        assertEquals("STARTED", session.events.get(0).data.get("status"));
        assertEquals("SUCCESSFUL", session.events.get(1).data.get("status"));
        assertNotNull(session.events.get(0).timestamp);
    }

    @Test
    void compEventContainsDurationAndFileNums(){
        compTracker.compileStarted(event);
        compTracker.compileSucceeded(event);

        var data = session.events.get(1).data;
        assertTrue(data.containsKey("duration_ms"));
        assertTrue(data.containsKey("file_count"));
        assertEquals(1, data.get("file_count"));
    }

    @Test 
    void failedCompWithErrorsUpdatesCount(){
        CompileEvent error = mock(CompileEvent.class);
        when(error.getErrorLineNumber()).thenReturn(7);
        when(error.getErrorMessage()).thenReturn("cannot find symbol: x");
        when(error.getErrorPosition()).thenReturn(new int[]{3, 8});

        compTracker.compileStarted(event);
        compTracker.compileError(error);
        compTracker.compileError(error);
        compTracker.compileFailed(event);
        compTracker.populateSession();

        assertEquals(1, session.totalCompilations);
        assertEquals(0, session.sessionData.successfulCompilations);
        assertEquals(2,session.totalErrors);
        assertEquals(1,session.sessionData.failedCompilations);
        assertEquals(0.0, session.derived.compileSuccessRate, 0.001);  
    }

    @Test 
    void failedCompPopulatesErrorBreakdown(){
        CompileEvent error = mock(CompileEvent.class);
        when(error.getErrorLineNumber()).thenReturn(7);
        when(error.getErrorMessage()).thenReturn("cannot find symbol: x");

        compTracker.compileStarted(event);
        compTracker.compileError(error);
        compTracker.compileFailed(event);
        compTracker.populateSession();

        assertEquals(1, session.derived.errorBreakdown.size());
        var entry = session.derived.errorBreakdown.get(0);
        assertEquals(7, entry.lineNumber);
        assertEquals("cannot find symbol: x", entry.message);
        assertEquals("missing_symbol", entry.category);
        assertNotNull(entry.timestamp);
        assertEquals(1, session.derived.errorCategoryTotals.get("missing_symbol"));
    }

     @Test
    void warningsDoNotCountAsErrors() {
        CompileEvent warn = mock(CompileEvent.class);
        when(warn.getErrorMessage()).thenReturn("unused variable");

        compTracker.compileStarted(event);
        compTracker.compileWarning(warn);
        compTracker.compileSucceeded(event);
        compTracker.populateSession();

        assertEquals(0, session.totalErrors);
        assertEquals(1, session.sessionData.successfulCompilations);
    }

    @Test
    void twoCompilesProduceOneInterval() throws Exception {
        compTracker.compileStarted(event);
        Thread.sleep(20);
        compTracker.compileSucceeded(event);
        compTracker.compileStarted(event);
        compTracker.compileSucceeded(event);
        compTracker.populateSession();

        assertEquals(1, session.derived.compileIntervalsMs.size());
        assertTrue(session.derived.compileIntervalsMs.get(0) >= 20);
        assertEquals(session.derived.compileIntervalsMs.get(0),session.derived.meanCompileIntervalMs);
    }

    @Test
    void populateSessionCanBeCalledMultipleTimes() {
        compTracker.compileStarted(event);
        compTracker.compileSucceeded(event);

        compTracker.populateSession();
        int firstBreakdownSize = session.derived.errorBreakdown.size();
        int firstTotal = session.totalCompilations;

        compTracker.populateSession();
        assertEquals(firstBreakdownSize, session.derived.errorBreakdown.size());
        assertEquals(firstTotal, session.totalCompilations);
    }

    @Test
    void mixedCompilesAccumulateCorrectly() {
        compTracker.compileStarted(event);
        compTracker.compileSucceeded(event);

        CompileEvent error = mock(CompileEvent.class);
        when(error.getErrorMessage()).thenReturn("incompatible types");
        compTracker.compileStarted(event);
        compTracker.compileError(error);
        compTracker.compileError(error);
        compTracker.compileFailed(event);

        compTracker.compileStarted(event);
        compTracker.compileSucceeded(event);

        compTracker.populateSession();

        assertEquals(3, session.totalCompilations);
        assertEquals(2, session.sessionData.successfulCompilations);
        assertEquals(1, session.sessionData.failedCompilations);
        assertEquals(2, session.totalErrors);
        assertEquals(2.0 / 3.0, session.derived.compileSuccessRate, 0.001);
    }

}
