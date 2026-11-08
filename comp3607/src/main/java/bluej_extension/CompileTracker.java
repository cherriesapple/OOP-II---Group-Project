package bluej_extension;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import bluej.extensions2.event.CompileEvent;

public class CompileTracker {
    public record ErrorRecord(int errorLine,String errorMessage,int[] errorPosition){}
    private int numCompiles = 0;
    private int numSuccessfulCompiles = 0;
    private int numFailedCompiles = 0;

    
    private final List<ErrorRecord> compileErrors = new ArrayList<>();
    private final List<ErrorRecord> compileWarnings = new ArrayList<>();
    private final List<File[]> compileSuccessfulFiles = new ArrayList<>();
    private final List<File[]> compileFailedFiles = new ArrayList<>();


    public void onCompileError(CompileEvent event){

        compileErrors.add(new ErrorRecord(event.getErrorLineNumber(),event.getErrorMessage(),event.getErrorPosition()));
    }

    public void onCompileWarning(CompileEvent event){

        compileWarnings.add(new ErrorRecord(event.getErrorLineNumber(),event.getErrorMessage(),event.getErrorPosition()));
    }

    public void onCompileSuccessful(CompileEvent event){
        numCompiles++;
        numSuccessfulCompiles++;
        compileSuccessfulFiles.add(event.getFiles());
    }

    public void onCompileFailed(CompileEvent event){
        numCompiles++;
        numFailedCompiles++;
        compileFailedFiles.add(event.getFiles());
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