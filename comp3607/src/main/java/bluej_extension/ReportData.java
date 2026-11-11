package bluej_extension;

import java.util.List;
import java.util.Map;

public class ReportData {

    public Map<String, Integer> activityMap;
    public List<TimelineEvent> projectTimeline;

    public int sucessfulComp;
    public int failedComp;

    public static class TimelineEvent{
        public String className;
        public String creationTime;
        public String lastModified;
    }
    
}
