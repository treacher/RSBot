package com.treacher.runespan.enums;

import com.treacher.runespan.Runespan;
import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Npc;

import java.util.concurrent.Callable;

/**
 * Created by Michael Treacher
 */
public enum EssenceMonster {

    FIRE_ESSLING(15277, Rune.FIRE, 17.4),
    EARTH_ESSLING(15276, Rune.EARTH, 14.5),
    WATER_ESSLING(15275, Rune.WATER, 12.6),
    MIND_ESSLING(15274, Rune.MIND, 10),
    AIR_ESSLING(15273, Rune.AIR, 9.5),
    LAW_ESSHOUND(15413, Rune.LAW, 54),
    NATURE_ESSHOUND(15412, Rune.NATURE, 43.33),
    ASTRAL_ESSHOUND(15411, Rune.ASTRAL, 35.5),
    CHAOS_ESSHOUND(15410, Rune.CHAOS, 30.8),
    COSMIC_ESSHOUND(15409, Rune.COSMIC, 26.5),
    BODY_ESSHOUND(15408, Rune.BODY, 23.13);

    private final int gameObjectId;
    private final Rune rune;
    private final double xp;

    private EssenceMonster(int gameObjectId, Rune rune, double xp) {
        this.gameObjectId = gameObjectId;
        this.rune = rune;
        this.xp = xp;
    }

    public static boolean hasMonster(int id) {
        return (findMonsterByGameObjectId(id) != null);
    }

    public static EssenceMonster findMonsterByGameObjectId(int id) {
        for(EssenceMonster es : EssenceMonster.values())
            if(es.gameObjectId == id && !es.excluded()) return es;
        return null;
    }

    public static void siphonMonster(final Npc essenceMonster, final ClientContext ctx) {
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
        }, 1000, 10);

        // Wait till siphoning the monster
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return !ctx.players.local().idle();
            }
        }, 1500, 2);
    }

    public int getGameObjectId() {
        return this.gameObjectId;
    }

    public double getXp() {
        return xp;
    }

    private boolean excluded() {
        return Runespan.getExclusionList().contains(this.rune);
    }
}
