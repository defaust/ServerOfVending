package processing.command;

import database.DataBase;
import execptions.command.LogException;
import org.hibernate.HibernateException;
import processing.object.Dump;

import java.sql.Timestamp;


public class LogCommand extends AbstractCommand {
    Dump dump = new Dump();
    private DataBase dataBase;
    public LogCommand(DataBase dataBase){
        this.dataBase = dataBase;
    }

    public Dump getDump() {
        return dump;
    }

    @Override
    public String perform(){
        try {
            dataBase.writeToDB(dump);
        } catch (HibernateException e) {
            e.printStackTrace();
        }

        System.out.println("Performed command LOG");
        //RECORD TO LOGDB (logObject) NetException(3) if trouble;
        return "0|OK";
    }

    @Override
    public void parseToObject(String str) throws LogException{
        //2|012345678901234|192.3.2.7|3.5|3.0|2175677234|13620|65.65|170|1657|320|310|5;
        //Temporary variable;
        String[] tempStr;
        tempStr = str.split("\\|");

        if (tempStr.length != 13){
            //RECORDS TO SERVER DataBase WARNING;
            throw new LogException("1|Message isn't enough information");
        }
        dump.setReceivedTime(new Timestamp(System.currentTimeMillis())); //Time of received command
        dump.setCommand(tempStr[0].charAt(0));
        dump.setImei(tempStr[1]);
        dump.setIp(tempStr[2]);
        dump.setCsq(Float.parseFloat(tempStr[3]));
        dump.setBalSim(Float.parseFloat(tempStr[4]));
        dump.setEventTime(new Timestamp(Long.parseLong(tempStr[5])));
        dump.setBanknotes(Integer.parseInt(tempStr[6]));
        dump.setCoins(Float.parseFloat(tempStr[7]));
        dump.setLevelTank(Integer.parseInt(tempStr[8]));
        dump.setMeter1(Integer.parseInt(tempStr[9]));
        dump.setMeter2(Integer.parseInt(tempStr[10]));
        dump.setMeter3(Integer.parseInt(tempStr[11]));
        dump.setEvents(Integer.parseInt(tempStr[12]));
    }

    @Override
    public char getCommand() {
        return dump.getCommand();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(90);
        str.append(dump.getCommand());
        str.append('|');
        str.append(dump.getImei());
        str.append('|');
        str.append(dump.getIp());
        str.append('|');
        str.append(dump.getCsq());
        str.append('|');
        str.append(dump.getBalSim());
        str.append('|');
        str.append(dump.getEventTime());
        str.append('|');
        str.append(dump.getBanknotes());
        str.append('|');
        str.append(dump.getCoins());
        str.append('|');
        str.append(dump.getLevelTank());
        str.append('|');
        str.append(dump.getMeter1());
        str.append('|');
        str.append(dump.getMeter2());
        str.append('|');
        str.append(dump.getMeter3());
        str.append('|');
        str.append(dump.getEvents());
        return str.toString();
    }
}