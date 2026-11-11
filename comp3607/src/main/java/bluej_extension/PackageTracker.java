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
    
    private TelemetryEvent createTelemetryEvent(){
        myEvent.timestamp = new Timestamp(System.currentTimeMillis());
        myEvent = new TelemetryEvent();

        return myEvent;
    }

    @Override 
    public void packageClosing(PackageEvent event){
        
        myEvent = createTelemetryEvent();
    
        try {
            eventDescription = " Deleted/Closed Package: " + event.getPackage().getProject().getName();
            System.out.println("Deleted/Closed Package: " + event.getPackage().getProject().getName());
        } catch (ExtensionException e){
            eventDescription = " Error retrieving package name: " + e.getMessage();
            System.out.println("Error retrieving package name: " + e.getMessage());
        }   

        myEvent.data.put("[Status]", eventDescription);
        session.events.add(myEvent);
    }

    @Override 
    public void packageOpened(PackageEvent event){

        myEvent = createTelemetryEvent();
    
        try {
            eventDescription = "Created/Opened Package: " + event.getPackage().getProject().getName();
            System.out.println("Created/Opened Package: " + event.getPackage().getProject().getName());
        } catch (ExtensionException e){
            eventDescription = "Error getting package name: " + e.getMessage();
            System.out.println("Error getting package name: " + e.getMessage());
        }   

        session.events.add(myEvent);
    }


}
