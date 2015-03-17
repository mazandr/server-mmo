package com.win.battle.utils;

import com.win.strategy.battle.model.condition.BMWinCondition;
import com.win.strategy.common.model.support.configuration.IGlobalConfiguration;
import com.win.strategy.common.utils.ProtocolStrings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author okopach
 */
public class RemoteGlobalConfigurationLoader extends BaseRemoteConfiguration implements IGlobalConfiguration {

    public static final Logger LOG = LoggerFactory.getLogger(RemoteGlobalConfigurationLoader.class);
    private Map<String, Object> winConditionMap;
    private Map<String, Object> general;
    private int gameProcessStep = 100;
    private Map<String, Object> config;

    @Override
    public Map<String, Object> getWinConditionMap() {
        return winConditionMap;
    }

    @Override
    public Map<String, Object> getGeneral() {
        return general;
    }

    @Override
    public int getGameProcessStep() {
        return gameProcessStep;
    }

    @Override
    public int getTotalGameFrames() {
        return 1800;
    }

    @Override
    public int getItterationSize() {
        return 1800;
    }

    @Override
    public List makeWinConditionsFromConfig() {
        try {
            List<BMWinCondition> winConditions = new ArrayList<>();
            for (Map.Entry<String, Object> condition : winConditionMap.entrySet()) {
                Map<String, Object> map = (Map<String, Object>) condition.getValue();
                if (map != null) {
                    Object value = map.get(ProtocolStrings.VALUE);
                    Object operator = map.get(ProtocolStrings.OPERATION);
                    if (value != null && operator != null) {
                        BMWinCondition winCondition = new BMWinCondition(condition.getKey(), Double.valueOf(String.valueOf(value)),
                                BMWinCondition.Operation.valueOf(String.valueOf(operator)));
                        winConditions.add(winCondition);
                    }
                }
            }
            return winConditions;
        } catch (Exception e) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public Map<String, Object> getConfig() {
        return config;
    }

    @Override
    protected void onLoadedConfig(Map<String, Object> config) {
        this.config = config;
        if (config.isEmpty()) {
            return;
        }

        try {
            winConditionMap = (Map<String, Object>) config.get(ProtocolStrings.WIN_CONDITION);
            general = (Map<String, Object>) config.get(ProtocolStrings.GENERAL);
        } catch (Exception e) {
            LOG.error("===> ERROR by getting global config parts", e);
        }

        try {
            gameProcessStep = Double.valueOf(String.valueOf(general.get(ProtocolStrings.GSTEP_VAL))).intValue();
//            totalGameFrames = Double.valueOf(String.valueOf(general.get(ProtocolStrings.GFRAMES_NUM))).intValue();
//            itterationSize = Double.valueOf(String.valueOf(general.get(ProtocolStrings.GFRAMES_PACK_SIZE))).intValue();

        } catch (Exception e) {
            gameProcessStep = 100;
//            totalGameFrames = 1800;
//            itterationSize = 10;

            LOG.error("===> ERROR by getting global config values", e);
        }
    }

    @Override
    protected String getConfigURL() {
        return "/admin/api/games/strategy/globaloptions/config";
    }

    @Override
    protected String getCurrentVersionURL() {
        return "/admin/api/games/strategy/globaloptions/getCurrentNumberVersion";
    }

    @Override
    public int getReturningCostPercent() {
        return 25;
    }
}
