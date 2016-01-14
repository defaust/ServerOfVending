package processing.command;

import database.DataBase;
import execptions.net.NetExceptionWarning;

public class UnknownCommand extends AbstractCommand {
    private DataBase dataBase;
    public UnknownCommand(DataBase dataBase){
        this.dataBase = dataBase;
    }

    @Override
    public String toString(){
        return "default";
    }
    @Override
    public String perform() throws NetExceptionWarning{
        //Write record to DataBase SERVER WARNING;
        throw new NetExceptionWarning("1|UNKNOWN COMMAND FROM CLIENT!");
    }

    @Override
    public void parseToObject(String str){
    }
    @Override
    public char getCommand() {
        return 'u';
    }
}
