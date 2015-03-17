package com.win.battle.utils;

import com.win.strategy.common.model.support.configuration.IWorldModelConfiguration;
import com.win.strategy.common.utils.ProtocolStrings;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author okopach
 */
public class LocalWorldModelConfigLoader implements IWorldModelConfiguration {

    private Map<String, Object> config = new HashMap<>();
    private final static Logger logger = Logger.getLogger(LocalWorldModelConfigLoader.class.getName());

    /**
     *
     * @param fileName file with world model from test resources
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
            config.put(ProtocolStrings.ENTITIES, objectMapper.readValue(inputStream, HashMap.class));
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
    public Map<String, Object> getUnitTypes() {
        return (Map<String, Object>) getEntityTypes().get(ProtocolStrings.UNITS);
    }

    @Override
    public Map<String, Object> getCarTypes() {
        return (Map<String, Object>) getEntityTypes().get(ProtocolStrings.CAR);
    }

    @Override
    public Map<String, Object> getEntityTypes() {
        return (Map<String, Object>) getConfig().get(ProtocolStrings.ENTITIES);
    }

    @Override
    public Map<String, Object> getHeroTypes() {
        return (Map<String, Object>) getEntityTypes().get(ProtocolStrings.HEROES);
    }

    @Override
    public Map<String, Object> getGarbageTypes() {
        return (Map<String, Object>) getEntityTypes().get(ProtocolStrings.GARBAGE);
    }

    @Override
    public Map<String, Object> getResourceTypes() {
        return (Map<String, Object>) getEntityTypes().get(ProtocolStrings.RESOURCES);
    }

    @Override
    public Map<String, Object> getBuildingTypes() {
        return (Map<String, Object>) getEntityTypes().get(ProtocolStrings.BUILDINGS);
    }

    @Override
    public Map<String, Object> getConfig() {
        return config;
    }

    @Override
    public Map<String, Object> getSkillTypes() {
        return (Map<String, Object>) getEntityTypes().get(ProtocolStrings.SKILLS);
    }
    
    public Object getBuildingParams(String buildingType, int level) {
        Map<String, Object> entities = (Map<String, Object>) config.get(ProtocolStrings.ENTITIES);
        Map<String, Object> buildings = (Map<String, Object>) entities.get(ProtocolStrings.BUILDINGS);
        Map<String, Object> building = (Map<String, Object>) buildings.get(buildingType);
        Map<String, Object> levels = (Map<String, Object>) building.get("lvls");
        return levels.get("lvl" + level);
    }

    public Object getBuildingParamValue(String buildingType, int level, String param) {
        return ((Map<String, Object>) getBuildingParams(buildingType, level)).get(param);
    }

    public Object getBuildingDistributedActionParam(String buildingType, int level, String param) {
        ArrayList actions = (ArrayList) getBuildingParamValue(buildingType, level, "actions");
        for (int i = 0; i < actions.size(); i++) {
            Map<String, Object> currAction = (Map<String, Object>) actions.get(i);
            Map<String, Object> actT = (Map<String, Object>) currAction.get(ProtocolStrings.AC_T);
            if (actT.get(ProtocolStrings.TYPE).toString().equalsIgnoreCase("distributed")) {
                Object paramValue =  currAction.get(param);
                return paramValue;
            }
        }
        return null;
    }    
    
    public String getBuildingDistributedActionAcTypeParams(String buildingType, int level, String param) {
        ArrayList actions = (ArrayList) getBuildingParamValue(buildingType, level, "actions");
        for (int i = 0; i < actions.size(); i++) {
            Map<String, Object> currAction = (Map<String, Object>) actions.get(i);
            Map<String, Object> actT = (Map<String, Object>) currAction.get(ProtocolStrings.AC_T);
            if (actT.get(ProtocolStrings.TYPE).toString().equalsIgnoreCase("distributed")) {
                return actT.get(param).toString();
            }
        }
        return null;
    }
    
}
