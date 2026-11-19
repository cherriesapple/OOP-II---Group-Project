package bluej_extension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TableElementTest {

    private TableElement tableElement;
    private String[] headers;
    private List<String[]> rows;
    private float[] widths;

    @BeforeEach
    void setUp() {
        headers = new String[]{"Class Name", "Event Type"};
        rows = new ArrayList<>();
        widths = new float[]{200f, 300f};
        
        rows.add(new String[]{"Class_1", "CREATED"});
        rows.add(new String[]{"Class_2", "MODIFIED"});
        rows.add(new String[]{"Class_3", "DELETED"});

        tableElement = new TableElement(headers, rows, widths);
    }

    @Test
    void testGetHeight() {
        float expectedHeight = 100f;
        assertEquals(expectedHeight, tableElement.getHeight());
    }

    @Test
    void testGetHeight_EmptyTable() {
        TableElement emptyTable = new TableElement(headers, new ArrayList<>(), widths);
        assertEquals(40f, emptyTable.getHeight(), "Height should work for an empty table");
    }

    @Test
    void testTruncateText_LongText() {
       
        String longText = "This is a very long class name that should be truncated";
        
        String result = tableElement.truncateText(longText, 50f);
        
        assertTrue(result.endsWith(".."), "Long text should end with '..'");
        assertEquals(9, result.length(), "Result should be exactly the max character count");
    }

    @Test
    void testTruncateText_NullText() {
        
        String result = tableElement.truncateText(null, 200f);
        assertEquals("", result, "Null text should return an empty string");
    }
}