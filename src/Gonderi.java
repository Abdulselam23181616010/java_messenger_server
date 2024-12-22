import java.io.Serializable;

public class Gonderi implements Serializable {
    private static final long serialVersionUID = 8185736556051931014L;
    private int requestType;
    private Mesaj mesaj;
    private int responseCode;

    //Gonderi için constructor oluşturalım
    public Gonderi(int requestType, Mesaj mesaj){
        this.requestType = requestType;
        this.mesaj = mesaj;

    }

     public void setResponseCode(int responseCode){
        this.responseCode = responseCode;
    }

    public int getResponseCode(){
        return responseCode;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setMesaj(Mesaj mesaj) {
        this.mesaj = mesaj;
    }

    public Mesaj getMesaj() {
        return mesaj;
    }
}
