package bluej_extension;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import bluej.extensions.event.ClassListener;

public class Class implements ClassListener{
    private ArrayList<ClassEvent> deletedClasses = new ArrayList<ClassEvent>();

    public void classNameChanged(/*bluej sends this*/){
        ClassEvent nameChange = new ClassEvent("MODIFIED", "BLUEJ SENDS THIS");
    }

    public void classRemoved(/*bluej sends this*/){
        ClassEvent deleteClass = new ClassEvent("DELETE", "BLUEJ SENDS THIS");
         
        deletedClasses.add(deleteClass);
    }
    

    public void classStateChanged (/*bluej sends this*/){
        ClassEvent stateChange = new ClassEvent("STATE", "BLUEJ SENDS THIS");
    }


}