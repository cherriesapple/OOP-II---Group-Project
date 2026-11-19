/**package bluej_extension;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class PDFReport {

    private final float pageTopY = 750;
    private final float chartW = 500;

    public void generateReport(SessionModel session, String filePath) throws IOException {
        
        // 1. Aggregate the data from the session
       ReportData data = ReportDataAggregator.aggregate(session);

        File out = new File(filePath);
        if (out.getParentFile() != null) out.getParentFile().mkdirs();

        try (PDDocument doc = new PDDocument()) {

            renderPage(doc, buildOverviewPage(data));
            renderPage(doc, buildActivityPage(doc, session, data));
            renderPage(doc, buildCompilationPage(doc, session, data));
            renderPage(doc, buildTimelinePage(data));
            doc.save(out);

        }

        System.out.println("PDF report saved to: " + out.getAbsolutePath());

    }

    public static String suggestedFileName(String projectName) {

        String base = (projectName == null || projectName.isBlank())
                ? "session"
                : projectName.replaceAll("[^a-zA-Z0-9_-]", "_");
        return base + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";

    }

    // Page 1 - Title and Overview
    private PageElement buildOverviewPage(ReportData d) {

        PageElement page = new PageElement();

        page.addElement(new TextElement("BlueJ Telemetry Report", 22, true));
        page.addElement(new TextElement("Project:    " + safe(d.projectName), 12, false));
        page.addElement(new TextElement("Student ID: " + safe(d.userId), 12, false));
        page.addElement(new TextElement("Session:    " + safe(d.startTime) + " to " + safe(d.endTime), 11, false));

        page.addElement(new TextElement(" ", 10, false));
        page.addElement(new TextElement("Table of Contents", 14, true));
        page.addElement(new TextElement("1. Session Overview ....................... Page 1", 11, false));
        page.addElement(new TextElement("2. User Activity (Area Chart) ............. Page 2", 11, false));
        page.addElement(new TextElement("3. Compilation Outcomes & Errors .......... Page 3", 11, false));
        page.addElement(new TextElement("4. Project Timeline ....................... Page 4", 11, false));

        page.addElement(new TextElement(" ", 10, false));
        page.addElement(new TextElement("1. Session Overview", 14, true));
        page.addElement(new TextElement("Total session duration:    " + d.totalSessionMinutes + " min", 11, false));  
        page.addElement(new TextElement("Active coding time:        " + d.activeMinutes + " min", 11, false));
        page.addElement(new TextElement("Idle time (>=180s gaps):   " + d.idleMinutes + " min", 11, false));
        page.addElement(new TextElement("Total compilations:        " + d.totalCompilations, 11, false));
        page.addElement(new TextElement("Successful / Failed:       " + d.sucessfulComp + " / " + d.failedComp, 11, false));
        page.addElement(new TextElement("Total compilation errors:  " + d.totalErrors, 11, false));
        page.addElement(new TextElement("Program runs:              " + d.totalRuns, 11, false));

        return page;

    }


    // Page 2 - Activity area chart + summary

     private PageElement buildActivityPage(PDDocument doc, SessionModel s, ReportData d) throws IOException {

        PageElement page = new PageElement();
        page.addElement(new TextElement("2. User Activity Over Session", 16, true));
        page.addElement(new TextElement("Edit signals recorded in 3-minute time buckets.", 11, false));

        PDImageXObject img = PDImageXObject.createFromByteArray(doc, ChartElement.generateActivityChart(s), "activityChart");
        page.addElement(new ImageElement(img, chartW, 220));

        page.addElement(new TextElement("Summary", 13, true));
        page.addElement(new TextElement("Active time: " + d.activeMinutes + " min out of " + d.totalSessionMinutes + " min total.",11, false));
        page.addElement(new TextElement("Idle time accumulated in gaps of 180s or more: " + d.idleMinutes + " min.",11, false));
        page.addElement(new TextElement("Peaks indicate focused coding bursts; flat sections indicate idle periods.",11, false));

        return page;

    }      


    // Page 3 - Compilation outcomes and error frequency

    private PageElement buildCompilationPage(PDDocument doc, SessionModel s, ReportData d) throws IOException {

        PageElement page = new PageElement();
        page.addElement(new TextElement("3. Compilation Outcomes & Error Frequency", 16, true));

        PDImageXObject outcomeImg = PDImageXObject.createFromByteArray(doc, ChartElement.generateCompileOutcomeChart(s), "compileOutcomeChart");
        page.addElement(new ImageElement(outcomeImg, chartW, 190));

        int total = Math.max(1, d.totalCompilations);
        double rate = 100.0 * d.sucessfulComp / total;
        page.addElement(new TextElement(String.format("Success rate: %.1f%% (%d of %d attempts).",rate, d.sucessfulComp, d.totalCompilations), 11, false));

        PDImageXObject errImg = PDImageXObject.createFromByteArray(doc, ChartElement.generateErrorFrequencyChart(s), "errorFrequencyChart");

        page.addElement(new ImageElement(errImg, chartW, 190));

        page.addElement(new TextElement("Summary", 13, true));
        page.addElement(new TextElement("Failed compilations: " + d.failedComp + ". Total errors reported: " + d.totalErrors + ".",11, false));
        page.addElement(new TextElement("Dominant error categories reveal which concepts students struggle with most.",11, false));

        return page;

    }

    //Page 4 - Project Timeline + summary

    private PageElement buildTimelinePage(ReportData d) {

        PageElement page = new PageElement();
        page.addElement(new TextElement("4. Project Timeline", 16f, true));
        page.addElement(new TextElement("Class-lifecycle events in chronological order.", 11f, false));

        String[] headers = { "#", "Class", "Event", "Timestamp" };
        float[] widths   = { 30, 120, 220, 130 };
        List<String[]> rows = new ArrayList<>();

        int i = 1;
        for (ReportData.TimelineEvent e : d.projectTimeline) {
            rows.add(new String[] {String.valueOf(i++),safe(e.className),safe(e.event), safe(e.timeStamp)});

            if (rows.size() >= 15) break;  
        }

        if (rows.isEmpty()) {

            rows.add(new String[] { "-", "-", "No class events recorded", "-" });

        }

        page.addElement(new TableElement(headers, rows, widths));

        page.addElement(new TextElement("Summary", 13, true));
        page.addElement(new TextElement("Total class-lifecycle events logged: " + d.projectTimeline.size() + ".",11, false));
        page.addElement(new TextElement("Many MODIFIED events after CLASS_CREATED = iterative refinement; "+ "repeated REMOVED events = design changes.", 11, false));

        return page;

    }

    // Utility methods
    private static String safe(String s) {

        return (s == null) ? "" : s;

    }

    private void renderPage(PDDocument doc, PageElement page) throws IOException {

        PDPage physical = new PDPage();
        doc.addPage(physical);
        try (PDPageContentStream cs = new PDPageContentStream(doc, physical)) {page.render(cs, pageTopY);}

    }
}
**/