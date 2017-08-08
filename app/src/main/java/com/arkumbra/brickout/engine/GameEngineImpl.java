package com.arkumbra.brickout.engine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;

import com.arkumbra.brickout.engine.entity.GameDrawable;
import com.arkumbra.brickout.engine.level.Level;
import com.arkumbra.brickout.engine.entity.BrickType;
import com.arkumbra.brickout.engine.entity.GameEntity;
import com.arkumbra.brickout.engine.entity.StandardBrick;
import com.arkumbra.brickout.engine.level.LevelFileLoader;
import com.arkumbra.brickout.engine.level.LevelProgress;
import com.arkumbra.brickout.engine.level.LoadingFileDetails;
import com.arkumbra.brickout.engine.touch.TouchEventType;
import com.arkumbra.brickout.view.ControlsUI;
import com.arkumbra.brickout.view.FontUtils;
import com.arkumbra.brickout.view.SurfaceDimensions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by lukegardener on 2017/07/28.
 */

public class GameEngineImpl implements GameEngine {

    private static final String LOG_TAG = "GameEngine";
    private static final int BACKGROUND_COLOR = Color.BLACK;
    private static final LevelFileLoader levelFileLoader = new LevelFileLoader();

    private SurfaceDimensions surfaceDimensions;

    private static final int MILLISECONDS_FOR_GAME_OVER_OR_CLEAR = 4 * 1000;
    private boolean initialised;

    // TODO Do proper state machine here...
    private boolean gameOver;
    private boolean levelClear;
    private int pauseMsRemaining;

    private float xPress;
    private float yPress;

    // Maintain two separate lists to save on CPU looping cycles at the cost of higher mem
    // May or may not be desirable depending on game
    private List<GameEntity> gameEntityList = new ArrayList<GameEntity>();
    private List<GameDrawable> gameDrawablesList = new ArrayList<GameDrawable>();

    private ControlsUI controlsUI;
    private Level currentLevel;
    private Context context;

    public GameEngineImpl(Context context) {
        this.context = context;

        FontUtils.initialise(context);
    }

    /**
     * Keep the initialisation of screen dimensions separate to instantiation of the engine
     * This is because the view is not always prepared by the time you want to create the engine
     * @param canvasWidth
     * @param canvasHeight
     */
    @Override
    public void initialise(int canvasWidth, int canvasHeight) {
        this.surfaceDimensions = new SurfaceDimensions(canvasWidth, canvasHeight);
        controlsUI = new ControlsUI(context, this, 0, canvasHeight - 120, canvasWidth, canvasHeight);

        this.initialised = true;
    }

    // temp
    private int levelCount = 0;
    private String[] levels = new String[]{"levelEasy", "level_cat", "level0-1", "level0-2", "level1"};

    // Lock this so only activity can activate?
    @Override
    public void startEngine() {
        if (levelCount > levels.length - 1) {
            levelCount = 0;
        }

        loadLevel("world1/" + levels[levelCount] + ".brk");

        // ----------

//        loadLevel("world1/level1.brk");

        // -----------
//        Random r = new Random();
//        int levelNum = r.nextInt(6) + 1;
//        loadLevel("level" + levelNum + ".brk");
    }


//    @Override
//    public boolean touch(float x, float y, TouchEventType touchEventType) {
//        this.xPress = x;
//        this.yPress = y;
//
//
//    }

    @Override
    public boolean isInitialisedYet() {
        return initialised;
    }

    @Override
    public void update(long msDelta) {
        if (currentLevel == null) {
            return;
        }

        this.pauseMsRemaining -= msDelta;
        if ((gameOver || levelClear) && pauseMsRemaining < 0) {
            this.gameOver = false;
            this.levelClear = false;
            this.startEngine();
            return;
        }

        if (!gameOver && !levelClear) {
            currentLevel.update(msDelta);
        }
    }

    private Paint pausePainter = new Paint();

    // TODO double buffer
    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(BACKGROUND_COLOR);

        if (currentLevel == null) {
            return;
        }


        currentLevel.draw(canvas);
        controlsUI.draw(canvas);

        // TODO TEMP
        pausePainter.setColor(Color.RED);
        pausePainter.setTypeface(FontUtils.getAppTypeface());
        pausePainter.setTextSize(surfaceDimensions.getWidthPixels() / 10);
        pausePainter.setTextAlign(Paint.Align.CENTER);

        if (gameOver) {
            canvas.drawText("GAME OVER", surfaceDimensions.getWidthPixels() / 2,
                                    surfaceDimensions.getHeightPixels() / 2,
                                    pausePainter);
        } else if (levelClear) {
            canvas.drawText("LEVEL CLEAR!", surfaceDimensions.getWidthPixels() / 2,
                    surfaceDimensions.getHeightPixels() / 2,
                    pausePainter);
        }
    }

    @Override
    public void notifyLevelProgress(LevelProgress levelProgress) {
        this.pauseMsRemaining = MILLISECONDS_FOR_GAME_OVER_OR_CLEAR;

        // TODO make good - reset or go to next level
        if (levelProgress == LevelProgress.CLEARED) {
            this.levelCount++;
            this.levelClear = true;
        }

        if (levelProgress == LevelProgress.FAILED) {
            this.gameOver = true;
        }
    }

    @Override
    public int getScore() {
        if (currentLevel == null)
            return 0;

        return currentLevel.getScore();
    }

    private void loadLevel(String fileName) {
        LoadingFileDetails loadingFileDetails = loadLevelFromFile(fileName);

        Level level = new Level(this,
                                loadingFileDetails.getLevelUnitsAcross(),
                                loadingFileDetails.getLevelUnitsTall(),
                                loadingFileDetails.getBricks(),
                                this.surfaceDimensions
        );

        this.currentLevel = level;
    }

    private LoadingFileDetails loadLevelFromFile(String fileName) {
        LoadingFileDetails loadingFileDetails = levelFileLoader.loadLevel(context, fileName);

        return loadingFileDetails;
    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.xPress = motionEvent.getRawX();
        this.yPress = motionEvent.getRawY();
        TouchEventType touchEventType = convertTouchEventType(motionEvent);

        if (currentLevel != null) {
            return currentLevel.touch(xPress, yPress, touchEventType);
        }

        return true;
    }

    private TouchEventType convertTouchEventType(MotionEvent motionEvent) {
        int eventAction = motionEvent.getAction();

        switch (eventAction) {
            case MotionEvent.ACTION_UP:
                return TouchEventType.MOVE_BAT_KEY_UP;
            default:
                return TouchEventType.MOVE_BAT_KEY_DOWN;
        }

    }

}
