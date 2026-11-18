package bluej_extension;

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

public class ChartElement {

    // Pass in your session data so you can draw the graph based on the numbers!
    public static byte[] generateErrorChart(SessionModel session) throws IOException {
        
        // 1. Prepare the data (Example: hardcoded for now, but you'll use session data)
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(5, "Errors", "Syntax Error");
        dataset.addValue(2, "Errors", "Missing Symbol");
        dataset.addValue(8, "Errors", "Type Mismatch");

        // 2. Create the chart
        JFreeChart barChart = ChartFactory.createBarChart(
                "Compilation Error Frequency", // Title
                "Error Type",                  // X-Axis
                "Count",                       // Y-Axis
                dataset,
                PlotOrientation.VERTICAL,
                false, false, false
        );

        // 3. Convert to byte array and return it!
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(stream, barChart, 500, 300);
        return stream.toByteArray();
    }

    // Chart 2: area chart of edit signals per time bucket. 
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

    // Chart 3: successful vs failed compilations. 
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

    // Chart 4: error frequency by category. 
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

    private static byte[] toPng(JFreeChart chart) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(out, chart, 500, 300);
        return out.toByteArray();
    }

}