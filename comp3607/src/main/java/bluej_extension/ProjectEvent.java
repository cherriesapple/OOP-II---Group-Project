package bluej_extension;

public class ProjectEvent implements TelemetryEvent, PackageListener {
    private String eventType;
    private String projectName;
    private long timestamp;

    public ProjectEvent(String eventType, String projectName, long timeStamp){
        this.eventType = eventType;
        this.projectName = projectName;
        this.timestamp = timestamp;
    }

    public String getEventType() {return this.eventType;}
    public String getProjectName() {return this.projectName;}
    public long getTimeStamp() {return this.timestamp;}

    public void packageClosing(ProjectEvent event){

    }

    public void packageOpened(ProjectEvent event){

    }
}
