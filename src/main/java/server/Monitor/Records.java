package server.Monitor;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by de.faust on 12.12.2015.
 */
public class Records{
    private ObservableList<Record> status = FXCollections.observableArrayList();
    private Map<Integer, Record> map = new HashMap<>();

    public void put(String str1, double i, String str2, int indexSocket){
        Platform.runLater(() -> {
            map.put(indexSocket, new Record(str1, i, str2));
            status.setAll(map.values());
        });
        try {
            Thread.currentThread().sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void del(int indexSocket){
        Platform.runLater(() -> {
            map.remove(indexSocket);
            status.setAll(map.values());
        });
        try {
            Thread.currentThread().sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Record> getStatus(){
        return status;
    }
}
