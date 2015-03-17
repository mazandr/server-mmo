package com.win.strategy.battle.model.command.impl;

import com.win.strategy.battle.model.BattleModelEngine;
import java.util.Map;

/**
 *
 * @author vlischyshyn
 */
public class BMCaptureBuildingCommand extends BMBaseBattleCommand {

    public BMCaptureBuildingCommand(BattleModelEngine battleModelEngine) {
        super(battleModelEngine);
    }

    @Override
    public void init(Map<String, Object> params) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<String, Object> execute() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<String, Object> info() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
//    
//     @Override
//    public DataBag captureBuilding(DoBattleAction data) {
//        IGameEnvironment environment = context.getEnvironments().get(data.getEnvironmentKey());
//        Preconditions.checkNotNull(environment);
//        List<DoBattleAction.BuildingData> buildings = data.getBuildings();
//        DataBag result = null;
//        List<String> ids = null;
//
//        if (buildings != null) {
//            for (DoBattleAction.BuildingData b : buildings) {
//                if (ids == null) {
//                    ids = new ArrayList<>();
//                }
//                if (result == null) {
//                    result = new DataBag();
//                    result.put(ProtocolStrings.BUILDINGS, ids);
//                }
//
//                BMBaseBuilding capturedBuilding = environment.getBattleManager().captureBuilding(b.getId());
//                if (capturedBuilding != null) {
//                    ids.add(capturedBuilding.getIdentifier());
//                }
//            }
//        }
//        return result;
//    }
//
//    @Override
//    public void init(Map<String, Object> params) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public Map<String, Object> execute() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public Map<String, Object> info() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
}
