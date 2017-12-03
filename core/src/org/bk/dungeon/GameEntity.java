package org.bk.dungeon;

import com.badlogic.gdx.graphics.g2d.Animation;

public class GameEntity<T extends GameEntity> {
    final String regionName;
    float frameDuration = 0.3f;
    Animation.PlayMode playMode = Animation.PlayMode.LOOP;

    public GameEntity(String regionName) {
        this.regionName = regionName;
    }

    public T setFrameDuration(float frameDuration) {
        this.frameDuration = frameDuration;
        return (T) this;
    }

    public T setPlayMode(Animation.PlayMode playMode) {
        this.playMode = playMode;
        return (T) this;
    }
}
