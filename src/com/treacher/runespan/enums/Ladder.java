package com.treacher.runespan.enums;

import org.powerbot.script.Locatable;
import org.powerbot.script.Tile;

/**
 * Created by Michael Treacher
 */
public enum Ladder implements Locatable {

    BONE_LADDER(70510, 66, new Tile(4106,6043,1)),
    VINE_LADDER(70508, 33, new Tile(3957,6107,1));

    private int gameObjectId, levelReq;
    private Tile tile;

    private Ladder(int gameObjectId, int levelReq, Tile tile) {
        this.gameObjectId = gameObjectId;
        this.levelReq = levelReq;
        this.tile = tile;
    }

    public boolean playerHasReqLevelToUse(int playerLevel) {
        return playerLevel >= levelReq;
    }

    @Override
    public Tile tile() {
        return tile;
    }

    public static Ladder findLadder(int gameObjectId) {
        for(Ladder ladder : values()) {
            if(ladder.gameObjectId == gameObjectId) return ladder;
        }
        return null;
    }

    public static boolean hasLadder(int gameObjectId) {
        return findLadder(gameObjectId) != null;
    }
}
