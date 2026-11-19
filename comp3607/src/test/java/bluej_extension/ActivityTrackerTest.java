package bluej_extension;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class ActivityTrackerTest {
    
    @Test
    void noEditsIsAllIdle() {
        SessionModel session = new SessionModel();
        ActivityTracker activityTracker = new ActivityTracker(session);
        
        activityTracker.beginTracker(null);          
        activityTracker.stopTracker();

        assertEquals(session.sessionLengthMinutes, session.idleMinutes, 0.01);
        assertEquals("no", session.active);
    }

    @Test
    void smallGapIsActive() {
        SessionModel session = new SessionModel();
        ActivityTracker activityTracker = new ActivityTracker(session);
        activityTracker.beginTracker(null);
        long base = activityTracker.getStartTimeNs();
        
        activityTracker.editLog(base + TimeUnit.SECONDS.toNanos(5));
        
        assertEquals(TimeUnit.SECONDS.toNanos(5), activityTracker.getIdleNs());
    }

  @Test
    void bigGapIsIdle() {
        SessionModel session = new SessionModel();
        ActivityTracker activityTracker = new ActivityTracker(session);
        activityTracker.beginTracker(null);
        long base = activityTracker.getStartTimeNs();
        
        activityTracker.editLog(base + TimeUnit.SECONDS.toNanos(1));  
        activityTracker.editLog(base + TimeUnit.SECONDS.toNanos(301));
        
        assertEquals(TimeUnit.SECONDS.toNanos(301), activityTracker.getIdleNs());
    }

   @Test
    void editsLandInBuckets() {
        SessionModel session = new SessionModel();
        ActivityTracker activityTracker = new ActivityTracker(session);
        activityTracker.beginTracker(null);
        long base = activityTracker.getStartTimeNs();
        
        activityTracker.editLog(base + TimeUnit.MINUTES.toNanos(1));

        activityTracker.editLog(base + TimeUnit.MINUTES.toNanos(2));

        activityTracker.editLog(base + TimeUnit.MINUTES.toNanos(4));
        
        activityTracker.stopTracker();

        var b = session.derived.activityTimeline.buckets;
        assertEquals(3, session.derived.activityTimeline.totalEditSignals);
        assertEquals(2, b.get(0).editSignals); // 1m and 2m are in bucket 0 (0-3m)
        assertEquals(1, b.get(1).editSignals); // 4m is in bucket 1 (3m-6m)
    }

    @Test
    void idleGapNotActiveSeconds() {
        SessionModel session = new SessionModel();
        ActivityTracker activityTracker = new ActivityTracker(session);
        activityTracker.beginTracker(null);
        long base = activityTracker.getStartTimeNs();
        
        activityTracker.editLog(base + TimeUnit.SECONDS.toNanos(1));
        activityTracker.editLog(base + TimeUnit.SECONDS.toNanos(301));

        long totalActive = session.derived.activityTimeline.buckets.stream().mapToLong(b -> b.activeSeconds).sum();
        assertEquals(0, totalActive); 
    }
}
