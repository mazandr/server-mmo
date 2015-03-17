package com.win.strategy.battle.model.entity.basic;

import com.win.strategy.battle.model.entity.BMCell;
import java.util.List;

/**
 *
 * @author vlischyshyn
 */
public interface IBMLocable extends IBMIndentifier {

    public void setPosition(BMCell cell);

    public BMCell getPosition();

    public List<BMCell> getLocation();

    public boolean isVisible();

    public void setVisible(boolean visible);

    public BMCell getCenter();
}
