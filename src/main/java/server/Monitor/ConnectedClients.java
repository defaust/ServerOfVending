package server.Monitor;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ProgressIndicator;


import java.util.HashMap;
import java.util.Map;

public class ConnectedClients{
    private ObservableList<CClient> status = FXCollections.observableArrayList();
    private Map<Integer, CClient> maps = new HashMap<>();

    public void set(int indexSocket, String imei){
        Platform.runLater(() -> {
            maps.put(indexSocket, new CClient(imei));
            status.setAll(maps.values());
        });
        try {
            Thread.currentThread().sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void add(int indexSocket, String imei, Double current){
        Platform.runLater(() -> {
            maps.put(indexSocket, new CClient(imei, current));
            status.setAll(maps.values());
        });
        try {
            Thread.currentThread().sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void del(int indexSocket){
        Platform.runLater(() -> {
            maps.remove(indexSocket);
            status.setAll(maps.values());
        });
        try {
            Thread.currentThread().sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<CClient> getStatus(){
        return status;
    }
}