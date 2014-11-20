package com.treacher.runespan.enums;

import com.treacher.runespan.Runespan;
import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Constants;
import org.powerbot.script.rt6.Npc;

import java.util.concurrent.Callable;

/**
 * Created by Michael Treacher
 */
public enum EssenceMonster {

    FIRE_ESSLING_FLOOR_0(15407, Rune.FIRE, 17.4, 14),
    EARTH_ESSLING_FLOOR_0(15406, Rune.EARTH, 14.5, 9),
    WATER_ESSLING_FLOOR_0(15405, Rune.WATER, 12.6, 5),
    MIND_ESSLING_FLOOR_0(15404, Rune.MIND, 10, 1),
    AIR_ESSLING_FLOOR_0(15403, Rune.AIR, 9.5, 1),
    EARTH_ESSLING_FLOOR_1(15276, Rune.EARTH, 14.5, 9),
    WATER_ESSLING_FLOOR_1(15275, Rune.WATER, 12.6, 5),
    MIND_ESSLING_FLOOR_1(15274, Rune.MIND, 10, 1),
    AIR_ESSLING_FLOOR_1(15273, Rune.AIR, 9.5, 1),
    LAW_ESSHOUND_FLOOR_1(15413, Rune.LAW, 54 , 54),
    NATURE_ESSHOUND_FLOOR_1(15412, Rune.NATURE, 43.33, 44),
    ASTRAL_ESSHOUND_FLOOR_1(15411, Rune.ASTRAL, 35.5, 40),
    CHAOS_ESSHOUND_FLOOR_1(15410, Rune.CHAOS, 30.8, 35),
    COSMIC_ESSHOUND_FLOOR_1(15409, Rune.COSMIC, 26.5, 27),
    BODY_ESSHOUND_FLOOR_1(15408, Rune.BODY, 23.13, 20);

    private final int gameObjectId;
    private final Rune rune;
    private final double xp;
    private int levelRequirement;

    private EssenceMonster(int gameObjectId, Rune rune, double xp, int levelRequirement) {
        this.gameObjectId = gameObjectId;
        this.rune = rune;
        this.xp = xp;
        this.levelRequirement = levelRequirement;
    }

    public static boolean hasMonster(int id, ClientContext ctx) {
        return (findMonsterByGameObjectId(id, ctx) != null);
    }

    public static EssenceMonster findMonsterByGameObjectId(int id, ClientContext ctx) {
        for(EssenceMonster es : EssenceMonster.values())
            if(es.gameObjectId == id && !es.excluded(ctx)) return es;
        return null;
    }

    public static void siphonMonster(final Npc essenceMonster, final ClientContext ctx, Runespan runespan) {
        if(!essenceMonster.valid()) return;
        ctx.camera.turnTo(essenceMonster);
        ctx.camera.pitch(60);

        // Wait till interacting with monster
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                boolean siphoningMonster = essenceMonster.interact(false, "Siphon");
                if (!siphoningMonster)
                    ctx.movement.findPath(essenceMonster).traverse();
                return siphoningMonster;
            }
        }, 1000, 4);

        // Wait till siphoning the monster
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return !ctx.players.local().idle();
            }
        }, 1500, 2);

        runespan.triggerAntiBan();
    }

    public int getGameObjectId() {
        return this.gameObjectId;
    }

    public double getXp() {
        return xp;
    }

    private boolean excluded(ClientContext ctx) {
        boolean excluded = Runespan.getExclusionList().contains(rune);

        if(ctx.skills.level(Constants.SKILLS_RUNECRAFTING) < this.levelRequirement) excluded = true;

        return excluded;
    }
}
