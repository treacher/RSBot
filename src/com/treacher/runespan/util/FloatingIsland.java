package com.treacher.runespan.util;

import com.treacher.runespan.Runespan;
import com.treacher.runespan.enums.Platform;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

import java.util.*;

/**
 * Created by Michael Treacher
 */
public class FloatingIsland {

    private final List<PlatformConnection> connections;
    private Set<Tile> tiles = new HashSet<Tile>();
    private ClientContext ctx;
    private final int currentFloor;
    private final Runespan runespan;

    public FloatingIsland(ClientContext ctx, Runespan runespan) {
        this.connections = new ArrayList<PlatformConnection>();
        this.ctx = ctx;
        this.currentFloor = ctx.players.local().tile().floor();
        this.runespan = runespan;
        floodFillTilesFromTile(ctx.players.local().tile());
    }

    public boolean onIsland(Tile tile) {
        return tiles.contains(tile);
    }

    public boolean containsPlatformTile(Tile tile) {
        for(PlatformConnection con : connections)
            if(con.getPlatformTile().equals(tile)) return true;
        return false;
    }

    public PlatformConnection nextPlatform() {
        final List<PlatformConnection> connectionsClone = new ArrayList<PlatformConnection>(connections);

        Collections.sort(connectionsClone, new Comparator<PlatformConnection>() {
            @Override
            public int compare(PlatformConnection o1, PlatformConnection o2) {
                return o1.compareTo(o2);
            }
        });

        // If there is more than one option for platforms choose one other than the one we came from.
        for(PlatformConnection c : connectionsClone)
            if(runespan.getPreviousPlatform().equals(c) && connections.size() > 1)
                connectionsClone.remove(c);

        if(!connectionsClone.isEmpty())
            return connectionsClone.get(0);
        return null;
    }

    private void floodFillTilesFromTile(Tile tile) {
        if(tiles.contains(tile) || containsPlatformTile(tile)) return;
        final int gameObjectId = ctx.objects.select().at(tile).peek().id();
        if(Platform.hasPlatform(gameObjectId)) {
            connections.add(new PlatformConnection(Platform.getPlatform(gameObjectId), tile, ctx, runespan));
            tiles.add(tile);
        }
        if(ctx.movement.reachable(ctx.players.local().tile(), tile)) {
            tiles.add(tile);
            floodFillTilesFromTile(new Tile(tile.x() + 1, tile.y(), currentFloor));
            floodFillTilesFromTile(new Tile(tile.x() - 1, tile.y(), currentFloor));
            floodFillTilesFromTile(new Tile(tile.x(), tile.y() + 1, currentFloor));
            floodFillTilesFromTile(new Tile(tile.x(), tile.y() - 1, currentFloor));
        }
    }
}
