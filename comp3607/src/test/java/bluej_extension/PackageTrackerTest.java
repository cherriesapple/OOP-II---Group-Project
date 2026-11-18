package bluej_extension;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PackageTrackerTest {

    private SessionModel session;
    private PackageTracker packageTracker;

    @BeforeEach
    void setUp() {
        session = new SessionModel();
        packageTracker = new PackageTracker(session);
    }

    @Test
    void checkInstantiations(){
        assertNotNull(session);
        assertNotNull(packageTracker);
    }

    @Test
    void testRecordPackageOpened() {

        packageTracker.recordPackageEvent("Package_1", true);

        assertEquals(1, session.events.size());
        assertEquals("Created/Opened Package: Package_1", session.events.get(0).data.get("[Status]"));
    }

    @Test
    void testRecordPackageClosed() {

        packageTracker.recordPackageEvent("Package_1", false);

        assertEquals(1, session.events.size());
        assertEquals("Deleted/Closed Package: Package_1", session.events.get(0).data.get("[Status]"));
    }

    @Test
    void testRecordPackageError() {
       
        packageTracker.recordPackageError("Package not open");

        assertEquals(1, session.events.size());
        assertEquals("Error retrieving package name: Package not open", session.events.get(0).data.get("[Status]"));
    }

    @Test
    void testStoreTelemetryEvents() {
    
        Timestamp time = new Timestamp(System.currentTimeMillis());

        packageTracker.storeTelemetryEvents("Test_Description", time);

        assertEquals(1, session.events.size());
        SessionModel.TelemetryEvent savedEvent = session.events.get(0);
        
        assertEquals(time, savedEvent.timestamp);
        assertEquals("Test_Description", savedEvent.data.get("[Status]"));
    }
}