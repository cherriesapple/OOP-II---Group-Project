package bluej_extension;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bluej.extensions2.event.CompileEvent;
import bluej.extensions2.event.CompileListener;
import bluej_extension.SessionModel.TelemetryEvent;

/**
 * This class is used by the extension to listen to all the compile changes made within the bluej environment
 * 
 * @author Zuri Boucaud
 */
public class CompileTracker implements CompileListener{

    public record ErrorRecord(int errorLine,String errorMessage,int[] errorPosition, Timestamp timestamp){}
    private int numCompiles = 0;
    private int numSuccessfulCompiles = 0;
    private int numFailedCompiles = 0;
    private Timestamp compileStart;
    private final SessionModel session;
    private TelemetryEvent telEvent;

    private Long lastCompStartMs = null;                
    private int numCompileErrors = 0;                     

    private final List<Long> compileIntervalsMs = new ArrayList<>();
    
    private final List<ErrorRecord> compileErrors = new ArrayList<>();
    private final List<ErrorRecord> compileWarnings = new ArrayList<>();
    private final List<File[]> compileSuccessfulFiles = new ArrayList<>();
    private final List<File[]> compileFailedFiles = new ArrayList<>();

    /**
     * Creates a compile tracker. 
     * 
     * @param session is used to store telemetry data.
     */
    public CompileTracker(SessionModel session){
        this.session = session;
    }

    /**
     * Updates the session events with the compilation event's timestamp, type and relevant information
     * 
     * @param type Event type is included in session.
     * @param data Variable used to store any amount of additional information, stored in key value pairs. 
     */
    public void addTelEvent(String type, Map<String, Object> data){
        telEvent = new TelemetryEvent();

        telEvent.timestamp = new Timestamp(System.currentTimeMillis());
        telEvent.type = type;
        telEvent.data = data;
        session.events.add(telEvent);
        
    }

    /**
     * Calculates the length of time taken by a single compilation event.
     * 
     * @return Returns 0 or null if the compilation just started, else it returns the duration of the compilation.  
     */
    public long duration(){
        return compileStart == null ? 0 : System.currentTimeMillis() - compileStart.getTime();
    }
 

    public String categorize(String message){
        if(message == null){
            return "other";
        }
        String mCommon = message.toLowerCase();

        if(mCommon.contains("cannot find symbol") || mCommon.contains("symbol not found")){
            return "missing_symbol";
        }
        if(mCommon.contains("incompatible types") || mCommon.contains("cannot be converted")){
            return "type_mismatch";
        }
        if(mCommon.contains("; expected") || mCommon.contains("illegal start") || mCommon.contains("reached end of file")){
            return "syntax";
        }
        if(mCommon.contains("might not have been initialized")){
            return "uninitialized";
        }
        if(mCommon.contains("missing return")){
            return "missing_return";
        }
        return "other";
    }

    /**
     * Method provided by bluej API that is invoked when a compilation attempt is initiated. 
     * 
     * The timestamp of the event is recorded and the difference between that timestamp and the most recent compilation is stored in an array. This keep tracks of the intervals between compiilation attempts. 
     * 
     * @param event This is an event that is triggered when either a user initiates a compilation.
     * 
     */
    @Override 
    public void compileStarted(CompileEvent event) {
        long time = System.currentTimeMillis();

        compileStart = new Timestamp(time);

        if(lastCompStartMs != null){
            compileIntervalsMs.add(time - lastCompStartMs);
        }
        lastCompStartMs = time;

        Map<String, Object> data = new HashMap<>();

        data.put("status", "STARTED");
        addTelEvent("COMPILE", data);
    }

    /**
     * Method invoked when a compulation error is detected.
     * 
     * This method updates a list of compilation errors including the line number the error occured on, the error message received, the error's position and the time it was detected.
     * As well as counts the number of compilation errors detected
     * @param event This is an event that is triggered when either a user initiates a compilation.
     */
    @Override 
    public void compileError(CompileEvent event){

        numCompileErrors++;
        compileErrors.add(new ErrorRecord(event.getErrorLineNumber(),event.getErrorMessage(),event.getErrorPosition(), new Timestamp(System.currentTimeMillis())));
    }

    /**
     * Method invoked when a compile warning is generated.
     * 
     * This method updates list of compilation errors including the line number the error occured on, the error message received, the error's position and the time it was detected.
     * @param event This is an event that is triggered when either a user initiates a compilation.
     * 
     */
    @Override 
    public void compileWarning(CompileEvent event){

        compileWarnings.add(new ErrorRecord(event.getErrorLineNumber(),event.getErrorMessage(),event.getErrorPosition(), new Timestamp(System.currentTimeMillis())));
    }

