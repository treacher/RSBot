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
    private static int currentFloor;
    private final Runespan runespan;

    public FloatingIsland(ClientContext ctx, Runespan runespan) {
        this.connections = new ArrayList<PlatformConnection>();
        this.ctx = ctx;
        this.runespan = runespan;
        currentFloor = setCurrentFloor();
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
        // Only add platforms we can use.
        List<PlatformConnection> hasRequiredRunesPlatforms = new ArrayList<PlatformConnection>();

        for(PlatformConnection con : connections)
            if(hasRequiredRunes(con)) hasRequiredRunesPlatforms.add(con);

        Collections.sort(hasRequiredRunesPlatforms, new Comparator<PlatformConnection>() {
            @Override
            public int compare(PlatformConnection o1, PlatformConnection o2) {
                return o1.compareTo(o2);
            }
        });

        if(!hasRequiredRunesPlatforms.isEmpty()) {
            PlatformConnection candidateConnection = hasRequiredRunesPlatforms.get(0);
            if(runespan.getPreviousPlatform() != null
                    && runespan.getPreviousPlatform().equals(candidateConnection)
                    && hasRequiredRunesPlatforms.size() > 1)
                candidateConnection = hasRequiredRunesPlatforms.get(1);
            return candidateConnection;
        }
        return null;
    }

    public static int floor() {
        return currentFloor;
    }

    private int setCurrentFloor() {
        int textureId = ctx.widgets.component(1274,8).textureId();
        switch(textureId){
            case 10708:
                return 0;
            case 10709:
                return 1;
            default:
                return 2;
        }
    }

    private boolean hasRequiredRunes(PlatformConnection connection) {
        for(int runeId : connection.getPlatform().getPlatformRequirementRuneIds()){
            if(ctx.backpack.select().id(runeId).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void floodFillTilesFromTile(Tile tile) {
        if(tiles.contains(tile) || containsPlatformTile(tile)) return;
        final int gameObjectId = ctx.objects.select().at(tile).peek().id();

        if(Platform.hasPlatform(gameObjectId, ctx)) {
            connections.add(new PlatformConnection(Platform.getPlatform(gameObjectId, ctx), tile, ctx, runespan));
            tiles.add(tile);
        }
        if(ctx.movement.reachable(ctx.players.local().tile(), tile)) {
            tiles.add(tile);
            floodFillTilesFromTile(new Tile(tile.x() + 1, tile.y(), tile.floor()));
            floodFillTilesFromTile(new Tile(tile.x() - 1, tile.y(), tile.floor()));
            floodFillTilesFromTile(new Tile(tile.x(), tile.y() + 1, tile.floor()));
            floodFillTilesFromTile(new Tile(tile.x(), tile.y() - 1, tile.floor()));
        }
    }
}
