package bluej_extension;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import bluej.extensions2.event.CompileEvent;
import bluej.extensions2.event.CompileListener;

public class CompileTracker implements CompileListener{

    public record ErrorRecord(int errorLine,String errorMessage,int[] errorPosition){}
    private int numCompiles = 0;
    private int numSuccessfulCompiles = 0;
    private int numFailedCompiles = 0;
    Timestamp compileStart;

    
    private final List<ErrorRecord> compileErrors = new ArrayList<>();
    private final List<ErrorRecord> compileWarnings = new ArrayList<>();
    private final List<File[]> compileSuccessfulFiles = new ArrayList<>();
    private final List<File[]> compileFailedFiles = new ArrayList<>();

    @Override 
    public void compileStarted(CompileEvent event) {
        compileStart = new Timestamp(System.currentTimeMillis());     
    }

    @Override 
    public void compileError(CompileEvent event){

        compileErrors.add(new ErrorRecord(event.getErrorLineNumber(),event.getErrorMessage(),event.getErrorPosition()));
    }
    @Override 
    public void compileWarning(CompileEvent event){

        compileWarnings.add(new ErrorRecord(event.getErrorLineNumber(),event.getErrorMessage(),event.getErrorPosition()));
    }

    @Override 
    public void compileSucceeded(CompileEvent event){
        numCompiles++;
        numSuccessfulCompiles++;
        compileSuccessfulFiles.add(event.getFiles());
    }

    @Override 
    public void compileFailed(CompileEvent event){
        numCompiles++;
        numFailedCompiles++;
        compileFailedFiles.add(event.getFiles());
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