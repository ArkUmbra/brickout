package com.arkumbra.brickout.engine.entity;

/**
 * Created by lukegardener on 2017/07/29.
 */

public class UnitSize {
    private final float widthInUnits;
    private final float heightInUnits;

    public UnitSize(float widthInUnits, float heightInUnits) {
        this.widthInUnits = widthInUnits;
        this.heightInUnits = heightInUnits;
    }

    public float getWidthInUnits() {
        return widthInUnits;
    }

    public float getHeightInUnits() {
        return heightInUnits;
    }
}
