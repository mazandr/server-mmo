package com.win.strategy.battle.model.command;

import com.win.strategy.battle.model.BattleModelEngine;
import com.win.strategy.battle.model.command.impl.BMEnterUnitCommand;
import com.win.strategy.battle.model.command.impl.BMUseSkillCommand;
import com.win.strategy.common.model.command.AbstractCommandManager;

/**
 *
 * @author vlischyshyn
 */
public class BMCommandManger extends AbstractCommandManager<BattleModelEngine> {

//    private final String ADDTRANSPORT = "addtransPort";
    private final String ADDUNIT = "addunit";
    private final String USESKILL = "useskill";
//    private final String MOVE = "move";
//    private final String CAPTURE = "capture";

    protected void initCommands() {

//        addCommand(ADDTRANSPORT, BMAddTransportCommand.class);
        addCommand(ADDUNIT, BMEnterUnitCommand.class);
        addCommand(USESKILL, BMUseSkillCommand.class);
//        addCommand(MOVE, BMMoveUnitCommand.class);
//        addCommand(CAPTURE, BMCaptureBuildingCommand.class);
    }

    public BMCommandManger(BattleModelEngine engine) {
        super(engine);
    }
}
