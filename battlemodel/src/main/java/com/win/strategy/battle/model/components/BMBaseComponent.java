package com.win.strategy.battle.model.components;

import com.win.strategy.battle.model.entity.basic.BMEntity;
import com.win.strategy.battle.model.entity.basic.BMObject;
import com.win.strategy.battle.model.entity.basic.IBMNotification;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author vlischyshyn
 */
public abstract class BMBaseComponent extends BMObject implements IBMComponent, IBMNotification {

    private BMEntity entity;

    public BMEntity getEntity() {
        return entity;
    }

    public void setEntity(BMEntity entity) {
        this.entity = entity;
    }

    protected void notifyEntity(Object arg) {
        if (entity.isInGame()) {
            entity.notify(arg);
        }
    }

    @Override
    public Map<String, Object> toMapObject() {
        return new HashMap<>();
    }
}
