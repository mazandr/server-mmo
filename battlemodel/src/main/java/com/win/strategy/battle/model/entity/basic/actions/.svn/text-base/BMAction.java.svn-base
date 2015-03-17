package com.win.strategy.battle.model.entity.basic.actions;

import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;
import java.util.UUID;

/**
 *
 * @author okopach
 */
public class BMAction {

    private String identifier;
    private BMFractionEntity actionTarget;
    private BMCell actionPosition;
    private BMActionProperties properties;
    private BMFractionEntity entity;
    private int accActionTime = 0;

    public BMAction(BMActionProperties properties) {
        this.identifier = UUID.randomUUID().toString();
        this.properties = properties;
        this.properties.initPulsTime();
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public BMFractionEntity getActionTarget() {
        return actionTarget;
    }

    public void setActionTarget(BMFractionEntity actionTarget) {
        this.actionTarget = actionTarget;
    }

    public BMCell getActionPosition() {
        return actionPosition;
    }

    public void setActionPosition(BMCell actionPosition) {
        this.actionPosition = actionPosition;
    }

    public BMActionProperties getProperties() {
        return properties;
    }

    public void setProperties(BMActionProperties properties) {
        this.properties = properties;
    }

    public BMFractionEntity getEntity() {
        return entity;
    }

    public void setEntity(BMFractionEntity entity) {
        this.entity = entity;
    }

    private int getActionTime() {
        if (entity == null || properties == null || properties.getActionSpeed() == 0) {
            return 0;
        }
        double koeficient = (properties.getActionSpeed() * getProperties().getField().getSpeedKoef() * getProperties().getField().getSpeedMultiplier()) / 10000;
        if (getActionTarget() != null) {
            double distance = getActionTarget().getPosition().distanceBetween(entity.getPosition());
            distance += 0.0001;
            return (int) (distance / koeficient);
        } else if (getActionPosition() != null) {
            double distance = getActionPosition().distanceBetween(entity.getPosition());
            distance += 0.0001;
            return (int) (distance / koeficient);
        }
        return 0;
    }

    public void incActionTime() {
        accActionTime += getProperties().getField().getGameProcessStep();
    }

    public boolean canActionApply() {
        if (accActionTime >= getActionTime()) {
            return true;
        } else {
            return false;
        }
    }

    public void resetActionTime() {
        accActionTime = 0;
    }
}
