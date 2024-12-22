import java.io.*;
import java.util.Properties;

//hassas içerikleri direk koda yazmamak lazım onları config dosyaysında saklayalım. Ordaki verilere ulaşmak için de bu sınıf lazım
public class ConfigHandler {
    public static Properties use() {
        try {
            String configFilePath = "src/config.properties";
            FileInputStream propsInput = new FileInputStream(configFilePath);
            Properties prop = new Properties();
            prop.load(propsInput);
            return prop;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}