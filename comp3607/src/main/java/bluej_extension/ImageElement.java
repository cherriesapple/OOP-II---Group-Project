package bluej_extension;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class ImageElement implements ReportElement {
    
    private final PDImageXObject image;
   float height = 300;

    public ImageElement(PDImageXObject image) {
        this.image = image;
    }

    @Override
    public void render(PDPageContentStream stream, float startY) throws IOException {
        stream.drawImage(image, 50, startY - 300, 500, 300);
    }

    @Override public float getHeight() {
         return height; 
        }
}
