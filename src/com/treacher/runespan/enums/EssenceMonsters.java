package com.treacher.runespan.enums;

/**
 * Created by treach3r on 11/11/14.
 */
public enum EssenceMonsters {

    FIRE_ESSLING(15277, Rune.FIRE),
    EARTH_ESSLING(15276, Rune.EARTH),
    WATER_ESSLING(15275, Rune.WATER),
    MIND_ESSLING(15274, Rune.MIND),
    AIR_ESSLING(15273, Rune.AIR),
    LAW_ESSHOUND(15413, Rune.LAW),
    NATURE_ESSHOUND(15412, Rune.NATURE),
    ASTRAL_ESSHOUND(15411, Rune.ASTRAL),
    CHAOS_ESSHOUND(15410, Rune.CHAOS),
    COSMIC_ESSHOUND(15409, Rune.COSMIC),
    BODY_ESSHOUND(15408, Rune.BODY);

    private final int gameObjectId;
    private final Rune rune;

    public static final int[] PRIORITY_ID_LIST = new int[] {
            LAW_ESSHOUND.getGameObjectId(),
            NATURE_ESSHOUND.getGameObjectId(),
            ASTRAL_ESSHOUND.getGameObjectId(),
            CHAOS_ESSHOUND.getGameObjectId(),
            COSMIC_ESSHOUND.getGameObjectId(),
            BODY_ESSHOUND.getGameObjectId(),
            AIR_ESSLING.getGameObjectId(),
            MIND_ESSLING.getGameObjectId(),
            WATER_ESSLING.getGameObjectId(),
            EARTH_ESSLING.getGameObjectId(),
            FIRE_ESSLING.getGameObjectId()
    };

    private EssenceMonsters(int gameObjectId, Rune rune) {
        this.gameObjectId = gameObjectId;
        this.rune = rune;
    }

    public int getGameObjectId() {
        return this.gameObjectId;
    }

    public Rune getRune() {
        return this.rune;
    }
}
