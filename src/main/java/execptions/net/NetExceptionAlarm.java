package execptions.net;

public class NetExceptionAlarm extends NetException {
    private String wrong;

    public NetExceptionAlarm(String wrong){
        this.wrong = wrong;
    }
    public String get(){
        return this.wrong;
    }
}
