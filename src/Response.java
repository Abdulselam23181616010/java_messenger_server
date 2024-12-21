public class Response extends Gonderi{
    private int responseCode;

    public Response(int requestType, Mesaj mesaj) {
        super(requestType,mesaj);
    }

    public void setResponseCode(int responseCode){
        this.responseCode = responseCode;
    }

    public int getResponseCode(){
        return this.responseCode;
    }


}
