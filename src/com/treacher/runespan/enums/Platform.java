package com.treacher.runespan.enums;

import com.treacher.runespan.RuneSpan;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Constants;

/**
 * Created by Michael Treacher
 */

public enum Platform {

    FLOAT_PLATFORM(new int[] {70494,70476,70483}, new int[]{Rune.AIR.getGameObjectId()}, 1, false),
    EARTH_PLATFORM(new int[]{70495,70478,70484}, new int[]{Rune.EARTH.getGameObjectId()}, 9, false),
    ICE_PLATFORM(new int[]{70496, 70480, 70486}, new int[]{Rune.AIR.getGameObjectId(), Rune.WATER.getGameObjectId()}, 15, false),
    SMALL_MISSILE(new int[]{70497, 70481, 70487}, new int[]{Rune.MIND.getGameObjectId()}, 20, false),
    CONJURATION_PLATFORM(new int[]{70498, 70488}, new int[]{Rune.MIND.getGameObjectId(), Rune.BODY.getGameObjectId()}, 25, true),
    MISSILE_PLATFORM(new int[]{70499,70489},new int[]{Rune.CHAOS.getGameObjectId()}, 35, true),
    VINE_PLATFORM(new int[]{70500, 70490}, new int[]{Rune.WATER.getGameObjectId(), Rune.EARTH.getGameObjectId(), Rune.NATURE.getGameObjectId()}, 44, true),
    MIST_PLATFORM(new int[]{70501, 70491}, new int[]{Rune.WATER.getGameObjectId(), Rune.NATURE.getGameObjectId(), Rune.BODY.getGameObjectId()}, 50, true),
    COMET_PLATFORM(new int[]{70502, 70493}, new int[]{Rune.ASTRAL.getGameObjectId(), Rune.LAW.getGameObjectId(), Rune.FIRE.getGameObjectId()}, 55, true),
    SKELETAL_PLATFORM(new int[]{70503}, new int[]{Rune.DEATH.getGameObjectId()}, 65, true),
    GREATER_MISSILE_PLATFORM(new int[]{70504}, new int[]{Rune.DEATH.getGameObjectId(), Rune.BLOOD.getGameObjectId()}, 77, true),
    FLESH_PLATFORM(new int[]{70505}, new int[]{Rune.DEATH.getGameObjectId(), Rune.BLOOD.getGameObjectId(), Rune.BODY.getGameObjectId()}, 85, true),
    GREATER_CONJURATION_PLATFORM(new int[]{70506}, new int[]{
            Rune.DEATH.getGameObjectId(),
            Rune.BLOOD.getGameObjectId(),
            Rune.BODY.getGameObjectId(),
            Rune.MIND.getGameObjectId(),
            Rune.SOUL.getGameObjectId(),
    }, 95, true);

    private final int[] platformIds;
    private final int levelRequirement;
    private final int[] platformRequirementRuneIds;
    private final boolean members;

    private Platform(int[] platformIds, int[] platformRequirementRuneIds, int levelRequirement, boolean members) {
        this.platformIds = platformIds;
        this.platformRequirementRuneIds = platformRequirementRuneIds;
        this.levelRequirement = levelRequirement;
        this.members = members;
    }

    public int[] getPlatformIds() {
        return platformIds;
    }

    public int[] getPlatformRequirementRuneIds(){
        return platformRequirementRuneIds;
    }

    public static boolean hasPlatform(int id, ClientContext ctx) {
        return getPlatform(id, ctx) != null;
    }

    public static Platform getPlatform(int gameObjectId, ClientContext ctx) {
        for(Platform p : Platform.values()) {
            if(p.hasPlatformId(gameObjectId) && !p.excluded(ctx)) return p;
        }
        return null;
    }

    public boolean hasPlatformId(int gameObjectId) {
        for(int id : this.platformIds) {
            if(id == gameObjectId) return true;
        }
        return false;
    }

    private boolean excluded(ClientContext ctx) {
        boolean exclude = ctx.skills.level(Constants.SKILLS_RUNECRAFTING) < this.levelRequirement;
        if(!exclude) exclude = RuneSpan.members() || !this.members;
        return exclude;
    }

}
