package bluej_extension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.LongSupplier;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import bluej.extensions2.editor.JavaEditor;
import bluej.extensions2.editor.TextLocation;
import bluej.extensions2.event.CompileEvent;
import bluej_extension.SessionModel.TelemetryEvent;
import javafx.application.Platform;

public class CompilerTest {


    @Test
    void idleGapOver180sCountsAsIdle() {
        SessionModel session = new SessionModel();
        ActivityTracker tracker = new ActivityTracker(session);

        JavaEditor editor = mock(JavaEditor.class);
        when(editor.getTextLength()).thenReturn(0);
        when(editor.getCaretLocation()).thenReturn(new TextLocation(0, 0));
        tracker.beginTracker(editor);

        // edit at 1s, then at 301s (300s gap = idle)
        tracker.editLog(TimeUnit.SECONDS.toNanos(1));
        tracker.editLog(TimeUnit.SECONDS.toNanos(301));
        tracker.stopTracker();

        assertEquals(300.0 / 60.0, session.idleMinutes, 0.01);
    }


}
