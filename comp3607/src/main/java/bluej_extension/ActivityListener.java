package bluej_extension;

import bluej.extensions2.BClass;
import bluej.extensions2.PackageNotFoundException;
import bluej.extensions2.ProjectNotOpenException;
import bluej.extensions2.editor.JavaEditor;
import bluej.extensions2.event.ClassEvent;
import bluej.extensions2.event.ExtensionEvent;
import bluej.extensions2.event.ExtensionEventListener;

/**
 * ActivityListener is used by the extension through the ExtensionEventListener to capture the editor used within 
 * a class event to track user activity.
 * @author Zuri Boucaud
 */
public class ActivityListener implements ExtensionEventListener{

    private final ActivityTracker activityTracker;

    public ActivityListener(SessionModel session){
        this.activityTracker = new ActivityTracker(session);
    }

    /** (BlueJ Extension)
     * @see bluej.extensions2.event.ExtensionEventListener#eventOccurred(bluej.extensions2.event.ExtensionEvent)
     * 
     * modifyied to get active editor and starts activity tracker. if the editor is null,
     * exception is thrown
     */
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
