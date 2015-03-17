package com.win.strategy.battle.model.entity.basic.resources;

import com.win.strategy.battle.model.entity.basic.BMObject;
import com.win.strategy.common.utils.ProtocolStrings;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author vlischyshyn
 */
public class BMUsableResources extends BMObject {

    private BMResourceHolder holder = new BMResourceHolder();

    public BMUsableResources() {
        setIdentifier(ProtocolStrings.RESOURCES);
    }

    public void addResource(BMResource resource) {
        holder.addResource(resource);
    }

    public void addAllResources(Collection<BMResource> resources) {
        holder.addAllRecources(resources);
    }

    public Collection<BMResource> getResourceList() {
        return holder.getResourceList();
    }

    public void dec(BMResource resource) {
        if (resource != null) {
            holder.decResource(resource.getIdentifier(), resource.getValue());
        }
    }

    public void dec(Collection<BMResource> resources) {
        if (resources == null) {
            return;
        }
        for (BMResource r : resources) {
            dec(r);
        }
    }

    public void inc(BMResource resource) {
        if (resource != null) {
            holder.incResource(resource.getIdentifier(), resource.getValue());
        }
    }

    public boolean canDecResources(Collection<BMResource> resources) {
        return holder.canDecResources(resources);
    }

    public void inc(Collection<BMResource> resources) {
        if (resources == null) {
            return;
        }
        for (BMResource r : resources) {
            inc(r);
        }
    }

    @Override
    public Map<String, Object> toMapObject() {
        Map<String, Object> map = new HashMap<>();
        map.put(ProtocolStrings.RESOURCES, holder.getResourceList());
        return map;
    }
}
