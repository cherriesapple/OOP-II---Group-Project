package bluej_extension;

import java.util.ArrayList;
import java.util.LinkedHashMap;
public class ReportDataAggregator {

    public static ReportData aggregate(SessionModel session) {

        ReportData data = new ReportData();
        data.projectName = session.sessionData.projectName;
        data.userId      = session.userId;
        data.startTime   = session.sessionData.startTime;
        data.endTime     = session.sessionData.endTime;

        data.totalSessionMinutes = Math.round(session.sessionLengthMinutes);
        data.idleMinutes         = Math.round(session.idleMinutes);
        data.activeMinutes       = Math.max(0, data.totalSessionMinutes - data.idleMinutes);

        data.totalCompilations = session.totalCompilations;
        data.totalErrors       = session.totalErrors;
        data.totalRuns         = session.sessionData.totalRuns;
        data.sucessfulComp    = session.sessionData.successfulCompilations;
        data.failedComp        = session.sessionData.failedCompilations;

        data.activityMap = new LinkedHashMap<>();

        SessionModel.ActivityTimeline tl = session.derived.activityTimeline;
        if (tl != null && tl.buckets != null) {
            for (SessionModel.Bucket b : tl.buckets)
                data.activityMap.put(b.bucketStart, b.editSignals);
        }

        data.errorFrequencies = new LinkedHashMap<>();
        if (session.derived.errorCategoryTotals != null)
            data.errorFrequencies.putAll(session.derived.errorCategoryTotals);

        data.projectTimeline = new ArrayList<>();
        if (session.classLifecycle != null) {
            for (SessionModel.ClassLifecycleEvent e : session.classLifecycle) {
                ReportData.TimelineEvent te = new ReportData.TimelineEvent();
                te.className = e.className;
                te.event     = e.event;
                te.timeStamp = String.valueOf(e.timestamp);
                data.projectTimeline.add(te);
            }
        }
        return data;

    }
    
}
