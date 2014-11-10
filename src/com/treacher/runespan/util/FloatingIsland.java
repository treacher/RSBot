package com.treacher.runespan.util;

import org.powerbot.script.Tile;

import java.util.*;

/**
 * Created by Michael Treacher on 10/11/14.
 */
public class FloatingIsland {

    private final List<Tile> interpolationTiles;
    private final List<Tile> interpolatedTiles;
    private final List<PlatformConnection> connections;
    private final int floor;
    private final String title;

    public FloatingIsland(String title, Tile t1, Tile t2, Tile t3, Tile t4) {
        this.interpolationTiles = Arrays.asList(t1,t2,t3,t4);
        this.interpolatedTiles = new ArrayList<Tile>();
        this.connections = new ArrayList<PlatformConnection>();
        this.floor = t1.floor();
        this.title = title;

        interpolateIslandTiles();
    }

    public boolean onIsland(Tile t) {
        return interpolatedTiles.contains(t);
    }

    public List<Tile> getTiles() {
        return interpolatedTiles;
    }

    public String getTitle() {
        return title;
    }

    public void addConnection(PlatformConnection connection) {
        connections.add(connection);
    }

    private void interpolateIslandTiles() {
        final List<Integer> xValues = new ArrayList<Integer>();
        final List<Integer> yValues = new ArrayList<Integer>();

        for(Tile tile : interpolationTiles) {
            xValues.add(tile.x());
            yValues.add(tile.y());
        }

        List<Integer> xRange = generateRange(Collections.min(xValues), Collections.max(xValues));
        List<Integer> yRange = generateRange(Collections.min(yValues), Collections.max(yValues));

        for(Integer x : xRange) {
            for(Integer y: yRange) {
                interpolatedTiles.add(new Tile(x,y,floor));
            }
        }
    }

    private List<Integer> generateRange(int min, int max) {
        List<Integer> range = new ArrayList<Integer>();
        for(int i = min; i <= max; i++)
            range.add(i);
        return range;
    }
}
