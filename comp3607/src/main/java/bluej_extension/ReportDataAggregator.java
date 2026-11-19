package bluej_extension;
/** @author Malik Blackman */

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Converts a populated SessionModel into the flat ReportData structure that the PDF generator consumes.
 *
 * Kept separate from PdfReport on purpose:
 *  - it has no PDFBox dependency, so it can be unit-tested on its own
 *  - any change to the aggregation maths doesn't risk breaking the layout
 *  - future report formats (HTML, CSV, etc.) can reuse the same output
 *
 * Everything here is pure computation - no I/O, no side effects.
 */
public class ReportDataAggregator {

    public static ReportData aggregate(SessionModel session) {

        ReportData data = new ReportData();

        /** Session metadata */
        data.projectName = session.sessionData.projectName;
        data.userId      = session.userId;
        data.startTime   = session.sessionData.startTime;
        data.endTime     = session.sessionData.endTime;

        /** Minutes are stored as doubles in the model but the report only needs whole numbers,
         *  round once here so every downstream calculation uses the same value
         */

        data.totalSessionMinutes = Math.round(session.sessionLengthMinutes);
        data.idleMinutes         = Math.round(session.idleMinutes);
        data.activeMinutes       = Math.max(0, data.totalSessionMinutes - data.idleMinutes);

        /**Counters */
        data.totalCompilations = session.totalCompilations;
        data.totalErrors       = session.totalErrors;
        data.totalRuns         = session.sessionData.totalRuns;
        data.successfulComp    = session.sessionData.successfulCompilations;
        data.failedComp        = session.sessionData.failedCompilations;

        /**Activity map
         * LinkedHashMap preserves bucket order, 
         * which the area chart relies on to draw points left-to-right.
         */
        data.activityMap = new LinkedHashMap<>();

        SessionModel.ActivityTimeline tl = session.derived.activityTimeline;
        if (tl != null && tl.buckets != null) {
            for (SessionModel.Bucket b : tl.buckets)
                data.activityMap.put(b.bucketStart, b.editSignals);
        }

        /**Error frequencies */
        data.errorFrequencies = new LinkedHashMap<>();
        if (session.derived.errorCategoryTotals != null){
            data.errorFrequencies.putAll(session.derived.errorCategoryTotals);
        }

        /**Timeline
         * Flatten the nested ClassLifecycleEvent into the simpler TimelineEvent, 
         * converting the timestamp to a string here so the page builder doesn't need to know about java.sql.Timestamp.
         */
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
