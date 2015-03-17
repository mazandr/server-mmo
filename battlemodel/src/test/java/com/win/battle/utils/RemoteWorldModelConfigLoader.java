package com.win.battle.utils;

import com.win.strategy.common.model.support.configuration.IWorldModelConfiguration;
import com.win.strategy.common.utils.ProtocolStrings;
import java.util.Map;

/**
 *
 * @author okopach
 */
public class RemoteWorldModelConfigLoader extends BaseRemoteConfiguration implements IWorldModelConfiguration {

    private Map<String, Object> config;

    @Override
    public Map<String, Object> getUnitTypes() {
        return (Map<String, Object>) getConfig().get("units");
    }

    @Override
    public Map<String, Object> getCarTypes() {
        return (Map<String, Object>) getConfig().get("cars");
    }

    @Override
    public Map<String, Object> getEntityTypes() {
        return (Map<String, Object>) getConfig().get("heroes");
    }

    @Override
    public Map<String, Object> getHeroTypes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<String, Object> getGarbageTypes() {
        return (Map<String, Object>) getConfig().get("garbage");
    }

    @Override
    public Map<String, Object> getResourceTypes() {
        return (Map<String, Object>) getConfig().get("resources");
    }

    @Override
    public Map<String, Object> getBuildingTypes() {
        return (Map<String, Object>) getConfig().get("buildings");
    }

    @Override
    public Map<String, Object> getConfig() {
        return (Map<String, Object>) this.config.get("entities");
    }

    @Override
    protected void onLoadedConfig(Map<String, Object> config) {
        this.config = config;
    }

    @Override
    protected String getConfigURL() {
        return "/admin/api/games/strategy/worldmodel/config";
    }

    @Override
    protected String getCurrentVersionURL() {
        return "/admin/api/games/strategy/worldmodel/getCurrentNumberVersion";
    }

    @Override
    public Map<String, Object> getSkillTypes() {
        return (Map<String, Object>) getConfig().get(ProtocolStrings.SKILLS);
    }
}
