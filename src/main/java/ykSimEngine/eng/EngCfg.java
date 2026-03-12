package ykSimEngine.eng;

import Lib.ykLoggerLib.*;

import java.io.*;
import java.util.Properties;
import Lib.ykLoggerLib.LogLevel.*;

public class EngCfg {
    private static final int DEFAULT_UPS = 30;
    private static final String FILENAME = "eng.properties";
    private static EngCfg instance;
    private int ups;
    private boolean vkValidate;

    private EngCfg() {
        // Singleton
        var props = new Properties();

        try (InputStream stream = EngCfg.class.getResourceAsStream("/" + FILENAME)) {
            props.load(stream);
            ups = Integer.parseInt(props.getOrDefault("ups", DEFAULT_UPS).toString());
            vkValidate = Boolean.parseBoolean(props.getOrDefault("vkValidate", false).toString());
        } catch (IOException excp) {
            Logger.log(LogLevel.ERROR,"Could not read [" + FILENAME + excp +"] properties file");
        }
    }

    public static synchronized EngCfg getInstance() {
        if (instance == null) {
            instance = new EngCfg();
        }
        return instance;
    }

    public int getUps() {
        return ups;
    }

    public boolean isVkValidate() {
        return vkValidate;
    }
}