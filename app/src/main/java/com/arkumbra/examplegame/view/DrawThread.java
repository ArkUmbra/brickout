package com.arkumbra.examplegame.view;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceView;

import com.arkumbra.examplegame.engine.GameConstants;
import com.arkumbra.examplegame.engine.GameEngine;

/**
 * Created by lukegardener on 2017/07/27.
 */

public class DrawThread implements Runnable {
    private static final String LOG_TAG = "DrawThread";

    private static final int MIN_SLEEP_MS = 10;
    private static final int MILLIS_PER_FRAME = 1000 / GameConstants.TARGET_FPS;

    private boolean running = true;

    private SurfaceView parentView;
    private GameEngine gameEngine;

    private long drawAndUpdateStartMs = 0l;
    private long drawAndUpdateFinishMs = 0l;

    private long lastUpdateMs = 0l;


    public DrawThread(GameView view, GameEngine gameEngine) {
        this.parentView = view;
        this.gameEngine = gameEngine;
    }

    @Override
    public void run() {
        Log.d(LOG_TAG, "Runnning thread");

        while (! gameEngine.isInitialisedYet()) {
            sleepBrieflyToFreeUpCPu();
        }


        // # tick start # drawAndUpdateStart
        // |            |
        // |            |
        // |            |
        // |            # drawAndUpdateEnd & sleepStart
        // |            ~
        // |            ~
        // # tick end   # sleepEnd
        while (running) {
            long updateStartMs = System.currentTimeMillis();
            updateEngineForCurrentTimeStep(lastUpdateMs, updateStartMs);
            lastUpdateMs = updateStartMs;

            drawToCanvasAndPost();
            drawAndUpdateFinishMs = System.currentTimeMillis();

            if (drawTimeTookLongerThanTargetFps(updateStartMs, drawAndUpdateFinishMs)) {
                sleepBrieflyToFreeUpCPu();
            } else {
                sleepForRemainingTick(updateStartMs, drawAndUpdateFinishMs);
            }
        }
    }

    private void updateEngineForCurrentTimeStep(long timeAtLastUpdateMs, long nowMs) {
        long msSinceLastUpdate;

        if (timeAtLastUpdateMs <= 0) {
            msSinceLastUpdate = MILLIS_PER_FRAME;
        } else {
            msSinceLastUpdate = nowMs - timeAtLastUpdateMs;
        }

        gameEngine.update(msSinceLastUpdate);
    }


    private void drawToCanvasAndPost() {
        Canvas canvas = null;

        try {
            canvas = parentView.getHolder().lockCanvas();

            if (canvas != null) {
                synchronized (parentView.getHolder()) {
                    gameEngine.draw(canvas);
                }
            }

        } finally {
            if (canvas != null) {
                parentView.getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }

    private boolean drawTimeTookLongerThanTargetFps(long drawStartMs, long drawFinishMs) {
        return drawStartMs + MILLIS_PER_FRAME <= drawFinishMs;
    }

    private void sleepBrieflyToFreeUpCPu() {
        sleepQuietly(MIN_SLEEP_MS);
    }

    private void sleepQuietly(long msToSleep) {
        try {
            Thread.sleep(msToSleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sleepForRemainingTick(long drawStartMs, long drawFinishMs) {
        long msRemainingInTick = MILLIS_PER_FRAME - (drawFinishMs - drawStartMs);

        if (msRemainingInTick > MIN_SLEEP_MS) {
            sleepQuietly(msRemainingInTick);
        } else {
            sleepQuietly(MIN_SLEEP_MS);
        }
    }

    public void stop() {
        this.running = false;
    }
}
