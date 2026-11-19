package bluej_extension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportDataAggregatorTest {

    private SessionModel session;

    @BeforeEach
    void setUp() {
        
        session = new SessionModel();
    }


    @Test
    void aggregatesSessionMetadata() {
        session.sessionData.projectName = "MyProject";
        session.userId = "816012345";
        session.sessionData.startTime = "2026-10-01T10:00:00Z";
        session.sessionData.endTime = "2026-10-01T11:00:00Z";

        ReportData data = ReportDataAggregator.aggregate(session);

        assertEquals("MyProject", data.projectName);
        assertEquals("student123", data.userId);
        assertEquals("2026-10-01T10:00:00Z", data.startTime);
        assertEquals("2026-10-01T11:00:00Z", data.endTime);
    }


    @Test
    void roundsMinsAndCalculatesActive() {
        
        session.sessionLengthMinutes = 10.4;
        session.idleMinutes = 3.6;

        ReportData data = ReportDataAggregator.aggregate(session);

        assertEquals(10, data.totalSessionMinutes);
        assertEquals(4, data.idleMinutes);
        assertEquals(6, data.activeMinutes); // 10 - 4
    }

    @Test
    void activeMinsNeverNegative() {
       
        session.sessionLengthMinutes = 5.0;
        session.idleMinutes = 10.0;

        ReportData data = ReportDataAggregator.aggregate(session);

        assertEquals(5, data.totalSessionMinutes);
        assertEquals(10, data.idleMinutes);
        assertEquals(0, data.activeMinutes); 
    }


    @Test
    void copiesCounters() {
        session.totalCompilations = 15;
        session.totalErrors = 3;
        session.sessionData.totalRuns = 8;
        session.sessionData.successfulCompilations = 12;
        session.sessionData.failedCompilations = 3;

        ReportData data = ReportDataAggregator.aggregate(session);

        assertEquals(15, data.totalCompilations);
        assertEquals(3, data.totalErrors);
        assertEquals(8, data.totalRuns);
        assertEquals(12, data.successfulComp);
        assertEquals(3, data.failedComp);
    }


    @Test
    void activityBucketsToLinkedHashMap() {
        SessionModel.Bucket b1 = new SessionModel.Bucket();
        b1.bucketStart = "2026-10-01T10:00:00Z";
        b1.editSignals = 5;

        SessionModel.Bucket b2 = new SessionModel.Bucket();
        b2.bucketStart = "2026-10-01T10:03:00Z";
        b2.editSignals = 12;

        session.derived.activityTimeline.buckets.add(b1);
        session.derived.activityTimeline.buckets.add(b2);

        ReportData data = ReportDataAggregator.aggregate(session);

        assertEquals(2, data.activityMap.size());
        
        var keys = new ArrayList<>(data.activityMap.keySet());
        assertEquals("2026-10-01T10:00:00Z", keys.get(0));
        assertEquals("2026-10-01T10:03:00Z", keys.get(1));
        assertEquals(5, data.activityMap.get("2026-10-01T10:00:00Z"));
        assertEquals(12, data.activityMap.get("2026-10-01T10:03:00Z"));
    }

    @Test
    void handleEmptyOrNullActivityBuckets() {
        
        session.derived.activityTimeline = null;
        ReportData data1 = ReportDataAggregator.aggregate(session);
        assertNotNull(data1.activityMap);
        assertTrue(data1.activityMap.isEmpty());

        // Test empty buckets list
        session.derived.activityTimeline = new SessionModel.ActivityTimeline();
        session.derived.activityTimeline.buckets = new ArrayList<>();
        ReportData data2 = ReportDataAggregator.aggregate(session);
        assertNotNull(data2.activityMap);
        assertTrue(data2.activityMap.isEmpty());
    }

    
    @Test
    void copiesErrorFreq() {
        session.derived.errorCategoryTotals.put("syntax", 5);
        session.derived.errorCategoryTotals.put("type_mismatch", 2);

        ReportData data = ReportDataAggregator.aggregate(session);

        assertEquals(2, data.errorFrequencies.size());
        assertEquals(5, data.errorFrequencies.get("syntax"));
        assertEquals(2, data.errorFrequencies.get("type_mismatch"));
    }


    @Test
    void flattenClassLifecycleIntoTimeline() {
        SessionModel.ClassLifecycleEvent event1 = new SessionModel.ClassLifecycleEvent();
        event1.className = "Main";
        event1.event = "CREATED";
        event1.timestamp = java.sql.Timestamp.valueOf("2026-10-01 10:05:00");

        SessionModel.ClassLifecycleEvent event2 = new SessionModel.ClassLifecycleEvent();
        event2.className = "Helper";
        event2.event = "MODIFIED";
        
        
        session.classLifecycle.add(event1);
        session.classLifecycle.add(event2);

        ReportData data = ReportDataAggregator.aggregate(session);

        assertEquals(2, data.projectTimeline.size());
        
        ReportData.TimelineEvent te1 = data.projectTimeline.get(0);
        assertEquals("Main", te1.className);
        assertEquals("CREATED", te1.event);
        assertTrue(te1.timeStamp.contains("2026-10-01")); 
        ReportData.TimelineEvent te2 = data.projectTimeline.get(1);
        assertEquals("Helper", te2.className);
        assertEquals("MODIFIED", te2.event);
        assertEquals("null", te2.timeStamp); 
    }

    @Test
    void handlesNullClassLifecycle() {
        session.classLifecycle = null;
        
        ReportData data = ReportDataAggregator.aggregate(session);
        
        assertNotNull(data.projectTimeline);
        assertTrue(data.projectTimeline.isEmpty());
    }
}