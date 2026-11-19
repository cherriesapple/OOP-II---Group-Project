package bluej_extension;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class serialises the contents of session into a JSON file. 
 * 
 * @author Shurona Jackson
 */
public class JsonExporter {

    /**
     * Method ensures the target directory exists before attempting to write to the file.
     * 
     * If the directory does not exist, one is created to prevent a thrown exception during the export process. 
     * 
     * @param session Contains all the telemtry data that is to be exported. 
     * 
     */
    public void export(SessionModel session)
        throws JsonGenerationException, JsonMappingException, IOException {

        File folder = new File("src/files");

        if (!folder.exists()) {
            folder.mkdirs();
    }

    File file = new File(folder, "Session_Data.json");

    ObjectMapper mapper = new ObjectMapper();
    mapper.writerWithDefaultPrettyPrinter().writeValue(file, session);
    
    }
}