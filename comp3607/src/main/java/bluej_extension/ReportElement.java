package bluej_extension;
/** @author Malik Blackman */

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDPageContentStream;

/**
 * Common interface for every drawable piece of the report (text, image,table, and the page itself). 
 * Part of the Composite pattern: a {@link PageElement} is just a {@code ReportElement} that contains other
 * {@code ReportElement}s, so pages can be rendered with the same call that renders a single line of text.
 */

public interface ReportElement {

    /**
     * Draws this element onto the given content stream.
     *
     * @param stream  the PDFBox stream for the current page
     * @param startY  y-coordinate of the element's top edge (PDF origin is bottom-left)
     */

    void render(PDPageContentStream stream, float startY) throws IOException;

    /**
     * Vertical space this element occupies, used by PageElement to work out where the next element should start.
     */
    float getHeight();
}
