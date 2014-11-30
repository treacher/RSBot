package com.treacher.runespan.enums;

/**
 * Created by Michael Treacher
 */
public enum Rune {
    FIRE(24213),
    AIR(24215),
    WATER(24214),
    BODY(24218),
    CHAOS(24221),
    NATURE(24220),
    LAW(24222),
    ESSENCE(24227),
    ASTRAL(24224),
    EARTH(24216),
    MIND(24217),
    DEATH(24219),
    BLOOD(24225),
    SOUL(24226);

    private final int gameObjectId;

    private Rune(int gameObjectId) {
        this.gameObjectId = gameObjectId;
    }

    public int getGameObjectId() {
        return gameObjectId;
    }
}
