package bluej_extension;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;










@JsonPropertyOrder({
    "schema version", "status", "user_id", "session_length_minutes",
    "active", "idle_minutes", "total_compilations", "total_errors",
    "session_data", "class_lifecycle", "events", "derived"
})
public class SessionModel {

    @JsonProperty("schema version")
    public String schemaVersion = "1.0";

    @JsonProperty("status")
    public String status = "IN PROGRESS";// flips to "COMPLETE" at close

    @JsonProperty("user_id")
    public String userId = null;// placeholder, filled by student

    @JsonProperty("session_length_minutes")
    public double sessionLengthMinutes = 0.0;

    @JsonProperty("active")
    public String active = "yes";// string per template

    @JsonProperty("idle_minutes")
    public double idleMinutes = 0.0;

    @JsonProperty("total_compilations")
    public int totalCompilations = 0;

    @JsonProperty("total_errors")
    public int totalErrors = 0;

    @JsonProperty("session_data")
    public SessionData sessionData = new SessionData();

    @JsonProperty("class_lifecycle")
    public List<ClassLifecycleEvent> classLifecycle = new ArrayList<>();

    @JsonProperty("events")
    public List<TelemetryEvent> events = new ArrayList<>();

    @JsonProperty("derived")
    public Derived derived = new Derived();

    public static class SessionData {

        @JsonProperty("session_id")               
        public String sessionId = null;

        @JsonProperty("project_name")             
        public String projectName = null;

        @JsonProperty("start_time")               
        public String startTime = null;// ISO-8601

        @JsonProperty("end_time")                 
        public String endTime = null;

        @JsonProperty("idle_threshold_seconds")   
        public int idleThresholdSeconds = 180;

        @JsonProperty("successful_compilations")  
        public int successfulCompilations = 0;

        @JsonProperty("failed_compilations")      
        public int failedCompilations = 0;

        @JsonProperty("total_runs")               
        public int totalRuns = 0;

        @JsonProperty("total_exceptions")         
        public int totalExceptions = 0;

    }


    public static class ClassLifecycleEvent {

        @JsonProperty("class_name") 
        public String className;

        @JsonProperty("event")      
        public String event;// "CREATED" / "MODIFIED" / "REMOVED"

        @JsonProperty("timestamp")  
        public Timestamp timestamp;

    }

    public static class TelemetryEvent {

        @JsonProperty("type")      
        public String type;// "COMPILE", "RUN", "EDIT_BURST"

        @JsonProperty("timestamp") 
        public Timestamp timestamp;

        @JsonProperty("data")      
        public Map<String, Object> data = new LinkedHashMap<>();

    }

    public static class Derived {

        @JsonProperty("compile_intervals_ms")    
        public List<Long> compileIntervalsMs = new ArrayList<>();

        @JsonProperty("mean_compile_interval_ms") 
        public long meanCompileIntervalMs = 0;

        @JsonProperty("compile_success_rate")    
        public double compileSuccessRate = 0.0;

        @JsonProperty("error_breakdown")         
        public List<ErrorEntry> errorBreakdown = new ArrayList<>();

        @JsonProperty("error_category_totals")   
        public Map<String, Integer> errorCategoryTotals = new LinkedHashMap<>();

        @JsonProperty("class_timeline")          
        public List<ClassLifecycleEvent> classTimeline = new ArrayList<>();

        @JsonProperty("activity_timeline")       
        public ActivityTimeline activityTimeline = new ActivityTimeline();

    }

     public static class ErrorEntry {

        @JsonProperty("timestamp")   
        public Timestamp timestamp;

        @JsonProperty("category")     
        public String category;

        @JsonProperty("message")      
        public String message;

        @JsonProperty("line_number")  
        public int lineNumber;

    }

    public static class ActivityTimeline {

        @JsonProperty("bucket_minutes")       
        public int bucketMinutes = 3;

        @JsonProperty("total_edit_signals")   
        public int totalEditSignals = 0;

        @JsonProperty("buckets")              
        public List<Bucket> buckets = new ArrayList<>();

    }

    public static class Bucket {

        @JsonProperty("bucket_index")    
        public int bucketIndex;

        @JsonProperty("bucket_start")     
        public String bucketStart;   // ISO-8601

        @JsonProperty("edit_signals")    
        public int editSignals;

        @JsonProperty("active_seconds")   
        public long activeSeconds;

    }
}
