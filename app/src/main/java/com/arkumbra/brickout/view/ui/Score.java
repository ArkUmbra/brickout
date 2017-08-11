package com.arkumbra.brickout.view.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.arkumbra.brickout.R;
import com.arkumbra.brickout.engine.GameEngine;
import com.arkumbra.brickout.engine.entity.GameDrawable;
import com.arkumbra.brickout.view.FontUtils;

/**
 * Created by lukegardener on 2017/08/10.
 */

public class Score implements GameDrawable {
    // FF3D2E
    private final Paint uiScorePainter = new Paint();

    private final float canvasWidth;
    private final float canvasHeight;

    private final float xDrawPos;
    private final float yDrawPos;
    private final GameEngine gameEngine;

    public Score(float canvasWidth, float canvasHeight, GameEngine gameEngine) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.gameEngine = gameEngine;


        this.uiScorePainter.setColor(Color.rgb(255, 45, 62));
        this.uiScorePainter.setTextAlign(Paint.Align.RIGHT);
        this.uiScorePainter.setTypeface(FontUtils.getAppTypeface());

        int screenScaledFontSize = gameEngine.getContext().getResources().getDimensionPixelSize(R.dimen.scoreFontSize);
        this.uiScorePainter.setTextSize(screenScaledFontSize);

        this.xDrawPos = canvasWidth * 0.98f;
        this.yDrawPos = canvasHeight * 0.982f;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawText("" + gameEngine.getScore(),
                this.xDrawPos,
                this.yDrawPos,
                uiScorePainter);
    }
}
