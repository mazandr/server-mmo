package com.win.strategy.battle.model.entity.basic.skills;

import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;
import com.win.strategy.battle.model.entity.basic.BMObject;
import com.win.strategy.battle.model.entity.basic.resources.BMResource;
import com.win.strategy.battle.model.entity.basic.resources.BMResourceHolder;
import java.util.Collection;

/**
 *
 * @author okopach
 */
public abstract class BMBaseSkill extends BMObject {

    private String resurceName = "energy";
    private BMResourceHolder resourceHolder = new BMResourceHolder();
    private BMField field;
    private int cooldown;
    private BMFractionEntity skillTarget;
    private BMCell skillApplyPos;

    public BMFractionEntity getSkillTarget() {
        return skillTarget;
    }

    public void setSkillTarget(BMFractionEntity skillTarget) {
        this.skillTarget = skillTarget;
        if (this.skillTarget != null) {
            setSkillApplyPos(skillTarget.getPosition());
        }
    }

    public BMCell getSkillApplyPos() {
        return skillApplyPos;
    }

    public void setSkillApplyPos(BMCell skillApplyPos) {
        this.skillApplyPos = skillApplyPos;
    }

    public BMBaseSkill(BMField field) {
        this.field = field;
    }

    public Collection<BMResource> getCost() {
        return resourceHolder.getResourceList();
    }
    
    public void setCost(Collection<BMResource> cost){
        resourceHolder.addAllRecources(cost);
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public BMField getField() {
        return field;
    }

    public Collection<BMResource> getResources() {
        return resourceHolder.getResourceList();
    }

    public abstract void use();
}
