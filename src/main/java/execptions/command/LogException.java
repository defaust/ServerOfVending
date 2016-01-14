package execptions.command;

public class LogException extends CmdException {
    private String wrong;

    public LogException(String wrong){
        this.wrong = wrong;
    }
    public String get(){
        return wrong;
    }
}
