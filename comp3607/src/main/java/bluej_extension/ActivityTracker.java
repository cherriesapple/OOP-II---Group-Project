package bluej_extension;

import bluej.extensions2.*;
import bluej.extensions2.editor.JavaEditor;
import bluej.extensions2.editor.TextLocation;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.scene.Scene;
import javafx.scene.input.*;
import javafx.util.Duration;


public class ActivityTracker {
    private final JavaEditor editor;
    private final long timeIdleMilli;
    private final long lastActivityTime;
    private long timeActive = 0;
    private int lastTextLength;
    private TextLocation lastCaretLocation;

    public ActivityTracker(BClass bClass, long idleThresholdMillis){
        this.editor = bClass.getJavaEditor();
        this.timeIdleMilli = idleThresholdMillis;
        this.lastActivityTime = System.currentTimeMillis();

        if(editor != null){
            this.lastTextLength = editor.getTextLength();
            this.lastCaretLocation = editor.getCaretLocation();
        }
    }

    

    
}
