package bluej_extension;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonExporter {

    public void export(SessionModel session)
        throws JsonGenerationException, JsonMappingException, IOException {

    File folder = new File("src/files");

    if (!folder.exists()) {
        folder.mkdirs();
    }

    File file = new File(folder, "Session_Data.json");

    ObjectMapper mapper = new ObjectMapper();
    mapper.writerWithDefaultPrettyPrinter().writeValue(file, session);

    System.out.println("JSON saved to: " + file.getAbsolutePath());
    
    }
}