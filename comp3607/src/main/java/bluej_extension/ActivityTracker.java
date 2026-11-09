package bluej_extension;

import java.util.ArrayList;
import java.util.List;

import bluej.extensions2.editor.JavaEditor;
import bluej.extensions2.editor.TextLocation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class ActivityTracker {

    private JavaEditor editor;
    private Timeline pollTimeline;

    private int lastTextLength = -1;
    private TextLocation lastCaretLoc = null;

    private final List<Long> activity = new ArrayList<>();
    private static final long IDLE_THRESH_NS = 120_000_000_000L;

    public ActivityTracker() {
        this.editor = null;
        this.pollTimeline = null;
    }

    public boolean isSameLoc(TextLocation x, TextLocation y){
        if(x == null || y == null){
            return x == y;
        }

        return x.getLine() == y.getLine() && x.getColumn() == y.getColumn();
    }


    private void checkActivty(){
        if (editor == null){
            return;
        }

        try {
            int currLength = editor.getTextLength();
            TextLocation currCaret = editor.getCaretLocation();

            if(currLength != lastTextLength || !isSameLoc(currCaret, lastCaretLoc)){
                long now = System.nanoTime();
                activity.add(now);

                lastTextLength = currLength;
                lastCaretLoc = currCaret;
            } 
        } catch (Exception e) {

        }
    }

    public void beginTracker(JavaEditor active){
        this.editor = active;
        
        this.lastTextLength =  editor.getTextLength();
        this.lastCaretLoc = editor.getCaretLocation();
        
        pollTimeline = new Timeline(new KeyFrame(Duration.millis(500), e -> checkActivty()));
        pollTimeline.setCycleCount(Timeline.INDEFINITE);
        pollTimeline.play();
    }

    public long calculateTimeIdle(){
        long timeIdle = 0;
        long idle;

        for(int z = 1; z < activity.size(); z++){
            idle = activity.get(z) - activity.get(z-1);

            if(idle >= IDLE_THRESH_NS){
                timeIdle += idle;
            }
        }

        return timeIdle;
    }

    public long calculateTimeActive(){
        long timeActive;
        
        long active = (activity.get(activity.size()-1)) - activity.get(0);
        
        timeActive = active - calculateTimeIdle();

        return timeActive;
    }
  
}
