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

    public CompileTracker(SessionModel session){
        this.session = session;
    }

     private void addTelEvent(String type, Map<String, Object> data){
        telEvent = new TelemetryEvent();

        telEvent.timestamp = new Timestamp(System.currentTimeMillis());
        telEvent.type = type;
        telEvent.data = data;
        session.events.add(telEvent);
        
    }

    private long duration(){
        return compileStart == null ? 0 : System.currentTimeMillis() - compileStart.getTime();
    }
 
    private String categorize(String message){
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

    @Override 
    public void compileError(CompileEvent event){

        numCompileErrors++;
        compileErrors.add(new ErrorRecord(event.getErrorLineNumber(),event.getErrorMessage(),event.getErrorPosition(), new Timestamp(System.currentTimeMillis())));
    }
    @Override 
    public void compileWarning(CompileEvent event){

        compileWarnings.add(new ErrorRecord(event.getErrorLineNumber(),event.getErrorMessage(),event.getErrorPosition(), new Timestamp(System.currentTimeMillis())));
    }

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

    public Timestamp getCompileStart() {
        return compileStart;
    }

    public int getNumCompiles() {
        return numCompiles;
    }

    public int getNumCompileErrors() {
        return compileErrors.size();
    }

    public int getNumCompileWarnings() {
        return compileWarnings.size();
    }

    public int getNumCompileSuccessful() {
        return numSuccessfulCompiles;
    }

    public int getNumCompileFailed() {
        return numFailedCompiles;
    }

    public List<File[]> getCompileSuccessfulFiles() {
        return compileSuccessfulFiles;
    }

    public List<File[]> getCompileFailedFiles() {
        return compileFailedFiles;
    }

    public List<ErrorRecord> getCompileErrors() {
        return compileErrors;
    }

    public List<ErrorRecord> getCompileWarnings() {
        return compileWarnings;
    }

}