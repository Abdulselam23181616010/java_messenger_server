public class Gonderi {
    protected int requestType;
    protected Mesaj mesaj;

    //Gonderi için constructor oluşturalım
    public Gonderi(int requestType, Mesaj mesaj){
        this.requestType = requestType;
        this.mesaj = mesaj;

    }

    //Sonra child sınıflarda bu metodu yeniden biçimlendireceğiz
    public int islemYap(){
        return 0;

    }


}
