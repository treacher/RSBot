package com.treacher.runespan.enums;

/**
 * Created by treach3r on 11/11/14.
 */
public enum ElementalNode {

    SHIFTER(70465,87),
    NEBULA(70464,86),
    CHAOTIC_CLOUD(70463,61),
    FLESHY_GROWTH(70461,46),
    FIRE_STORM(70462,42),
    FIRE_BALL(70459,35);

    private final int gameObjectId, xp;

    private ElementalNode(int gameObjectId, int xp) {
        this.gameObjectId = gameObjectId;
        this.xp = xp;
    }

    public int getGameObjectId() {
        return gameObjectId;
    }

    public int getXp() {
        return gameObjectId;
    }
}
