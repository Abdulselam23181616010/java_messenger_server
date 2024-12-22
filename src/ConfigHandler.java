import java.io.*;
import java.util.Properties;

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