package bluej_extension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImageElementTest {

    private ImageElement imageElement;

    @BeforeEach
    void setUp() {
        imageElement = new ImageElement(null, 500f, 300f);
    }

    @Test
    void testGetHeight() {
        assertEquals(300f, imageElement.getHeight());
    }
}