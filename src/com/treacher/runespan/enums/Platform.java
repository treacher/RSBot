package com.treacher.runespan.enums;

/**
 * Created by Michael Treacher
 */

public enum Platform {

    MISSILE_PLATFORM(70489,new Rune[]{Rune.CHAOS}),
    VINE_PLATFORM(70490, new Rune[]{Rune.WATER, Rune.EARTH, Rune.NATURE}),
    COMET_PLATFORM(70493, new Rune[]{Rune.ASTRAL, Rune.LAW, Rune.FIRE}),
    MIST_PLATFORM(70491, new Rune[]{Rune.WATER, Rune.NATURE, Rune.BODY});

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

}
