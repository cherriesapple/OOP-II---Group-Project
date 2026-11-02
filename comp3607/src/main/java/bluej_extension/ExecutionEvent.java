package bluej_extension;

public class ExecutionEvent implements InvokationListener {

    private long timestamp = System.currentTimeMillis();
    private String outputSummary;
    private int numExecutions;

    public ExecutionEvent(long timestamp, String outputSummary){
        this.timestamp = timestamp;
        this.outputSummary = outputSummary;

    }
    public String getType(){return "PROGRAM_EXEUTED";}
    public long getTimestamp(){return this.timestamp;}

    public void invocationFinished (ExecutionEvent event){
        numExecutions++;
    }

}
