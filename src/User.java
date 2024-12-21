public class User {
    String username;
    String sifre;
    String isim;
    String soyisim;
    Boolean varMi;

    public User(String username, String sifre){
        this.username = username;
        this.sifre = sifre;
        this.varMi = true;


    }

    public User(String username,String sifre, String isim, String soyisim){
        this.username = username;
        this.sifre = sifre;
        this.isim = isim;
        this.soyisim = soyisim;
        this.varMi = false;

    }

}
