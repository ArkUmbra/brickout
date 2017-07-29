package com.arkumbra.examplegame.engine;

import android.graphics.Canvas;
import android.graphics.Color;

import com.arkumbra.examplegame.drawable.GameDrawable;
import com.arkumbra.examplegame.entity.DummyGameObject;
import com.arkumbra.examplegame.entity.GameEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by lukegardener on 2017/07/28.
 */

public class GameEngineImpl implements GameEngine {
    private static final String LOG_TAG = "GameEngine";
    private static final int BACKGROUND_COLOR = Color.BLACK;

    private int canvasWidth;
    private int canvasHeight;

    private boolean initialised;

    // Maintain two separate lists to save on CPU looping cycles at the cost of higher mem
    // May or may not be desirable depending on game
    private List<GameEntity> gameEntityList = new ArrayList<GameEntity>();
    private List<GameDrawable> gameDrawablesList = new ArrayList<GameDrawable>();


    public GameEngineImpl() {

    }

    /**
     * Keep the initialisation of screen dimensions separate to instantiation of the engine
     * This is because the view is not always prepared by the time you want to create the engine
     * @param canvasWidth
     * @param canvasHeight
     */
    @Override
    public void initialise(int canvasWidth, int canvasHeight) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;

        this.initialised = true;
    }

    // Lock this so only activity can activate?
    @Override
    public void startEngine() {
        loadLevel();
    }

    @Override
    public boolean isInitialisedYet() {
        return initialised;
    }

    @Override
    public void update(long msDelta) {
        for (GameEntity entity : gameEntityList) {
            entity.update(msDelta);
        }
    }

    // TODO double buffer
    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(BACKGROUND_COLOR);

        for (GameDrawable entity : gameDrawablesList) {
            entity.draw(canvas);
        }

    }

    private void loadLevel() {
        Random r = new Random();

        // Set example objects
        for (int i = 0; i < 5; i++) {
            float angle = (r.nextFloat() - 0.5f) * 3;
            DummyGameObject dummyGameObject = new DummyGameObject(canvasWidth, canvasHeight, angle);

            gameEntityList.add(dummyGameObject);
            gameDrawablesList.add(dummyGameObject);
        }
    }

}
