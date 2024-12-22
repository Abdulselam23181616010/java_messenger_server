import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

//Gonderilerin gittiği zaman kimsenin onları açık açık okumaması için bu verilerin simetrik anahtarla şifrelenmiş olması gerek.
public class AESUtil {
    private static final String ALGORITHM = "AES";

    // Gonderiyi sifrelemek için metod yazalım
    public static String encrypt(String data, String key) throws Exception {
        SecretKey secretKey = getKeyFromBase64(key);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    // Gelen Gonderiyi çözmek için metod da oluşturalım
    public static String decrypt(String encryptedData, String key) throws Exception {
        SecretKey secretKey = getKeyFromBase64(key);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
        return new String(cipher.doFinal(decodedData));
    }

    // Base64'li anahtarımızı Secret Key formatına çevirmek için metod da yazalım
    private static SecretKey getKeyFromBase64(String key) {
        byte[] decodedKey = Base64.getDecoder().decode(key);
        return new SecretKeySpec(decodedKey, ALGORITHM);
    }
}
