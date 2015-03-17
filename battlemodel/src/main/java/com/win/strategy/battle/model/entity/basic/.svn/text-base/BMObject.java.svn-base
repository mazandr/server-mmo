package com.win.strategy.battle.model.entity.basic;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author vlischyshyn
 */
public abstract class BMObject implements IBMIndentifier {

    private String identifier;
    private String uid = null;

    @Override
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getUid() {
        if (uid == null) {
            return identifier;
        }
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Map<String, Object> toMapObject() {
        Map<String, Object> result = new HashMap<>();
        result.put("id", identifier);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }
        BMObject target = (BMObject) obj;

        if (this.identifier.equals(target.identifier)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.identifier);
        return hash;
    }
}
