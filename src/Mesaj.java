import java.io.Serializable;
import java.time.LocalDateTime;

public class Mesaj implements Serializable {
    private static final long serialVersionUID = 7818547970534850237L;
    private int id;
    private String gonderici;
    private String mesaj;
    private LocalDateTime time;

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

    public String getGonderici() {
        return gonderici;
    }

    public String getMesaj() {
        return mesaj;
    }




}

