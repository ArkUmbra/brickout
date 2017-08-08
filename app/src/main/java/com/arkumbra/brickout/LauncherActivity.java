package com.arkumbra.brickout;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.arkumbra.brickout.engine.GameEngine;
import com.arkumbra.brickout.engine.GameEngineImpl;
import com.arkumbra.brickout.engine.touch.TouchEventController;
import com.arkumbra.brickout.view.GameView;


public class LauncherActivity extends Activity {
    private static final String LOG_TAG = "LauncherActivity";
    private GameView view;
//    private TouchEventController touchEventController;
    private GameEngine gameEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameEngine = new GameEngineImpl(this);
//        touchEventController = new TouchEventController(gameEngine);

        // Create a view that we can draw to
        view = new GameView(this, gameEngine);
        // Set content view to our game view, rather than an XML definition
        setContentView(view);

        hideSystemUI();
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();

        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(LOG_TAG, "Touch motion event");
        // TODO handle response
//        touchEventController.onTouchEvent(event);
        return gameEngine.onTouchEvent(event);
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
