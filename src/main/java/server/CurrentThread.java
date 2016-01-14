package server;

import database.DataBase;
import execptions.command.LogException;
import execptions.command.RequestException;
import execptions.net.NetException;
import execptions.net.NetExceptionAlarm;
import execptions.net.NetExceptionClose;
import execptions.net.NetExceptionWarning;
import javafx.application.Platform;
import javafx.scene.control.ProgressIndicator;
import org.hibernate.HibernateException;
import processing.command.AbstractCommand;
import processing.Handler;
import server.Monitor.*;

import java.net.Socket;
import java.sql.Timestamp;
import java.util.Random;

public class CurrentThread implements Runnable{
    private Handler handler;
    private AbstractCommand currenObject;
    private String message;
    private long timeOut = 0; //Default for timeout is 10 seconds;

    private DataBase dataBase;

    private Socket socket;
    private int indexSocket;
    private HeapList heapList;
    private Records records;
    private ConnectedClients connectedClients;
    public CurrentThread(int indexSocket, Socket socket, DataBase dataBase){
        this.handler = new Handler(socket, dataBase);
        this.dataBase = dataBase;
        this.socket = socket;
        this.indexSocket = indexSocket;
    }

    public void setList(HeapList heapList, Records records, ConnectedClients connectedClients){
        this.heapList = heapList;
        this.records = records;
        this.connectedClients = connectedClients;
    }

    @Override
    public void run(){
        System.out.println("Server: [Has new client]");
        System.out.flush();
        heapList.add("Server: [Has new client]");

        String imei = new String();

        do{
            try {
                records.put("[wait]", 0.00, "who is it?", indexSocket);
                //Block listens and receives messages;
                /* ******************************************************** */
                // 1: Exception if time out for inbound command (code: 2);
                    handler.acceptMessage(timeOut); //Listen socket and expect STARTLINE(protokol);
                records.put("[message recieved]", 0.16, "message", indexSocket);
                    message = handler.getMessage(); //Return expected message;
                records.put("[get message]", 0.32, message, indexSocket);
                    message = handler.deCryption(message);//Decrypted message;
                records.put("[deCryption]", 0.48, message, indexSocket);
                /* ******************************************************** */

                //Block verifications and performs commands;
                /* ******************************************************** */
                // 2: Exception if crc is incorrect (code: 1);
                    message = handler.verificatorCRC(message); //verification CRC
                records.put("[tested]", 0.64, message, indexSocket);
                    System.out.println("Server: [received origin message] " + message + " : " + message.length());
                    heapList.add("Server: [received origin message] " + message + " : " + message.length());
                /* ******************************************************** */

                //Manufacturing from message to commands/processing.objects;
                /* ******************************************************** */
                    currenObject = handler.factoryObjects(message); //Manufacture for choosing processing.command depend of commands;
                records.put("[recognized command]", 0.8, "", indexSocket);

                if (currenObject.getCommand() == '0'){
                    int startIndex = message.indexOf('|')+1;
                    int stopIndex = message.indexOf('|', startIndex);
                    imei = message.substring(startIndex, stopIndex);
                    connectedClients.add(indexSocket, imei, 1.0);
                    //connectedClients.set(indexSocket, imei, 0.1);
                }

                    message = currenObject.perform(); //Perform current processing.command/command;
                records.put("[performed command]", 0.96, message, indexSocket);
                /* ******************************************************** */


            //Handler of NetException;
            /* ****************************************************************************************** */
            } catch (NetExceptionWarning netException) { //When received exception is broken (CRC & Length of CRC);
                                                         //Or UNKNOWN command;
                message = netException.get();
                System.err.println("Server: [Warning: " + message +"]");
                System.err.flush();
                heapList.add("Server: [Warning: " + message +"]");
                //RECORD WARNING TO SERVER LOG;
                dataBase.writeToDB("Warning: " + message);
            } catch (NetExceptionAlarm netException){
                message = netException.get(); //When received exception is DANGEROUS;
                System.err.println("Server: [Alarm: " + message+"]");
                System.err.flush();
                heapList.add("Server: [Alarm: " + message+"]");
                //RECORD WARNING TO SERVER LOG;
                dataBase.writeToDB("Alarm: " + message);
                break;
            } catch(NetExceptionClose netException) {
                message = netException.get();
                System.out.println("Server: [Socket closed]");
                System.out.flush();
                //RECORD WARNING TO SERVER LOG;
                heapList.add("Server: [Socket closed]");
                break;
            } catch(NetException netException) {
                message = "2|Unexpected NET EXCEPTION";
                System.err.println("Server: [Unexpected NET exception]");
                System.err.flush();
                heapList.add("Server: [Unexpected NET exception]");
                //RECORD WARNING TO SERVER LOG;
                dataBase.writeToDB("Alarm: " + message);
            } catch (HibernateException hibernateException) {
                message = hibernateException.getMessage();
                System.err.println("Server: [Alarm: DataBase "+ message +"]");
                System.err.flush();
                heapList.add("Server: [Unexpected NET exception]");
                //RECORD WARNING TO SERVER LOG;
                dataBase.writeToDB("Alarm: " + message);
            } catch (LogException logException) {
                message = logException.get(); //When received exception in parsing LOG COMMAND;
                System.err.println("Server: [Warning: " + message+"]");
                System.err.flush();
                heapList.add("Server: [Warning: " + message+"]");
                //RECORD WARNING TO SERVER LOG;
                dataBase.writeToDB("Warning: " + message);
            } catch (RequestException requestException){
                message = requestException.get(); //When received exception in current COMMAND(request);
                System.err.println("Server: [Warning: " + message+"]");
                System.err.flush();
                heapList.add("Server: [Warning: " + message+"]");
                //RECORD WARNING TO SERVER LOG;
                dataBase.writeToDB("Warning: " + message);
            } catch (Exception exception){
                message = "2|Unexpected EXCEPTION" + exception.getMessage();
                System.err.println("Server: [Unexpected exception]");
                System.err.flush();
                heapList.add("Server: [Unexpected exception]");
                //RECORD WARNING TO SERVER LOG;
                dataBase.writeToDB("Alarm: " + message);
            /* ****************************************************************************************** */
            } finally {
                //if here isn't NetException than answer client;
                /* ******************************************************** */
                    message = handler.enCryption(message);
                    handler.sendMessage(message);

                records.put("[sent to client]", 1, message, indexSocket);
                records.del(indexSocket);
                /* ******************************************************** */
                System.out.println();
            }
        } while(true); //Eternity loop. it breaks loop have NetException;

        connectedClients.del(indexSocket);

        handler.close();
        System.err.println("Server: [Closed socket " + indexSocket + "]");
        System.err.flush();
        heapList.add("Server: [Closed socket " + indexSocket + "]");
    }
}