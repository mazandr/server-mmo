
package com.win.battle.utils;

import com.win.strategy.common.model.support.configuration.IGlobalConfiguration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author okopach
 */
public class GlobalConfiguration implements IGlobalConfiguration{

    @Override
    public Map<String, Object> getWinConditionMap() {
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> getGeneral() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getGameProcessStep() {
        return 100;
    }

    @Override
    public int getTotalGameFrames() {
        return 2400;
    }

    @Override
    public int getItterationSize() {
        return 2400;
    }

    @Override
    public List makeWinConditionsFromConfig() {
        return new ArrayList();
    }

    @Override
    public Map<String, Object> getConfig() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getReturningCostPercent() {
        return 25;
    }

}
