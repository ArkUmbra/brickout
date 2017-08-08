package com.arkumbra.brickout.engine.entity;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by lukegardener on 2017/07/29.
 */
public enum BrickType {

    // name, health, map character, colour
    GREEN(1, 'g', Color.rgb(15, 98, 14)),
    YELLOW(2, 'y', Color.YELLOW),
    ORANGE(3, 'o', Color.rgb(255, 153, 0)),
    RED(4, 'r', Color.RED),
    PURPLE(5, 'p', Color.rgb(128, 0, 128)),
    BLUE(6, 'b', Color.BLUE),
    PINK(7, 'k', Color.rgb(255, 105, 180))
    ;


    private int hitsToBreak;
    private char levelIcon;
    private int colour;

    private BrickType(int hitsToBreak, char levelIcon, int colour) {
        this.hitsToBreak = hitsToBreak;
        this.levelIcon = levelIcon;
        this.colour = colour;
    }

    public int getHitsToBreak() {
        return hitsToBreak;
    }

    public char getLevelIcon() {
        return levelIcon;
    }

    public int getBrickCanvasColour() {
        return colour;
    }

    public Bitmap getBitmap() {
        // TODO
        return null;
    }

    public static BrickType getByLevelIcon(char levelIcon) {
        for (BrickType brick : values()) {
            if (brick.levelIcon == levelIcon) {
                return brick;
            }
        }

        return null;
    }

    public static BrickType getByHits(int hits) {
        for (BrickType brick : values()) {
            if (brick.hitsToBreak == hits) {
                return brick;
            }
        }

        return BrickType.PINK;
    }

}
