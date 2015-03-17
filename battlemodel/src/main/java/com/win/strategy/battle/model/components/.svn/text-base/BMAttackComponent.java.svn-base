package com.win.strategy.battle.model.components;

import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.BMEntityActionState;
import java.util.HashMap;
import java.util.Map;

import com.win.strategy.battle.model.entity.basic.BMAttackPriorityType;
import com.win.strategy.battle.model.entity.basic.BMEntity;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;
import com.win.strategy.battle.model.entity.basic.IBMLocable;
import com.win.strategy.battle.model.entity.basic.IBMTargetable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author vlischyshyn
 */
public class BMAttackComponent extends BMBaseComponent {

    private int dps;
    private double attackSpeed;
    private int splashRadius = 0;
    private double attackDuration = 0;
    private double favoriteTargetMultiplier = 1;
    private IBMTargetable target;
    private BMCell attackPosition;
    private BMCell splashAttackNextPosition;
    private boolean instanceSplashDamage;
    private BMAttackPriorityType attackPriorityType = BMAttackPriorityType.any;
    private boolean damageOnlyPriorityType = false;
    private int selfDamage = 0;

    public boolean isDamageOnlyPriorityType() {
        return damageOnlyPriorityType;
    }

    public void setDamageOnlyPriorityType(boolean damageOnlyPriorityType) {
        this.damageOnlyPriorityType = damageOnlyPriorityType;
    }

    public BMAttackComponent() {
        this.setIdentifier("attack");
    }

    public BMAttackPriorityType getAttackPriorityType() {
        return attackPriorityType;
    }

    public void setAttackPriorityType(BMAttackPriorityType attackPriorityType) {
        this.attackPriorityType = attackPriorityType;
    }

    public double getFavoriteTargetMultiplier() {
        return favoriteTargetMultiplier;
    }

    public void setFavoriteTargetMultiplier(double multiplier) {
        if (multiplier > 0) {
            this.favoriteTargetMultiplier = multiplier;
        } else {
            this.favoriteTargetMultiplier = 1;
        }
    }

    public void attack() {
        if (target == null
                || !target.isAlive()
                || !((BMEntity) target).isInGame()) {
            return;
        }

        if (this.getEntity().getActionState().contains(BMEntityActionState.move)) {
            this.getEntity().addActionState(BMEntityActionState.attack);
        } else {
            this.getEntity().setActionState(BMEntityActionState.attack);
        }
        this.notifyEntity(this);

        if (splashRadius > 0) {
            if (instanceSplashDamage) {
                splashAttackNextPosition = ((IBMLocable) getEntity()).getPosition();

            } else {
                if (attackDuration == 0) {
                    splashAttackNextPosition = getEntity().getField()
                            .getCell(target.getPosition().getPosX(), target.getPosition().getPosY());
                }
            }
        }

        attackDuration += getEntity().getField().getGameProcessStep()
                * getEntity().getField().getSpeedMultiplier();

        if (attackDuration >= attackSpeed * 1000) {
            if (splashRadius > 0) {
                splashAttack();
            } else {
                instantAttack();
            }
            ((BMFractionEntity) getEntity()).damage(selfDamage);
            attackDuration = 0;

        }
    }

    private void instantAttack() {
        boolean canDamage = false;
        if (damageOnlyPriorityType) {
            if (target.getPriorityState() == attackPriorityType) {
                canDamage = true;
            }

        } else {
            canDamage = true;
        }

        if (canDamage) {
            int damage = dps;
            if (target.getPriorityState() == attackPriorityType) {
                damage = (int) (dps * favoriteTargetMultiplier);
            }

            this.target.damage(damage);
        }
    }

    private void splashAttack() {
        if (splashAttackNextPosition == null) {
            return;
        }
        BMCell pos = splashAttackNextPosition;
        Set<BMFractionEntity> trgts = new HashSet<>();
        for (int i = -splashRadius; i <= splashRadius; i++) {
            for (int j = -splashRadius; j <= splashRadius; j++) {
                if (i * i + j * j < splashRadius * splashRadius) {
                    BMCell cell = pos.getField().getCell(pos.getPosX() + i, pos.getPosY() + j);
                    if (cell != null) {
                        for (BMEntity t : cell.getInnerObjects().values()) {
                            BMFractionEntity trg = (BMFractionEntity) t;
                            if (trg.getFraction() != ((BMFractionEntity) getEntity()).getFraction()) {
                                trgts.add(trg);
                            }
                        }
                    }
                }
            }
        }
        int damage;
        boolean canDamage = false;
        for (BMFractionEntity tg : trgts) {

            if (damageOnlyPriorityType) {
                if (tg.getPriorityState() == attackPriorityType) {
                    canDamage = true;
                }
            } else {
                canDamage = true;
            }

            if (canDamage) {
                damage = dps;
                if (target.getPriorityState() == attackPriorityType) {
                    damage = (int) (dps * favoriteTargetMultiplier);
                }

                tg.damage(damage);
            }

            canDamage = false;
        }
    }

    public double getAttackSpeed() {
        return attackSpeed;
    }

    public int getDps() {
        return dps;
    }

    public int getSplashRadius() {
        return splashRadius;
    }

    public IBMTargetable getTarget() {
        return target;
    }

    public void setAttackSpeed(double attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public void setDps(int dps) {
        this.dps = dps;
    }

    public void setSplashRadius(int splashRadius) {
        this.splashRadius = splashRadius;
    }

    public void setTarget(IBMTargetable target) {
        if (this.target != target) {
            this.target = target;
            notifyEntity(this);
        }
    }

    @Override
    public Map<String, Object> toMapObject() {
        Map<String, Object> result = new HashMap<>();
        if (this.target != null) {
            result.put("target", this.target.getIdentifier());

            if (attackPosition != null) {
                result.put("targetPosX", this.attackPosition.getPosX());
                result.put("targetPosY", this.attackPosition.getPosY());
            }

        }
        return result;
    }

    @Override
    public void notify(Object arg) {
    }

    public void setAttackPosition(BMCell attackPos) {
        attackPosition = attackPos;
    }

    public boolean isInstanceSplashDamage() {
        return instanceSplashDamage;
    }

    public void setInstanceSplashDamage(boolean instanceSplashDamage) {
        this.instanceSplashDamage = instanceSplashDamage;
    }

    public void setSelfDamage(int selfDamage) {
        this.selfDamage = selfDamage;
    }
}
