package bluej_extension;

/** @author Malik Blackman */

import java.util.List;
import java.util.Map;

/**
 * Plain data holder that sits between the session model and the PDF generator. 
 * Doing the aggregation once, up front, means the page-building methods only deal with primitives and lists,
 * no need to know anything about SessionModel's nested structure.
 */

public class ReportData {

    /**Session metadata */
    public String projectName;
    public String userId;
    public String startTime;
    public String endTime;

    /**Summary counters,rounded to whole minutes */

    public long totalSessionMinutes;
    public long activeMinutes;
    public long idleMinutes;
    public int  totalCompilations;
    public int totalErrors;
    public int totalRuns;

    /** Chart/table inputs */
    
    public Map<String, Integer> activityMap; /** bucket start -> edit signals */
    public List<TimelineEvent> projectTimeline; /** chronoligical class events */
    public Map<String, Integer> errorFrequencies; /** error category -> count */

    /** Compile outcomes (kept with the misspelling so callers already using the field don't break; worth renaming in a later refactor) */

    public int successfulComp;
    public int failedCompilations;

    /** Flattened version of SessionModel.ClassLifecycleEvent for the report. */

    public static class TimelineEvent{
        public String className;
        public String timeStamp;
        public String event;
    }
    
}
