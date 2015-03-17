package com.win.strategy.battle.model.components;

import com.win.strategy.battle.model.entity.BMCell;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author vlischyshyn
 */
public class BMSmallLocatorComponent extends BMBaseComponent {

    private BMCell position;
    private boolean visible = true;

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public BMSmallLocatorComponent() {
        this.setIdentifier("locator");
    }

    public BMCell getPosition() {
        return position;
    }

    public void setPosition(BMCell pos) {
        if (pos != this.position) {
            if (this.position != null) {
                this.position.removeObject(this.getEntity());
            }

            this.position = pos;
            this.position.putObject(this.getEntity());
            this.notifyEntity(this);
        }
    }

    @Override
    public void notify(Object arg) {
    }

    public void clearLocation() {
        if (this.position != null) {
            this.position.removeObject(this.getEntity());
        }
    }

    @Override
    public Map<String, Object> toMapObject() {
        Map<String, Object> result = new HashMap<>();
        if (position != null) {
            result.put("posX", position.getPosX());
            result.put("posY", position.getPosY());
        }
        return result;
    }
}
