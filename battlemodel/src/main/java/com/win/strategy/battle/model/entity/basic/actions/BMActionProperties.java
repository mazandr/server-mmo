package com.win.strategy.battle.model.entity.basic.actions;

import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.basic.BMObject;
import com.win.strategy.battle.model.entity.basic.effects.BMEffectApplyType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vlischyshyn
 */
public class BMActionProperties extends BMObject implements Cloneable {

    private int radius;
    private int fraction;
    private int tick;
    private int value;
    private int time;
    private int accTime = 0;
    private int maxStack = 1;
    private int captureTargetTime = 0;
    private int reloadTime = 0;
    private int actionSpeed = 0;
    private boolean modifyOnlyPriorityTargets = false;
    private double favoriteTargetMultiplier = 1;
    private int selfDamage = 0;
    private BMAbilitylType abilitylType;
    private BMEffectApplyType applyType;
    private BMActionType actionType;
    private BMActionTargetType actionTargetType;
    private BMField field;
    private BMObject owner;
    private List<BMCell> zone;
    private String effectKey = "";
    private BMActionPropertiesGroup group;
    private int pulsTime = 0;

    public BMActionProperties() {
    }

    public BMActionProperties(BMActionProperties source) {
        this.radius = source.getRadius();
        this.fraction = source.getFraction();
        this.tick = source.getTick();
        this.value = source.getValue();
        this.time = source.getTime();
        this.maxStack = source.getMaxStack();
        this.abilitylType = source.getAbilitylType();
        this.applyType = source.getApplyType();
        this.actionType = source.getActionType();
        this.actionTargetType = source.getActionTargetType();
        this.selfDamage = source.getSelfDamage();
        this.owner = source.getOwner();
        this.modifyOnlyPriorityTargets = source.isModifyOnlyPriorityTargets();
        this.favoriteTargetMultiplier = source.getFavoriteTargetMultiplier();
        this.effectKey = source.effectKey();
        this.zone = source.getZone();
        this.field = source.getField();
        this.actionSpeed = source.getActionSpeed();
        this.captureTargetTime = source.getCaptureTargetTime();
        this.reloadTime = source.getReloadTime();
        this.accTime = source.accTime;
        this.group = source.group;
        this.pulsTime = source.pulsTime;
    }

    public BMField getField() {
        return field;
    }

    public void setField(BMField field) {
        this.field = field;
    }

    public BMObject getOwner() {
        return owner;
    }

    public void setOwner(BMObject owner) {
        this.owner = owner;
    }

    public int getSelfDamage() {
        return selfDamage;
    }

    public void setSelfDamage(int selfDamage) {
        if (selfDamage < 0) {
            selfDamage = 0;
        }
        this.selfDamage = selfDamage;
    }

    public boolean isModifyOnlyPriorityTargets() {
        return modifyOnlyPriorityTargets;
    }

    public void setModifyOnlyPriorityTargets(boolean modifyOnlyPriorityTargets) {
        this.modifyOnlyPriorityTargets = modifyOnlyPriorityTargets;
    }

    public double getFavoriteTargetMultiplier() {
        return favoriteTargetMultiplier;
    }

    public void setFavoriteTargetMultiplier(double favoriteTargetMultiplier) {
        if (favoriteTargetMultiplier < 1) {
            favoriteTargetMultiplier = 1;
        }
        this.favoriteTargetMultiplier = favoriteTargetMultiplier;
    }

    protected void incAccTime() {
        accTime += field.getGameProcessStep();
    }

    private boolean expired() {
        return time <= accTime;
    }

    protected boolean canPuls() {
        return accTime >= pulsTime;
    }

    protected void resetPusl() {
        accTime = 0;
        pulsTime = captureTargetTime + reloadTime;
    }

    public BMActionProperties clone() {
        return new BMActionProperties(this);
    }

    public void addZoneCell(BMCell cell) {
        if (zone == null) {
            zone = new ArrayList<>();
        }

        zone.add(cell);
    }

    public BMActionTargetType getActionTargetType() {
        return actionTargetType;
    }

    public void setActionTargetType(BMActionTargetType actionTargetType) {
        this.actionTargetType = actionTargetType;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public BMActionType getActionType() {
        return actionType;
    }

    public void setActionType(BMActionType actionType) {
        this.actionType = actionType;
    }

    public String effectKey() {
        if (this.effectKey.isEmpty()) {
            genEffectKey();
        }
        return this.effectKey;
    }

    public BMAbilitylType getAbilitylType() {
        return abilitylType;
    }

    public void setAbilitylType(BMAbilitylType abilitylType) {
        this.abilitylType = abilitylType;
    }

    public int getFraction() {
        return fraction;
    }

    public void setFraction(int fraction) {
        this.fraction = fraction;
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getMaxStack() {
        return maxStack;
    }

    public void setMaxStack(int max) {
        if (max < 1) {
            max = 1;
        }
        this.maxStack = max;
    }

    public BMActionPropertiesGroup getGroup() {
        return group;
    }

    public void setGroup(BMActionPropertiesGroup group) {
        this.group = group;
    }

    public BMEffectApplyType getApplyType() {
        return applyType;
    }

    public void setApplyType(BMEffectApplyType applyType) {
        this.applyType = applyType;
    }

    public List<BMCell> getZone() {
        return zone;
    }

    public int getCaptureTargetTime() {
        return captureTargetTime;
    }

    public void setCaptureTargetTime(int captureTargetTime) {
        this.captureTargetTime = captureTargetTime;
    }

    public int getReloadTime() {
        return reloadTime;
    }

    public void setReloadTime(int reloadTime) {
        this.reloadTime = reloadTime;
    }

    public int getActionSpeed() {
        return actionSpeed;
    }

    public void setActionSpeed(int actionSpeed) {
        this.actionSpeed = actionSpeed;
    }

    public void initPulsTime() {
        this.pulsTime = this.captureTargetTime;
    }

    private void genEffectKey() {
        StringBuilder key = new StringBuilder();
//        if (owner != null) {
//            key.append(owner.getIdentifier());
//        }
        key.append(this.abilitylType.toString());
        key.append(this.applyType.toString());
        key.append(this.fraction);
        effectKey = key.toString();
    }

    public static class BMActionPropertiesHolder extends ArrayList<BMActionProperties> {

        @Override
        public boolean add(BMActionProperties e) {
            if (e == null) {
                return false;
            }
            if (e.getMaxStack() > this.size()) {
                return super.add(e);
            } else {
                return false;
            }
        }

        public void invalidate() {
            for (BMActionProperties e : this) {
                if (e.expired()) {
                    this.remove(e);
                } else {
                    e.incAccTime();
                }
            }
        }
    }

    public static enum BMActionPropertiesGroup {

        offensive,
        defensive
    }
}
