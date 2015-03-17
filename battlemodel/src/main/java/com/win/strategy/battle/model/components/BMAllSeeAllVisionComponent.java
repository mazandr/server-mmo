package com.win.strategy.battle.model.components;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.basic.BMAttackPriorityType;
import com.win.strategy.battle.model.entity.basic.BMEntity;
import com.win.strategy.battle.model.entity.basic.BMFractionEntity;
import com.win.strategy.battle.model.entity.basic.IBMLocable;
import com.win.strategy.battle.model.entity.basic.IBMTargetable;
import com.win.strategy.battle.model.entity.basic.buildings.BMCoverBuilding;
import com.win.strategy.battle.model.entity.basic.units.BMBaseUnit;

/**
 * Vision component used in finding targets, that can be attacked
 *
 * @author vlischyshyn
 */
public class BMAllSeeAllVisionComponent extends BMBaseComponent {

    private int visionRange;
    private int attackRange;
    private Set<BMFractionEntity> targets;
    private Set<BMCoverBuilding> covers;
    private BMField field;
    private BMFractionEntity entity;
    private BMAttackPriorityType attackPriorityType = BMAttackPriorityType.any;
    private int compareCoef = 10000;

    public BMAllSeeAllVisionComponent() {
        this.setIdentifier("vision");
        this.targets = new TreeSet<>(new Comparator<BMFractionEntity>() {
            @Override
            public int compare(BMFractionEntity o1, BMFractionEntity o2) {
                long d1 = getPos().squreDistanceBetween(o1.getPosition());
                long d2 = getPos().squreDistanceBetween(o2.getPosition());

                if (attackPriorityType != BMAttackPriorityType.any) {
                    d1 = (o1.getPriorityState() == attackPriorityType) ? d1 : d1 * compareCoef;
                    d2 = (o2.getPriorityState() == attackPriorityType) ? d2 : d2 * compareCoef;
                }
                return Long.compare(d1, d2);
            }
        });

        this.covers = new TreeSet<>(new Comparator<BMFractionEntity>() {
            @Override
            public int compare(BMFractionEntity o1, BMFractionEntity o2) {
                long d1 = getPos().squreDistanceBetween(o1.getPosition());
                long d2 = getPos().squreDistanceBetween(o2.getPosition());

                return Long.compare(d1, d2);
            }
        });
    }

    public void calculateVision() {
        if (getPos() == null) {
            return;
        }

        entity = (BMFractionEntity) getEntity();
        field = entity.getField();

        targets.clear();

        fillTargets();
    }

    public IBMTargetable findTarget() {
        calculateVision();
        if (!targets.isEmpty()) {
            return targets.iterator().next();
        }
        return null;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public int getVisionRange() {
        return visionRange;
    }

    public void setAttackRange(int attackRange) {
        this.attackRange = attackRange;
    }

    public void setVisionRange(int visionRange) {
        this.visionRange = visionRange;
    }

    public boolean canAttact(BMCell targetPos) {
        return getPos().distanceBetween(targetPos) < attackRange;
    }

    private BMCell getPos() {
        BMFractionEntity fe = (BMFractionEntity) getEntity();
        return fe.getCenter();
    }

    @Override
    public void notify(Object arg) {
    }

    /**
     * Check if entity can be focused, if true then method returns this entity,
     * in other way it returns null;
     *
     * @param e entity
     * @return {@link BMFractionEntity} target;
     */
    private BMFractionEntity canAddEntityToTarget(BMEntity e) {
        if (IBMTargetable.class.isInstance(e)) {
            BMFractionEntity target = (BMFractionEntity) e;

            if (target.getFraction() != entity.getFraction()
                    && target.isInGame()
                    && target.isAlive()
                    && target.isVisible()
                    && target.canBeFocused()) {
                return target;
            }
        }
        return null;
    }

    public void setAttackPriorityType(BMAttackPriorityType attackPriorityType) {
        this.attackPriorityType = attackPriorityType;
    }

    public BMAttackPriorityType getAttackPriorityType() {
        return this.attackPriorityType;
    }

    private void fillTargets() {
        for (BMFractionEntity e : field.getAllEntities()) {
            BMFractionEntity target = canAddEntityToTarget(e);
            if (target != null) {
                targets.add(target);
            }
        }
    }

    private BMCoverBuilding canAddEntityToCovers(BMEntity entity, BMFractionEntity target) {
        if (entity.innerType().contains(BMEntity.BMEntityType.cover)) {
            BMCoverBuilding fe = (BMCoverBuilding) entity;
            BMBaseUnit self = (BMBaseUnit) getEntity();

            boolean result = fe.isAlive() && fe.isInGame();
            result = result && (fe.getFraction() == self.getFraction()
                    || fe.getFraction() == BMField.NETRAL_FRACTION_IDX);
            if (result) {
                int d1 = ((IBMLocable) getEntity()).getPosition().squreDistanceBetween(target.getCenter());
                int d2 = ((IBMLocable) getEntity()).getPosition().squreDistanceBetween(fe.getCenter());

                result = result && (d1 >= d2);
                result = result && (fe.getFreeHousingSpace() >= self.getHousingSpace());

                if (result) {
                    List<BMBaseUnit> units = fe.getUnits();
                    if (!units.isEmpty()) {
                        BMBaseUnit firstUnit = units.get(0);
                        result = result && getEntity().getType().equals(firstUnit.getType());
                    }
                }
            }
            return result ? fe : null;
        }

        return null;
    }

    public BMCoverBuilding getCover(BMFractionEntity target) {
        covers.clear();
        BMCell pos = target.getCenter();
        int x = pos.getPosX();
        int y = pos.getPosY();
        int i, j;
        int range = attackRange;
        for (i = -range; i <= range; i++) {
            for (j = -range; j <= range; j++) {
                if (i * i + j * j < range * range) {
                    BMCell cell = field.getCell(x + i, y + j);
                    if (cell != null) {
                        BMEntity building = cell.getBuilding();
                        if (building != null) {
                            BMCoverBuilding cover = canAddEntityToCovers(building, target);
                            if (cover != null) {
                                covers.add(cover);
                            }
                        }
                    }
                }
            }
        }
        if (!covers.isEmpty()) {
            return covers.iterator().next();
        }
        return null;
    }
}
