import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//Şifreleri güvenlik amaçlı açık halde saklamak önerilmiyor bu yüzden onları basit bir hash şeklinde saklamak için bu metodu oluşturalım
abstract public class HashOlustur {
    public static String md5HashOlustur(String girdi) {
        try {
            // MessageDigest'ten md5 alalım
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Hash oluşturalım
            byte[] hashBytes = md.digest(girdi.getBytes());

            // Bit diziyi on alt ıtabanlık stringe yazdıralım
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0'); // 2 basamaklı bir format sağlayalım
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algoritması bulunmadı", e);
        }
    }
}
