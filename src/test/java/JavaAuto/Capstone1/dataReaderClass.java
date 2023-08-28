package JavaAuto.Capstone1;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class dataReaderClass {

    private Properties properties;

    public dataReaderClass(String filePath) {
        properties = new Properties();
        try {
            FileInputStream inputStream = new FileInputStream("C:\\Users\\MRIDUL\\eclipse\\myworkspace\\Capstone1\\data.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
