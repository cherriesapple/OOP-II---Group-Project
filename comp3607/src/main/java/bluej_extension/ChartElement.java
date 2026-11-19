package bluej_extension;

/** @author Malik Blackman */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * Static helpers that turn session data into chart PNGs (as byte arrays),
 * ready to be wrapped in an ImageElement and dropped onto a page.
 *
 * JFreeChart is used for the rendering; every method ends up calling
 * {@link #toPng(JFreeChart)} so the export settings stay consistent.
 */

public class ChartElement {

    /**
     * Placeholder bar chart with fixed values - kept for quick testing of
     * the PDF layout. The real error-frequency chart is {@link #generateErrorFrequencyChart(SessionModel)}.
     */
        public static byte[] generateErrorChart(SessionModel session) throws IOException {
        
        
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(5, "Errors", "Syntax Error");
        dataset.addValue(2, "Errors", "Missing Symbol");
        dataset.addValue(8, "Errors", "Type Mismatch");

        JFreeChart barChart = ChartFactory.createBarChart(
                "Compilation Error Frequency", 
                "Error Type",                  
                "Count",                       
                dataset,
                PlotOrientation.VERTICAL,
                false, false, false
        );

        return toPng(barChart);
    }

    /**
     * Area chart showing how edit signals are distributed across time.
     * If the tracker never recorded anything (empty buckets) we add a single zero point so JFreeChart doesn't throw on an empty dataset.
     */
    public static byte[] generateActivityChart(SessionModel session) throws IOException {
        XYSeries series = new XYSeries("Edit signals");
        SessionModel.ActivityTimeline tl = session.derived.activityTimeline;
        if (tl == null || tl.buckets == null || tl.buckets.isEmpty()) {
            series.add(0, 0);
        } else {
            for (SessionModel.Bucket b : tl.buckets) series.add(b.bucketIndex, b.editSignals);
        }
        JFreeChart chart = ChartFactory.createXYAreaChart(
                "User Activity Over Session",
                "Time (" + (tl != null ? tl.bucketMinutes : 3) + "-min buckets)",
                "Edit signals",
                new XYSeriesCollection(series),
                PlotOrientation.VERTICAL, false, false, false);
        return toPng(chart);
    }

    /**
     * Side-by-side bar chart of successful vs failed compilation attempts.
     * Uses the aggregate counters from SessionData,simplest source and already populated by CompileTracker.populateSession().
     */
    public static byte[] generateCompileOutcomeChart(SessionModel session) throws IOException {
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        ds.addValue(session.sessionData.successfulCompilations, "Compiles", "Successful");
        ds.addValue(session.sessionData.failedCompilations,     "Compiles", "Failed");
        JFreeChart chart = ChartFactory.createBarChart(
                "Successful vs Failed Compilations",
                "Outcome", "Count", ds,
                PlotOrientation.VERTICAL, false, false, false);
        return toPng(chart);
    }

    /**
     * Error-frequency chart driven by the categorised error totals (syntax,type mismatch, missing symbol, etc.). 
     * Falls back to a single "No errors" bar so the chart still renders on a clean session.
     */
    public static byte[] generateErrorFrequencyChart(SessionModel session) throws IOException {
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        Map<String, Integer> cats = session.derived.errorCategoryTotals;
        if (cats == null || cats.isEmpty()) {
            ds.addValue(0, "Errors", "No errors");
        } else {
            for (Map.Entry<String, Integer> e : cats.entrySet())
                ds.addValue(e.getValue(), "Errors", e.getKey());
        }
        JFreeChart chart = ChartFactory.createBarChart(
                "Compilation Error Frequency",
                "Error Category", "Count", ds,
                PlotOrientation.VERTICAL, false, false, false);
        return toPng(chart);
    }


     /** Shared PNG export - using the same dimensions for every chart keeps the layout predictable. */

    private static byte[] toPng(JFreeChart chart) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(out, chart, 500, 300);
        return out.toByteArray();
    }

}