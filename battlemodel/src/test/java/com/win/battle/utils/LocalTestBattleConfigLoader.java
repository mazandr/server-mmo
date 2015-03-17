package com.win.battle.utils;

import com.win.battle.BaseBattleTest;
import com.win.strategy.common.model.support.IModelConfiguration;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author okopach
 */
public class LocalTestBattleConfigLoader implements IModelConfiguration {

    private Map<String, Object> config = new HashMap<>();
    private final static Logger logger = Logger.getLogger(LocalTestBattleConfigLoader.class.getName());

    /**
     *
     * @param fileName file with battle test configuration from test resources
     */
    public void load(String fileName) {
        InputStream inputStream = null;
        try {
            inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
            if (inputStream == null) {
                logger.log(Level.SEVERE, "Sorry, unable to find world model file" + fileName);
                throw new FileNotFoundException();
//                return;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            config.putAll(objectMapper.readValue(inputStream, HashMap.class));
        } catch (IOException ex) {
            logger.log(Level.SEVERE, ex.getLocalizedMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.log(Level.SEVERE, e.getLocalizedMessage());
                }
            }
        }
    }

    @Override
    public Map<String, Object> getConfig() {
        return this.config;
    }
}
