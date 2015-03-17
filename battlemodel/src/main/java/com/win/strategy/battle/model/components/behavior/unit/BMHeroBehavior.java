package com.win.strategy.battle.model.components.behavior.unit;

import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.basic.IBMTargetable;

/**
 *
 * @author vlischyshyn
 */
public class BMHeroBehavior extends BMBaseUnitBehavior {

    private IBMTargetable focusTarget;
    private BMCell cmdCell = null;

    @Override
    public void behave(int step) {
//        if (!heals.isAlive() || !getEntity().isInGame() || cmdCell == null) {
//            return;
//        }
//        BMBaseBuilding targetBuilding = (BMBaseBuilding) cmdCell.getBuilding();
//
//        if (targetBuilding != null) {
//
//            if (!targetBuilding.equals(focusTarget)) {
//
//                attackPos = targetBuilding.getCloserAttackPos(location.getPosition());
//                targetDest = targetBuilding.getCloserDestPos(location.getPosition());
//                attack.setReactPos(attackPos);
//                move.setDestinationCell(targetDest);
//
//            }
//
//            if (targetBuilding.getBuildingState() == BMBuildingState.occupied
//                    || targetBuilding.innerType().contains(BMEntity.BMEntityType.defence)
//                    || targetBuilding.innerType().contains(BMEntity.BMEntityType.wall)) {
//
//                attack.setTarget(targetBuilding);
//
//                if (vision.canAttact(attackPos)) {
//                    attack.react();
//
//                    if (!attack.getTarget().isAlive()) {
//                        getEntity().setActionState(BMEntityActionState.idle);
//                        attack.setTarget(null);
//                        move.resetPath();
//                    }
//                    return;
//                }
//            } else {
//                if (targetBuilding.canBeCaptured()) {
//                    if (location.getPosition().equals(targetDest)) {
//                        getEntity().getField().captureBuilding(targetBuilding.getIdentifier());
//                        targetBuilding.setModified(true);
//
//                        // TODO add capturing time
////                        getEntity().setActionState(BMEntityActionState.capture);
//                        cmdCell = null;
//                        return;
//                    }
//                }
//            }
//        } else {
//            if (location.getPosition().equals(cmdCell)) {
//                cmdCell = null;
//                IBMTargetable target;
//                if (focusTarget == null) {
//                    target = vision.findTarget();
//                } else {
//                    target = focusTarget;
//                }
//                if (attack.getTarget() == null) {
//                    if (target != null) {
//                        attack.setTarget(target);
//                    }
//                } else {
//                    if (target != null
//                            && !attack.getTarget().equals(target)) {
//                        attack.setTarget(target);
//                    }
//                }
//
//                if (attack.getTarget() != null) {
//
//                    if (((BMEntity) attack.getTarget()).innerType().contains(BMEntity.BMEntityType.building)) {
//                        BMBaseBuilding tb = (BMBaseBuilding) attack.getTarget();
//
//                        //todo fix !!!
//                        attackPos = tb.getCloserAttackPos(location
//                                .getPosition());
//                    } else {
//                        attackPos = attack.getTarget().getPosition();
//                    }
//
//                    attack.setReactPos(attackPos);
//
//                    if (vision.canAttact(attackPos)) {
//                        attack.react();
//
//                        if (!attack.getTarget().isAlive()) {
//                            getEntity().setActionState(BMEntityActionState.idle);
//                            attack.setTarget(null);
//
//                            focusTarget = null;
//                        }
//                        return;
//                    }
//                }
//            } else {
//                move.setDestinationCell(cmdCell);
//            }
//        }
//        move.move(step);
    }

    @Override
    public void notify(Object arg) {
    }

    @Override
    public void init(Object initParams) {
        if (initParams == null) {
            return;
        }
        try {
            String cmd = String.valueOf(initParams);
            BMCell cell = getEntity().getField().getCellByCode(cmd);
            if (cell != null && !cell.equals(cmdCell)) {
                cmdCell = cell;
            }
        } catch (Exception e) {
        }
    }
}
