public class User {
    String username;
    String sifre;
    String isim;
    String soyisim;

    public User(String username, String sifre){
        this.username = username;
        this.sifre = sifre;


    }

    public User(String username,String sifre, String isim, String soyisim){
        this.username = username;
        this.sifre = sifre;
        this.isim = isim;
        this.soyisim = soyisim;

    }

}
