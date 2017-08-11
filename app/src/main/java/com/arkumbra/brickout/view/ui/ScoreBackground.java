package com.arkumbra.brickout.view.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.arkumbra.brickout.engine.entity.GameDrawable;

/**
 * Created by lukegardener on 2017/08/10.
 */

public class ScoreBackground implements GameDrawable {

    // 272F32
    private final Paint uiBackgroundPainter = new Paint();

    private final float canvasWidth;
    private final float canvasHeight;

    private final float xMin;
    private final float yMin;
    private final float xMax;
    private final float yMax;

    public ScoreBackground(float canvasWidth, float canvasHeight) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;

        this.xMin = 0;
        this.yMin = canvasHeight - 120; // TODO scale
        this.xMax = canvasWidth;
        this.yMax = canvasHeight;

        this.uiBackgroundPainter.setColor(Color.rgb(39, 47, 50));
    }


    @Override
    public void draw(Canvas canvas) {
        if (canvas == null) return;

        canvas.drawRect(xMin, yMin, xMax, yMax, uiBackgroundPainter);
    }
}
