package com.win.strategy.battle.model.components;

import com.win.strategy.battle.model.entity.basic.resources.BMResource;
import com.win.strategy.battle.model.entity.basic.resources.BMResourceHolder;
import java.util.Collection;

/**
 *
 * @author vlischyshyn
 */
public class BMResourceComponent extends BMBaseComponent {

    BMResourceHolder holder = new BMResourceHolder();

    public void addResource(BMResource resource) {
        holder.addResource(resource);
    }

    @Override
    public void notify(Object arg) {
    }

    public Collection<BMResource> getResourceList() {
        return holder.getResourceList();
    }
}
