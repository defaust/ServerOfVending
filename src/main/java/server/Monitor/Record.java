package server.Monitor;

import javafx.collections.ObservableList;
import javafx.scene.control.ProgressBar;

import java.util.Observable;

/**
 * Created by de.faust on 12.12.2015.
 */
public class Record{
    private String callsign;
    private ProgressBar progressBar;
    private String message;

    public Record(String callsign, double i, String message) {
        this.callsign = callsign;
        this.progressBar = new ProgressBar(i);
        this.progressBar.setStyle(" -fx-progress-color: darkgreen;");
        this.message = message;
    }

    public String getCallsign() {
        return callsign;
    }

    public void setCallsign(String callsign) {
        this.callsign = callsign;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(double i) {
        progressBar.setProgress(i);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
