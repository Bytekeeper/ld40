package org.bk.dungeon;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class Jobs extends Window {
    private final DungeonGame game;
    private Label jobLabel;
    private final Button acceptButton;
    private final Button declineButton;
    private final Vector2 tv = new Vector2();

    public Jobs(final DungeonGame game) {
        super("", game.skin);
        this.game = game;

        jobLabel = new Label("", game.skin);
        add(jobLabel);
        row();
        HorizontalGroup buttons = new HorizontalGroup();
        acceptButton = new Button(new Image(game.dungeon_sprites.findRegion("gui/prompt_yes")), game.skin);
        buttons.addActor(acceptButton);
        declineButton = new Button(new Image(game.dungeon_sprites.findRegion("gui/prompt_no")), game.skin);
        buttons.addActor(declineButton);
        add(buttons);

        acceptButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float score = 0;
                for (MonsterGroup g: game.employedMonsters.monsters) {
                    if (g.selected) {
                        game.evil -= g.cost;
                        g.occupiedRemaining = game.job.timeSeconds;
                        for (Hero h: game.job.heroes) {
                            float mod = g.amount * g.level / 5f - h.level;
                            score += mod;
                            for (ElementType et: g.strength) {
                                if (h.weakness.contains(et, true)) {
                                    score += mod / 4;
                                }
                                if (h.strength.contains(et, true)) {
                                    score -= mod / 4;
                                }
                            }
                            for (ElementType et: h.strength) {
                                if (g.weakness.contains(et, true)) {
                                    score -= mod / 4;
                                }
                                if (g.strength.contains(et, true)) {
                                    score += mod / 4;
                                }
                            }
                        }
                    }
                }
                boolean survivors = false;
                for (MonsterGroup g: game.employedMonsters.monsters) {
                    if (g.selected) {
                        g.willDie = (int) MathUtils.clamp((MathUtils.random() * 2 - score) * g.amount, 0, g.amount);
                        g.selected = false;
                        survivors |= g.amount > g.willDie;
                    }
                }
                game.employedMonsters.updateMonsters();
                if (survivors) {
                    game.payments.add(new Payment(game.job.evil, game.job.timeSeconds));
                }
                game.job = null;
            }
        });

        declineButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.job = null;
            }
        });

        Window warningWindow = new Window("", game.skin) {
            private Label warningLabel = new Label("", game.skin);
            {
                warningLabel.setColor(Color.RED);
                warningLabel.setWrap(true);
                add(warningLabel).expand().fill().left();
            }
            @Override
            public void act(float delta) {
                int selectedCost = 0;
                for (MonsterGroup g: game.employedMonsters.monsters) {
                    if (g.selected) {
                        selectedCost += g.cost;
                    }
                }
                if (game.evil < selectedCost) {
                    warningLabel.setText("Not enough evil for selected monsters!");
                    setVisible(true);
                } else {
                    setVisible(false);
                }
                super.act(delta);
            }

            @Override
            public void layout() {
                super.layout();
                Jobs.this.localToStageCoordinates(tv.setZero());
                getParent().stageToLocalCoordinates(tv);
                setBounds(tv.x, tv.y + Jobs.this.getHeight(), Jobs.this.getWidth(), 100);
            }
        };

        final Window infoWindow = new QuestInfo(game);
        jobLabel.addListener(new InputListener() {
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                infoWindow.setVisible(false);
                infoWindow.setUserObject(null);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (game.job != null && game.job != infoWindow.getUserObject()) {
                    infoWindow.setUserObject(game.job);
                    infoWindow.setVisible(true);
                }
            }
        });
        infoWindow.setVisible(false);
        game.root.addActor(infoWindow);
        game.root.addActor(warningWindow);
    }

    @Override
    public void act(float delta) {
        if (game.job == null) {
            jobLabel.setText("No Quests");
            acceptButton.setVisible(false);
            declineButton.setVisible(false);
        } else {
            jobLabel.setText(game.job.title);
            acceptButton.setDisabled(true);
            acceptButton.setVisible(true);
            acceptButton.setTouchable(Touchable.disabled);
            int cost = 0;
            for (MonsterGroup m : game.employedMonsters.monsters) {
                if (m.selected) {
                    acceptButton.setDisabled(false);
                    acceptButton.setTouchable(Touchable.enabled);
                    cost += m.cost;
                    break;
                }
            }
            if (cost > game.evil) {
                acceptButton.setDisabled(true);
                acceptButton.setTouchable(Touchable.disabled);
            }
            declineButton.setVisible(true);
        }
        super.act(delta);
    }

    private class QuestInfo extends Window {
        private final DungeonGame game;

        private HorizontalGroup lines;

        public QuestInfo(DungeonGame game) {
            super("", game.skin);
            this.game = game;
            setMovable(false);
            setResizable(false);
            left();
            top();
        }

        @Override
        public void layout() {
            super.layout();
            Jobs.this.localToStageCoordinates(tv.setZero());
            getParent().stageToLocalCoordinates(tv);
            setBounds(tv.x, tv.y + Jobs.this.getHeight(), Jobs.this.getWidth(), 300);
            if (lines != null) {
                lines.setWidth(Jobs.this.getColumnWidth(0));
            }
        }

        @Override
        public void setUserObject(Object userObject) {
            super.setUserObject(userObject);
            if (userObject == null) {
                return;
            }
            Quest quest = (Quest) userObject;
            clearChildren();
            defaults().left().top();
            for (Hero h : quest.heroes) {
                lines = new HorizontalGroup();
                lines.addActor(h.createActor(game));
                lines.addActor(new Label("L: " + h.level, getSkin()));
                add(lines).row();

                lines = new HorizontalGroup();
                lines.addActor(new Label("S:", getSkin()));
                for (ElementType et : h.strength) {
                    lines.addActor(et.createActor(game));
                }
                lines.addActor(new Label(",W:", getSkin()));
                for (ElementType et : h.weakness) {
                    lines.addActor(et.createActor(game));
                }
                add(lines).row();
            }
            add("Evil Reward:" + quest.evil);
        }
    }
}
