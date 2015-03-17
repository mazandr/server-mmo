package com.win.strategy.battle.model.command.impl;

import com.win.strategy.battle.model.BattleModelBuilder;
import com.win.strategy.battle.model.BattleModelEngine;
import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.BMMovementType;
import com.win.strategy.battle.model.entity.basic.units.BMTransport;
import com.win.strategy.common.utils.ProtocolStrings;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author vlischyshyn
 */
public class BMAddTransportCommand extends BMBaseBattleCommand {

    private BMTransport transport = null;
    private boolean validArgs = false;
    private final BattleModelBuilder builder;
    private String transId;

    public BMAddTransportCommand(BattleModelEngine battleModelEngine) {
        super(battleModelEngine);
        builder = battleModelEngine.getBuilder();
    }

    @Override
    public Map<String, Object> execute() {
        if (!validArgs) {
            return null;
        }
        if (transport != null) {
            getField().addTransport(transport);
            transport.notify(null);
        }
        return null;
    }

    @Override
    public void init(Map<String, Object> args) {
        if (args != null) {
            parseArgs(args);
        }
    }

    private void parseArgs(Map<String, Object> data) {
        String start;
        String dest;
        try {
            start = String.valueOf(data.get("start"));
            dest = String.valueOf(data.get("dest"));
        } catch (Exception e) {
            return;
        }

        BMCell startCell = getField().getCellByCode(start);
        BMCell destCell = getField().getCellByCode(dest);

        if (startCell == null || destCell == null) {
            return;
        }
        
        if (startCell.getMovementType() != BMMovementType.ROAD
                || destCell.getMovementType() != BMMovementType.ROAD) {
            return;
        }
        transId = String.valueOf(data.get(ProtocolStrings.ID));

        if (transId == null || transId.isEmpty()) {
            transId = "trans" + getField().getActivities().size();
        }

        transport = null;

        List<Map<String, Object>> cars = (List<Map<String, Object>>) builder.getGameConfiguration().getConfig().get(ProtocolStrings.CARS);
        Map<String, Object> currentCar = null;
        for (Map<String, Object> car : cars) {
            if (String.valueOf(car.get(ProtocolStrings.NAME)).equalsIgnoreCase(transId)) {
                currentCar = car;
            }
        }

        if (currentCar != null) {
            transport = builder.getTransportInstanceFactory().createTransport(currentCar);
            if (transport != null) {
                transport.setPosition(startCell);
                transport.setDestinationCell(destCell);
                validArgs = true;
            }
        }
    }

    @Override
    public Map<String, Object> info() {
        Map<String, Object> info = new HashMap<>();
        if (transport != null) {
            info.put(ProtocolStrings.ID, transport.getIdentifier());
            info.put(ProtocolStrings.TYPE, transport.getType());
        }
        return info;
    }

}
