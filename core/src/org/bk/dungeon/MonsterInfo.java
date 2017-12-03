package org.bk.dungeon;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

class MonsterInfo extends Window {
    private final Actor relativeTo;
    private final boolean above;
    private final DungeonGame game;
    private final Label level;
    private final Label cost;
    private final HorizontalGroup weakness;
    private final HorizontalGroup strength;
    private Label amount;
    private final Vector2 tv = new Vector2();

    public MonsterInfo(Actor relativeTo, boolean above, DungeonGame game) {
        super("", game.skin);
        this.relativeTo = relativeTo;
        this.above = above;
        this.game = game;
        setVisible(false);
        setMovable(false);
        setResizable(false);
        padTop(40);
        top();
        left();

        defaults().left().top();

        amount = new Label("", game.skin);
        add(amount);
        row();
        level = new Label("", game.skin);
        add(level);
        row();
        cost = new Label("", game.skin);
        add(cost);
        row();
        strength = new HorizontalGroup();
        add(strength);
        row();
        weakness = new HorizontalGroup();
        add(weakness);
    }

    @Override
    public void layout() {
        super.layout();
        relativeTo.localToStageCoordinates(tv.setZero());
        getParent().stageToLocalCoordinates(tv);
        setBounds(tv.x, tv.y + (above ? relativeTo.getHeight() : -210), relativeTo.getWidth(), 210);
    }

    @Override
    public void setUserObject(Object userObject) {
        MonsterGroup mg = (MonsterGroup) userObject;
        getTitleLabel().setText(mg.name);
        amount.setText("Amount: " + mg.amount);
        level.setText("Level: " + mg.level);
        cost.setText("Cost per use: " + mg.cost);
        weakness.clear();
        weakness.addActor(new Label("Weakness: ", game.skin));
        for (ElementType elementType : mg.weakness) {
            weakness.addActor(elementType.createActor(game));
        }
        strength.clear();
        strength.addActor(new Label("Strength: ", game.skin));
        for (ElementType elementType : mg.strength) {
            strength.addActor(elementType.createActor(game));
        }
    }
}
