package bluej_extension;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class TableElement implements ReportElement{

    private final String[] headers;
    private final List<String[]> rows;
    private final float[] widths;

    public TableElement(String[] headers, List<String[]> rows, float[] widths){

        this.headers = headers;
        this.rows = rows;
        this.widths = widths;

    }

    @Override
    public void render(PDPageContentStream stream, float startY) throws IOException {
        
        float total = 0;

        for(float w : widths){
            total += w;
        }

        float y = startY;

        drawRow(stream, y, headers, widths, true);
        drawLine(stream, y-23, total);
        y -= 20;

        for(String[] row : rows){
            
            drawRow(stream, y, row, widths, false);
            drawLine(stream, y, total);
            y -= 20;

        }
        
    }

    private void drawRow(PDPageContentStream stream,float y, String [] cells, float[] widths, boolean header) throws IOException {
        
        float cx= 50;

        for(int i=0; i<cells.length && i<widths.length; i++){
            stream.beginText();
            stream.setFont(header ? PDType1Font.HELVETICA_BOLD : PDType1Font.HELVETICA, 12);
            stream.newLineAtOffset(cx + 2, y - 15);
            stream.showText(truncateText(cells[i],widths[i]));
            stream.endText();
            cx += widths[i];
        }
    }

    private void drawLine(PDPageContentStream stream, float y, float totalWidth) throws IOException {

        stream.moveTo(50, y);
        stream.lineTo(50 + totalWidth, y);
        stream.stroke();

    }


    private String truncateText(String s, float width) {
        if (s == null) return "";
        int max = (int)(width / 5.5f);
        return s.length() > max ? s.substring(0, Math.max(0, max - 2)) + ".." : s;
    }

    @Override
    public float getHeight() {
        return 20 + (rows.size() + 1) * 20; // 20 for header, 20 for each row
    }


}