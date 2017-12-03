package org.bk.dungeon;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;

public class Ephemeral extends Container<Actor> {
    public BuyableItem item;

    public Ephemeral() {
        setTouchable(Touchable.disabled);
        setColor(1, 1, 1, 0.7f);
    }

    public void set(BuyableItem item, Actor actor) {
        this.item = item;
        setActor(actor);
    }
}
