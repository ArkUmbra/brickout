package com.arkumbra.brickout.view;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.arkumbra.brickout.engine.GameEngine;
import com.arkumbra.brickout.engine.entity.GameDrawable;

/**
 * Created by lukegardener on 2017/08/06.
 */

public class ControlsUI implements GameDrawable {
    // 272F32
    private final Paint uiBackgroundPainter = new Paint();
    // 9DBDC6
    private final Paint uiBackgroundOutlinePainter = new Paint();
    // FF3D2E
    private final Paint uiScorePainter = new Paint();

    private final GameEngine gameEngine;
    private final float xMin;
    private final float yMin;
    private final float xMax;
    private final float yMax;


    public ControlsUI(Context context, GameEngine gameEngine, float xMin, float yMin, float xMax, float yMax) {
        this.gameEngine = gameEngine;
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;

        this.uiBackgroundPainter.setColor(Color.rgb(39, 47, 50));
        this.uiBackgroundOutlinePainter.setColor(Color.rgb(157, 189, 198));
        this.uiScorePainter.setColor(Color.rgb(255, 45, 62));
        this.uiScorePainter.setTextAlign(Paint.Align.CENTER);
        this.uiScorePainter.setTextSize((yMax - yMin) * 0.8f);

        this.uiScorePainter.setTypeface(FontUtils.getAppTypeface());
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(xMin, yMin, xMax, yMax, uiBackgroundPainter);
        // canvas.drawRect(xMin, yMin, xMax, yMax, uiBackgroundOutlinePainter);
        // canvas.drawRect(xMin + 10, yMin + 10, xMax - 10, yMax - 10, uiBackgroundPainter);

        canvas.drawText("" + gameEngine.getScore(),
                xMax / 2, // Middle of screen on x
                yMax - ((yMax - yMin) * 0.2f), // 10% of the way off the bottom of the UI bar
                uiScorePainter);
    }
}
