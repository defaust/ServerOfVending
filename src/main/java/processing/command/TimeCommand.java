package processing.command;

import database.DataBase;

import java.sql.Timestamp;
import java.util.StringTokenizer;

/**
 * Created by de.faust on 01.12.2015.
 */
public class TimeCommand extends AbstractCommand {
    private DataBase dataBase;
    private char command = '0';
    private String information;
    public TimeCommand(DataBase dataBase){
        this.dataBase = dataBase;
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(command);
        str.append('|');
        str.append(information);
        return str.toString();
    }
    @Override
    public String perform(){
        StringBuilder sb = new StringBuilder();
        sb.append("0|");
        Timestamp time =  new Timestamp(System.currentTimeMillis());
        System.out.println("TIME: " + time);
        sb.append(time.getTime());
        System.out.println("Performed TIME command. [" + time.getTime() + "]");
        return sb.toString();
    }

    @Override
    public void parseToObject(String str){
        //Temporary variable
        String tempStr;

        //Reads input string by each symbol '|'
        StringTokenizer readString = new StringTokenizer(str, "|");

        tempStr = readString.nextElement().toString();
        this.command = tempStr.charAt(0);

        tempStr = readString.nextElement().toString();
        this.information = tempStr;
    }

    public char getCommand() {
        return command;
    }

    public String getInformation() {
        return information;
    }
}
