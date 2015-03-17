package com.win.strategy.battle.model.components;

import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.common.utils.ProtocolStrings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author vlischyshyn
 */
public class BMLargeLocatorComponent extends BMBaseComponent {

    private int sizeX;
    private int sizeY;
    private BMCell position;
    private BMCell center;
    private List<BMCell> innerArea = new ArrayList<>();
    private List<BMCell> outerArea = new ArrayList<>();
    private int margin = 1;
    private boolean visible = true;
    private BMCell[] corners = new BMCell[4];
    private boolean rotated = false;

    public boolean isRotated() {
        return rotated;
    }

    public void setRotated(boolean rotated) {
        this.rotated = rotated;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public BMCell[] getCorners() {
        return corners;
    }

    public BMLargeLocatorComponent() {
        this.setIdentifier("locator");
    }

    private void recalculateMetrics() {
        if (position == null || sizeX == 0 || sizeY == 0) {
            return;
        }

        BMField field = position.getField();
        int x = position.getPosX();
        int y = position.getPosY();

        for (BMCell cell : innerArea) {
            cell.removeObject(this.getEntity());
        }
        innerArea.clear();
        outerArea.clear();

        int centerX = x + sizeX / 2;
        int centerY = y + sizeY / 2;

        center = field.getCell(centerX, centerY);

        corners[0] = position;
        corners[1] = field.getCell(x + sizeX - 1, y);
        corners[2] = field.getCell(x, y + sizeY - 1);
        corners[3] = field.getCell(x + sizeX - 1, y + sizeY - 1);

        for (int ix = x; ix < x + sizeX; ix++) {
            for (int iy = y; iy < y + sizeY; iy++) {

                BMCell cell = field.getCell(ix, iy);
                if (cell != null) {
                    if (ix >= x + margin && ix < x + sizeX - margin
                            && iy >= y + margin && iy < y + sizeY - margin) {
                        innerArea.add(cell);
                        cell.putObject(this.getEntity());
                    } else {
                        outerArea.add(cell);
                    }
                }
            }
        }
    }

    public void clearLocation() {
        for (BMCell cell : innerArea) {
            cell.removeObject(this.getEntity());
        }
    }

    public boolean hasCenter() {
        return center != null;
    }

    public BMCell getPosition() {
        return position;
    }

    public void setPosition(BMCell position) {
        if (position != this.position) {
            this.position = position;
            recalculateMetrics();
            this.notifyEntity(this);
        }
    }

    public int getSizeX() {
        return sizeX;
    }

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }

    public BMCell getCenter() {
        return center;
    }

    public List<BMCell> getInnerArea() {
        return innerArea;
    }

    public List<BMCell> getOuterArea() {
        return outerArea;
    }

    @Override
    public Map<String, Object> toMapObject() {
        Map<String, Object> result = new HashMap<>();
        if (position != null) {
            result.put(ProtocolStrings.POS_X, position.getPosX());
            result.put(ProtocolStrings.POS_Y, position.getPosY());
        }
        result.put(ProtocolStrings.SX, sizeX);
        result.put(ProtocolStrings.SY, sizeY);
        if (rotated) {
            result.put(ProtocolStrings.ORNT, 1);
        } else {
            result.put(ProtocolStrings.ORNT, 0);
        }

        if (hasCenter()) {
            result.put("centerX", center.getPosX());
            result.put("centerY", center.getPosY());
        }

        return result;
    }

    @Override
    public void notify(Object arg) {
    }

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }
}
