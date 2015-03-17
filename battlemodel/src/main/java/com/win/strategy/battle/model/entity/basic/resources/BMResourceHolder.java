package com.win.strategy.battle.model.entity.basic.resources;

import com.win.strategy.battle.model.entity.basic.BMObject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author vlischyshyn
 */
public class BMResourceHolder extends BMObject {

    private Map<String, BMResource> resources = new HashMap<>();

    public void addResource(BMResource resource) {
        if (resource != null) {
            resources.put(resource.getIdentifier(), resource);
        }
    }

    public void addAllRecources(Collection<BMResource> allResources) {
        if (allResources == null) {
            return;
        }
        for (BMResource resource : allResources) {
            resources.put(resource.getIdentifier(), resource);
        }

    }

    public boolean incResource(BMResource resource, int value) {
        if (resource != null) {
            if (incResource(resource.getIdentifier(), value)) {
                addResource(resource);
                return true;
            }
        }
        return false;
    }

    public boolean incResource(String resourceId, int value) {
        BMResource resource = resources.get(resourceId);
        if (resource != null) {
            resource.inc(value);
            return true;
        }
        return false;
    }

    public boolean decResource(BMResource resource, int value) {
        if (resource != null) {
            return decResource(resource.getIdentifier(), value);
        }
        return false;
    }

    public boolean decResource(String resourceId, int value) {
        BMResource resource = resources.get(resourceId);
        if (resource != null) {
            resource.dec(value);
            return true;
        }
        return false;
    }

    public Map<String, BMResource> getResources() {
        return resources;
    }

    public Collection<BMResource> getResourceList() {
        return resources.values();
    }

    public int getResourceValue(String resourceId) {
        BMResource resource = resources.get(resourceId);
        return resource == null ? 0 : resource.getValue();
    }

    public boolean canDecResources(Collection<BMResource> bMResources) {
        if (bMResources == null) {
            return true;
        }
        if (bMResources.isEmpty()) {
            return true;
        }
        for (BMResource resource : bMResources) {
            if (!resources.containsKey(resource.getIdentifier()) || resources.get(resource.getIdentifier()).getValue() < resource.getValue()) {
                return false;
            }
        }
        return true;
    }
}
