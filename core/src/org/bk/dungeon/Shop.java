package org.bk.dungeon;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Array;

public class Shop extends Table {
    private final DungeonGame game;
    private final Window officeStuff;
    private final Window itemInfo;
    private Label descLabel;
    private Label costLabel;
    private Array<MonsterGroup> applicants = new Array<MonsterGroup>();
    private Window monsterInfo;
    private final Vector2 tv = new Vector2();

    public Shop(final DungeonGame game) {
        super(game.skin);
        this.game = game;


        officeStuff = new Window("Ye Olde Office Stuff", game.skin);
        officeStuff.padTop(40);
        officeStuff.left().top();
        officeStuff.setResizable(false);
        officeStuff.setMovable(false);

        updateShopItems();

        add(officeStuff).expandX().fillX();
        row();
        Window recruiting = new Recruiting(game);
        add(recruiting).expandX().fillX();
        row();
        itemInfo = new Window("", game.skin) {
            @Override
            public void layout() {
                super.layout();
                officeStuff.localToStageCoordinates(tv.setZero());
                getParent().stageToLocalCoordinates(tv);
                setBounds(tv.x, tv.y - 300, officeStuff.getWidth(), 300);
                getCell(descLabel).width(itemInfo.getColumnWidth(0));
            }
        };
        itemInfo.padTop(40)
                .left()
                .top();
        itemInfo.setResizable(false);
        itemInfo.setMovable(false);
        itemInfo.row().expandX().left();
        descLabel = new Label("", getSkin());
        descLabel.setWrap(true);
        costLabel = new Label("", getSkin());
        itemInfo.add(costLabel);
        itemInfo.row();
        itemInfo.add(descLabel);
        hideItemInfo();

        game.root.addActor(itemInfo);
        game.root.addActor(monsterInfo);
    }

    void updateShopItems() {
        officeStuff.clearChildren();
        int column = 0;
        for (final BuyableItem item : game.availableShopItems) {
            AnimatedImage itemActor = game.actorFor(item);
            itemActor.addListener(new InputListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    event.getTarget().setColor(Color.SKY);
                    describeItem(item);
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    if (event.getTarget() != toActor) {
                        event.getTarget().setColor(Color.WHITE);
                        hideItemInfo();
                    }
                }
            });
            itemActor.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (game.canAfford(item)) {
                        game.setEphemeral(item);
                    }
                    return true;
                }
            });
            officeStuff.add(itemActor);
            if (column++ == 4) {
                officeStuff.row();
                column = 0;
            }
        }
    }

    private void describeItem(BuyableItem item) {
        itemInfo.getTitleLabel().setText(item.name);
        costLabel.setText("Upkeep&price: " + item.cost);
        if (game.canAfford(item)) {
            costLabel.setColor(Color.WHITE);
        } else {
            costLabel.setColor(Color.RED);
        }
        descLabel.setText(item.description);
        itemInfo.setVisible(true);
    }

    private void hideItemInfo() {
        itemInfo.setVisible(false);
    }

    private class Recruiting extends Window {
        private final DungeonGame game;
        private boolean dirty;
        private float nextApplicantTimer;

        public Recruiting(DungeonGame game) {
            super("Monster Resources", game.skin);
            this.game = game;
            padTop(40);
            left().top();
            setResizable(false);
            setMovable(false);
            update();

            monsterInfo = new MonsterInfo(this, false, game);
        }

        private void update() {
            clearChildren();
            for (final MonsterGroup g: applicants) {
                final AnimatedImage actor = game.actorFor(g);
                actor.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        applicants.removeValue(g, true);
                        dirty = true;
                        if (game.employedMonsters.monsters.size < 8) {
                            g.occupiedRemaining = MathUtils.random(5, 10);
                            game.employedMonsters.monsters.add(g);
                            game.employedMonsters.updateMonsters();
                        }
                        return true;
                    }

                    @Override
                    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                        if (event.getTarget() != actor) {
                            return;
                        }
                        monsterInfo.setVisible(false);
                    }

                    @Override
                    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                        monsterInfo.setUserObject(g);
                        monsterInfo.setVisible(true);
                    }
                });
                add(actor);
            }
        }

        @Override
        public void act(float delta) {
            for (int i = applicants.size - 1; i >= 0; i--) {
                MonsterGroup group = applicants.get(i);
                group.occupiedRemaining -= delta;
                if (group.occupiedRemaining <= 0) {
                    applicants.removeIndex(i);
                    dirty = true;
                }
            }
            if (applicants.size < 6) {
                nextApplicantTimer -= delta;
                if (nextApplicantTimer <= 0) {
                    float baseTimer = 3 + 5 / (game.mr + 1);
                    nextApplicantTimer = MathUtils.random(baseTimer, baseTimer + 5 / (game.mr + 1));

                    dirty = true;
                    addApplicant();
                }
            }
            if (dirty) {
                dirty = false;
                update();
            }
            super.act(delta);
        }

        private void addApplicant() {
            MonsterGroup monsterGroup = new MonsterGroup(MathUtils.random(game.mr * 10 + 5, game.mr * 15 + 10));
            monsterGroup.occupiedRemaining = MathUtils.random(15, 40);
            applicants.add(monsterGroup);
        }
    }
}
