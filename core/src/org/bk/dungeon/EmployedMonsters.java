package org.bk.dungeon;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Array;

public class EmployedMonsters extends Window {
    private final DungeonGame game;
    private final Window monsterInfo;
    Array<MonsterGroup> monsters = new Array<MonsterGroup>(8);
    private boolean dirty;

    public EmployedMonsters(final DungeonGame game) {
        super("", game.skin);
        this.game = game;

        monsterInfo = new MonsterInfo(this, true, game);

        game.root.addActor(monsterInfo);
        updateMonsters();
        addListener(new TextTooltip("Left-click to select\nright-click to fire", game.skin));
    }

    @Override
    public void act(float delta) {
        for (int i = monsters.size - 1; i >= 0; i--) {
            MonsterGroup g = monsters.get(i);
            if (g.occupiedRemaining > 0) {
                g.occupiedRemaining -= delta;
                if (g.occupiedRemaining <= 0) {
                    g.occupiedRemaining = 0;
                    g.amount -= g.willDie;
                    if (g.amount <= 0) {
                        game.messages.add(new Message(g.willDie == 1 ? g.name + " was KIA" : "All " + g.name + " were KIA.", 5));
                        monsters.removeIndex(i);
                    } else if (g.willDie > 0) {
                        game.messages.add(new Message(g.willDie + " of " + g.name + " were killed on duty", 5));
                    }
                    if (g.willBeFired) {
                        monsters.removeIndex(i);
                    }
                    dirty = true;
                }
            }
        }
        if (dirty) {
            dirty = false;
            updateMonsters();
        }
        super.act(delta);
    }

    public void updateMonsters() {
        clearChildren();
        for (int i = 0; i < 8; i++) {
            if (i < monsters.size) {
                final MonsterGroup group = monsters.get(i);
                final Stack stack = new Stack();
                AnimatedImage actor = game.actorFor(group);
                if (group.occupiedRemaining > 0) {
                    actor.addAction(Actions.forever(Actions.sequence(Actions.alpha(0.5f, 0.5f), Actions.alpha(1f, 0.5f))));
                }
                stack.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        if (button == 0) {
                            if (!group.selected && group.occupiedRemaining == 0) {
                                markSelected(stack);
                                group.selected = true;
                            } else if (group.selected) {
                                stack.getChildren().removeIndex(1);
                                group.selected = false;
                            }
                        } else if (button == 1) {
                            group.willBeFired = true;
                            group.occupiedRemaining = MathUtils.random(5, 10);
                            dirty = true;
                        }
                        return true;
                    }

                    @Override
                    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                        monsterInfo.setVisible(true);
                        monsterInfo.setUserObject(group);
                    }

                    @Override
                    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                        if (toActor == event.getTarget()) {
                            return;
                        }
                        monsterInfo.setVisible(false);
                    }
                });
                stack.add(actor);
                if (group.selected) {
                    markSelected(stack);
                }
                add(stack).size(48);
            } else {
                add().size(48);
            }
        }
    }

    private void markSelected(Stack stack) {
        Image selectionFrame = new Image(game.selected);
        selectionFrame.setColor(1, 1, 0, 1);
        stack.add(selectionFrame);
    }
}
