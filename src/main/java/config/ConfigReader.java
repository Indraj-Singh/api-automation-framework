package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static volatile ConfigReader instance;
    private Properties properties;

    private ConfigReader(){
        loadProperties();
    }

    public static ConfigReader getInstance(){
        if(instance==null){
            synchronized (ConfigReader.class){
                if(instance==null){
                    instance= new ConfigReader();
                }
            }
        }
        return instance;
    }

    private void loadProperties(){
        properties=new Properties();
        String env = System.getProperty("env", "qa");
        String fileName = "config-" + env + ".properties";

        try(InputStream is = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream(fileName)){

            if (is == null) {
                throw new RuntimeException("Config file not found in classpath : "+fileName);
            }
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config file : "+fileName,e);
        }
    }

    private String getProperty(String key){
        String value = properties.getProperty(key);
        if(value == null) {
            throw new RuntimeException("Missing config key: " + key);
        }
        return value;
    }

    public String getBaseUrl(){
        return getProperty("BaseUrl");
    }

    public String getUsername(){
        return getProperty("username");
    }

    public String getPassword(){
        return getProperty("password");
    }
}
