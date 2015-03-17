package com.win.battle.utils;

import com.win.strategy.common.model.support.IModelConfiguration;
import java.util.Map;

/**
 *
 * @author okopach
 */
public class RemoteTestBattleConfigLoader extends BaseRemoteConfiguration implements IModelConfiguration {

    Map<String, Object> config;

    @Override
    public Map<String, Object> getConfig() {
        return config;
    }

    @Override
    protected void onLoadedConfig(Map<String, Object> config) {
        this.config = config;
    }

    @Override
    protected String getConfigURL() {
        return "/admin/api/games/strategy/battle/config";
    }

    @Override
    protected String getCurrentVersionURL() {
        return "/admin/api/games/strategy/battle/getCurrentNumberVersion";
    }
}
