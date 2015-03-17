package com.win.strategy.battle.model.entity.basic;

import com.win.strategy.battle.model.entity.BMEntityActionState;
import com.win.strategy.battle.model.components.BMBaseComponent;
import com.win.strategy.battle.model.entity.BMField;
import java.util.EnumSet;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author vlischyshyn
 */
public abstract class BMEntity extends BMObject implements IBMNotification {

    private Map<Object, BMBaseComponent> components = new HashMap<>();
    private EnumSet<BMEntityActionState> actionState;
    private boolean modified;
    private String type;
    private BMField field;
    private boolean inGame = false;
    private Map<String, Object> perforatigData = new HashMap<>();

    public BMEntity(BMField field) {
        this.field = field;
        this.actionState = EnumSet.of(BMEntityActionState.idle);
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public <T extends BMBaseComponent> T addComponent(T component) {
        component.setEntity(this);
        components.put(component.getClass(), component);
        return component;
    }

    @SuppressWarnings("unchecked")
    public <T extends BMBaseComponent> T getComponent(Class<T> component) {
        return (T) components.get(component);
    }

    public EnumSet<BMEntityActionState> getActionState() {
        return actionState;
    }

    public String getType() {
        return type;
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public void setActionState(BMEntityActionState actionState) {
        this.actionState = EnumSet.of(actionState);
    }

    public void addActionState(BMEntityActionState actionState) {
        this.actionState.add(actionState);
    }

    public void setType(String type) {
        this.type = type;
    }

    public BMField getField() {
        return field;
    }

    public void setField(BMField field) {
        this.field = field;
    }

    public Map<Object, BMBaseComponent> getComponents() {
        return components;
    }

    public abstract EnumSet<BMEntityType> innerType();

    protected void notificateComponents() {
        for (BMBaseComponent component : components.values()) {
            component.notify(this);
        }
    }

    @Override
    public void notify(Object arg) {
        this.modified = true;
    }

    @Override
    public Map<String, Object> toMapObject() {
        Map<String, Object> result = super.toMapObject();
        result.put("state", this.getActionState());
        result.put("type", this.type);
        for (BMBaseComponent c : components.values()) {
            result.putAll(c.toMapObject());
        }
        return result;
    }

    public Map<String, Object> getPerforatigData() {
        return perforatigData;
    }

    public void setPerforatigData(Map<String, Object> perforData) {
        if (perforData != null) {
            this.perforatigData.putAll(perforData);
        }
    }

    public <T> T getPerfotatingData(String key) {
        Object object = perforatigData.get(key);
        if (object == null) {
            return null;
        }
        try {
            T result = (T) object;
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public void setPerforatigData(String key, Object data) {
        perforatigData.put(key, data);
    }

    public static enum BMEntityType {

        wall, unit, building, defence, cover, trap, trash, transport, hero
    }
}
