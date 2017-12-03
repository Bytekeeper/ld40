package org.bk.dungeon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

public class DungeonGame extends ApplicationAdapter {
    public static final String PLACEHOLDER = "placeholder";
    public static final int ROOM_HEIGHT = 15;
    public static final int ROOM_WIDTH = 15;
    SpriteBatch batch;
    Skin skin;
    private Stage stage;
    TextureAtlas dungeon_sprites;
    TextureRegion selected;
    private Table floorItems;
    private Ephemeral ephemeral;
    Array<BuyableItem> availableShopItems = new Array<BuyableItem>();
    Array<Payment> payments = new Array<Payment>();
    Array<Message> messages = new Array<Message>();
    int evil;
    private Label evilLevel;
    WidgetGroup root;
    public Quest job;
    public Window lostWindow;

    public int mr;
    public int marketing;
    public EmployedMonsters employedMonsters;
    private float nextQuest;
    private float nextUpKeep;
    private int baseUpkeep;
    private float nextBaseUpkeepIncrease;
    private Label upkeepLabel;
    private Label rentLabel;
    private VerticalGroup messageList;
    private Music music;

    @Override
    public void create() {
        dungeon_sprites = new TextureAtlas(Gdx.files.internal("assets/dcssf.atlas"));
        skin = new Skin(Gdx.files.internal("assets/skin/pixthulhu-ui.json"));
        selected = new TextureRegion(new Texture(Gdx.files.internal("assets/selected.png")));

        batch = new SpriteBatch();

        TooltipManager.getInstance().instant();
        TooltipManager.getInstance().subsequentTime = 10;

        stage = new Stage(new ScalingViewport(Scaling.fit, 800, 600, new OrthographicCamera()));
        Gdx.input.setInputProcessor(stage);

        ephemeral = new Ephemeral();

        availableShopItems.addAll(
                new BuyableItem("Jiyva", 10, "Better Monster Resources! Stronger minions will come for jobs, and cost more.", "dungeon/altars/altar_jiyva", 1, 0),
                new BuyableItem("Makhleb", 15, "Better marketing! Quests reward more evil (and are harder).", "dungeon/altars/altar_makhleb_flame", 0, 1)
                        .setFrameDuration(0.1f)
                        .setPlayMode(Animation.PlayMode.LOOP_RANDOM));


        root = new WidgetGroup();
        root.setFillParent(true);

        Table table = new Table(skin);
        table.setFillParent(true);
        root.addActor(table);

        Table center = new Table();
        center.add(setupOffice()).expand();
        center.row().bottom();
        center.add(employedMonsters = new EmployedMonsters(this));
        center.row();
        center.add(setupStatusLine()).bottom();

        Table right = new Table();
        right.add(new Shop(this)).top().expand();
        right.row();
        right.add(new Jobs(this)).fill().bottom();

        table.add(center).expand().fill();
        table.add(right).fill();


        messageList = new VerticalGroup();
        messageList.setTouchable(Touchable.disabled);
        messageList.setFillParent(true);
        messageList.top().left();
        messageList.fill();
        root.addActor(messageList);
        stage.addActor(root);

        nextQuest = 12;
        evil = 1000;
        nextBaseUpkeepIncrease = 10;
        nextUpKeep = 10;

        messages.addAll(new Message("Game Design And Programming By", 10, Color.GREEN),
                new Message("Dennis Waldherr", 10, Color.YELLOW),
                new Message("Music By Eric Matyas", 10, Color.GREEN),
                new Message("www.soundimage.org", 10, Color.YELLOW),
                new Message("Graphics By various artists", 10, Color.GREEN),
                new Message("opengameart.org", 10, Color.YELLOW));
        music = Gdx.audio.newMusic(Gdx.files.internal("assets/Analog-Nostalgia.mp3"));
        music.setLooping(true);
        music.play();
    }

    private Actor setupStatusLine() {
        Table table = new Table(skin);
        table.defaults().space(0, 10, 0, 0);
        table.add("Evil:");
        table.add(evilLevel = new Label("", skin));
        table.add("Altar Upkeep:");
        table.add(upkeepLabel = new Label("", skin));
        table.add("Rent:");
        table.add(rentLabel = new Label("", skin));
        return table;
    }

