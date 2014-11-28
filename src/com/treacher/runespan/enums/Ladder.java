package com.treacher.runespan.enums;

/**
 * Created by treach3r on 28/11/14.
 */
public enum Ladder {

    BONE_LADDER(70510),
    VIN_LADDER(70508);

    private int gameObjectId;

    private Ladder(int gameObjectId) {
        this.gameObjectId = gameObjectId;
    }

    public static boolean hasLadder(int gameObjectId) {
        for(Ladder ladder : values()) {
            if(ladder.gameObjectId == gameObjectId) return true;
        }
        return false;
    }
}
