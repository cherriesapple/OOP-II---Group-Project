package bluej_extension;

import bluej.extensions2.BClass;
import bluej.extensions2.PackageNotFoundException;
import bluej.extensions2.ProjectNotOpenException;
import bluej.extensions2.editor.JavaEditor;
import bluej.extensions2.event.ClassEvent;
import bluej.extensions2.event.ExtensionEvent;
import bluej.extensions2.event.ExtensionEventListener;

public class ActivityListener implements ExtensionEventListener{

    private final ActivityTracker activityTracker;

    public ActivityListener(SessionModel session){
        this.activityTracker = new ActivityTracker(session);
    }

    @Override
    public void eventOccurred(ExtensionEvent event){

        if (!(event instanceof ClassEvent classEvent)){
            return;
        } 

        BClass bClass = classEvent.getBClass();
        try {
            JavaEditor editor = bClass.getJavaEditor();
            if (editor != null) {
                activityTracker.beginTracker(editor);   // make idempotent
            }
        } catch (PackageNotFoundException | ProjectNotOpenException e) {
            System.err.println("Could not open editor for "
                + bClass.getName() + ": " + e.getMessage());
        }
    }

    public ActivityTracker getActivityTracker() {
        return activityTracker;
    }
    
    
}
