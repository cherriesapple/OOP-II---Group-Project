package bluej_extension;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;

import bluej.extensions2.BlueJ;
import bluej.extensions2.Extension;
import bluej_extension.SessionModel.TelemetryEvent;

/**
 * This is the main entry point for the Telemetry Extension plugin. 
 * It acts as proxy between the blueJ environment and the internal tracking components. 
 * 
 * It is responsible for instantiating and registering the respective listeners.
 * 
 * @author Shurona Jackson
 */
public class MyExtension extends Extension
{

    private final SessionModel session = new SessionModel();
    private final PackageTracker packageTracker = new PackageTracker(session);
    private final ClassTracker classTracker = new ClassTracker(session);
    private final InvocationTracker invocationTracker = new InvocationTracker(session);
    private final ConsoleSummary summary = new ConsoleSummary();
    private final CompileTracker compileTracker = new CompileTracker(session);
    private final ActivityListener activityListener = new ActivityListener(session);
    private BlueJ bluejProxy;
    
    /**
     * Called by BlueJ when the extenstion is initialized.
     */
    @Override 
    public void startup(BlueJ bluej)
    {
        this.bluejProxy = bluej;

        bluej.addCompileListener(compileTracker);
        bluej.addClassListener(classTracker);
        bluej.addPackageListener(packageTracker);
        bluej.addInvocationFinishedListener(invocationTracker);    
        bluej.addExtensionEventListener(activityListener);
        summary.captureMessages();
    }

    /**Checks if this extension is compatible with the current version of the BlueJ Extensions API. */
    @Override 
    public boolean isCompatible()
    { 
        return (getExtensionsAPIVersionMajor() >= 3); 
    }

    /**
     * Retrieves the version number of this extension.
     * 
     * @return A string representing the version (e.g., "1.2.3")
     */
    @Override 
    public String getVersion ()
    { 
        return ("1.2.3"); 
    }

    /**
     * Retrieves the user-visible name of this extension.
     * 
     * @return The name of the extension as it appears in the BlueJ "Installed Extensions" menu.
     */
    @Override 
    public String getName ()
    { 
        return ("Telemetry Extension"); 
    }


    @Override
    public void terminate() {
        TelemetryEvent console = new TelemetryEvent();
        console.timestamp = new Timestamp(System.currentTimeMillis());
        console.type = "[CONSOLE_OUTPUT_SUMMARY]: ";
        console.data.put("Total_Output_Messages: ", summary.getMessageCount());

        session.status = "COMPLETE";
        session.sessionData.endTime = Instant.now().toString();

        activityListener.getActivityTracker().stopTracker();
        compileTracker.populateSession();;

         try {
            new JsonExporter().export(session);

            File folder = new File("src/files");
            folder.mkdirs();
            String name = PDFReport.suggestedFileName(session.sessionData.projectName);
            new PDFReport().generateReport(session, new File(folder, name).getPath());

        } catch (IOException e) {

                System.err.println("Telemetry export failed: " + e.getMessage());

        }
                System.out.println("End of Telemetry Extension");

    }

       
    /**
     * Retrieves the description of what this extension does.
     * 
     * @return A brief description of the plugin's functionality.
     */
    @Override 
    public String getDescription ()
    {
        return ("Collects telemetry data and generates a summary report each time the application is used");
    }   

}