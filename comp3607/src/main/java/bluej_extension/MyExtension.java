
package bluej_extension;

import bluej.extensions2.BlueJ;
import bluej.extensions2.Extension;

/*
 * This is the starting point of a BlueJ Extension
 */
public class MyExtension extends Extension
{
    private final PackageTracker packageTracker = new PackageTracker();
    private final ClassTracker classTracker = new ClassTracker();
    private final InvocationTracker invocationTracker = new InvocationTracker();
    private final CompileTracker compileTracker = new CompileTracker();

    @Override 
    public void startup(BlueJ bluej)
    {
        bluej.addCompileListener(compileTracker);
        bluej.addClassListener(classTracker);
        bluej.addPackageListener(packageTracker);
        bluej.addInvocationFinishedListener(invocationTracker);
    }

    /*
     * A package has been opened. Print the name of the project it is part of.
     * System.out is redirected to the BlueJ debug log file.
     * The location of this file is given in the Help/About BlueJ dialog box.
    
    public void packageOpened(PackageEvent ev)
    {
        try
        {
            System.out.println("Project " + ev.getPackage().getProject().getName() 
                               + " opened.");
        }
        catch (ExtensionException e)
        {
            System.out.println("Project closed by BlueJ");
        }
    } 
          
    /*
    /*
     * A package is closing.
    
    public void packageClosing(PackageEvent ev)
    {
    } 
               
     * This method must decide if this Extension is compatible with the                
     * current release of the BlueJ Extensions API
     */

    @Override 
    public boolean isCompatible()
    { 
        return (getExtensionsAPIVersionMajor() >= 3); 
    }

    /*
     * Returns the version number of this extension
     */

    @Override 
    public String getVersion ()
    { 
        return ("1.2.3"); 
    }

    /*
     * Returns the user-visible name of this extension
     */

    @Override 
    public String getName ()
    { 
        return ("Telemetry Extension"); 
    }

    @Override 
    public void terminate()
    {
        System.out.println ("End of Telemetry Extension");
    }
        
    @Override 
    public String getDescription ()
    {
        return ("Collects telemetry data and generates a summary report each time the application is used");
    }

    /*
     * Returns a URL where you can find info on this extension.
     * The real problem is making sure that the link will still be alive 
     * in three years...
     */

   

}