package org.bk.dungeon;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class AnimatedImage extends Image {
    private final Animation<TextureRegion> animation;
    private float time;
    private TextureRegionDrawable textureRegionDrawable;

    public AnimatedImage(Animation<TextureRegion> animation) {
        this.animation = animation;
        textureRegionDrawable = new TextureRegionDrawable();
        setDrawable(textureRegionDrawable);
    }

    @Override
    public void act(float delta) {
        time += delta;
        textureRegionDrawable.setRegion(animation.getKeyFrame(time));
        super.act(delta);
    }
}
