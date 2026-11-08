package bluej_extension;

import bluej.extensions.CompileListener;
import bluej.extensions.CompileEvent;

public class Compile implements CompileListener {

    private int numFailedCompiles;
    private int numSuccessfulCompiles;
     
    public void compileError (/*bluej proviedes this*/){
       
    }

    public void compileFailed (/*bluej proviedes this*/){
        CompileEvent compileFail= new CompileEvent();
        numFailedCompiles++;
    }

    public void compileStarted (/*bluej proviedes this*/){
        CompileEvent compileStart = new CompileEvent();
    }

    public void compileSucceeded (/*bluej proviedes this*/){
        CompileEvent compileSuccess = new CompileEvent();
        numSuccessfulCompiles++;
    }

    public void compileWarning (/*bluej proviedes this*/){
       CompileEvent compileWarn = new CompileEvent();
    }

}