    private WidgetGroup setupOffice() {
        Array<TextureAtlas.AtlasRegion> wall = dungeon_sprites.findRegions("dungeon/wall/brick_dark");
        final Array<TextureAtlas.AtlasRegion> floor = dungeon_sprites.findRegions("dungeon/floor/marble_floor");
        Table ground = new Table(skin);
        for (int j = 0; j < ROOM_HEIGHT; j++) {
            for (int i = 0; i < ROOM_WIDTH; i++) {
                if (j == 0 || j == ROOM_HEIGHT - 1 || i == 0 || i == ROOM_WIDTH - 1) {
                    ground.add(new Image(wall.random())).size(32);
                } else {
                    ground.add(new Image(floor.random())).size(32);
                }
            }
            ground.row();
        }

        floorItems = new Table();
        Array<TextureRegion> exits = new Array<TextureRegion>();
        exits.add(dungeon_sprites.findRegion("dungeon/gateways/exit_abyss_flickering_new"));
        exits.add(dungeon_sprites.findRegion("dungeon/gateways/exit_abyss_new"));
        AnimatedImage actor = new AnimatedImage(new Animation<TextureRegion>(0.2f,
                exits, Animation.PlayMode.LOOP_RANDOM));
        for (int j = 0; j < ROOM_HEIGHT; j++) {
            for (int i = 0; i < ROOM_WIDTH; i++) {
                if (j == 0 || j == ROOM_HEIGHT - 1 || i == 0 || i == ROOM_WIDTH - 1) {
                    floorItems.add().size(32);
                } else if (i != 3 || j != ground.getRows() - 2) {
                    Container placeHolder = new Container();
                    placeHolder.setName(PLACEHOLDER);
                    placeHolder.setTouchable(Touchable.enabled);
                    floorItems.add(placeHolder).size(32);
                } else {
                    floorItems.add(actor).size(32);
                }
            }
            floorItems.row();
        }

        floorItems.addListener(new InputListener() {
            Container<Actor> previousHover;

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if (previousHover != null && toActor != previousHover) {
                    previousHover.setActor(null);
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (button == 1 && previousHover != null) {
                    previousHover.setActor(null);
                    setEphemeral(null);
                    return true;
                } else if (button == 0 && previousHover != null && previousHover.getActor() != null) {
                    floorItems.getCell(previousHover).setActor(ephemeral.getActor());
                    evil -= ephemeral.item.cost;
                    mr += ephemeral.item.mr;
                    marketing += ephemeral.item.marketing;
                    previousHover.setActor(null);
                    setEphemeral(null);
                }
                return false;
            }

            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                if (previousHover != null) {
                    if (previousHover.getActor() == event.getTarget()) {
                        return false;
                    }
                    previousHover.setActor(null);
                }
                if (PLACEHOLDER.equals(event.getTarget().getName()) && ephemeral.item != null) {
                    previousHover = (Container<Actor>) event.getTarget();
                    previousHover.setActor(ephemeral);
                }
                return false;
            }
        });

        return new Stack(ground, floorItems);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        float deltaTime = Gdx.graphics.getDeltaTime();

        if (evil <= 0 && payments.size == 0) {
            if (lostWindow == null) {
                music.stop();
                Gdx.audio.newMusic(Gdx.files.internal("assets/Dont-Mess-with-the-8-Bit-Knight.mp3")).play();
                lostWindow = new Window("Better luck next time!", skin);
                lostWindow.padTop(40);
                lostWindow.add(new Label("The competiton in the\ndungeon management\nbusiness proved to be\ntoo much!", skin, "subtitle", Color.WHITE));
                lostWindow.setPosition(50, 100);
                lostWindow.setSize(700, 400);
                lostWindow.addAction(Actions.sequence(Actions.alpha(0), Actions.alpha(1, 3)));
                lostWindow.addAction(Actions.forever(new MoveToAction() {
                    @Override
                    protected void begin() {
                        super.begin();
                        setPosition(MathUtils.random(0, 100), MathUtils.random(0, 200));
                        setDuration(5);
                    }
                }));
                stage.clear();
                stage.addActor(lostWindow);
            }
        } else {
            nextUpKeep -= deltaTime;
            if (nextUpKeep < 0) {
                nextUpKeep = 30;
                int cost = mr * 10 + marketing * 15 + baseUpkeep;
                this.evil -= cost;
                messages.add(new Message("Upkeep+Rent cost " + cost, 5));
            }
            nextBaseUpkeepIncrease -= deltaTime;
            if (nextBaseUpkeepIncrease < 0) {
                nextBaseUpkeepIncrease = 50;
                baseUpkeep += 15;
                messages.add(new Message("Rent increased by 15", 5));
            }
            for (int i = payments.size - 1; i >= 0; i--) {
                Payment p = payments.get(i);
                p.timeRemaining -= deltaTime;
                if (p.timeRemaining < 0) {
                    payments.removeIndex(i);
                    evil += p.evil;
                    messages.add(new Message("Received payment: " + p.evil + " evil", 5));
                }
            }
            for (int i = messages.size - 1; i >= 0; i--) {
                Message m = messages.get(i);
                m.remaining -= deltaTime;
                if (m.remaining < 0) {
                    messages.removeIndex(i);
                    for (Actor a : messageList.getChildren()) {
                        if (a.getUserObject() == m) {
                            messageList.removeActor(a);
                        }
                    }
                }
            }
            for (int i = 0; i < messages.size; i++) {
                Message m = messages.get(i);
                if (m.isNew) {
                    Label label = new Label(m.text, skin);
                    label.setColor(m.color);
                    label.setUserObject(m);
                    messageList.addActor(label);
                    m.isNew = false;
                }
            }

            upkeepLabel.setText(Integer.toString(mr * 10 + marketing * 15));
            rentLabel.setText(Integer.toString(baseUpkeep));
            evilLevel.setText(Integer.toString(evil));
            handleNextQuest(deltaTime);
        }
        stage.act();
        stage.draw();
    }

    private void handleNextQuest(float deltaTime) {
        if (job == null) {
            if (nextQuest <= 0) {
                nextQuest = MathUtils.random(20 / (marketing + 1), 30 / (marketing + 1));
            } else {
                nextQuest -= deltaTime;
                if (nextQuest < 0) {
                    job = new Quest(MathUtils.random(5, 15 + marketing * 5), MathUtils.random(marketing * 30 + 10, marketing * 80 + 30));
                }
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        skin.dispose();
    }

    public void setEphemeral(BuyableItem item) {
        ephemeral.set(item, item != null ? actorFor(item) : null);
    }

    public boolean canAfford(BuyableItem item) {
        return evil >= item.cost;
    }

    public AnimatedImage actorFor(GameEntity entity) {
        return new AnimatedImage(new Animation<TextureRegion>(entity.frameDuration,
                dungeon_sprites.findRegions(entity.regionName), entity.playMode));
    }
}
