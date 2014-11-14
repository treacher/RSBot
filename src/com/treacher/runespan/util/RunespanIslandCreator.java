package com.treacher.runespan.util;

import com.treacher.runespan.enums.Rune;
import org.powerbot.script.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by treach3r on 14/11/14.
 */
public class RunespanIslandCreator {

    public static List<FloatingIsland> createIslands() {
        List<FloatingIsland> islands = new ArrayList<FloatingIsland>();

        FloatingIsland island1 = new FloatingIsland("Island 1",
                new Tile(4136, 6068, 1),
                new Tile(4137, 6091, 1),
                new Tile(4143, 6080, 1),
                new Tile(4126, 6083, 1)
        );

        islands.add(island1);

        return islands;
    }
}
