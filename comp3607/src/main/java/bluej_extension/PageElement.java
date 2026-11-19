package bluej_extension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDPageContentStream;

/**
 * Composite node — holds a list of child elements and renders them top to bottom.
 * Spacing between consecutive children is a fixed 10pt,
 * which keeps the layout readable without needing a proper layout engine.
 *
 * Because PageElement is itself a ReportElement, pages can technically be nested. 
 * We don't use that here but the pattern allows it.
 * 
 * @author Malik Blackman
 */
public class PageElement implements ReportElement {

    private List<ReportElement> elements = new ArrayList<>();

    public void addElement(ReportElement element) {
        elements.add(element);
    }

    @Override
    public void render(PDPageContentStream stream, float startY) throws IOException {
        float currentY = startY;

        /** Walk the tree in insertion order. Each child reports its own height,
         * so we don't need to know in advance what kind of element it is (text, image, table, etc.). 
         */
    
        for (ReportElement element : elements) {
            element.render(stream, currentY);
            currentY -= element.getHeight() + 10; 
        }
    }

    @Override
    public float getHeight() {

        /** Used when a page is nested inside another page,
         * not exercised in the current report, but required by the interface.
         */

        float height = 0;
        for (ReportElement element : elements) {
            height += element.getHeight() + 10;
        }
        return height;

    }
}
