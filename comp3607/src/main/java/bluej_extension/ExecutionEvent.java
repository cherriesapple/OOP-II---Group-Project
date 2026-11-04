package bluej_extension;
import java.sql.Timestamp;


public class ExecutionEvent{

    private Timestamp timestamp;
    private String outputSummary;
    private int numExecutions;

    public ExecutionEvent(String outputSummary){
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.outputSummary = outputSummary;
        //this.numExecutions = 1;
    }
    public String getType(){return "PROGRAM_EXEUTED";}
    public Timestamp getTimestamp(){return this.timestamp;}


}
