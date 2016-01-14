package server.Monitor;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class HeapList {
    private ObservableList<String> eventMessage = FXCollections.observableArrayList();

    public void add(String string){
        Platform.runLater(() -> {
            eventMessage.add(0,string);
        });
        }

    public void del(String string){
        Platform.runLater(() -> {
        eventMessage.remove(string);
        });
    }

    public ObservableList<String> getEventMessage(){
        return eventMessage;
    }
}
