package com.win.strategy.battle.model.entity.basic.resources;

import com.win.strategy.battle.model.entity.basic.BMObject;
import java.util.Map;

/**
 *
 * @author vlischyshyn
 */
public class BMResource extends BMObject {

    private int value;
    private int maxValue = Integer.MAX_VALUE;

    public BMResource() {
    }

    public BMResource(String name, int value) {
        this.setIdentifier(name);
        this.value = value;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void inc(int value) {
        this.value += value;
        if (this.value > maxValue) {
            this.value = maxValue;
        }
    }

    public void dec(int value) {
        this.value -= value;
        if (this.value < 0) {
            this.value = 0;
        }
    }
}
