package com.arkumbra.brickout.engine.collision;

/**
 * Created by lukegardener on 16/03/2019.
 */

public interface CollisionObserver {

  public void registerCollisionListener(CollisionListener listener);
  public void notifyDamaged();
  public void notifyDestroyed();
  public void notifyBatTouchBall();

}