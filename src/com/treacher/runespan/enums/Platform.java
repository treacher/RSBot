package com.treacher.runespan.enums;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Constants;

/**
 * Created by Michael Treacher
 */

public enum Platform {

    EARTH_PLATFORM_FLOOR_0(70478, new int[]{Rune.EARTH.getGameObjectId()}, 9),
    FLOAT_PLATFORM_FLOOR_0(70479, new int[]{Rune.AIR.getGameObjectId()}, 1),
    ICE_PLATFORM_FLOOR_0(70480, new int[]{Rune.AIR.getGameObjectId(), Rune.WATER.getGameObjectId()}, 15),
    SMALL_MISSILE_FLOOR_0(70481, new int[]{Rune.MIND.getGameObjectId()}, 20),
    VINE_PLATFORM_FLOOR_1(70490, new int[]{Rune.WATER.getGameObjectId(), Rune.EARTH.getGameObjectId(), Rune.NATURE.getGameObjectId()}, 44),
    COMET_PLATFORM_FLOOR_1(70493, new int[]{Rune.ASTRAL.getGameObjectId(), Rune.LAW.getGameObjectId(), Rune.FIRE.getGameObjectId()}, 55),
    MIST_PLATFORM_FLOOR_1(70491, new int[]{Rune.WATER.getGameObjectId(), Rune.NATURE.getGameObjectId(), Rune.BODY.getGameObjectId()}, 50),
    FLOAT_PLATFORM_FLOOR_1(70483, new int[]{Rune.AIR.getGameObjectId()}, 1),
    EARTH_PLATFORM_FLOOR_1(70484, new int[]{Rune.EARTH.getGameObjectId()}, 9),
    ICE_PLATFORM_FLOOR_1(70486, new int[]{Rune.AIR.getGameObjectId(), Rune.WATER.getGameObjectId()}, 15),
    SMALL_MISSILE_FLOOR_1(70487, new int[]{Rune.MIND.getGameObjectId()}, 20),
    CONJURATION_PLATFORM_FLOOR_1(70488, new int[]{Rune.MIND.getGameObjectId(), Rune.BODY.getGameObjectId()}, 25),
    MISSILE_PLATFORM_FLOOR_1(70489,new int[]{Rune.CHAOS.getGameObjectId()}, 35);

    private final int platformId, levelRequirement;
    private final int[] platformRequirementRuneIds;

    private Platform(int platformId, int[] platformRequirementRuneIds, int levelRequirement) {
        this.platformId = platformId;
        this.platformRequirementRuneIds = platformRequirementRuneIds;
        this.levelRequirement = levelRequirement;
    }

    public int getPlatformId() {
        return platformId;
    }

    public int[] getPlatformRequirementRuneIds(){
        return platformRequirementRuneIds;
    }

    public static boolean hasPlatform(int id, ClientContext ctx) {
        return getPlatform(id, ctx) != null;
    }

    public static Platform getPlatform(int gameObjectId, ClientContext ctx) {
        for(Platform p : Platform.values()) {
            if(p.levelRequirement > ctx.skills.level(Constants.SKILLS_RUNECRAFTING)) continue;
            if(p.platformId == gameObjectId) return p;
        }
        return null;
    }

}
