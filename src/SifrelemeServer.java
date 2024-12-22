import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.Properties;

public class SifrelemeServer {
    //Hassas içerikleri config filedan çekelim
    private static Properties prop = ConfigHandler.use();

    private static final String SECRET_KEY = prop.getProperty("SECRET_KEY","");
    public static String sifrele(Gonderi gonderi){
        try {
            // Convert the user object to a byte array
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(gonderi);

            // Encode the byte array to Base64
            String base64Encoded = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());

            // Encrypt the Base64-encoded string
            return AESUtil.encrypt(base64Encoded, SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    public static Gonderi cevir(String sifrelenmisVeri){
        try {
            // Decrypt the encrypted string
            String decryptedBase64 = AESUtil.decrypt(sifrelenmisVeri, SECRET_KEY);

            // Decode the Base64 string back to bytes
            byte[] decryptedBytes = Base64.getDecoder().decode(decryptedBase64);

            // Convert the byte array back into a User object
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decryptedBytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

            return (Gonderi) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static User userCevir(String sifrelenmisVeri){
        try {
            // Decrypt the encrypted string
            String decryptedBase64 = AESUtil.decrypt(sifrelenmisVeri, SECRET_KEY);

            // Decode the Base64 string back to bytes
            byte[] decryptedBytes = Base64.getDecoder().decode(decryptedBase64);

            // Convert the byte array back into a User object
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decryptedBytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

            return (User) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}

