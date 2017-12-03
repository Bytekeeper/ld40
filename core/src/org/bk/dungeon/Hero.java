package org.bk.dungeon;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Array;

public class Hero {
    private static final Array<String> RACES = new Array<String>();
    private static final Array<String> BODIES = new Array<String>();

    static {
        RACES.addAll("deep_dwarf", "deep_elf",
                "demonspawn_black", "demonspawn_red", "draconian_black", "draconian", "draconian_gold", "draconian_green",
                "draconian_gray", "draconian_mottled", "draconian_pale", "draconian_pale", "draconian_purple", "draconian_red",
                "draconian_white", "dwarf", "elf", "ghoul_2", "gnome", "halfling", "human", "kenku_winged",
                "lorc", "merfolk", "merfolk_water");
        BODIES.addAll("animal_skin",
                "aragorn", "armor_blue_gold", "armor_mummy", "arwen", "banded",
                "belt", "bikini_red", "bloody", "boromir", "bplate_green", "bplate_metal", "breast_black",
                "chainmail", "china_red", "china_red", "chunli", "coat_black", "coat_red",
                "crystal_plate", "dragon_armor_blue_new", "dragon_armor_blue_old", "dragon_armor_brown_new",
                "dragon_armor_brown_old", "dragon_armor_cyan_new", "dragon_armor_cyan_old", "dragon_armor_gold_new",
                "dragon_armor_gold_old", "dragon_armor_green", "dragon_armor_magenta_new", "dragon_armor_magenta_old",
                "dragon_armor_pearl", "dragon_armor_quicksilver", "dragon_armor_shadow", "dragon_armor_white_new",
                "dragon_armor_white_old", "dragon_scale_blue_new", "dragon_scale_blue_old", "dragon_scale_brown_new",
                "dragon_scale_brown_old", "dragon_scale_cyan_new", "dragon_scale_cyan_old", "dragon_scale_gold_new",
                "dragon_scale_gold_old", "dragon_scale_green", "dragon_scale_magenta_new", "dragon_scale_magenta_old",
                "dragon_scale_pearl", "dragon_scale_quicksilver", "dragon_scale_shadow", "dragon_scale_white_new",
                "dragon_scale_white_old", "dress_green", "dress_white", "faerie_dragon_armor", "frodo", "gandalf_g",
                "gil-galad", "gimli", "green_chain", "green_susp", "half_plate",
                "isildur", "jacket", "jacket_stud", "jessica", "karate", "lears_chain_mail",
                "leather", "leather_armor", "leather_green", "leather_heavy",
                "leather_jacket", "leather_metal", "leather_red", "leather_short", "leather_stud", "legolas",
                "maxwell_new", "maxwell_old", "merry", "mesh_black", "mesh_red", "metal_blue", "monk_black",
                "monk_blue", "neck", "orange_crystal", "pipin", "pj", "plate", "plate_and_cloth",
                "plate_and_cloth_2", "plate_black", "ringmail", "robe_black", "robe_black_gold", "robe_black_hood",
                "robe_black_red", "robe_blue", "robe_blue_green", "robe_blue_white", "robe_brown",
                "robe_clouds", "robe_cyan", "robe_gray", "robe_green", "robe_green_gold",
                "robe_jester", "robe_misfortune", "robe_of_night", "robe_purple", "robe_rainbow", "robe_red",
                "robe_red_gold", "robe_white", "robe_white", "robe_white_blue",
                "robe_white_green", "robe_white_red", "robe_yellow", "sam", "saruman", "scalemail",
                "shirt_black", "shirt_black_and_cloth", "shirt_blue", "shirt_check", "shirt_hawaii",
                "shirt_vest", "shirt_white", "shirt_white_yellow", "shoulder_pad",
                "skirt_onep_grey", "slit_black", "susp_black", "troll_hide", "vanhel", "vest_red", "zhor");
    }

    private final String sex;
    private final String race;
    private final String body;
    final int level;
    final Array<ElementType> weakness = new Array<ElementType>();
    final Array<ElementType> strength = new Array<ElementType>();

    public Hero(int level) {
        this.level = level;
        sex = MathUtils.randomBoolean() ? "male" : "female";
        race = RACES.random();
        body = BODIES.random();

        for (ElementType t: ElementType.values()) {
            if (MathUtils.random() < level / 10f) {
                strength.add(t);
            }
            if (MathUtils.random() > level / 10f) {
                weakness.add(t);
            }
        }
    }

    public Actor createActor(DungeonGame game) {
        Stack result = new Stack();
        Array<TextureAtlas.AtlasRegion> baseRegions = game.dungeon_sprites.findRegions("player/base/" + race + "_" + sex);
        if (baseRegions.size == 0) {
            System.err.println("NOT FOUND: " + "player/base/" + race + "_" + sex);
        } else {
            result.add(new AnimatedImage(new Animation<TextureRegion>(0.3f, baseRegions)));
        }
        TextureAtlas.AtlasRegion bodyRegion = game.dungeon_sprites.findRegion("player/body/" + body);
        if (bodyRegion == null) {
            System.err.println("NOT FOUND: " + "player/body/" + body);
        } else {
            result.add(new Image(bodyRegion));
        }

        return result;
    }
}
