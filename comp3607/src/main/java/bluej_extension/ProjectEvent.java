package bluej_extension;
import java.sql.Timestamp;

public class ProjectEvent implements TelemetryEvent{
    private String eventType;
    private String projectName;
    private Timestamp timestamp;

    public ProjectEvent(String eventType, String projectName){
        this.eventType = eventType;
        this.projectName = projectName;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public String getEventType() {return this.eventType;}
    public String getProjectName() {return this.projectName;}
    public Timestamp getTimeStamp() {return this.timestamp;}
   
}
