package bluej_extension;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class ImageElement implements ReportElement {
    
    private final PDImageXObject image;
    private final float width;
    private final float height;

    public ImageElement(PDImageXObject image, float width, float height) {

        this.image = image;
        this.width = width;
        this.height = height;

    }

    @Override
    public void render(PDPageContentStream stream, float startY) throws IOException {
        stream.drawImage(image, 50, startY - height, width, height);
    }

    @Override public float getHeight() {
         return height; 
        }
}
