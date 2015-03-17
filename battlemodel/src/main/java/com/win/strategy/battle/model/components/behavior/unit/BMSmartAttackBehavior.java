package com.win.strategy.battle.model.components.behavior.unit;

import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.BMEntityActionState;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.basic.buildings.BMBaseBuilding;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;
import com.win.strategy.battle.model.entity.basic.buildings.BMCoverBuilding;
import com.win.strategy.battle.model.entity.basic.units.BMBaseUnit;
import com.win.strategy.battle.model.markers.BMMoveMotivatorMarker;
import com.win.strategy.common.utils.ProtocolStrings;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BMSmartAttackBehavior extends BMBaseUnitBehavior {

    private BMMoveMotivatorMarker marker = null;

    private boolean attackExecution() {
        if (attack.getTarget() != null) {
            BMFractionEntity attackableTarget = (BMFractionEntity) attack.getTarget();

            if (attackableTarget.isAlive() && attackableTarget.isInGame()) {
                attackPos = attackableTarget.getCenter();
                targetDest = attackableTarget.nearestDestCell((BMFractionEntity) getEntity(), location.getPosition(), attackPos, vision.getAttackRange());

                attackableTarget.addReservedCell((BMFractionEntity) getEntity(), targetDest);
                attack.setReactPos(attackPos);
                BMCoverBuilding cover = vision.getCover(attackableTarget);
                if (cover != null) {
                    BMCell coverPos = cover.getCloserDestPos(location.getPosition());
                    move.setDestinationCell(coverPos);
                    if (location.getPosition().equals(coverPos)) {
                        cover.putUnit((BMBaseUnit) getEntity());
                    }
                    if (!((BMBaseUnit) getEntity()).isActionOnMove()) {
                        return true;
                    }
                } else {
                    move.setDestinationCell(targetDest);
                }

                if (vision.canAttact(attackPos) && getEntity().isInGame()) {
                    if (!((BMBaseUnit) getEntity()).isActionOnMove()) {
                        getEntity().setActionState(BMEntityActionState.idle);
                    }

                    attack.react();

                    if (!attackableTarget.isAlive() || !attackableTarget.isInGame()) {
                        clearFocus();
                    }

                    if (!((BMBaseUnit) getEntity()).isActionOnMove()) {
                        return false;
                    }

                    if (cover == null) {
                        return false;
                    }
                }
            } else {
                clearFocus();
            }
        }
        return true;
    }

    @Override
    public void behave(int step) {
        if (!heals.isAlive() || !getEntity().isInGame()) {
            return;
        }
        boolean needMove = true;
        tryDisableMarker();

        if (marker != null && !marker.expired()) {
            if (marker.getTargetEntity() != null) {
                attack.setTarget(marker.getTargetEntity());
                needMove = attackExecution();
            } else {
                move.setDestinationCell(marker.getTargetCell());
            }
        } else {
            manageTarget();
            needMove = attackExecution();
        }

        if (getEntity().isInGame() && heals.isAlive() && needMove) {
            move.move(step);
        }
    }

    @Override
    protected void clearFocus() {
        super.clearFocus();
        BMCell attackTarget = getNextAttackDest();
        move.setDestinationCell(attackTarget);
    }

    @Override
    public void notify(Object arg) {
        // TODO Auto-generated method stub
    }

    @Override
    public void init(Object initParams) {
        marker = checkMarcker(initParams);
        if (marker == null) {
            move.setDestinationCell(getNextAttackDest());
        }
    }

    private BMMoveMotivatorMarker checkMarcker(Object initParams) {
        if (initParams == null) {
            return marker;
        }
        try {
            Map<String, Object> params = (Map<String, Object>) initParams;
            return (BMMoveMotivatorMarker) params.get("marker");
        } catch (Exception e) {
            return marker;
        }
    }

    private void tryDisableMarker() {
        if (marker != null) {

            boolean disableMarker = marker.expired();
            disableMarker = disableMarker || marker.getTargetCell().equals(location.getPosition());
            disableMarker = disableMarker || (marker.getTargetEntity() != null
                    && (!marker.getTargetEntity().isAlive() || !marker.getTargetEntity().isInGame()));

            if (disableMarker) {
                marker = null;
                clearFocus();
            }
        }
    }

    private BMCell getNextAttackDest() {
        if (location.getPosition() == null) {
            return null;
        }
        List<BMBaseBuilding> buildings;
        if (((BMFractionEntity) getEntity()).getFraction() == BMField.ALLY_FRACTION_IDX) {
            buildings = getEntity().getField().getEnemyWinCondBuildings();
        } else {
            buildings = getEntity().getField().getAllyWinCondBuildings();
        }
        double dest = Integer.MAX_VALUE;
        BMCell cell = null;

        for (BMBaseBuilding b : buildings) {
            if (b.canBeFocused() && b.isAlive() && b.isInGame()) {

                BMCell nearestCell = b.getCloserDestPos(location.getPosition());
                double d = location.getPosition().distanceBetween(nearestCell);
                if (d < dest) {
                    dest = d;
                    cell = nearestCell;
                }
            }
        }
        return cell;
    }

    @Override
    public Map<String, Object> toMapObject() {
        Map<String, Object> result = new HashMap<>();
        if (marker != null && !marker.expired()) {
            result.put(ProtocolStrings.MOTIVATED, true);
        }
        return result;
    }
}
