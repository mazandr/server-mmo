package com.win.strategy.battle.model.entity.basic;

import com.win.strategy.battle.model.entity.basic.actions.BMActionProperties;

/**
 *
 * @author vlischyshyn
 */
public interface IBMAttactable extends IBMIndentifier {

    public IBMTargetable getFocus();

    public void setFocus(IBMTargetable target);

    public void setAttackPriorityType(BMAttackPriorityType attackPriorityType);

    public BMAttackPriorityType getAttackPriorityType();

    public void addEffect(BMActionProperties properties);

    /**
     * Add description of action for user
     * @param properties 
     */
    public void addReactionItem(BMActionProperties properties);
}
