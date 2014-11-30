package com.treacher.runespan.enums;

import com.treacher.runespan.RuneSpan;
import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Constants;
import org.powerbot.script.rt6.Npc;

import java.util.concurrent.Callable;

/**
 * Created by Michael Treacher
 */
public enum EssenceMonster {

    AIR_ESSLING(new int[]{15403, 15273}, Rune.AIR, 9.5, 1),
    MIND_ESSLING(new int[]{15404, 15274}, Rune.MIND, 10, 1),
    WATER_ESSLING(new int[]{15405, 15275}, Rune.WATER, 12.6, 5),
    EARTH_ESSLING(new int[]{15406, 15276}, Rune.EARTH, 14.5, 9),
    FIRE_ESSLING(new int[]{15407}, Rune.FIRE, 17.4, 14),
    
    BODY_ESSHOUND(new int[]{15408}, Rune.BODY, 23.13, 20),
    COSMIC_ESSHOUND(new int[]{15409}, Rune.COSMIC, 26.5, 27),
    CHAOS_ESSHOUND(new int[]{15410}, Rune.CHAOS, 30.8, 35),
    ASTRAL_ESSHOUND(new int[]{15411}, Rune.ASTRAL, 35.5, 40),
    NATURE_ESSHOUND(new int[]{15412}, Rune.NATURE, 43.33, 44),
    LAW_ESSHOUND(new int[]{15413}, Rune.LAW, 54 , 54),

    DEATH_ESSWRAITH(new int[]{15414},Rune.DEATH, 60, 65),
    BLOOD_ESSWRAITH(new int[]{15415},Rune.BLOOD, 73, 77),
    SOUL_ESSWRAITH(new int[]{15416},Rune.SOUL, 106.5, 90);

    private final int[] gameObjectIds;
    private final Rune rune;
    private final double xp;
    private int levelRequirement;

    private EssenceMonster(int[] gameObjectIds, Rune rune, double xp, int levelRequirement) {
        this.gameObjectIds = gameObjectIds;
        this.rune = rune;
        this.xp = xp;
        this.levelRequirement = levelRequirement;
    }

    public static boolean hasMonster(int id, ClientContext ctx) {
        return (findMonsterByGameObjectId(id, ctx) != null);
    }

    public static EssenceMonster findMonsterByGameObjectId(int id, ClientContext ctx) {
        for(EssenceMonster es : EssenceMonster.values())
            if(es.hasGameObjectId(id) && !es.excluded(ctx)) return es;
        return null;
    }

    public boolean hasGameObjectId(int gameObjectId) {
        for(int id : this.gameObjectIds) {
            if(id == gameObjectId) return true;
        }
        return false;
    }

    public static void siphonMonster(final Npc npc, final ClientContext ctx, RuneSpan runeSpan) {
        if(!npc.valid()) return;

        final EssenceMonster essenceMonster = EssenceMonster.findMonsterByGameObjectId(npc.id(), ctx);

        if(essenceMonster != null)
            runeSpan.setCurrentXpRate(essenceMonster.getXp());

        ctx.camera.turnTo(npc);
        ctx.camera.pitch(60);

        // Wait till interacting with monster
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                boolean siphoningMonster = npc.interact(false, "Siphon");
                if (!siphoningMonster)
                    ctx.movement.findPath(npc).traverse();
                return siphoningMonster;
            }
        }, 500, 8);

        // Wait till siphoning the monster
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return !ctx.players.local().idle();
            }
        }, 1500, 2);

        runeSpan.triggerAntiBan();
    }

    public double getXp() {
        return xp;
    }

    private boolean excluded(ClientContext ctx) {
        return ctx.skills.level(Constants.SKILLS_RUNECRAFTING) < this.levelRequirement;
    }
}
