package processing.command;

import database.DataBase;
import execptions.command.CmdException;
import execptions.net.NetException;

abstract public class AbstractCommand {
    abstract public void parseToObject(String str) throws CmdException;
    abstract public String perform() throws NetException;
    abstract public char getCommand();
}
