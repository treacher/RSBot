package com.treacher.runespan.enums;

/**
 * Created by Michael Treacher
 */

public enum Platform {

    MISSILE_PLATFORM(70489,new int[]{24221}),
    VINE_PLATFORM(70490, new int[]{24214,24216,24220}),
    COMET_PLATFORM(70493, new int[]{24224,24222,24223});

    private int platformId;
    private int[] platformRequirementRuneIds;

    Platform(int platformId, int[] platformRequirementRuneIds) {
        this.platformId = platformId;
        this.platformRequirementRuneIds = platformRequirementRuneIds;
    }

    public int getPlatformId() {
        return platformId;
    }

    public int[] getPlatformRequirementRuneIds(){
        return platformRequirementRuneIds;
    }

}
