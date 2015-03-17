package com.win.strategy.battle.model.components.behavior;

import com.win.strategy.battle.model.components.BMBaseComponent;
import com.win.strategy.battle.model.entity.basic.IBMBehaviorable;

public abstract class BMBaseBehavior extends BMBaseComponent implements
		IBMBehaviorable {
    
    protected Object initializedParams;
    
    public abstract void init(Object initParams);
    
    public void reinitBehavior(){
        init(initializedParams);
    }
    
}
