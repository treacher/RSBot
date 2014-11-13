package com.treacher.runespan.enums;

import com.treacher.runespan.Runespan;

import java.util.List;

/**
 * Created by Michael Treacher
 */
public enum ElementalNode {
    JUMPER(70466,107.8, new Rune[]{Rune.LAW}),
    SHIFTER(70465,86.8, new Rune[]{Rune.NATURE}),
    NEBULA(70464,77, new Rune[]{Rune.COSMIC, Rune.ASTRAL}),
    CHAOTIC_CLOUD(70463,61.6,new Rune[]{Rune.CHAOS}),
    FIRE_STORM(70462,35,new Rune[]{Rune.AIR, Rune.FIRE}),
    FLESHY_GROWTH(70461,46.2, new Rune[]{Rune.BODY}),
    FIREBALL(70459, 34.8,new Rune[]{Rune.FIRE}),
    VINE(70460,30.3, new Rune[]{Rune.WATER, Rune.EARTH}),
    ROCK_FRAGMENT(70458, 28.6,new Rune[]{Rune.EARTH}),
    WATER_POOL(70457,25.3, new Rune[]{Rune.WATER}),
    MIND_STORM(70456,20, new Rune[]{Rune.MIND}),
    CYCLONE(70455,19, new Rune[]{Rune.AIR});

    private final int gameObjectId;
    private final double xp;
    private final Rune[] runes;

    private ElementalNode(int gameObjectId, double xp, Rune[] runes) {
        this.gameObjectId = gameObjectId;
        this.xp = xp;
        this.runes = runes;
    }

    public int getGameObjectId() {
        return gameObjectId;
    }

    public double getXp() {
        return xp;
    }

    public static boolean hasNode(int id) {
        return (findNodeByGameObjectId(id) != null);
    }

    public static ElementalNode findNodeByGameObjectId(int id) {
        for(ElementalNode node : ElementalNode.values())
            if(node.gameObjectId == id && !node.excluded()) return node;
        return null;
    }

    private boolean excluded() {
        boolean excluded = false;

        for(Rune rune : this.runes) {
            excluded = Runespan.getExclusionList().contains(rune);
        }

        return excluded;
    }
}
