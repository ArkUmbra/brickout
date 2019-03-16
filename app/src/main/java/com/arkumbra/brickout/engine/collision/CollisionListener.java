package com.arkumbra.brickout.engine.collision;

/**
 * Created by lukegardener on 16/03/2019.
 */

public interface CollisionListener {

  public void onBlockDamaged();
  public void onBlockDestroyed();
  public void onBatTouchBall();

}
