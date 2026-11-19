package bluej_extension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import bluej.extensions2.editor.JavaEditor;
import bluej.extensions2.editor.TextLocation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class ActivityTracker {
    private static final long POLL_MS = 500;
    static final int  BUCKET_MINS = 3;
    private static final long IDLE_THRESH_NS = TimeUnit.SECONDS.toNanos(180);
    private static final double NS_PER_MIN = TimeUnit.MINUTES.toNanos(1);

    private Instant startI;
    private JavaEditor editor;
    private Timeline pollTimeline;
    private final SessionModel session;
    
    private long startTimeNs;
    private long lastActivityNs;
    private long idleNs;
    private int totalEdits = 0;

    private int lastTextLength = -1;
    private TextLocation lastCaretLoc = null;

    
    private final List<SessionModel.Bucket> buckets = new ArrayList<>();

    public ActivityTracker(SessionModel session) {
        this.editor = null;
        this.pollTimeline = null;
        this.session = session;
    }

    public boolean isSameLoc(TextLocation x, TextLocation y){
        if(x == null || y == null){
            return x == y;
        }

        return x.getLine() == y.getLine() && x.getColumn() == y.getColumn();
    }

    public void buildBucket(int x){
        while(buckets.size() <= x){
            SessionModel.Bucket b = new SessionModel.Bucket();
            b.bucketIndex = buckets.size();
            b.bucketStart = startI.plus(b.bucketIndex * (long) BUCKET_MINS, ChronoUnit.MINUTES).toString();
            b.editSignals = 0;
            b.activeSeconds = 0;
            buckets.add(b);
        }
    }

    public void editLog(long now){
        long gapStart = (lastActivityNs < 0) ? startTimeNs : lastActivityNs;
        long gap = now - gapStart;

        if(lastActivityNs < 0){
            idleNs += now - startTimeNs;
        }
        else{
            if( gap >= IDLE_THRESH_NS){
                idleNs += gap;
            }
            if(gap < IDLE_THRESH_NS){
                int gIndex = (int)((gapStart - startTimeNs) / TimeUnit.MINUTES.toNanos(BUCKET_MINS));
                
                buildBucket(gIndex);
                buckets.get(gIndex).activeSeconds += TimeUnit.NANOSECONDS.toSeconds(gap);
            }

        }

       
        lastActivityNs = now;

        int x = (int) ((now - startTimeNs)/TimeUnit.MINUTES.toNanos(BUCKET_MINS));
        
        buildBucket(x);
        buckets.get(x).editSignals++;
        totalEdits ++;
    }


    public void checkActivty(){
        if (editor == null){
            return;
        }

        try {
            int currLength = editor.getTextLength();
            TextLocation currCaret = editor.getCaretLocation();

            if(currLength != lastTextLength || !isSameLoc(currCaret, lastCaretLoc)){
                long now = System.nanoTime();
                editLog(now);

                lastTextLength = currLength;
                lastCaretLoc = currCaret;
            } 
        } catch (Exception e) {
            stopTracker();
        }
    }

    public void beginTracker(JavaEditor active){
        if(this.editor == active && pollTimeline != null ){
            return;
        }
        if(pollTimeline != null){
            pollTimeline.stop();
            pollTimeline = null;
        }

        startI = Instant.now();
        this.editor = active;
        startTimeNs = System.nanoTime();
        lastActivityNs = -1;
        idleNs = 0;

        buckets.clear();
        totalEdits = 0;
        
        if (active != null) {
            this.lastTextLength = active.getTextLength();
            this.lastCaretLoc = active.getCaretLocation();
            pollTimeline = new Timeline(new KeyFrame(Duration.millis(POLL_MS), e -> checkActivty()));
            pollTimeline.setCycleCount(Timeline.INDEFINITE);
            pollTimeline.play();
        }
    }


    public void stopTracker(){
        if(pollTimeline != null){
            pollTimeline.stop();
            pollTimeline = null;
        }

        if(startTimeNs == -1){
        return;
    }

        long now = System.nanoTime();
        long gapStart = (lastActivityNs < 0) ? startTimeNs : lastActivityNs;
        long gap = now - gapStart;

        if(lastActivityNs < 0){
            idleNs += now - startTimeNs;
        }
        else{
            if( gap >= IDLE_THRESH_NS){
                idleNs += gap;
            }
            if(gap < IDLE_THRESH_NS){
                int gIndex = (int)((gapStart - startTimeNs) / TimeUnit.MINUTES.toNanos(BUCKET_MINS));
                
                buildBucket(gIndex);
                buckets.get(gIndex).activeSeconds += TimeUnit.NANOSECONDS.toSeconds(gap);
            }
        }

        editor = null;

        session.idleMinutes = idleNs/NS_PER_MIN;
        session.sessionLengthMinutes = (now - startTimeNs)/NS_PER_MIN;
        
        long activeNs = now - startTimeNs - idleNs;
        session.active = activeNs >= TimeUnit.SECONDS.toNanos(1) ? "yes" : "no"; 

        int currBucket = (int)((now - startTimeNs) / TimeUnit.MINUTES.toNanos(BUCKET_MINS));
       
        buildBucket(currBucket);

        SessionModel.ActivityTimeline timeline = session.derived.activityTimeline;
        timeline.bucketMinutes = BUCKET_MINS;
        timeline.totalEditSignals = totalEdits;
        timeline.buckets = new ArrayList<>(buckets);

        startTimeNs = -1;
        lastActivityNs = -1;
        startI = null;
    }

    public long getStartTimeNs() {
        return startTimeNs;
    }

    public long getLastActivityNs() {
        return lastActivityNs;
    }

    public long getIdleNs() {
        return idleNs;
    }

    public List<SessionModel.Bucket> getBuckets() {
        return buckets;
    }

    public int getTotalEdits() {
        return totalEdits;
    }
}
   