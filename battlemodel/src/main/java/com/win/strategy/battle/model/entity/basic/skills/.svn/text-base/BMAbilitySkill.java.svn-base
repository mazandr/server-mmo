package com.win.strategy.battle.model.entity.basic.skills;

import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.basic.actions.BMAction;
import com.win.strategy.battle.model.entity.basic.actions.BMActionExecutor;
import com.win.strategy.battle.model.entity.basic.actions.BMActionProperties;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vlischyshyn
 */
public class BMAbilitySkill extends BMBaseSkill {

    private List<BMActionProperties> items = new ArrayList<>();

    public BMAbilitySkill(BMField field) {
        super(field);
    }

    public List<BMActionProperties> getItems() {
        return items;
    }

    public void addItem(BMActionProperties skillItem) {
        skillItem.setOwner(this);
        this.items.add(skillItem);
    }

    @Override
    public void use() {
        BMActionExecutor executor = getField().getActionExecutor();
        for (BMActionProperties item : items) {
            BMAction action = new BMAction(item);
            action.setActionPosition(getSkillApplyPos());
            action.setActionTarget(getSkillTarget());
            executor.addAction(action);
        }
    }
}
