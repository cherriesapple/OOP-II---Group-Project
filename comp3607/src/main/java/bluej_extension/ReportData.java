package bluej_extension;

import java.util.List;
import java.util.Map;

public class ReportData {

    public long totalSessionMinutes;
    public long activeMinutes;
    public long idleMinutes;
    public int  totalCompilations;
    public int totalErrors;
    public int totalRuns;
    
    public Map<String, Integer> activityMap;
    public List<TimelineEvent> projectTimeline;
    public Map<String, Integer> errorFrequencies;

    public int sucessfulComp;
    public int failedComp;

    public static class TimelineEvent{
        public String className;
        public String timeStamp;
        public String event;
    }
    
}
