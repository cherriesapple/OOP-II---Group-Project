package bluej_extension;

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

/**
 * Builds a four-page PDF telemetry report from a finished SessionModel.
 *
 * Page layout:
 *   1. Title, table of contents, session overview numbers
 *   2. User activity area chart with a short summary
 *   3. Compilation outcome + error frequency bar charts with a summary
 *   4. Project timeline table with a summary
 *
 * Each page is assembled as a PageElement (composite) tree of text, image, and table leaves. 
 * Rendering a page is then a single recursive call,
 * the page doesn't care what its children are, only that they implement ReportElement.
 * 
 * @author Malik Blackman
 */
public class PDFReport {

    /** A4/Letter page height is 792pt; starting just below the top margin leaves room for a header if one is added later. */

    private final float pageTopY = 750;
    private final float chartWidth = 500;

    /**
     * Writes the PDF to {@code filePath}. Any missing parent directories are
     * created automatically so the caller can just pass "src/files/x.pdf".
     */

    public void generateReport(SessionModel session, String filePath) throws IOException {
        
        /**Flatten the session into a simple data holder first, so the page builders below only ever look at ReportData, never the raw model. */
       ReportData data = ReportDataAggregator.aggregate(session);

        File out = new File(filePath);
        if (out.getParentFile() != null) out.getParentFile().mkdirs();

        try (PDDocument document = new PDDocument()) {

            renderPage(document, buildOverviewPage(data));
            renderPage(document, buildActivityPage(document, session, data));
            renderPage(document, buildCompilationPage(document, session, data));
            renderPage(document, buildTimelinePage(data));
            document.save(out);

        }

        System.out.println("PDF report saved to: " + out.getAbsolutePath());

    }

    /**
     * Builds a filename of the form {@code ProjectName_YYYYMMDD_HHMMSS.pdf}.
     * Illegal filename characters are replaced with underscores so the
     * caller doesn't have to pre-sanitise the project name.
     */
    public static String suggestedFileName(String projectName) {

        String base = (projectName == null || projectName.isBlank())
                ? "session"
                : projectName.replaceAll("[^a-zA-Z0-9_-]", "_");
        return base + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";

    }

    /** Page 1 - Title and Overview */
    private PageElement buildOverviewPage(ReportData data) {

        PageElement page = new PageElement();

        page.addElement(new TextElement("BlueJ Telemetry Report", 22, true));
        page.addElement(new TextElement("Project:    " + safe(data.projectName), 12, false));
        page.addElement(new TextElement("Student ID: " + safe(data.userId), 12, false));
        page.addElement(new TextElement("Session:    " + safe(data.startTime) + " to " + safe(data.endTime), 11, false));

        page.addElement(new TextElement(" ", 10, false));
        page.addElement(new TextElement("Table of Contents", 14, true));
        page.addElement(new TextElement("1. Session Overview ....................... Page 1", 11, false));
        page.addElement(new TextElement("2. User Activity (Area Chart) ............. Page 2", 11, false));
        page.addElement(new TextElement("3. Compilation Outcomes & Errors .......... Page 3", 11, false));
        page.addElement(new TextElement("4. Project Timeline ....................... Page 4", 11, false));

        page.addElement(new TextElement(" ", 10, false));
        page.addElement(new TextElement("1. Session Overview", 14, true));
        page.addElement(new TextElement("Total session duration:    " + data.totalSessionMinutes + " min", 11, false));  
        page.addElement(new TextElement("Active coding time:        " + data.activeMinutes + " min", 11, false));
        page.addElement(new TextElement("Idle time (>=180s gaps):   " + data.idleMinutes + " min", 11, false));
        page.addElement(new TextElement("Total compilations:        " + data.totalCompilations, 11, false));
        page.addElement(new TextElement("Successful / Failed:       " + data.successfulComp + " / " + data.failedCompilations, 11, false));
        page.addElement(new TextElement("Total compilation errors:  " + data.totalErrors, 11, false));
        page.addElement(new TextElement("Program runs:              " + data.totalRuns, 11, false));

        return page;

    }


