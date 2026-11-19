package bluej_extension;

import java.io.File;
import java.io.IOException;

import bluej.extensions2.BlueJ;
import bluej.extensions2.Extension;

/**
 * 
 */
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
    public void terminate() {

         try {
             // JSON export
            new JsonExporter().export(session);

            // PDF export (timestamped, session-named file in src/files/)
            File folder = new File("src/files");
            folder.mkdirs();
            String name = PDFReport.suggestedFileName(session.sessionData.projectName);
            new PDFReport().generateReport(session, new File(folder, name).getPath());

    } catch (IOException e) {

            System.err.println("Telemetry export failed: " + e.getMessage());

    }
            System.out.println("End of Telemetry Extension");

    }
        
    @Override 
    public String getDescription ()
    {
        return ("Collects telemetry data and generates a summary report each time the application is used");
    }   

}