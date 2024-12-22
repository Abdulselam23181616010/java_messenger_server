import java.io.Serializable;

//Çok kullanacağımız kullancı bilgileri bir nesnede saklamak için User sınıfını oluşturalım
public class User implements Serializable {
    private String username;
    private String sifre;
    private String isim;
    private String soyisim;
    private Boolean varMi;

    //İki türde constructor oluşturalım.Biri girisYap() diğeri uyeOl() metodları içindir.
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

    //getter - setterleri oluşturalım
    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getSoyisim() {
        return soyisim;
    }

    public void setSoyisim(String soyisim) {
        this.soyisim = soyisim;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public Boolean getVarMi() {
        return varMi;
    }

    public void setVarMi(Boolean varMi) {
        this.varMi = varMi;
    }

}
