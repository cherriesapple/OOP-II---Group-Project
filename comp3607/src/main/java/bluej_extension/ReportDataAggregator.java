package bluej_extension;

import java.util.ArrayList;
import java.util.LinkedHashMap;
public class ReportDataAggregator {

    public static ReportData aggregate(SessionModel session) {

        ReportData d = new ReportData();
        d.projectName = session.sessionData.projectName;
        d.userId      = session.userId;
        d.startTime   = session.sessionData.startTime;
        d.endTime     = session.sessionData.endTime;

        d.totalSessionMinutes = Math.round(session.sessionLengthMinutes);
        d.idleMinutes         = Math.round(session.idleMinutes);
        d.activeMinutes       = Math.max(0, d.totalSessionMinutes - d.idleMinutes);

        d.totalCompilations = session.totalCompilations;
        d.totalErrors       = session.totalErrors;
        d.totalRuns         = session.sessionData.totalRuns;
        d.sucessfulComp    = session.sessionData.successfulCompilations;
        d.failedComp        = session.sessionData.failedCompilations;

        d.activityMap = new LinkedHashMap<>();

        SessionModel.ActivityTimeline tl = session.derived.activityTimeline;
        if (tl != null && tl.buckets != null) {
            for (SessionModel.Bucket b : tl.buckets)
                d.activityMap.put(b.bucketStart, b.editSignals);
        }

        d.errorFrequencies = new LinkedHashMap<>();
        if (session.derived.errorCategoryTotals != null)
            d.errorFrequencies.putAll(session.derived.errorCategoryTotals);

        d.projectTimeline = new ArrayList<>();
        if (session.classLifecycle != null) {
            for (SessionModel.ClassLifecycleEvent e : session.classLifecycle) {
                ReportData.TimelineEvent te = new ReportData.TimelineEvent();
                te.className = e.className;
                te.event     = e.event;
                te.timeStamp = String.valueOf(e.timestamp);
                d.projectTimeline.add(te);
            }
        }
        return d;

    }
    
}
