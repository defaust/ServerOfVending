package Main;

import FXController.MainController;
import client.Client;
import database.DataBase;
import database.UpdVal;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import server.SocketObserver;

import java.util.Random;

public class MainApp extends Application {
    private DataBase dataBase;
    private SocketObserver socketObserver;

    private MainController controller;
    private FXMLLoader loader;
    private Stage stage;

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                Platform.exit();
                System.exit(0);
            }
        });

        String fxmlFile = "/fxml/scene.fxml";
        loader = new FXMLLoader();
        Parent root = (Parent)loader.load(getClass().getResourceAsStream(fxmlFile));
        stage.setTitle("Observer");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();

        dataBase = new DataBase(30000); //Size of Buffer;

        socketObserver = new SocketObserver(13090, 5);
        socketObserver.setName("Main socket");
        socketObserver.setDB(dataBase);

        controller = loader.getController();

        dataBase.setUpdList(new UpdVal() {
            @Override
            public void update(int a1) {
                controller.updValBufferInfo(a1);
            }
        }, new UpdVal() {
            @Override
            public void update(int a1) {
                controller.upValBufferWarning(a1);
            }
        });

        controller.setEventHeap(socketObserver.getHeapList(), socketObserver.getRecords(), socketObserver.getConnected());

        socketObserver.start();

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {

                Client[] client = new Client[7];
                for (int i = 0; i < client.length; i++) {
                    try {
                        Thread.currentThread().sleep(new Random().nextInt(1500));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    new Client("127.0.0.1", 13090).start();
                }
            }
        });
        th.start();
    }
}