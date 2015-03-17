package com.win.battle.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author okopach
 */
public abstract class BaseRemoteConfiguration implements IRemoteConfiguration {

    private AccessURLWithAuthentication access;
    private ObjectMapper objectMapper;
    private Properties urlProperties = new Properties();
    private static final String propertyFile = "uris.properties";
    

    public BaseRemoteConfiguration() {
        objectMapper = new ObjectMapper();
        InputStream input = null;
        try {

            input = this.getClass().getClassLoader().getResourceAsStream(propertyFile);
            if (input == null) {
                System.out.println("Sorry, unable to find props file" + propertyFile);
                return;
            }
            urlProperties.load(input);
        } catch (IOException ex) {
            System.out.println(ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
    }

    @Override
    public void load(Integer version) {
        this.access = new AccessURLWithAuthentication(this.urlProperties.getProperty("url.remote"), Integer.valueOf(this.urlProperties.getProperty("url.remote.port")), this.urlProperties.getProperty("url.remote.protocol"), "Support1", "Supportster2");

        try {
            if (version == null) {
                String versionResponse = this.access.getPageThroughAuth(getCurrentVersionURL());
                version = (Integer) objectMapper.readValue(versionResponse, HashMap.class).get("currentNumberVersion");
            }

            String dataStr = this.access.getPageThroughAuth(getConfigURL() + "?version=" + version);
            Map<String, Object> config;
            List<HashMap> configs;
            configs = (ArrayList<HashMap>) objectMapper.readValue(dataStr, HashMap.class).get("object");
            config = (Map<String, Object>) configs.get(0).get("config");
            if (config == null || config.isEmpty()) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "config is null or empty (Bad structure of configuration).", objectMapper.readValue(dataStr, HashMap.class));
            }
            onLoadedConfig(config);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected abstract void onLoadedConfig(Map<String, Object> config);

    protected abstract String getConfigURL();

    protected abstract String getCurrentVersionURL();
    
    protected Properties getUrlProperties(){
        return urlProperties;
    }
}
