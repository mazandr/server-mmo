package com.win.strategy.battle.model.components;

import com.win.strategy.battle.model.entity.basic.actions.BMAction;
import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.BMEntityActionState;
import com.win.strategy.battle.model.entity.basic.BMEntity;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;
import com.win.strategy.battle.model.entity.basic.IBMTargetable;
import com.win.strategy.common.utils.ProtocolStrings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author vlischyshyn
 */
public class BMReactionComponent extends BMBaseComponent {

    private BMFractionEntity target;
    private List<BMAction> actions = new ArrayList<>();
    private BMCell reactPos;

    @Override
    public void setEntity(BMEntity entity) {
        super.setEntity(entity);
    }

    public BMReactionComponent() {
        this.setIdentifier("reaction");
    }

    public BMCell getReactPos() {
        return reactPos;
    }

    public void setReactPos(BMCell reactPos) {
        this.reactPos = reactPos;
    }

    public BMFractionEntity getTarget() {
        return target;
    }

    public void setTarget(IBMTargetable target) {
        if (this.target != target) {
            this.target = (BMFractionEntity) target;
            notifyEntity(this);
        }
    }

    public void react() {

        if (target != null
                && !target.isAlive()
                && !target.isInGame()) {
            return;
        }

        if (!this.getEntity().getActionState().contains(BMEntityActionState.attack)) {
            this.notifyEntity(this);
        }

        if (this.getEntity().getActionState().contains(BMEntityActionState.move)) {
            this.getEntity().addActionState(BMEntityActionState.attack);
        } else {
            this.getEntity().setActionState(BMEntityActionState.attack);
        }

        for (BMAction action : actions) {
            action.setActionTarget(target);
            action.setActionPosition(reactPos);
            getEntity().getField().getActionExecutor().addAction(action);
        }
    }

    public void addReactionAction(BMAction action) {
        action.getProperties().setOwner(getEntity());
        action.setEntity((BMFractionEntity) getEntity());
        actions.add(action);
    }

    public List<BMAction> getActions() {
        return actions;
    }

    @Override
    public void notify(Object arg) {
    }

    @Override
    public Map<String, Object> toMapObject() {
        Map<String, Object> result = new HashMap<>();
        if (this.target != null) {
            result.put(ProtocolStrings.TARGET, this.target.getIdentifier());

            if (reactPos != null) {
                result.put("targetPosX", this.reactPos.getPosX());
                result.put("targetPosY", this.reactPos.getPosY());
            }
        }
        return result;
    }
}
