package bluej_extension;

import java.time.Instant;
import bluej.extensions2.BlueJ;
import bluej.extensions2.Extension;

public class MyExtension extends Extension
{

    private final SessionModel session = new SessionModel();
    private final PackageTracker packageTracker = new PackageTracker(session);
    private final ClassTracker classTracker = new ClassTracker(session);
    private final InvocationTracker invocationTracker = new InvocationTracker(session);
    private ConsoleSummary summary = new ConsoleSummary();
    private final CompileTracker compileTracker = new CompileTracker(session);
    private final ActivityListener activityListener = new ActivityListener(session);
    

    @Override 
    public void startup(BlueJ bluej)
    {
        bluej.addCompileListener(compileTracker);
        bluej.addClassListener(classTracker);
        bluej.addPackageListener(packageTracker);
        bluej.addInvocationFinishedListener(invocationTracker);    
        //bluej.addExtensionEventListener(ExtensionEvent event);
        
    
summary.captureMessages();

//System.out.println("Test 1");
//System.out.println("Test 2");



        bluej.addExtensionEventListener(activityListener);
    }

    @Override 
    public boolean isCompatible()
    { 
        return (getExtensionsAPIVersionMajor() >= 3); 
    }

    @Override 
    public String getVersion ()
    { 
        return ("1.2.3"); 
    }

    @Override 
    public String getName ()
    { 
        return ("Telemetry Extension"); 
    }

    @Override 
    public void terminate()
    {
        System.out.println("Console Count = " + summary.getMessageCount());
        activityListener.getActivityTracker().stopTracker();
        compileTracker.populateSession();
        session.status = "COMPLETE";
        session.sessionData.endTime = Instant.now().toString();
        System.out.println ("End of Telemetry Extension");
    }
        
    @Override 
    public String getDescription ()
    {
        return ("Collects telemetry data and generates a summary report each time the application is used");
    }   

}