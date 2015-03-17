package com.win.strategy.battle;

import com.win.strategy.battle.model.condition.BMWinCondition;
import com.win.strategy.battle.model.entity.basic.buildings.BMBaseBuilding;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author okopach
 */
public class BMWinConditionHandler {

    private List<BMWinCondition> winConditions = new ArrayList<>();

    public BMWinConditionHandler() {
    }

    public void addWinCondition(BMWinCondition winCondition) {
        winConditions.add(winCondition);
    }

    public void addWinConditions(List<BMWinCondition> winCondition) {
        if (winCondition != null) {
            winConditions.addAll(winCondition);
        }
    }

    public void auditBuildingRate(BMBaseBuilding bMBaseBuilding) {
        Map<String, Double> winRate = bMBaseBuilding.getWinRate();
        if (winRate == null) {
            return;
        }

        for (String key : winRate.keySet()) {
            for (BMWinCondition winCondition : winConditions) {
                if (winCondition.getKey().equals(key)) {
                    winCondition.incCurrentRate(winRate.get(key));
                }
            }
        }
    }

    public void initBuildingRates(BMBaseBuilding bMBaseBuilding) {
        Map<String, Double> winRate = bMBaseBuilding.getWinRate();
        if (winRate == null) {
            return;
        }

        for (String key : winRate.keySet()) {
            for (BMWinCondition winCondition : winConditions) {
                if (winCondition.getKey().equals(key)) {
                    winCondition.incAllRate(winRate.get(key));
                }
            }
        }
    }

    public boolean checkValues() {
        for (BMWinCondition winCondition : winConditions) {
            if (winCondition.checkValue()) {
                return true;
            }
        }
        return false;
    }
}