     /**
     * Method invoked when a compilation attempt was successful.
     * 
     * Method updates list of compile warnings received including the line number the error occured on, the error message received, the error's position and the time it was detected.
     * It counts the number of total and successful compilation attemps as well as stores a list of the classes that successfully compiled. 
     * 
     * Telemtry event is added to the session by invoking addTelEvent;
     * 
     * @param event This is an event that is triggered when either a user initiates a compilation.
     */
    @Override 
    public void compileSucceeded(CompileEvent event){
        numCompiles++;
        numSuccessfulCompiles++;
        compileSuccessfulFiles.add(event.getFiles());

        
        Map<String, Object> data = new HashMap<>();

        data.put("status", "SUCCESSFUL");
        data.put("duration", duration());
        data.put("numFiles", event.getFiles().length);
        data.put("Errors", numCompileErrors);
        addTelEvent("COMPILE", data);
    }

    /**
     * Method invoked when a compilation failed and increments the number of both total and failed compilation attemps. 
     * 
     * List of classes that failed the compilation is maintained and telEvent is invoked to update the session.
     */
    @Override 
    public void compileFailed(CompileEvent event){
        numCompiles++;
        numFailedCompiles++;
        compileFailedFiles.add(event.getFiles());

        Map<String, Object> data = new HashMap<>();

        data.put("status", "FAILED");
        data.put("duration", duration());
        data.put("numFiles", event.getFiles().length);
        data.put("Errors", numCompileErrors);
        addTelEvent("COMPILE", data);
    }

    /**
     * Updates the session with the data accumulated over the session duration. Including the totals for compilation attemps, errors, failed compilations and successful compilations
     */
    public void populateSession(){
        long total = 0;
        session.totalCompilations = numCompiles;
        session.totalErrors = numCompileErrors;
        session.sessionData.failedCompilations = numFailedCompiles;
        session.sessionData.successfulCompilations = numSuccessfulCompiles;

        SessionModel.Derived d = session.derived;
        d.compileIntervalsMs = new ArrayList<>(compileIntervalsMs);
        
        if(!compileIntervalsMs.isEmpty()){
            for(int x = 0; x < compileIntervalsMs.size(); x++){
                total += compileIntervalsMs.get(x);
            }
            d.meanCompileIntervalMs = total/compileIntervalsMs.size();
        }

        if(numCompiles != 0){
            d.compileSuccessRate = numSuccessfulCompiles/numCompiles;
        }

        d.errorBreakdown.clear();
        d.errorCategoryTotals.clear();

        for(ErrorRecord e : compileErrors){
            SessionModel.ErrorEntry entry = new SessionModel.ErrorEntry();
            entry.timestamp = e.timestamp;
            entry.category = categorize(e.errorMessage);
            entry.message = e.errorMessage;
            entry.lineNumber = e.errorLine;
            d.errorBreakdown.add(entry); 
        }

    }

    /**
     * Used to generate reports. 
     * 
     * @return Returns the time at which the compilation started.  
     */
    public Timestamp getCompileStart() {
        return compileStart;
    }

    /**
     * Used to generate reports. 
     * 
     * @return Returns the total number of compilation attempts.  
     */
    public int getNumCompiles() {
        return numCompiles;
    }

    /**
     * Used to generate reports. 
     * 
     * @return Returns the number of compilation errors.  
     */
    public int getNumCompileErrors() {
        return compileErrors.size();
    }

    /**
     * Used to generate reports. 
     * 
     * @return Returns the number of compilation warnings.  
     */
    public int getNumCompileWarnings() {
        return compileWarnings.size();
    }

    /**
     * Used to generate reports. 
     * 
     * @return Returns the number of successful compilation attempts.  
     */
    public int getNumCompileSuccessful() {
        return numSuccessfulCompiles;
    }

    /**
     * Used to generate reports. 
     * 
     * @return Returns the number of failed compilation attempts.  
     */
    public int getNumCompileFailed() {
        return numFailedCompiles;
    }

    /**
     * Used to generate reports. 
     * 
     * @return Returns the names of the classes whose compilations succeeded.  
     */
    public List<File[]> getCompileSuccessfulFiles() {
        return compileSuccessfulFiles;
    }

    /**
     * Used to generate reports. 
     * 
     * @return Returns the names of the classes whose compilations failed.  
     */
    public List<File[]> getCompileFailedFiles() {
        return compileFailedFiles;
    }

    /**
     * Used to generate reports. 
     * 
     * @return Returns a list of the compilation errors. 
     */
    public List<ErrorRecord> getCompileErrors() {
        return compileErrors;
    }

    /**
     * Used to generate reports. 
     * 
     * @return Returns a list of the compilation warnings. 
     */
    public List<ErrorRecord> getCompileWarnings() {
        return compileWarnings;
    }

}