package com.arkumbra.brickout.view;

/**
 * Created by lukegardener on 2017/07/31.
 */

public class SurfaceDimensions {

    private final float widthPixels;
    private final float heightPixels;

    public SurfaceDimensions(float widthPixels, float heightPixels) {
        this.widthPixels = widthPixels;
        this.heightPixels = heightPixels;
    }

    public float getWidthPixels() {
        return widthPixels;
    }

    public float getHeightPixels() {
        return heightPixels;
    }
}
