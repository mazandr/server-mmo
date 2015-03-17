package com.win.strategy.battle.model;

import com.win.strategy.battle.configuration.factory.BMUnitInstanceFactory;
import com.win.strategy.battle.model.command.BMCommandManger;
import com.win.strategy.battle.model.entity.BMCell;
import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.basic.IBMBehaviorable;
import com.win.strategy.battle.model.entity.basic.resources.BMResource;
import com.win.strategy.battle.model.entity.basic.skills.BMBaseSkill;
import com.win.strategy.battle.model.entity.basic.units.BMBaseUnit;
import com.win.strategy.common.model.IModelCallback;
import com.win.strategy.common.utils.ProtocolStrings;
import com.win.strategy.common.model.IModelEngine;
import com.win.strategy.common.model.IModelEngineState;
import com.win.strategy.common.model.command.ModelCommandException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vlischyshyn
 */
public class BattleModelEngine implements Runnable, IModelEngine {

    private static final Logger lOG = Logger.getLogger(BattleModelEngine.class.getName());
    private boolean notFinished = true;
    private boolean timeIsOut;
    private boolean userStopedInProcessing = false;
    private int timeTicks = 0;
    private BMField field;
    private BMCommandManger commandManger;
    private BattleModelBuilder builder;
    private Set<IModelCallback> callbacks = new HashSet<>();
    private boolean processing;

    public boolean isNotFinished() {
        return notFinished;
    }

    public boolean isTimeIsOut() {
        return timeIsOut;
    }

    public boolean isUserStopedInProcessing() {
        return userStopedInProcessing;
    }

    public BMField getField() {
        return field;
    }

    public void setField(BMField field) {
        this.field = field;
    }

    @Override
    public IModelEngineState getState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setState(IModelEngineState state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public BattleModelEngine() {
        this.commandManger = new BMCommandManger(this);
    }

    public void addCallback(IModelCallback callback) {
        callbacks.add(callback);
    }

    public BMCommandManger getCommandManger() {
        return commandManger;
    }

    public int getTimeTicks() {
        return timeTicks;
    }

    @Override
    public void run() {
        timeIsOut = false;

        if (this.field == null) {
            return;
        }

        processing = false;
        int itterationCounter = 0;
        while (!isTimeExpaire()
                && (notFinished = field.checkNotGameOver())
                && itterationCounter < field.getItterationSize() && !isUserStopedInProcessing()) {
            processing = true;
            try {
                commandManger.getScheduler().executeScheduledCommands(timeTicks);
            } catch (ModelCommandException ex) {
                lOG.log(Level.SEVERE, null, ex);
            }
            callBevavior();
            callActions();
            callSkillsBehavior();
            executeCallbacks();
            invalidateField();
            timeTicks++;
            itterationCounter++;
        }
        finalCallbackExecution();
        processing = false;
    }

    private void callBevavior() {
        if (field.getActivities() == null) {
            return;
        }
        for (String actId : field.getActivities().keySet()) {
            field.getActivities().get(actId).behave(timeTicks);
        }
    }

    private boolean isTimeExpaire() {
        return timeIsOut = timeTicks >= field.getTotalGameFrames();
    }

    private void executeCallbacks() {
        if (!callbacks.isEmpty()) {
            for (IModelCallback processCallback : callbacks) {
                processCallback.executeInsideLoop();
            }
        }
    }

    private void finalCallbackExecution() {
        if (!callbacks.isEmpty()) {
            for (IModelCallback processCallback : callbacks) {
                processCallback.executeEndLoop();
            }
        }
    }

    private void callSkillsBehavior() {

        if (field.getDurableActions() == null) {
            return;
        }
        for (IBMBehaviorable action : field.getDurableActions()) {
            action.behave(timeTicks);
        }
    }

    private void callActions() {
        if (field.getActionExecutor() == null) {
            return;
        }
        field.getActionExecutor().execute();
    }

    private void invalidateField() {
        for (ArrayList<BMCell> row : field.getCells()) {
            for (BMCell cell : row) {
                cell.invalidate();
            }
        }
        
    }

    @Override
    public Map<String, Object> executeCommand(String commandName, Map<String, Object> args) throws ModelCommandException {
        Object delay = null;
        if (args != null) {
            delay = args.get(ProtocolStrings.DELAY_STEPS);
        }
        Map<String, Object> info = new HashMap<>();
        if (delay == null) {
            Map<String, Object> result = commandManger.executeCommand(commandName, args);
            if (result != null) {
                info.putAll(result);
            }
        } else {
            int delaySteps = Integer.valueOf(String.valueOf(delay));
            long execStep = delaySteps + timeTicks;
            Map<String, Object> result = commandManger.scheduleExecCommand(commandName, execStep, args);
            if (result != null) {
                info.putAll(result);
            }
            info.put(ProtocolStrings.EXEC_STEP, execStep);
        }
        return info;
    }

    @Override
    public synchronized void interrupt() {
        userStopedInProcessing = true;
    }

    public BattleModelBuilder getBuilder() {
        return builder;
    }

    public void setBuilder(BattleModelBuilder builder) {
        this.builder = builder;
    }

    public void intializeField() throws Exception {
        if (builder != null) {
            field = builder.createField();
        } else {
            throw new Exception("builder is not defined");
        }
    }

    public void registerUnits(Map<String, Object> unitsDescription) throws Exception {
        BMUnitInstanceFactory unitFactory = builder.getUnitInstanceFactory();
        Object quantityObj = unitsDescription.get(ProtocolStrings.Q);
        if (quantityObj == null) {
            throw new Exception("units are not registred");
        }
        int quantity = Integer.valueOf(String.valueOf(quantityObj)).intValue();
        for (int i = 0; i < quantity; i++) {
            BMBaseUnit simpleUnit = unitFactory.createUnitForField(unitsDescription);
            if (simpleUnit != null) {
                field.registerUnit(simpleUnit);
            }
        }
    }

    public void registerSkill(String skillName, int lvl) {
        BMBaseSkill skill = builder.createSkill(skillName, lvl);
        if (skill != null) {
            field.addSkill(skill);
        }
    }

    /**
     * registration available resources that can be use on battle
     *
     * @param resources
     */
    public void registerResources(Map<String, Integer> resources) {
        if (resources == null || resources.isEmpty()) {
            return;
        }
        Collection<BMResource> bMResources = new HashSet<>();
        for (Map.Entry<String, Integer> resMap : resources.entrySet()) {
            BMResource resource = new BMResource(resMap.getKey(), resMap.getValue());
            bMResources.add(resource);
        }
        if (!bMResources.isEmpty()) {
            field.registerResources(bMResources);
        }
    }
}