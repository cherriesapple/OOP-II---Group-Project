package bluej_extension;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 * Simple fixed-width table: one header row, then data rows separated by horizontal rules. 
 * Column widths are passed in so callers can match the table to the page geometry.
 *
 * Deliberately basic with no word wrap, no cell background, no spanning.
 * Anything fancier would be overkill for the timeline page.
 *  @author Malik Blackman 
 */
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

        /** Total width is needed twice (for the lines under each row),
        * so cache it once instead of recomputing in the loop.
        */
        
        float total = 0;

        for(float w : widths){
            total += w;
        }

        /**Header row */

        drawRow(stream, startY, headers, widths, true);
        drawLine(stream, startY-23, total);
        startY -= 20;

        /** Data rows */

        for(String[] row : rows){
            
            drawRow(stream, startY, row, widths, false);
            drawLine(stream, startY, total);
            startY -= 20;

        }
        
    }

    /**
     * Draws a single row of cells. Text is nudged down slightly (y - 15)
     * so it sits visually centred inside the 20pt row.
     */
    private void drawRow(PDPageContentStream stream,float y, String [] cells, float[] widths, boolean header) throws IOException {
        
        float currentX= 50;

        for(int i=0; i<cells.length && i<widths.length; i++){
            stream.beginText();
            stream.setFont(header ? PDType1Font.HELVETICA_BOLD : PDType1Font.HELVETICA, 12);
            stream.newLineAtOffset(currentX + 2, y - 15);
            stream.showText(truncateText(cells[i],widths[i]));
            stream.endText();
            currentX += widths[i];
        }
    }

    private void drawLine(PDPageContentStream stream, float y, float totalWidth) throws IOException {

        stream.moveTo(50, y);
        stream.lineTo(50 + totalWidth, y);
        stream.stroke();

    }


    /**
     * Rough character-count truncation. 5.5 is an empirical average character width for 12pt Helvetica; 
     * good enough to stop columns spilling into each other without needing proper text metrics.
     */

    public String truncateText(String s, float width) {
        if (s == null) return "";
        int max = (int)(width / 5.5f);
        return s.length() > max ? s.substring(0, Math.max(0, max - 2)) + ".." : s;
    }

    @Override
    public float getHeight() {

        /** Header row plus every data row, plus a little extra space */
        return 20 + (rows.size() + 1) * 20; 

    }


}