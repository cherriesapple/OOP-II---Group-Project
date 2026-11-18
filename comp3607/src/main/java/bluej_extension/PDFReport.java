/**package bluej_extension;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import java.io.IOException;

public class PdfReport {

    public void generateReport(CompileTracker tracker, String filePath) throws IOException {
        
        // 1. Generate the chart bytes (from JFreeChart)
        byte[] chartBytes = ChartGenerator.generateErrorChart(tracker);

        // 2. Open the PDF document
        try (PDDocument doc = new PDDocument()) {
            
            // 3. Convert bytes to PDF Image
            PDImageXObject pdfImage = PDImageXObject.createFromByteArray(doc, chartBytes, "chart");

            // 4. BUILD THE COMPOSITE TREE
            PdfPage page1 = new PdfPage();
            page1.addElement(new TextElement("Compilation Report"));
            page1.addElement(new TextElement("Total Compiles: " + tracker.getNumCompiles()));
            page1.addElement(new ImageElement(pdfImage));

            // 5. Create the physical PDF page and render the composite tree onto it
            PDPage physicalPage = new PDPage();
            doc.addPage(physicalPage);
            
            try (PDPageContentStream stream = new PDPageContentStream(doc, physicalPage)) {
                // This single line renders the text AND the image!
                page1.render(stream, 700); 
            }

            // 6. Save the PDF
            doc.save(filePath);
            System.out.println("PDF saved to: " + filePath);
        }
    }
}
**/