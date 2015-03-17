package com.win.strategy.battle.model.command.impl;

import com.win.strategy.battle.model.BattleModelEngine;
import com.win.strategy.common.utils.ProtocolStrings;
import java.util.Map;

/**
 *
 * @author vlischyshyn
 */
public class BMUseSkillCommand extends BMBaseBattleCommand {

    private int posX;
    private int posY;
    private String skillId;
    private String targetId;
    private Map<String, Object> params;

    public BMUseSkillCommand(BattleModelEngine battleModelEngine) {
        super(battleModelEngine);
    }

    public void useSkill(Map<String, Object> data) {
        Object skill = data.get(ProtocolStrings.ID);
        if (skill == null) {
            return;
        }

        posX = 0;
        posY = 0;
        skillId = String.valueOf(skill);
        Object target = data.get(ProtocolStrings.TARGET);
        targetId = String.valueOf(target);

        try {
            posX = Integer.valueOf(String.valueOf(data.get("posX")));
            posY = Integer.valueOf(String.valueOf(data.get("posY")));
        } catch (Exception e) {
            if (target == null) {
                return;
            }
        }

        if (target != null) {
            getField().useSkill(skillId, targetId, 0);
        } else {
            getField().useSkill(skillId, posX, posY, 0);
        }
//        field.useSkill(skillId, pX, pY, process.getTimeTicks());
    }

    @Override
    public void init(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public Map<String, Object> execute() {
        useSkill(params);
        return null;
    }

    @Override
    public Map<String, Object> info() {
        return null;
    }
}
