package com.win.strategy.battle.utils.outputbattle;

import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.BMMovementType;
import com.win.strategy.battle.model.entity.basic.BMEntity;
import com.win.strategy.common.model.IModelCallback;
import java.util.ArrayList;

/**
 *
 * @author okopach
 */
public class BattleConsoleOutput implements IModelCallback {

    private BMField field;

    public BattleConsoleOutput(BMField field) {
        this.field = field;
    }

    @Override
    public void executeInsideLoop() {
        System.out.println(fieldToString(field));
    }

    private String fieldToString(BMField field) {
        String result = "    ";
        for (int x = 0; x < field.getMaxSizeX(); x++) {
            result += String.format("%02d", x);
            result += " ";
        }
        result += "\n";
        for (int y = 0; y < field.getMaxSizeX(); y++) {
            result += String.format("%02d", y);
            result += "| ";

            for (int x = 0; x < field.getMaxSizeY(); x++) {

                BMCell c = field.getCells().get(x).get(y);
                if (c.getInnerObjects().size() > 0) {
                    BMEntity obj = new ArrayList<>(c.getInnerObjects().values()).get(0);

                    if (obj.innerType().contains(BMEntity.BMEntityType.building)) {
                        result += " B ";
                    } else if (obj.innerType().contains(BMEntity.BMEntityType.trap)) {
                        result += " T ";
                    } else {
                        result += obj.getIdentifier();
                    }
                } else {
//                    boolean isVision = false;
//                    for (IBMBehaviorable a : activities.values()) {
//                        BMEntity e = (BMEntity) a;
//
//                        if (e.innerType() == BMEntity.BMEntityType.unit || e.innerType() == BMEntity.BMEntityType.trap) {
//                            IBMAttactable act = (IBMAttactable) a;
//                            if (((IBMTargetable) act).isAlive() && act.getVision().contains(c)) {
//                                isVision = true;
//                                break;
//                            }
//                        }
//                    }
//                    if (isVision) {
//                        result += " x ";
//                    } else {
//                        result += " . ";
//                    }

                    if (c.getMovementType() == BMMovementType.ROAD) {
                        result += " R ";
                    } else {
                        result += " . ";
                    }
                }
            }
            result += "\n";
        }
        return result;
    }

    @Override
    public void executeEndLoop() {
    }
}
