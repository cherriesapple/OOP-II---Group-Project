package bluej_extension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDPageContentStream;

public class PageElement implements ReportElement {
    private List<ReportElement> elements = new ArrayList<>();

    public void addElement(ReportElement element) {
        elements.add(element);
    }

    @Override
    public void render(PDPageContentStream stream, float startY) throws IOException {
        float currentY = startY;
        
        for (ReportElement element : elements) {
            element.render(stream, currentY);
            currentY -= element.getHeight() + 10; 
        }
    }

    @Override
    public float getHeight() {
        float height = 0;
        for (ReportElement element : elements) {
            height += element.getHeight() + 10;
        }
        return height;
    }
}
