package bluej_extension;

import bluej.extensions.event.InvocationListener;

public class Invocation implements InvocationListener{
     
    public void invocationFinished (ExecutionEvent event){
        numExecutions++;
    }

}