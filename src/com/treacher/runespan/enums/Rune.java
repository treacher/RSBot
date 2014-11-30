package com.treacher.runespan.enums;

import com.treacher.runespan.RuneSpan;
import com.treacher.runespan.util.FloatingIsland;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Michael Treacher
 */
public enum Rune {
    FIRE(24213),
    AIR(24215),
    COSMIC(24223),
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
    private static final List<Rune> highPriorityRunes = new ArrayList<Rune>(
            Arrays.asList(
                    SOUL,
                    BLOOD,
                    DEATH,
                    ASTRAL,
                    LAW,
                    NATURE,
                    CHAOS
            )
    );

    private static final List<Rune> membersRunes = new ArrayList<Rune>(
            Arrays.asList(
                    SOUL,
                    BLOOD,
                    DEATH,
                    ASTRAL,
                    LAW,
                    NATURE,
                    CHAOS,
                    NATURE,
                    COSMIC,
                    ASTRAL
            )
    );

    private Rune(int gameObjectId) {
        this.gameObjectId = gameObjectId;
    }

    public int getGameObjectId() {
        return gameObjectId;
    }

    public boolean removable(RuneSpan runeSpan) {
        final FloatingIsland currentIsland = runeSpan.currentIsland();

        if(currentIsland != null) {
            if(runeSpan.members()) {
                return FloatingIsland.floor() != 0 && !highPriorityRunes.contains(this);
            } else {
                return membersRunes.contains(this);
            }
        } else {
            return false;
        }
    }
}
