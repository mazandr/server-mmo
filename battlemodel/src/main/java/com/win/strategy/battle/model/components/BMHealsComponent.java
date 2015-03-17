package com.win.strategy.battle.model.components;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author vlischyshyn
 */
public class BMHealsComponent extends BMBaseComponent {

    private int maxHP;
    private int currentHP;

    public BMHealsComponent() {
        this.setIdentifier("heals");
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    public boolean isAlive() {
        return this.currentHP > 0;
    }

    public void damage(int damage) {
        currentHP = currentHP < damage ? 0 : currentHP - damage;
        this.notifyEntity(this);
    }

    public void heal(int heal) {
        currentHP += heal;
        if (currentHP > maxHP) {
            currentHP = maxHP;
        }
        this.notifyEntity(this);
    }

    @Override
    public Map<String, Object> toMapObject() {
        Map<String, Object> result = new HashMap<>();
        result.put("hp", currentHP);
        result.put("maxHP", maxHP);
        result.put("isAlive", this.isAlive());

        return result;
    }

    @Override
    public void notify(Object arg) {
    }
}
