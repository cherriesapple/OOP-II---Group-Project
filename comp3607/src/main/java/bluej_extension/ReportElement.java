package bluej_extension;

import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

public interface ReportElement {
    void render(PDPageContentStream stream, float startY) throws IOException;
    float getHeight();
}
