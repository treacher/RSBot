package com.treacher.runespan.enums;

import com.treacher.runespan.Runespan;

import java.util.List;

/**
 * Created by Michael Treacher
 */
public enum EssenceMonsters {

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

    private EssenceMonsters(int gameObjectId, Rune rune, double xp) {
        this.gameObjectId = gameObjectId;
        this.rune = rune;
        this.xp = xp;
    }

    public static boolean hasMonster(int id) {
        return (findMonsterByGameObjectId(id) != null);
    }

    public static EssenceMonsters findMonsterByGameObjectId(int id) {
        for(EssenceMonsters es : EssenceMonsters.values())
            if(es.gameObjectId == id && !es.excluded()) return es;
        return null;
    }

    public static EssenceMonsters findMonsterByRune(Rune rune) {
        for(EssenceMonsters monster : EssenceMonsters.values())
            if(monster.rune.equals(rune))return monster;
        return null;
    }

    public int getGameObjectId() {
        return this.gameObjectId;
    }

    public Rune getRune() {
        return this.rune;
    }

    public double getXp() {
        return xp;
    }


    private boolean excluded() {
        return Runespan.getExclusionList().contains(this.rune);
    }
}
