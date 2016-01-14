package FXController;

import Main.MainApp;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import server.Monitor.CClient;
import server.Monitor.ConnectedClients;
import server.Monitor.Record;
import server.Monitor.Records;

import java.net.URL;
import java.util.ResourceBundle;


public class MainController implements Initializable{

    private final static String info = "Info";
    private final static String warning = "Warning";
    @FXML
    private TableView<CClient> connectedMain;
    @FXML
    private TableColumn<CClient, String> connectedIMEI;
    @FXML
    private TableColumn<CClient, ProgressIndicator> connectedProgress;
    @FXML
    private TableView<Record> tableMain;
    @FXML
    private TableColumn<Record, String> table1;
    @FXML
    private TableColumn<Record, ProgressBar> table2;
    @FXML
    private TableColumn<Record, String> table3;
    private XYChart.Data<String, Integer> bufferWarning;
    private XYChart.Data<String, Integer> bufferInfo;

    @FXML
    private BarChart chart;
    @FXML
    private ListView<String> heapList;

    public void initialize(URL location, ResourceBundle resources) {

        chart.setTitle("Буфер");

        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();

        bufferWarning = new XYChart.Data(warning, 0);
        bufferInfo = new XYChart.Data(info, 0);

        series1.setName("Предупреждения");
        series2.setName("Информация");

        series1.getData().add(bufferWarning);
        series2.getData().add(bufferInfo);

        chart.getData().addAll(series1, series2);
    }

    public void setEventHeap(ObservableList<String> heapList, Records records, ConnectedClients connectedClients){
        /* ********************************************************************************************** */
        this.heapList.setItems(heapList);
        /* ********************************************************************************************** */
        ObservableList<Record> recordsList = records.getStatus();
        tableMain.setItems(recordsList);
        table1.setCellValueFactory(new PropertyValueFactory<Record, String>("callsign"));
        table2.setCellValueFactory(new PropertyValueFactory<Record, ProgressBar>("progressBar"));
        table3.setCellValueFactory(new PropertyValueFactory<Record, String>("message"));
        /* ********************************************************************************************** */
        ObservableList<CClient> connectedList = connectedClients.getStatus();
        connectedMain.setItems(connectedList);
        connectedIMEI.setCellValueFactory(new PropertyValueFactory<CClient, String>("imei"));
        connectedProgress.setCellValueFactory(new PropertyValueFactory<CClient, ProgressIndicator>("progressIndicator"));
        /* ********************************************************************************************** */
        //addAutoScroll(tableMain);
    }

    public void updValBufferInfo(int a1){
        bufferInfo.setYValue(a1);
    }

    public void upValBufferWarning(int a1){
        bufferWarning.setYValue(a1);
    }

/*    public static <S> void addAutoScroll(final TableView<S> view) {
        if (view == null) {
            throw new NullPointerException();
        }

        view.getItems().addListener((ListChangeListener<S>) (c -> {
            c.next();
            final int size = view.getItems().size();
            if (size > 0) {
                view.scrollTo(size-1);
            }
        }));
    }*/
}