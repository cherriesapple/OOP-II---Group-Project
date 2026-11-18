package bluej_extension;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

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
        stream.newLineAtOffset(50, startY);
        stream.showText(text);
        stream.endText();
    }

     @Override 
     public float getHeight() { 
        return fontSize * 1.4f; 
    }

    

}
