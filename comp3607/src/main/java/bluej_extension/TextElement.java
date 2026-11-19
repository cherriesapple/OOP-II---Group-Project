package bluej_extension;
/** @author Malik Blackman */
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 * A single line of text at a fixed x-position. Font size and weight are
 * passed in by the caller so the same class can be reused for headings,labels and body text.
 */
public class TextElement implements ReportElement {

    private final String text;
    private final float fontSize;
    private final boolean bold;
  


    public TextElement(String text, float fontsize, boolean bold) {

        this.text = text;
        this.fontSize = fontsize;
        this.bold = bold;
    }

    @Override
    public void render(PDPageContentStream stream, float startY) throws IOException {
        stream.beginText();
        stream.setFont(bold ? PDType1Font.HELVETICA_BOLD : PDType1Font.HELVETICA, fontSize);

        /**  All text elements are left-aligned at x=50. If centre-alignment is
         * ever needed, add a width parameter and compute the offset here.
         */

        stream.newLineAtOffset(50, startY);
        stream.showText(text);
        stream.endText();
    }

     @Override 
     public float getHeight() { 
        /**1.4 is roughly the line-height multiplier for Helvetica — good enough to keep consecutive lines from overlapping.  
         */
        return fontSize * 1.4f; 
    }

    

}
