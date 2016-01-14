package processing;

import database.DataBase;
import execptions.command.CmdException;
import execptions.command.LogException;
import execptions.command.RequestException;
import execptions.net.NetExceptionAlarm;
import execptions.net.NetExceptionWarning;
import processing.command.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.SecureRandom;

public class Handler {
    private final int sizeOfCRC = 5;
    //Objects;
    AbstractCommand performObject;
    //Length of open key for each message;
    private int lenKey = 25;
    private byte[] startMessage = {3, 5, 7, 4, 6};
    private int inBoundByte;
    private Socket socket;
    private DataBase dataBase;
    //Stream
    private InputStream in;
    private OutputStream out;

    public Handler(Socket socket, DataBase dataBase){
        try {
            this.socket = socket;
            this.in = socket.getInputStream();
            this.out = socket.getOutputStream();

            this.dataBase = dataBase;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Handler(Socket socket){
        try {
            this.socket = socket;
            this.in = socket.getInputStream();
            this.out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public AbstractCommand factoryObjects(String message) throws NetExceptionWarning, LogException, RequestException, CmdException {
        char charCommand = message.charAt(0);
        switch(charCommand){
            case '0':
                performObject = new StartCommand(dataBase);
                performObject.parseToObject(message);
                break;
            case '1':
                performObject = new TimeCommand(dataBase);
                performObject.parseToObject(message);
                break;
            case '2':
                performObject = new LogCommand(dataBase);
                performObject.parseToObject(message);
                break;
            case 'X':
                performObject = new FinishCommand(dataBase);
                performObject.parseToObject(message);
                break;
            default:
                performObject = new UnknownCommand(dataBase);
                break;
        }
        return performObject;
    }

    public String verificatorCRC(String message) throws NetExceptionWarning{
        int endIndex;

        endIndex =  message.charAt(sizeOfCRC);
        if (endIndex != '|'){
            System.err.println("Decryption trouble: impossible to find checksum");
            throw new NetExceptionWarning("1|Decryption trouble: impossible to find checksum");
        }

        String crc = message.substring(0, sizeOfCRC);

        String internalMessage = message.substring(sizeOfCRC+1);
        String crcCurrent = getCRC(internalMessage);

        if (!crcCurrent.equals(crc)){
            System.err.println("Decryption trouble: Not match CRCs!");
            System.err.println("Recieved:[" + crc + "]" + " == [" + crcCurrent + "] FALSE");
            throw new NetExceptionWarning("1|Decryption trouble: Not match CRCs!");
        }
        return internalMessage;
    }

    public String getMessage(){
        StringBuilder message = new StringBuilder();
        try {

            while((inBoundByte = in.read()) > 31){
                message.appendCodePoint(inBoundByte);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return message.toString();
    }

    public void sendMessage(String str){
        try {
            out.write(3);
            out.write(5);
            out.write(7);
            out.write(4);
            out.write(6);
            for (int i = 0; i < str.length(); i++) {
                out.write(str.charAt(i));
            }
            out.write(3);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCRC(String str) {
        StringBuilder result = new StringBuilder();
        char[] crc = new char[sizeOfCRC];
        for (int i = 0; i < str.length(); i++) {
            crc[0] ^= str.charAt(i);
        }
        for (int i = 0; i < str.length()/2; i++) {
            crc[1] ^= str.charAt(i);
        }
        for (int i = str.length()/2; i < str.length(); i++) {
            crc[2] ^= str.charAt(i);
        }
        for (int i = str.length()/3; i < str.length(); i++) {
            crc[3] ^= str.charAt(i);
        }
        for (int i = 0; i < str.length()*2/3; i++) {
            crc[4] ^= str.charAt(i);
        }
        result.append(crc);
        return result.toString();
    }

    public String enCryption(String str){
        //Create this StringBuilder for formation encrypted message;
        StringBuilder log = new StringBuilder();

        //Generate CRC;
        String crc = this.getCRC(str);

        //Create this inBoundString
        StringBuilder inBoundString = new StringBuilder();
        inBoundString.append(crc);
        inBoundString.append('|');
        inBoundString.append(str);

        //Randomize for generation first key
        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < lenKey; i++) {
            log.appendCodePoint(secureRandom.nextInt(224)+32); //
        }

        int outGoingByte;
        for (int i = 0; i < inBoundString.length(); i++) {

            outGoingByte = ((inBoundString.charAt(i) + log.charAt(i % lenKey)) % 224)+32;
            log.appendCodePoint(outGoingByte);
        }
        return log.toString();
    }

    public String deCryption(String inboundMessage) {
        StringBuilder log = new StringBuilder();
        for (int i = lenKey; i < inboundMessage.length(); i++) {
            inBoundByte = (((inboundMessage.charAt(i) - inboundMessage.charAt(i % lenKey)) + 224) % 224)-32;
            log.appendCodePoint(inBoundByte);
        }
        //log.delete(0, 2);
        return log.toString();
    }

    public void acceptMessage(long timeOut) throws NetExceptionAlarm {
        int count = 0;
        long time1, time2;

        time1 = System.currentTimeMillis();

        try {
            while(count < startMessage.length){

                //if time is out than throws exception about abort socket;
                /* ********************************************************* */
                time2 = System.currentTimeMillis();
                if ((timeOut != 0) && (time2 - time1) > timeOut){
                    System.err.println("Server: [Time out for inbound command]");
                    throw new NetExceptionAlarm("2|Time out for inbound commands!");
                }
                /* ********************************************************* */

                //Listen start combination chars;
                /* ********************************************************* */
                inBoundByte = in.read();
                if (startMessage[count] == inBoundByte){
                    count++;
                }else
                if (startMessage[0] == inBoundByte){
                    count = 1;
                }else
                    count = 0;
                /* ********************************************************* */
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            in.close();
            out.flush();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}