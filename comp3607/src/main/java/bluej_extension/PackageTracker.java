package bluej_extension;

import bluej.extensions2.ExtensionException;
import bluej.extensions2.event.PackageEvent;
import bluej.extensions2.event.PackageListener;

public class PackageTracker implements PackageListener {

    @Override 
    public void packageClosing(PackageEvent event){
        try {
            System.out.println("/Opened Package: " + event.getPackage().getProject().getName());
        } catch (ExtensionException e){
            System.out.println("Error getting package name: " + e.getMessage());
        }   
        
    }

    @Override 
    public void packageOpened(PackageEvent event){
try {
            System.out.println("Created/Opened Package: " + event.getPackage().getProject().getName());
        } catch (ExtensionException e){
            System.out.println("Error getting package name: " + e.getMessage());
        }   
    }


}
