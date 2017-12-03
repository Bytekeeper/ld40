package org.bk.dungeon;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public enum ElementType {
    FIRE("effect/flame"), FROST("effect/frost");

    private final String regionName;
    private float frameDuration = 0.3f;
    private Animation.PlayMode playMode = Animation.PlayMode.LOOP;

    ElementType(String regionName) {

        this.regionName = regionName;
    }

    public Actor createActor(DungeonGame game) {
        Array<TextureAtlas.AtlasRegion> regions = game.dungeon_sprites.findRegions(regionName);
        if (regions == null) {
            throw new IllegalStateException("Not found: " + regionName);
        }
        return new AnimatedImage(new Animation<TextureRegion>(frameDuration,
                regions, playMode));
    }
}
