package com.treacher.runespan.enums;

/**
 * Created by Michael Treacher
 */

public enum Platform {


    VINE_PLATFORM(70490, new Rune[]{Rune.WATER, Rune.EARTH, Rune.NATURE}),
    COMET_PLATFORM(70493, new Rune[]{Rune.ASTRAL, Rune.LAW, Rune.FIRE}),
    MIST_PLATFORM(70491, new Rune[]{Rune.WATER, Rune.NATURE, Rune.BODY}),
    FLOAT_PLATFORM(70483, new Rune[]{Rune.AIR}),
    EARTH_PLATFORM(70484, new Rune[]{Rune.EARTH}),
    ICE_PLATFORM(70486, new Rune[]{Rune.AIR, Rune.WATER}),
    SMALL_MISSILE(70487, new Rune[]{Rune.MIND}),
    CONJURATION_PLATFORM(70488, new Rune[]{Rune.MIND, Rune.BODY}),
    MISSILE_PLATFORM(70489,new Rune[]{Rune.CHAOS});

    private final int platformId;
    private final Rune[] platformRequirementRunes;

    private Platform(int platformId, Rune[] platformRequirementRunes) {
        this.platformId = platformId;
        this.platformRequirementRunes = platformRequirementRunes;
    }

    public int getPlatformId() {
        return platformId;
    }

    public Rune[] getPlatformRequirementRunes(){
        return platformRequirementRunes;
    }

    public static boolean hasPlatform(int id) {
        return getPlatform(id) != null;
    }

    public static Platform getPlatform(int gameObjectId) {
        for(Platform p : Platform.values())
            if(p.platformId == gameObjectId) return p;
        return null;
    }

}
