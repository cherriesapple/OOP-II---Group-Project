package bluej_extension;
import java.sql.Timestamp;

import bluej.extensions2.ExtensionException;
import bluej.extensions2.event.PackageEvent;
import bluej.extensions2.event.PackageListener;
import bluej_extension.SessionModel.TelemetryEvent;

public class PackageTracker implements PackageListener {

    private TelemetryEvent myEvent;
    String eventDescription;
    private final SessionModel session;

    public PackageTracker(SessionModel session){
        this.session = session;
    }

    public void storeTelemetryEvents(String description, Timestamp time){
        myEvent = new TelemetryEvent();
        myEvent.timestamp = time;
        
        myEvent.data.put("[Status]", description);
        session.events.add(myEvent);
    }

    public void recordPackageEvent(String packageName, boolean hasOpened){
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String action = hasOpened ? "Created/Opened" : "Deleted/Closed";
        eventDescription = action + " Package: " + packageName;
        storeTelemetryEvents(eventDescription, time); 
    }

    public void recordPackageError(String errorMessage) {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        eventDescription = "Error retrieving package name: " + errorMessage;
        storeTelemetryEvents(eventDescription, time); 
    }

    @Override 
    public void packageClosing(PackageEvent event){
    
        try {
            String packageName = event.getPackage().getProject().getName();
            recordPackageEvent(packageName, false);

        } catch (ExtensionException e){
            recordPackageError(e.getMessage());
        }
    }

    @Override 
    public void packageOpened(PackageEvent event){
    
        try {
            String packageName = event.getPackage().getProject().getName();
            recordPackageEvent(packageName, true);
        } catch (ExtensionException e){
            recordPackageError(e.getMessage());
        }   
    }

}
