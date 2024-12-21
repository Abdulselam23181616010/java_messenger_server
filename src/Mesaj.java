import java.sql.Timestamp;

public class Mesaj {
    private int id;
    private String gonderici;
    private String message;
    private Timestamp timestamp;

    public Mesaj(int id, String gonderici, String message, Timestamp timestamp) {
        this.id = id;
        this.gonderici = gonderici;
        this.message = message;
        this.timestamp = timestamp;
    }




}

