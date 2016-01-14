package execptions.net;

public class NetExceptionWarning extends NetException{
    private String wrong;

    public NetExceptionWarning(String wrong){
        this.wrong = wrong;
    }

    public String get(){
        return this.wrong;
    }
}
