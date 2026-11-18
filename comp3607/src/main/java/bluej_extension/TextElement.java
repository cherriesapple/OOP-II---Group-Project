package bluej_extension;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class TextElement implements ReportElement {

    private final String text;
    private final float fontSize;
  


    public TextElement(String text){

        this.text = text;
        this.fontSize = 14;
        
    }

    @Override
    public void render(PDPageContentStream stream, float startY) throws IOException {
        stream.beginText();
        stream.setFont(PDType1Font.HELVETICA_BOLD, 14);
        stream.newLineAtOffset(50, startY);
        stream.showText(text);
        stream.endText();
    }

     @Override 
     public float getHeight() { 
        return fontSize * 1.4f; 
    }

    

}
