package com.win.strategy.battle.model.command.impl;

import com.win.strategy.battle.model.BattleModelBuilder;
import com.win.strategy.battle.model.BattleModelEngine;
import com.win.strategy.common.model.command.ModelCommandException;
import com.win.strategy.common.utils.ProtocolStrings;
import java.util.List;
import java.util.Map;

/**
 *
 * @author vlischyshyn
 */
public class BMEnterUnitCommand extends BMBaseBattleCommand {

    private final BattleModelBuilder builder;
    private Map<String, Object> params;

    public BMEnterUnitCommand(BattleModelEngine battleModelEngine) {
        super(battleModelEngine);
        builder = battleModelEngine.getBuilder();
    }

    @Override
    public void init(Map<String, Object> params) {
        this.params = params;
    }

    //TODO: Add validation
    private void enterUnits(Map<String, Object> params) throws ModelCommandException {
        List<Map<String, Object>> _units = (List<Map<String, Object>>) params.get(ProtocolStrings.UNITS);
        if (_units == null) {
            throw new ModelCommandException("Mismatch command format. 'units' array is undefined'");
        }

        for (Map<String, Object> u : _units) {
            String id = String.valueOf(u.get(ProtocolStrings.ID));
            Object objX = u.get(ProtocolStrings.POS_X);
            Object objY = u.get(ProtocolStrings.POS_Y);

            try {
                int x = Integer.valueOf(String.valueOf(objX)).intValue();
                int y = Integer.valueOf(String.valueOf(objY)).intValue();

                getField().enterUnit(id, x, y);
            } catch (Exception e) {
                continue;
            }
        }
    }

    @Override
    public Map<String, Object> execute() throws ModelCommandException {
        enterUnits(params);
        return null;
    }

    @Override
    public Map<String, Object> info() {
        return null;
    }
}
