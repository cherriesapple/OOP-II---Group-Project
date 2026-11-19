package bluej_extension;
import java.sql.Timestamp;

import bluej.extensions2.ExtensionException;
import bluej.extensions2.event.PackageEvent;
import bluej.extensions2.event.PackageListener;
import bluej_extension.SessionModel.TelemetryEvent;

/**
 * This class is used by the extension to listen to all the package events generated within the bluej environment
 * 
 * @author Shurona Jackson
 */
public class PackageTracker implements PackageListener {

    private TelemetryEvent myEvent;
    String eventDescription;
    private final SessionModel session;

    /**
     * Creates a package tracker.
     * 
     * @param session is used to store telemetry data.
     */
    public PackageTracker(SessionModel session){
        this.session = session;
    }

    /**
     * Adds the telemetry event to the session with its relevant data including its timestamp and additional information;
     * 
     */
    public void storeTelemetryEvents(String description, Timestamp time){
        myEvent = new TelemetryEvent();
        myEvent.timestamp = time;
        
        myEvent.data.put("[Status]", description);
        session.events.add(myEvent);
    }

    /**
     * Generates the event description of the event that occured and invokes the method that updates the session 
     * 
     * @param action If hasOpened is true the action contains Created/Opened or Deleted/Closed if false
     */
    public void recordPackageEvent(String packageName, boolean hasOpened){
        Timestamp time = new Timestamp(System.currentTimeMillis());
        String action = hasOpened ? "Created/Opened" : "Deleted/Closed";
        eventDescription = action + " Package: " + packageName;
        storeTelemetryEvents(eventDescription, time); 
    }

    /**
     * Generates the event description of the error that occured and invokes the method that updates the session 
     * 
     * @param eventDescription Stores the information pertaining to the event that occured. 
     */
    public void recordPackageError(String errorMessage) {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        eventDescription = "Error retrieving package name: " + errorMessage;
        storeTelemetryEvents(eventDescription, time); 
    }

      /**
     * Method provided by bluej API that records either the package that was deleted/closed or the error generated.
     * 
     */
    @Override 
    public void packageClosing(PackageEvent event){
    
        try {
            String packageName = event.getPackage().getProject().getName();
            recordPackageEvent(packageName, false);

        } catch (ExtensionException e){
            recordPackageError(e.getMessage());
        }
    }

     /**
     * Method provided by bluej API that records either the package that was created/opened or the error generated.
     * 
     */
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
