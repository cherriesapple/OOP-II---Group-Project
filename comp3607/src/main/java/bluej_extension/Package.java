package bluej_extension;

import bluej.extensions.event.PackageListener;

public class Package implements PackageListener{
     public void packageClosing(/*bluej proviedes this*/){
        ProjectEvent projectClose = new ProjectEvent();
    }

    public void packageOpened(/*bluej proviedes this*/){
        ProjectEvent projectOpen = new ProjectEvent();

    }
}