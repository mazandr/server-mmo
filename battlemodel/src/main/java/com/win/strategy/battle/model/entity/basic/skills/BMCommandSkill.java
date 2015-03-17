package com.win.strategy.battle.model.entity.basic.skills;

import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.markers.BMMoveMotivatorMarker;

/**
 *
 * @author okopach
 */
public class BMCommandSkill extends BMBaseSkill {

    private int radius;
    private int expireTime;

    public BMCommandSkill(BMField field) {
        super(field);
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public void use() {
        BMMoveMotivatorMarker marker = new BMMoveMotivatorMarker(expireTime, radius, getSkillApplyPos());
        marker.prepareParams();
        getField().getDurableActions().add(marker);
    }
}
