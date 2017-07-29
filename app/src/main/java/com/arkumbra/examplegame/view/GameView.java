package com.arkumbra.examplegame.view;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.arkumbra.examplegame.engine.GameEngine;

/**
 * Created by lukegardener on 2017/07/27.
 */

public class GameView extends SurfaceView {
    private static final String LOG_TAG = "GameView";

    private DrawThread drawThread;
    private Thread runningDrawThread;
    private GameEngine gameEngine;


    public GameView(Context context, GameEngine gameEngine) {
        super(context);

        this.gameEngine = gameEngine;
        drawThread = new DrawThread(this, gameEngine);

        // Listen for when the view is ready
        addSurfaceCallback();
        setFocusable(true);
    }

    // Note: tried to have this view class implement this interface directly, using addCallback(this)
    // but that resulted in the callback not being executed for whatever reason.
    private void addSurfaceCallback() {
        getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.d(LOG_TAG, "SurfaceCreated: x" + getX() + ", y" + getY());

                startDrawingThread();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.d(LOG_TAG, "Surface changed: width" + width + ", height" + height);
                Log.d(LOG_TAG, "Surface changed: x" + getX() + ", y" + getY());

                startEngineNowViewIsReady(width, height);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.d(LOG_TAG, "Surface destroyed");
            }
        });
    }

    private void startEngineNowViewIsReady(int width, int height) {
        gameEngine.initialise(width, height);
        gameEngine.startEngine();
    }

    private void startDrawingThread() {
        runningDrawThread = new Thread(drawThread);
        runningDrawThread.start();
    }

}
