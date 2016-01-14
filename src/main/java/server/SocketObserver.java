package server;

import database.DataBase;
import javafx.collections.ObservableList;
import javafx.scene.control.ProgressIndicator;
import server.Monitor.ConnectedClients;
import server.Monitor.HeapList;
import server.Monitor.Records;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.sql.Timestamp;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketObserver extends Thread{
    private int port;
    private int qntThread;

    private DataBase dataBase;

    private ServerSocket serverSocket;
    private ExecutorService executeCurrentSocket;

    private HeapList heapList;
    private ConnectedClients connectedClients = new ConnectedClients();
    private Records records = new Records();


    private int count;

    public SocketObserver(int port, int qntThread){
        this.port = port;
        this.qntThread = qntThread;
        this.heapList = new HeapList();
    }

    @Override
    public void run(){
        try {
            serverSocket = new ServerSocket(port);
            executeCurrentSocket = Executors.newFixedThreadPool(qntThread);

            System.out.println("Server: [Started]");
            System.out.flush();
            heapList.add("Server: [Started]");
            /* */
            while(!isInterrupted()) {
                System.out.println("Server: [waiting for a client]");
                System.out.flush();
                heapList.add("Server: [waiting for a client]");

                Socket socket = serverSocket.accept();

                System.out.println("Server: [Index connection: " + count +"]");

                CurrentThread currentThread = new CurrentThread(count, socket, dataBase);
                currentThread.setList(heapList, records, connectedClients);

                connectedClients.set(count, socket.getInetAddress().getHostAddress());

                executeCurrentSocket.execute(currentThread);

                count++;
                //interrupt();
            }
            /* */
        }catch(Exception x){
            x.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            executeCurrentSocket.shutdown();
        }
    }

    public void setDB(DataBase dataBase){
        this.dataBase = dataBase;
    }

    public ObservableList<String> getHeapList(){
        return heapList.getEventMessage();
    }

    public Records getRecords(){
        return records;
    }

    public ConnectedClients getConnected(){
        return connectedClients;
    }
}