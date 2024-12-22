import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SifrelemeServer {
    private static final String SECRET_KEY = "5ROIfv7Sf0nK9RfeqIkhtC6378OiR5E0VyTnjmXejY0=";
    public static String sifrele(Response response){
        try{
            //Gonderimizi bayt dizisine çevirelim
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(response);

            //Oluşan diziyi şifreleyelim
            String sifrelenmisVeri = AESUtil.encrypt(new String(byteArrayOutputStream.toByteArray()), SECRET_KEY);

            //Son olarak şifrelenmiş diziyi döndürelim
            return sifrelenmisVeri;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    public static Gonderi cevir(String sifrelenmisVeri){
        try{
            //ALdığımız verinin şifrelemesini çözelim
            byte[] decryptedBytes = AESUtil.decrypt(sifrelenmisVeri, SECRET_KEY).getBytes();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decryptedBytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

            //Veriyi bizim anlayabileceğimiz türden objeye çevirelim
            Gonderi gonderi = (Gonderi)objectInputStream.readObject();

            //Son olarak çıkan objemizi döndürelim
            return gonderi;

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static User userCevir(String sifrelenmisVeri){
        try{
            //Aldığımız kullancı verinin şifrelemesini çözelim
            byte[] decryptedBytes = AESUtil.decrypt(sifrelenmisVeri, SECRET_KEY).getBytes();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decryptedBytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

            //Kullancı veriyi bizim anlayabileceğimiz türden objeye çevirelim
            User user = (User)objectInputStream.readObject();

            //Son olarak çıkan objemizi döndürelim
            return user;

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }


}

