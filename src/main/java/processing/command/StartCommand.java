package processing.command;

import database.DataBase;

import java.util.StringTokenizer;

public class StartCommand extends AbstractCommand {
    private DataBase dataBase;
    private char command;
    private String information;
    public StartCommand(DataBase dataBase){
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
        System.out.println("Performed command START");

        return "0|OK";
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
    @Override
    public char getCommand() {
        return command;
    }

    public String getInformation() {
        return information;
    }
}
