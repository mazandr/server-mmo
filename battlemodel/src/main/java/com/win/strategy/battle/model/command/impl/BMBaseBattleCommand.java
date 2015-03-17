package com.win.strategy.battle.model.command.impl;

import com.win.strategy.battle.model.BattleModelEngine;
import com.win.strategy.battle.model.command.BMCommandManger;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.common.model.command.AbstractModelCommand;

/**
 *
 * @author vlischyshyn
 */
public abstract class BMBaseBattleCommand extends AbstractModelCommand<BattleModelEngine> {

    private BMField field;
    private BMCommandManger manger;

    public BMBaseBattleCommand(BattleModelEngine battleModelEngine) {
        super(battleModelEngine);
        this.field = battleModelEngine.getField();
        this.manger = battleModelEngine.getCommandManger();
    }

    public BMField getField() {
        return field;
    }

    public BMCommandManger getManger() {
        return manger;
    }
}
