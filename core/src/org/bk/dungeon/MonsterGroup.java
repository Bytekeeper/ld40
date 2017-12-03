package org.bk.dungeon;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class MonsterGroup extends GameEntity<MonsterGroup> {
    private static final Array<String> REG_NAMES = new Array<String>(new String[]{"monster/angel",
            "monster/anubis_guard", "monster/big_kobold_new",
            "monster/big_kobold_old", "monster/boggart_new", "monster/boggart_old",
            "monster/brown_ooze", "monster/centaur", "monster/centaur-melee",
            "monster/centaur_warrior", "monster/centaur_warrior-melee",
            "monster/cyclops_new", "monster/cyclops_old", "monster/daeva",
            "monster/death_drake", "monster/death_knight", "monster/deep_dwarf",
            "monster/deep_dwarf_artificer", "monster/deep_dwarf_berserker",
            "monster/deep_dwarf_death_knight", "monster/deep_elf_annihilator",
            "monster/deep_elf_blademaster", "monster/deep_elf_conjurer",
            "monster/deep_elf_death_mage", "monster/deep_elf_demonologist",
            "monster/deep_elf_fighter_new", "monster/deep_elf_fighter_old",
            "monster/deep_elf_high_priest", "monster/deep_elf_knight_new",
            "monster/deep_elf_knight_old", "monster/deep_elf_mage",
            "monster/deep_elf_master_archer", "monster/deep_elf_priest",
            "monster/deep_elf_soldier", "monster/deep_elf_sorcerer",
            "monster/deep_elf_summoner", "monster/deep_troll",
            "monster/deep_troll_berserker", "monster/deep_troll_earth_mage",
            "monster/deep_troll_shaman", "monster/demonspawn", "monster/dryad",
            "monster/dwarf_new", "monster/dwarf_old", "monster/elf_new", "monster/elf_old",
            "monster/enchantress_human", "monster/entropy_weaver", "monster/ettin_new",
            "monster/ettin_old", "monster/faun", "monster/fire_drake",
            "monster/fire_giant_new", "monster/fire_giant_old", "monster/forest_drake",
            "monster/formicid", "monster/formicid_venom_mage", "monster/frost_giant_new",
            "monster/frost_giant_old", "monster/giant_amoeba_new",
            "monster/giant_amoeba_old", "monster/giant_orange_brain",
            "monster/glowing_shapeshifter", "monster/gnoll_new", "monster/gnoll_old",
            "monster/gnoll_sergeant", "monster/gnoll_shaman", "monster/gnome",
            "monster/goblin_new", "monster/goblin_old", "monster/golden_dragon",
            "monster/grand_avatar", "monster/greater_naga", "monster/griffon",
            "monster/guardian_naga", "monster/guardian_serpent_new",
            "monster/guardian_serpent_old", "monster/halfling_new", "monster/halfling_old",
            "monster/harpy", "monster/hell_knight_new", "monster/hell_knight_old",
            "monster/hill_giant_new", "monster/hill_giant_old", "monster/hippogriff_new",
            "monster/hippogriff_old", "monster/hobgoblin_new", "monster/hobgoblin_old",
            "monster/human", "monster/human_monk_ghost", "monster/human_new",
            "monster/human_old", "monster/human_slave", "monster/hydrataur",
            "monster/ice_beast", "monster/iron_troll", "monster/iron_troll_monk_ghost",
            "monster/ironbrand_convoker", "monster/ironheart_preserver", "monster/jelly",
            "monster/juggernaut", "monster/kenku_winged", "monster/killer_klown",
            "monster/killer_klown_blue", "monster/killer_klown_green",
            "monster/killer_klown_purple", "monster/killer_klown_red",
            "monster/killer_klown_yellow", "monster/kobold_demonologist",
            "monster/kobold_new", "monster/kobold_old", "monster/labrat_unseen",
            "monster/lava_worm", "monster/lindwurm", "monster/manticore",
            "monster/merfolk", "monster/merfolk_aquamancer_new",
            "monster/merfolk_aquamancer_old", "monster/merfolk_aquamancer_water_new",
            "monster/merfolk_aquamancer_water_old", "monster/merfolk_avatar",
            "monster/merfolk_avatar_water", "monster/merfolk_fighter",
            "monster/merfolk_fighter_water", "monster/merfolk_impaler_new",
            "monster/merfolk_impaler_old", "monster/merfolk_impaler_water_new",
            "monster/merfolk_impaler_water_old", "monster/merfolk_javelineer_new",
            "monster/merfolk_javelineer_old", "monster/merfolk_javelineer_water_new",
            "monster/merfolk_javelineer_water_old", "monster/merfolk_plain",
            "monster/merfolk_plain_water", "monster/merfolk_water", "monster/mermaid",
            "monster/mermaid_water", "monster/minotaur", "monster/moth_of_suppression",
            "monster/mutant_beast", "monster/naga", "monster/naga_mage",
            "monster/naga_ritualist", "monster/naga_sharpshooter", "monster/naga_warrior",
            "monster/naga_warrior_unique", "monster/necromancer_new",
            "monster/necromancer_old", "monster/ogre_mage_new", "monster/ogre_mage_old",
            "monster/ogre_new", "monster/ogre_old", "monster/orb_guardian_new",
            "monster/orb_guardian_old", "monster/orc_high_priest_new",
            "monster/orc_high_priest_old", "monster/orc_knight_new",
            "monster/orc_knight_old", "monster/orc_new", "monster/orc_old",
            "monster/orc_priest_new", "monster/orc_priest_old", "monster/orc_sorcerer_new",
            "monster/orc_sorcerer_old", "monster/orc_warlord", "monster/orc_warrior_new",
            "monster/orc_warrior_old", "monster/orc_wizard_new", "monster/orc_wizard_old",
            "monster/phoenix", "monster/pulsating_lump", "monster/quasit", "monster/raven",
            "monster/rock_troll", "monster/rock_troll_monk_ghost", "monster/salamander",
            "monster/salamander_firebrand", "monster/salamander_mystic",
            "monster/salamander_stormcaller", "monster/satyr", "monster/shadow_imp",
            "monster/shapeshifter", "monster/siren_new", "monster/siren_old",
            "monster/siren_water_new", "monster/siren_water_old", "monster/slave_freed",
            "monster/sphinx_new", "monster/sphinx_old", "monster/spriggan_berserker",
            "monster/spriggan_defender_shieldless", "monster/spriggan_enchanter",
            "monster/spriggan_rider", "monster/stone_giant_new", "monster/stone_giant_old",
            "monster/swamp_drake", "monster/tengu", "monster/tengu_conjurer",
            "monster/tengu_reaver", "monster/tengu_warrior", "monster/titan_new",
            "monster/titan_old", "monster/troll", "monster/two_headed_ogre_new",
            "monster/two_headed_ogre_old", "monster/water_nymph", "monster/wizard",
            "monster/yaktaur-melee_new", "monster/yaktaur-melee_old",
            "monster/yaktaur_captain-melee_new", "monster/yaktaur_captain-melee_old",
            "monster/yaktaur_captain_new", "monster/yaktaur_captain_old",
            "monster/yaktaur_new", "monster/yaktaur_old"});
    private static final Array<String> NAME_SINGLE1 = new Array<String>(new String[]{"Ha", "Go", "To", "Za", "Te",
            "Lo", "Nefe", "Ana", "Ara", "Boro"});
    private static final Array<String> NAME_SINGLE2 = new Array<String>(new String[]{"nk", "bbler", "rdy", "lork",
            "kraz", "ki", "ral", "kin", "gorn", "mir"});
    private static final Array<String> ADD_WEAK = new Array<String>(new String[]{"the fearful", "the scared",
            "the puny", "the fast", "the sticky"});
    private static final Array<String> ADD_MEDIOCRE = new Array<String>(new String[]{"the mediocre", "the not so scary",
            "the 'ok' ", "the average", "the placeholder"});
    private static final Array<String> ADD_STRONG = new Array<String>(new String[]{"the resilient", "the vigilant",
            "the brutal", "the gory", "the deathbringer", "the killjoy", "the ruffian"});
    final String name;
    final int cost;
    final String description;
    public final int level;
    public final Array<ElementType> weakness = new Array<ElementType>();
    public final Array<ElementType> strength = new Array<ElementType>();
    public int amount;
    public boolean selected;
    public float occupiedRemaining;
    public int willDie;
    public boolean willBeFired;


    public MonsterGroup(int cost) {
        super(REG_NAMES.random());
        this.cost = cost;
        amount = Math.max(1, MathUtils.random(0, (int) Math.sqrt(10 * cost)) - 10);
        level = Math.max(1, MathUtils.random((int) Math.sqrt(5 * cost), (int) Math.sqrt(12 * cost)) / amount);
        String add = level < 10 ? ADD_WEAK.random() : level < 20 ? "" : level < 30 ? ADD_MEDIOCRE.random() : ADD_STRONG.random();
        String nameElement = (NAME_SINGLE1.random() + NAME_SINGLE2.random()) + (amount == 1 ? "" : "s");
        if (add.length() > 0) {
            if (amount > 1) {
                nameElement = Character.toUpperCase(add.charAt(0)) + add.substring(1) + " " + nameElement;
            } else {
                nameElement += " " + add;
            }
        }
        this.name = nameElement;
        this.description = "";
        for (ElementType t: ElementType.values()) {
            if (MathUtils.random() < cost / 200f) {
                strength.add(t);
            }
            if (MathUtils.random() > cost / 200f) {
                weakness.add(t);
            }
        }
    }
}
