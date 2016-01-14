package execptions.net;

public class NetExceptionClose extends NetException {
    private String wrong;

    public NetExceptionClose(String wrong){
        this.wrong = wrong;
    }
    public String get(){
        return this.wrong;
    }
}