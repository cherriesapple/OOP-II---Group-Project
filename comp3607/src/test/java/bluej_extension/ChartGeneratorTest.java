package bluej_extension;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap; 

import static org.junit.jupiter.api.Assertions.*;

class ChartGeneratorTest {

    private SessionModel session;

    @BeforeEach
    void setUp() {
        session = new SessionModel();
    }

    @Test
    void testGenerateErrorChart() throws IOException {
        byte[] chartBytes = ChartGenerator.generateErrorChart(session);
        
        assertNotNull(chartBytes);
        assertTrue(chartBytes.length > 0);
    }

    @Test
    void testGenerateCompileOutcomeChart() throws IOException {

        session.sessionData.successfulCompilations = 5;
        session.sessionData.failedCompilations = 2;

        byte[] chartBytes = ChartGenerator.generateCompileOutcomeChart(session);
        
        assertNotNull(chartBytes);
        assertTrue(chartBytes.length > 0);
    }

    @Test
    void testGenerateErrorFrequencyChart_WithData() throws IOException {
       
        session.derived.errorCategoryTotals = new HashMap<>();
        session.derived.errorCategoryTotals.put("Syntax Error", 3);
        session.derived.errorCategoryTotals.put("Type Mismatch", 1);

        byte[] chartBytes = ChartGenerator.generateErrorFrequencyChart(session);
        
        assertNotNull(chartBytes);
        assertTrue(chartBytes.length > 0);
    }

    @Test
    void testGenerateErrorFrequencyChart_EmptyData() throws IOException {
        
        session.derived.errorCategoryTotals = new HashMap<>();

        byte[] chartBytes = ChartGenerator.generateErrorFrequencyChart(session);
        
        assertNotNull(chartBytes);
        assertTrue(chartBytes.length > 0);
    }

    @Test
    void testGenerateActivityChart_WithData() throws IOException {
   
        session.derived.activityTimeline = new SessionModel.ActivityTimeline();
        session.derived.activityTimeline.bucketMinutes = 5;
        session.derived.activityTimeline.buckets = new java.util.ArrayList<>();
        
        SessionModel.Bucket bucket = new SessionModel.Bucket();
        bucket.bucketIndex = 1;
        bucket.editSignals = 10;
        
        session.derived.activityTimeline.buckets.add(bucket);

        byte[] chartBytes = ChartGenerator.generateActivityChart(session);
        
        assertNotNull(chartBytes);
        assertTrue(chartBytes.length > 0);
    }

    @Test
    void testGenerateActivityChart_EmptyData() throws IOException {

        byte[] chartBytes = ChartGenerator.generateActivityChart(session);
        
        assertNotNull(chartBytes);
        assertTrue(chartBytes.length > 0);
    }
}