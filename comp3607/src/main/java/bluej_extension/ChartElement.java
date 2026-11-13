package bluej_extension;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

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
}