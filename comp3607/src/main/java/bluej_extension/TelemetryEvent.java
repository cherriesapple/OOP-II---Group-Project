package bluej_extension;
import java.sql.Timestamp;

public interface TelemetryEvent {
    
    String getEventType();
    Timestamp getTimeStamp();

}
