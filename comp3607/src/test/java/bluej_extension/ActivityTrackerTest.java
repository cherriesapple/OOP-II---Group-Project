package bluej_extension;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import bluej.extensions2.editor.JavaEditor;
import bluej.extensions2.editor.TextLocation;

class ActivityTrackerTest {
    private SessionModel session;
    private ActivityTracker activityTracker;
    private JavaEditor editor;

    @BeforeEach
    void setUp() {
        session = new SessionModel();
        activityTracker = new ActivityTracker(session);
        editor = mock(JavaEditor.class);
        when(editor.getTextLength()).thenReturn(0);
        when(editor.getCaretLocation()).thenReturn(new TextLocation(0, 0));
        activityTracker.beginTracker(editor);
    }

    private long min(double m)  { return TimeUnit.MINUTES.toNanos((long) (m * 60)); }
    private long sec(double s)  { return TimeUnit.SECONDS.toNanos((long) s); }


    @Test
    void stopWithoutEditsCountsAllIdle() {
        activityTracker.stopTracker();

        assertEquals(session.sessionLengthMinutes, session.idleMinutes, 0.01);
        assertEquals("no", session.active);
        assertEquals(0, session.derived.activityTimeline.totalEditSignals);
    }

     @Test
    void leadingGapBelowThresholdIsActive() {
        activityTracker.editLog(sec(5));      
        activityTracker.stopTracker();

        assertEquals(0.0, session.idleMinutes, 0.01);
        assertEquals("yes", session.active);
    }

    @Test
    void leadingGapOverThresholdIsIdle() {
        activityTracker.editLog(min(5));    
        activityTracker.stopTracker();

        assertTrue(session.idleMinutes >= 4.99);
        assertTrue(session.idleMinutes <= 5.01);
    }

     @Test
    void interEditGapBelowThresholdIsActive() {
        activityTracker.editLog(sec(1));      
        activityTracker.editLog(sec(100));    
        activityTracker.stopTracker();

        assertEquals(0.0, session.idleMinutes, 0.01);
    }

    @Test
    void interEditGapOverThresholdIsIdle() {
        activityTracker.editLog(sec(1));      
        activityTracker.editLog(sec(301));    
        activityTracker.stopTracker();

        assertEquals(5.0, session.idleMinutes, 0.01);
    }

     @Test
    void editsLandInCorrectBuckets() {
        activityTracker.editLog(min(1));      
        activityTracker.editLog(min(2));      
        activityTracker.editLog(min(4));   
        activityTracker.stopTracker();

        var timeline = session.derived.activityTimeline;
        assertEquals(3, timeline.totalEditSignals);
        assertEquals(2, timeline.buckets.size());
        assertEquals(2, timeline.buckets.get(0).editSignals);
        assertEquals(1, timeline.buckets.get(1).editSignals);
        assertEquals(3, timeline.bucketMinutes);
    }

    @Test
    void activeSecondsAccumulateInBucket() {
        activityTracker.editLog(sec(10));
        activityTracker.editLog(sec(20));    // 10s active in bucket 0
        activityTracker.stopTracker();

        var b0 = session.derived.activityTimeline.buckets.get(0);
        assertTrue(b0.activeSeconds >= 9 && b0.activeSeconds <= 10,"expected ~10s active, got " + b0.activeSeconds);
    }

    @Test
    void idleGapDoesNotCountAsActiveSeconds() {
        activityTracker.editLog(sec(1));
        activityTracker.editLog(sec(301));   // 300s gap → idle, not active
        activityTracker.stopTracker();

        var totalActive = session.derived.activityTimeline.buckets.stream().mapToLong(b -> b.activeSeconds).sum();
        assertTrue(totalActive < 5, "300s idle gap should not count as active, got " + totalActive);
    }

     @Test
    void stopTrackerWritesAllSessionFields() {
        activityTracker.editLog(min(5));       // 5min leading idle
        activityTracker.editLog(min(5.5));     // active, closes gap < 180s
        activityTracker.stopTracker();

        assertTrue(session.idleMinutes >= 4.99);
        assertTrue(session.sessionLengthMinutes > 0);
        assertEquals("yes", session.active);
        assertNotNull(session.derived.activityTimeline.buckets);
        assertNotNull(session.derived.activityTimeline.bucketMinutes);
    }


     @Test
    void editorThrowingDuringPollStopsTracker() throws Exception {
        
        JavaEditor broken = mock(JavaEditor.class);
        when(broken.getTextLength())
            .thenReturn(0)
            .thenThrow(new RuntimeException("editor closed"));
        when(broken.getCaretLocation()).thenReturn(new TextLocation(0, 0));

        SessionModel s2 = new SessionModel();
        ActivityTracker t2 = new ActivityTracker(s2);
        t2.beginTracker(broken);


        var method = ActivityTracker.class.getDeclaredMethod("checkActivty");
        method.setAccessible(true);
        method.invoke(t2); 

        t2.stopTracker();

        assertEquals(0, s2.derived.activityTimeline.totalEditSignals);
    }
}
