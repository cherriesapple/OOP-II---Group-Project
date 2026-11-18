package bluej_extension;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class TextElement implements ReportElement {

    private final String text;
    private final float fontSize, x;
    private final boolean bold;

    public TextElement(String text) {
         this.text = text; 
    }
    public TextElecment(String text,float fontsize,boolean bold,float x){
        this.text = text;
        this.fontSize = fontSize;
        this.bold = bold;
        this.x= x;
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
        return fontSize * 1.4; 
    }


}
