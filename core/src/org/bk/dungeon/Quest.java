package org.bk.dungeon;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class Quest {
    private static final Array<String> NAME1 = new Array<String>(new String[] {"The", "The dark", "Tremor in"});
    private static final Array<String> NAME2 = new Array<String>(new String[] {"pit", "awful dungeon", "beacon of hope"});
    public final String title;
    public final Array<Hero> heroes = new Array<Hero>();
    public final int evil;
    public final int timeSeconds;
    public float remainingTime;

    public Quest(float remainingTime, int evil) {
        this.title = NAME1.random() + " " + NAME2.random();
        this.evil = evil;
        this.timeSeconds = MathUtils.random(15, 20 + evil / 100);
        this.remainingTime = remainingTime;

        int numHeros = MathUtils.random(1, Math.min(4, 1 + evil / 100));

        for (int i = 0; i < numHeros; i++) {
            Hero hero = new Hero(MathUtils.random(1, 1 + evil / numHeros / 100));
            heroes.add(hero);
        }
    }
}
