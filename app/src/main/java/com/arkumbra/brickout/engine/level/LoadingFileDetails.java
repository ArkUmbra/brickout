package com.arkumbra.brickout.engine.level;

import com.arkumbra.brickout.engine.entity.Brick;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lukegardener on 2017/07/31.
 */

public class LoadingFileDetails {

    private float levelUnitsTall;
    private float levelUnitsAcross;
    private List<Brick> bricks = new ArrayList<>();

//    public LoadingFileDetails(float levelUnitWidth, float levelUnitHeight, List<Brick> bricks) {
//        this.levelUnitsAcross = levelUnitWidth;
//        this.levelUnitsTall = levelUnitHeight;
//        this.bricks = bricks;
//    }

    public float getLevelUnitsAcross() {
        return levelUnitsAcross;
    }

    public void setLevelUnitsAcross(float levelUnitsAcross) {
        this.levelUnitsAcross = levelUnitsAcross;
    }

    public float getLevelUnitsTall() {
        return levelUnitsTall;
    }

    public void setLevelUnitsTall(float levelUnitsTall) {
        this.levelUnitsTall = levelUnitsTall;
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    public void setBricks(List<Brick> bricks) {
        this.bricks = bricks;
    }
}
