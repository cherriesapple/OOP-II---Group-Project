package bluej_extension;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClassTrackerTest {

    private SessionModel session;
    private ClassTracker classTracker;

    @BeforeEach
    void setUp() {
        session = new SessionModel();
        classTracker = new ClassTracker(session);
    }

    @Test
    void checkInstantiations(){
        assertNotNull(session);
        assertNotNull(classTracker);
    }

    @Test
    void testStoreTelemetryEvents() {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        classTracker.storeTelemetryEvents("Class_1", "TEST_EVENT", time);
        
        assertEquals(1, session.classLifecycle.size());
        
        SessionModel.ClassLifecycleEvent savedEvent = session.classLifecycle.get(0);
        assertEquals("Class_1", savedEvent.className);
        assertEquals("TEST_EVENT", savedEvent.event);
        assertEquals(time, savedEvent.timestamp);
    }

    @Test
    void testFormatEventDescription() {

        String newClass = classTracker.formatStateChangeEvent(false, true, "Class_1");
        assertEquals("CLASS_CREATED", newClass);

        String modifiedClass = classTracker.formatStateChangeEvent(false, false, "Class_1");
        assertTrue(modifiedClass.contains("was modified"));

        String compileAttempt = classTracker.formatStateChangeEvent(true, false, "Class_1");
        assertTrue(compileAttempt.contains("[Compilation Attempt]"));
    }
}