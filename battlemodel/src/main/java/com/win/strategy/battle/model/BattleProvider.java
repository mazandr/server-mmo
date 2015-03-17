package com.win.strategy.battle.model;

import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.utils.BMFrameBuilder;
import com.win.strategy.common.model.support.DefaultModelProvider;
import com.win.strategy.common.model.support.ModelInitailzationException;
import java.util.Map;

/**
 *
 * @author vlischyshyn
 */
public class BattleProvider extends DefaultModelProvider<BattleModelEngine> {

    @Override
    public void initModel() throws ModelInitailzationException {
        super.initModel();
        try {
            getModelEngine().intializeField();
        } catch (Exception e) {
            throw new ModelInitailzationException("Field initialization exception", e);
        }
    }

    public Map<String, Object> getInitialState() {
        try {
            BattleModelEngine engine = getModelEngine();
            BMField field = engine.getField();
            return BMFrameBuilder.initialFrameBuild(field);
        } catch (Exception e) {
            return null;
        }
    }
}
