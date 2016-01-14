package execptions.command;

public class RequestException extends CmdException{
    private String wrong;

    public RequestException(String wrong){
        this.wrong = wrong;
    }
    public String get(){
        return wrong;
    }
}

