package com.win.strategy.battle.model.components;

import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.BMEntityActionState;
import com.win.strategy.battle.model.entity.basic.IBMLocable;
import com.win.strategy.battle.model.entity.basic.units.BMBaseUnit;
import com.win.strategy.common.utils.ProtocolStrings;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author vlischyshyn
 */
public class BMCarrierComponent extends BMBaseComponent {

    private int maxCapacity = 0;
    private int currentCapacity = 0;
    private List<BMBaseUnit> carriedUnits = new ArrayList<>();
    private TreeSet<BMCell> firstPriorityLandingArea;
    private Set<BMCell> secondPriorityArea = new HashSet<>();
    private boolean useAreas = false;

    public BMCarrierComponent() {

        firstPriorityLandingArea = new TreeSet<>(new Comparator<BMCell>() {
            @Override
            public int compare(BMCell o1, BMCell o2) {
                BMCell pos = ((IBMLocable) getEntity()).getPosition();
                int d1 = pos.squreDistanceBetween(o1);
                int d2 = pos.squreDistanceBetween(o2);
                return Integer.compare(d1, d2);
            }
        });
    }

    public void initLandingAreas() {
        BMLargeLocatorComponent locator = getEntity().getComponent(BMLargeLocatorComponent.class);
        if (locator == null) {
            useAreas = false;
            return;
        }

        boolean evenCenter = locator.getCenter().evenCell();
        for (BMCell pos : locator.getInnerArea()) {
            if (pos.evenCell() == evenCenter) {
                firstPriorityLandingArea.add(pos);
            } else {
                secondPriorityArea.add(pos);
            }
        }

        for (BMCell pos : locator.getOuterArea()) {
            if (pos.evenCell() == evenCenter) {
                firstPriorityLandingArea.add(pos);
            } else {
                secondPriorityArea.add(pos);
            }
        }
        useAreas = true;
    }

    @Override
    public void notify(Object arg) {
    }

    public int getCurrentSize() {
        return carriedUnits.size();
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public boolean addUnit(BMBaseUnit unit) {
        int capacity = this.currentCapacity + unit.getHousingSpace();
        if (this.maxCapacity >= capacity) {
            this.carriedUnits.add(unit);
            this.currentCapacity = capacity;
            getEntity().setModified(true);
            unit.setActionState(BMEntityActionState.capture);
            unit.setInGame(false);
            unit.setModified(true);
            return true;
        }
        return false;
    }

    public boolean landUnit(BMBaseUnit unit) {
        if (carriedUnits.isEmpty()) {
            return false;
        }

        if (this.carriedUnits.remove(unit)) {
            this.currentCapacity -= unit.getHousingSpace();
            if (this.currentCapacity < 0) {
                this.currentCapacity = 0;
            }
            getEntity().setModified(true);
            unit.setInGame(true);
            return true;
        }
        return false;
    }

    public List<BMBaseUnit> getCarriedUnits() {
        return carriedUnits;
    }

    public void despersionLandUnits() {
        getEntity().setModified(true);
        if (carriedUnits.isEmpty()) {
            return;
        }

        if (!useAreas) {
            onePrionLandUnits();
            return;
        }

        getEntity().setActionState(BMEntityActionState.land);
        for (BMBaseUnit unit : carriedUnits) {
            BMCell point = null;
            if (firstPriorityLandingArea.size() > 0) {
                point = firstPriorityLandingArea.first();
                firstPriorityLandingArea.remove(point);
            } else if (secondPriorityArea.size() > 0) {
                point = secondPriorityArea.iterator().next();
                secondPriorityArea.remove(point);
            } else {
                point = ((IBMLocable) getEntity()).getPosition();
            }

            if (point != null) {
                this.getEntity().getField().enterUnit(unit, point);
                unit.setActionState(BMEntityActionState.land);
                unit.notify(null);
            }
        }
        carriedUnits.clear();
        currentCapacity = 0;
    }

    public void onePrionLandUnits() {
        getEntity().setModified(true);
        if (carriedUnits.isEmpty()) {
            return;
        }
        BMCell point = ((IBMLocable) getEntity()).getPosition();
        getEntity().setActionState(BMEntityActionState.land);
        for (BMBaseUnit unit : carriedUnits) {
            unit.setActionState(BMEntityActionState.land);
            this.getEntity().getField().enterUnit(unit, point);
            unit.notify(null);
        }
        carriedUnits.clear();
        currentCapacity = 0;
    }

    public boolean isEmpty() {
        return carriedUnits.isEmpty();
    }

    @Override
    public Map<String, Object> toMapObject() {
        Map<String, Object> result = new HashMap<>();
        if (!carriedUnits.isEmpty()) {
            List<Object> inv = new ArrayList<>();
            for (BMBaseUnit u : carriedUnits) {
                inv.add(u.toMapObject());
            }
            result.put(ProtocolStrings.INV, inv);
        }
        return result;
    }

    public int getCurrentCapacity() {
        return this.currentCapacity;
    }

    public int getFreeHousingSpace() {
        return maxCapacity - currentCapacity;
    }
}
