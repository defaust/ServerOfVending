package server.Monitor;

import javafx.scene.control.ProgressIndicator;

/**
 * Created by de.faust on 15.12.2015.
 */
public class CClient {
    private String imei;
    private ProgressIndicator progressIndicator;


    public CClient(String imei){
        this.imei = imei;
        progressIndicator = null;
    }

    public CClient(String imei, Double current) {
        this.imei = imei;
        this.progressIndicator = new ProgressIndicator(-1);
        this.progressIndicator.setPrefHeight(15);
        this.progressIndicator.setStyle(" -fx-progress-color: red;");
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public ProgressIndicator getProgressIndicator() {
        return this.progressIndicator;
    }

    public void setProgressIndicator(ProgressIndicator progressIndicator) {
        this.progressIndicator = progressIndicator;
    }

    public void setProgress(double current) {
        progressIndicator.setProgress(current);
    }
}
