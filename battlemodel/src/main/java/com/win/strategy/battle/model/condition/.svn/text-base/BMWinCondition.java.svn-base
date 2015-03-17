package com.win.strategy.battle.model.condition;

/**
 *
 * @author okopach
 */
public class BMWinCondition {

    private String key;
    private Double percent = 0D;
    private Double currentRate = 0D;
    private Double allRate = 0D;
    private Operation operation;

    public BMWinCondition(String key, Double value, Operation operation) {
        this.key = key;
        this.percent = value;
        this.operation = operation;
    }

    public Double getAllRate() {
        return allRate;
    }

    public void setAllRate(Double allRate) {
        this.allRate = allRate;
    }

    private Double countPercent() {
        if (allRate == 0) {
            return 0D;
        }
        return currentRate / allRate * 100D;
    }

    public boolean checkValue() {
        switch (operation) {
            case eq:
                if (percent == countPercent()) {
                    return true;
                } else {
                    return false;
                }
            case gt:
                if (percent < countPercent()) {
                    return true;
                } else {
                    return false;
                }
            case lt:
                if (percent > countPercent()) {
                    return true;
                } else {
                    return false;
                }
            default:
                return true;
        }
    }

    public void incCurrentRate(Double inc) {
        currentRate += inc;
    }

    public void incAllRate(Double inc) {
        allRate += inc;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public Double getCurrentRate() {
        return currentRate;
    }

    public void setCurrentRate(Double currentRate) {
        this.currentRate = currentRate;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public enum Operation {

        lt, gt, eq
    }
}
