package com.arkumbra.brickout.engine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Vibrator;
import android.view.MotionEvent;

import com.arkumbra.brickout.engine.collision.CollisionListener;
import com.arkumbra.brickout.engine.entity.GameDrawable;
import com.arkumbra.brickout.engine.level.Level;
import com.arkumbra.brickout.engine.entity.GameEntity;
import com.arkumbra.brickout.engine.level.LevelFileLoader;
import com.arkumbra.brickout.engine.level.LevelProgress;
import com.arkumbra.brickout.engine.level.LoadingFileDetails;
import com.arkumbra.brickout.engine.touch.TouchEventType;
import com.arkumbra.brickout.view.ControlsUI;
import com.arkumbra.brickout.view.FontUtils;
import com.arkumbra.brickout.view.SurfaceDimensions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lukegardener on 2017/07/28.
 */

public class GameEngineImpl implements GameEngine, CollisionListener {

    private static final String LOG_TAG = "GameEngine";
    private static final int BACKGROUND_COLOR = Color.BLACK;
    private static final LevelFileLoader levelFileLoader = new LevelFileLoader();

    private SurfaceDimensions surfaceDimensions;

    private GameState gameState = GameState.UNPAUSE;

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

    private Vibrator vibrator;

    public GameEngineImpl(Context context) {
        this.context = context;

        FontUtils.initialise(context);

        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
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

        //controlsUI = new ControlsUI(context, this, 0, canvasHeight - 120, canvasWidth, canvasHeight);
        controlsUI = new ControlsUI(context, this, canvasWidth, canvasHeight);

        this.initialised = true;
    }

    // temp
    private int levelCount = 0;
    private String[] levels = new String[]{"level1", "level2", "level4", "level3", "level5", "level6", "level7", "level8"};

    // Lock this so only activity can activate?
    @Override
    public void startEngine() {
        if (levelCount > levels.length - 1) {
            levelCount = 0;
        }

        loadLevel("world1/" + levels[levelCount] + ".brk");
    }

    @Override
    public boolean isInitialisedYet() {
        return initialised;
    }

    @Override
    public void update(long msDelta) {
        if (currentLevel == null) {
            return;
        }

        if (gameState == GameState.PAUSE) {
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

    // TODO move
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
        pausePainter.setColor(Color.WHITE);
        pausePainter.setTypeface(FontUtils.getAppTypeface());
        pausePainter.setTextSize(surfaceDimensions.getWidthPixels() / 10);
        pausePainter.setTextAlign(Paint.Align.CENTER);



        if (gameState == GameState.PAUSE) {
            drawTextInMiddleOfScreen(canvas, "PAUSE");
        }

        if (gameOver) {
            drawTextInMiddleOfScreen(canvas, "GAME OVER");
        } else if (levelClear) {
            drawTextInMiddleOfScreen(canvas, "LEVEL CLEAR!");

        }
    }

    private void drawTextInMiddleOfScreen(Canvas canvas, String text) {
        canvas.drawText(text, surfaceDimensions.getWidthPixels() / 2,
                surfaceDimensions.getHeightPixels() / 2,
                pausePainter);
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

        level.registerCollisionListener(this);
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

        // TODO needed?
        if (currentLevel == null) {
            return false;
        }

        if (controlsUI.onTouchEvent(motionEvent)) {
            return true;
        }

        TouchEventType touchEventType = convertTouchEventType(motionEvent);
        if (currentLevel.touch(xPress, yPress, touchEventType)) {
            return true;
        }

        return false;
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

    @Override
    public void onStateChange(GameState gameState) {
        this.gameState = gameState;

        // TODO other set up, timers etc
    }

    public Context getContext() {
        return context;
    }

    @Override
    public void onBlockDamaged() {
      vibrator.vibrate(20);
    }

    @Override
    public void onBlockDestroyed() {
      vibrator.vibrate(70);
    }

  @Override
  public void onBatTouchBall() {
    vibrator.vibrate(70);
  }
}