    /**  Page 2 - Activity area chart + summary */
     private PageElement buildActivityPage(PDDocument document, SessionModel session, ReportData data) throws IOException {

        PageElement page = new PageElement();
        page.addElement(new TextElement("2. User Activity Over Session", 16, true));
        page.addElement(new TextElement("Edit signals recorded in 3-minute time buckets.", 11, false));

        PDImageXObject img = PDImageXObject.createFromByteArray(document, ChartGenerator.generateActivityChart(session), "activityChart");
        page.addElement(new ImageElement(img, chartWidth, 220));

        page.addElement(new TextElement("Summary", 13, true));
        page.addElement(new TextElement("Active time: " + data.activeMinutes + " min out of " + data.totalSessionMinutes + " min total.",11, false));
        page.addElement(new TextElement("Idle time accumulated in gaps of 180s or more: " + data.idleMinutes + " min.",11, false));
        page.addElement(new TextElement("Peaks indicate focused coding bursts; flat sections indicate idle periods.",11, false));

        return page;

    }      


    /** Page 3 - Compilation outcomes and error frequency */
    private PageElement buildCompilationPage(PDDocument document, SessionModel session, ReportData data) throws IOException {

        PageElement page = new PageElement();
        page.addElement(new TextElement("3. Compilation Outcomes & Error Frequency", 16, true));

        PDImageXObject outcomeImg = PDImageXObject.createFromByteArray(document, ChartGenerator.generateCompileOutcomeChart(session), "compileOutcomeChart");
        page.addElement(new ImageElement(outcomeImg, chartWidth, 190));

        int total = Math.max(1, data.totalCompilations);
        double rate = 100.0 * data.successfulComp / total;
        page.addElement(new TextElement(String.format("Success rate: %.1f%% (%d of %d attempts).",rate, data.successfulComp, data.totalCompilations), 11, false));

        PDImageXObject errorImage = PDImageXObject.createFromByteArray(document, ChartGenerator.generateErrorFrequencyChart(session), "errorFrequencyChart");

        page.addElement(new ImageElement(errorImage, chartWidth, 190));

        page.addElement(new TextElement("Summary", 13, true));
        page.addElement(new TextElement("Failed compilations: " + data.failedCompilations + ". Total errors reported: " + data.totalErrors + ".",11, false));
        page.addElement(new TextElement("Dominant error categories reveal which concepts students struggle with most.",11, false));

        return page;

    }

    /** Page 4 - Project Timeline + summary */

    private PageElement buildTimelinePage(ReportData data) {

        PageElement page = new PageElement();
        page.addElement(new TextElement("4. Project Timeline", 16f, true));
        page.addElement(new TextElement("Class-lifecycle events in chronological order.", 11f, false));

        String[] headers = { "#", "Class", "Event", "Timestamp" };
        float[] widths   = { 30, 120, 220, 130 };
        List<String[]> rows = new ArrayList<>();

        int i = 1;
        for (ReportData.TimelineEvent e : data.projectTimeline) {
            rows.add(new String[] {String.valueOf(i++),safe(e.className),safe(e.event), safe(e.timeStamp)});

            /**Cap at 15 rows so the table always fits on one page. 
             * If a session produces more events than this we'd need a paged table, 
             * leaving that as a future extension.
             */

            if (rows.size() >= 15) break;  
        }

        if (rows.isEmpty()) {

            rows.add(new String[] { "-", "-", "No class events recorded", "-" });

        }

        page.addElement(new TableElement(headers, rows, widths));

        page.addElement(new TextElement("Summary", 13, true));
        page.addElement(new TextElement("Total class-lifecycle events logged: " + data.projectTimeline.size() + ".",11, false));
        page.addElement(new TextElement("Many MODIFIED events after CLASS_CREATED = iterative refinement; "+ "repeated REMOVED events = design changes.", 11, false));

        return page;

    }

    /** Utility methods */

    /** Replaces null strings with an empty string so they render cleanly. */
    private static String safe(String s) {

        return (s == null) ? "" : s;

    }

    /** Creates a fresh PDF page and renders the composite . */
    private void renderPage(PDDocument document, PageElement page) throws IOException {

        PDPage physical = new PDPage();
        document.addPage(physical);
        try (PDPageContentStream cs = new PDPageContentStream(document, physical)) {page.render(cs, pageTopY);}

    }
}
