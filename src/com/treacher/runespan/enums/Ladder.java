package com.treacher.runespan.enums;

import com.treacher.runespan.Runespan;
import org.powerbot.script.Locatable;
import org.powerbot.script.Tile;

/**
 * Created by Michael Treacher
 */
public enum Ladder implements Locatable {

    BONE_LADDER(70510, 66, new Tile(4106,6043,1), true),
    VINE_LADDER(70508, 33, new Tile(3957,6107,1), false);

    private final int gameObjectId, levelReq;
    private final Tile tile;
    private final boolean members;

    private Ladder(int gameObjectId, int levelReq, Tile tile, boolean members) {
        this.gameObjectId = gameObjectId;
        this.levelReq = levelReq;
        this.tile = tile;
        this.members = members;
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
            if(ladder.gameObjectId == gameObjectId && !ladder.excluded()) return ladder;
        }
        return null;
    }

    public static boolean hasLadder(int gameObjectId) {
        return findLadder(gameObjectId) != null;
    }

    private boolean excluded() {
        return !Runespan.members() && this.members;
    }
}
