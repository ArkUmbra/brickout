package com.arkumbra.examplegame;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.arkumbra.examplegame.engine.GameEngine;
import com.arkumbra.examplegame.engine.GameEngineImpl;
import com.arkumbra.examplegame.view.GameView;


public class LauncherActivity extends Activity {
    private static final String LOG_TAG = "LauncherActivity";
    private GameView view;
    private TouchEventHandler touchEventHandler;
    private GameEngine gameEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameEngine = new GameEngineImpl();
        touchEventHandler = new TouchEventHandler(gameEngine);

        // Create a view that we can draw to
        view = new GameView(this, gameEngine);
        // Set content view to our game view, rather than an XML definition
        setContentView(view);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO handle response
        touchEventHandler.onTouchEvent(event);

        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        // Handle this if required
    }

    @Override
    public void onResume() {
        super.onResume();
        // Handle this if required
    }



}
