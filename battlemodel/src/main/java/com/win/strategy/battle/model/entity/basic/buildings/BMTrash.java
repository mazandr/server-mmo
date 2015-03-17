package com.win.strategy.battle.model.entity.basic.buildings;

import com.win.strategy.battle.model.components.BMLargeLocatorComponent;
import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.basic.BMEntity;
import com.win.strategy.battle.model.entity.basic.IBMLocable;
import java.util.EnumSet;
import java.util.List;

/**
 *
 * @author vlischyshyn
 */
public class BMTrash extends BMEntity implements IBMLocable {

    private BMLargeLocatorComponent location;

    public BMTrash(BMField field) {
        super(field);
        location = addComponent(new BMLargeLocatorComponent());
    }

    @Override
    public EnumSet<BMEntityType> innerType() {
        return EnumSet.of(BMEntityType.trash);
    }

    @Override
    public void notify(Object arg) {
    }

    @Override
    public void setPosition(BMCell cell) {
        location.setPosition(cell);
    }

    @Override
    public BMCell getPosition() {
        return location.getPosition();
    }

    @Override
    public List<BMCell> getLocation() {
        return location.getInnerArea();
    }

    public void setSizeX(int sizeX) {
        location.setSizeX(sizeX);
    }

    public void setSizeY(int sizeY) {
        location.setSizeY(sizeY);
    }

    @Override
    public boolean isVisible() {
        return location.isVisible();
    }

    @Override
    public void setVisible(boolean visible) {
        location.setVisible(visible);
    }

    @Override
    public BMCell getCenter() {
        return location.getCenter();
    }
}
