package client;

import processing.Handler;

import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

public class Client extends Thread{
    private String address;
    private int serverPort;
    private Handler handler;
    private Socket socket;
    private String str;

    private char[] cmdChar ={'0','1','2','X'};
    private char[] cmd;

    public Client(String address, int serverPort){
        this.address = address;
        this.serverPort = serverPort;
    }

    private String randomRecord(int length){
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i <length ; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    @Override
    public void run(){
        Random random = new Random();
        cmd = new char[random.nextInt(7)+3];
        cmd[0] = '0';
        cmd[1] = '1';
        cmd[cmd.length-1] = 'X';
        for (int i = 2; i <cmd.length-1 ; i++) {
            cmd[i] = cmdChar[2];
        }

        try {
            InetAddress ipAddress = InetAddress.getByName(address);
            socket = new Socket(ipAddress, serverPort);
            System.out.println("Client: [CONNECTED]");
            handler = new Handler(socket);

            for (int i = 0; i < cmd.length; i++) {
                str = "";
                /* ************************************************************************************* */
                str += cmd[i%cmd.length];
                char varChar = str.charAt(0);
                String imei = randomRecord(15);

                switch (varChar){
                    case '0':
                        str += "|"+imei + "|" + Integer.toString(cmd.length);
                        break;
                    case '1':
                        str += "|time";
                        break;
                    case '2':
                        str += "|012345678901234|192.3.2.7|3.5|3.0|2175677234|13620|65.65|170|1657|320|310|5";
                        break;
                    case 'X':
                        str += "|finish";
                        break;
                    default:
                        str += "|something";
                        System.err.println("Client: [" + str.charAt(0) + " UNKNOWN COMMAND]");
                        System.err.flush();
                        break;
                }

                str = handler.enCryption(str);
                handler.sendMessage(str);

                handler.acceptMessage(0);
                str = handler.getMessage();
                str = handler.deCryption(str);
                str = handler.verificatorCRC(str);
                System.out.println("Client: [recieved answer] " + str);
                System.out.println();
                System.out.flush();
                /* ************************************************************************************* */
                if (varChar == 'X'){
                    handler.close();
                    return;
                }
                if (str.charAt(0) == '1'){
                    System.err.println("Client: [Server report: Wrong command]");
                    System.err.flush();
                }
                if (str.charAt(0) == '2'){
                    System.err.println("Client: [SESSION CLOSED]");
                    handler.close();
                    return;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        handler.close();
    }
}