package com.win.strategy.battle.model.entity;

import com.win.strategy.battle.model.entity.basic.BMEntity;
import com.win.strategy.battle.model.entity.basic.BMObject;
import com.win.strategy.battle.model.entity.basic.IBMAttactable;
import com.win.strategy.battle.model.entity.basic.IBMIndentifier;
import com.win.strategy.battle.model.entity.basic.IBMLocable;
import com.win.strategy.battle.model.entity.basic.actions.BMActionProperties;
import com.win.strategy.battle.model.entity.basic.actions.BMActionProperties.BMActionPropertiesHolder;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author vlischyshyn
 */
public class BMCell extends BMObject {

    public static String getIdentifierFromPos(int posX, int posY) {
        return new StringBuilder().append(posX).append("x").append(posY).toString();
    }
    
    private int posX;
    private int posY;
    private BMMovementType movementType;
    private BMCellType type;
    private Map<Object, BMEntity> innerObjects = new HashMap<>();
    private BMField field;
    private BMEntity building;
    private BMEntity trash;
    private Map<String, BMActionPropertiesHolder> effects = new HashMap<>();

    public void addEffect(BMActionProperties effectProperties) {
        BMActionPropertiesHolder effectCategory = effects.get(effectProperties.effectKey());
        if (effectCategory == null) {
            effectCategory = new BMActionPropertiesHolder();
            effects.put(effectProperties.effectKey(), effectCategory);
        }
        if (effectCategory.add(effectProperties)) {
            for (BMEntity obj : innerObjects.values()) {
                if (IBMAttactable.class.isInstance(obj)) {
                    ((IBMAttactable) obj).addEffect(effectProperties);
                }
            }
        }
    }

    private void addEffect(BMEntity entiry) {
        for (BMActionPropertiesHolder holder : effects.values()) {
            for (BMActionProperties effect : holder) {
                if (IBMAttactable.class.isInstance(entiry)) {
                    ((IBMAttactable) entiry).addEffect(effect);
                }
            }
        }
    }

    public void invalidate() {
        for (BMActionPropertiesHolder holder : effects.values()) {
            holder.invalidate();
        }
    }

    public BMEntity getBuilding() {
        return building;
    }

    public BMMovementType getMovementType() {
        return movementType;
    }

    public void setMovementType(BMMovementType movementType) {
        this.movementType = movementType;
    }

    public BMField getField() {
        return field;
    }

    public void setField(BMField field) {
        this.field = field;
    }

    @Override
    public String getIdentifier() {
        return getIdentifierFromPos(this.posX, this.posY);
    }

    public Map<Object, BMEntity> getInnerObjects() {
        return innerObjects;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public BMCellType getType() {
        return type;
    }

    public void setType(BMCellType type) {
        this.type = type;
    }

    public int squreDistanceBetween(BMCell cell) {
        int dx = this.getPosX() - cell.getPosX();
        int dy = this.getPosY() - cell.getPosY();
        return dx * dx + dy * dy;
    }

    public boolean evenCell() {
        return (posX + posY) % 2 == 0;
    }

    public double distanceBetween(BMCell cell) {
        return Math.sqrt(squreDistanceBetween(cell));
    }

    public void putObject(BMEntity context) {
        innerObjects.put(context.getIdentifier(), context);

        if (IBMLocable.class.isInstance(context)) {
            if (!((IBMLocable) context).isVisible()) {
                return;
            }
        }

        addEffect(context);

        if (context.innerType().contains(BMEntity.BMEntityType.building)) {
            building = context;
        } else if (context.innerType().contains(BMEntity.BMEntityType.trash)) {
            trash = context;
        }
    }

    public BMEntity getTrash() {
        return trash;
    }

    public void setTrash(BMEntity trash) {
        this.trash = trash;
    }

    public void removeObject(IBMIndentifier context) {
        innerObjects.remove(context.getIdentifier());
        
        if (context.equals(building)) {
            building = null;
        } else if (context.equals(trash)) {
            trash = null;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        BMCell other = (BMCell) obj;
        return this.posX == other.getPosX() && this.posY == other.getPosY();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.posX;
        hash = 97 * hash + this.posY;
        return hash;
    }

    @Override
    public String toString() {
        return String.format("(%d %d)", posX, posY);
    }

    @Override
    public Map<String, Object> toMapObject() {
        Map<String, Object> result = super.toMapObject();
        result.put("posX", this.posX);
        result.put("posY", this.posY);
        result.put("type", this.type.toString());
        result.put("movmentType", this.movementType.toString());
        return result;
    }
}
