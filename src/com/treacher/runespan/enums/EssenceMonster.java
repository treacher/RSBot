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

    AIR_ESSLING(new int[]{15403, 15273}, 9.5, 1, false),
    MIND_ESSLING(new int[]{15404, 15274}, 10, 1, false),
    WATER_ESSLING(new int[]{15405, 15275}, 12.6, 5, false),
    EARTH_ESSLING(new int[]{15406, 15276}, 14.5, 9, false),
    FIRE_ESSLING(new int[]{15407}, 17.4, 14, false),

    BODY_ESSHOUND(new int[]{15408}, 23.13, 20, false),
    COSMIC_ESSHOUND(new int[]{15409}, 26.5, 27, true),
    CHAOS_ESSHOUND(new int[]{15410},  30.8, 35, true),
    ASTRAL_ESSHOUND(new int[]{15411}, 35.5, 40, true),
    NATURE_ESSHOUND(new int[]{15412}, 43.33, 44, true),
    LAW_ESSHOUND(new int[]{15413}, 54 , 54, true),

    DEATH_ESSWRAITH(new int[]{15414}, 60, 65, true),
    BLOOD_ESSWRAITH(new int[]{15415}, 73, 77, true),
    SOUL_ESSWRAITH(new int[]{15416}, 106.5, 90, true);

    private final int[] gameObjectIds;
    private final double xp;
    private int levelRequirement;
    private final boolean members;

    private EssenceMonster(int[] gameObjectIds, double xp, int levelRequirement, boolean members) {
        this.gameObjectIds = gameObjectIds;
        this.xp = xp;
        this.levelRequirement = levelRequirement;
        this.members = members;
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

    public static void siphonMonster(final Npc npc, final ClientContext ctx, Runespan runespan) {
        if(!npc.valid()) return;

        final EssenceMonster essenceMonster = EssenceMonster.findMonsterByGameObjectId(npc.id(), ctx);

        if(essenceMonster != null)
            runespan.setCurrentXpRate(essenceMonster.getXp());

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

        runespan.triggerAntiBan();
    }

    public double getXp() {
        return xp;
    }

    private boolean excluded(ClientContext ctx) {
        boolean levelTooHigh =  this.levelRequirement > ctx.skills.level(Constants.SKILLS_RUNECRAFTING);
        boolean exclude = false;
        if(!Runespan.members()) exclude = this.members;
        return exclude || levelTooHigh;
    }
}
