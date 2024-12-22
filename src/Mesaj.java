import java.io.Serializable;
import java.time.LocalDateTime;

//Mesajların id, gönderici time gibi özellikleri saklamak ve böylelikle iletişimin daha kolay olmasını amaçlayan bir sınıftır
public class Mesaj implements Serializable {
    //Karşı taraftaki Sınıfla uyuşmamazlığını gidermek amaçlı sınıfın ID'sini oluşturalım
    private static final long serialVersionUID = 7818547970534850237L;
    private int id;
    private String gonderici;
    private String mesaj;
    private LocalDateTime time;


    //Constructorları oluşturalım
    public Mesaj(int id, String gonderici, String mesaj, LocalDateTime time) {
        this.id = id;
        this.gonderici = gonderici;
        this.mesaj = mesaj;
        this.time = time;
    }

    public Mesaj(String gonderici, String mesaj){
        this.gonderici = gonderici;
        this.mesaj = mesaj;

    }

    //Gerekli olan getterleri de beirtelim
    public String getGonderici() {
        return gonderici;
    }

    public String getMesaj() {
        return mesaj;
    }

    public LocalDateTime getTime() {
        return time;
    }


}

