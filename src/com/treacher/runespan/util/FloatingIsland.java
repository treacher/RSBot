package com.treacher.runespan.util;

import com.treacher.runespan.Runespan;
import com.treacher.runespan.enums.Ladder;
import com.treacher.runespan.enums.Platform;
import org.powerbot.script.Locatable;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.CollisionFlag;
import org.powerbot.script.rt6.CollisionMap;
import org.powerbot.script.rt6.GameObject;

import java.util.*;
import java.util.List;

/**
 * Created by Michael Treacher
 */
public class FloatingIsland {

    private final List<PlatformConnection> connections;
    private Set<Tile> tiles = new HashSet<Tile>();
    private ClientContext ctx;
    private static int currentFloor;
    private final Runespan runespan;
    private GameObject ladder;

    public FloatingIsland(ClientContext ctx, Runespan runespan) {
        this.connections = new ArrayList<PlatformConnection>();
        this.ctx = ctx;
        this.runespan = runespan;
        currentFloor = setCurrentFloor();
        floodFillTilesFromTile(ctx.players.local().tile());
        runespan.log.info(tiles.size() + " <- tiles");
        runespan.log.info(connections.size() + "<- connections");
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
        return findPlatformForTarget();
    }

    public GameObject getLadder() {
        if(ladder == null) return ctx.objects.nil();
        return ladder;
    }

    public static int floor() {
        return currentFloor;
    }

    private PlatformConnection findPlatformForTarget() {
        runespan.log.info("We have a target!");

        final Locatable target = runespan.getLocatableTarget();
        final PlatformConnection platform = closestPlatformToTarget(target);

        if(target != null && platform != null && target.tile() != Tile.NIL) {
            final FloatingIsland nextIsland = platform.getConnection();
            if(runespan.getPreviousIsland() == null || !runespan.getPreviousIsland().equals(platform.getConnection())) {
                if (nextIsland != null) {
                    runespan.log.info("We have been to the island we are heading to before. We can make a smarter decision about heading to it.");
                    if (nextIsland.connections.size() > 1 || nextIsland.onIsland(target.tile())) {
                        runespan.log.info("Found a connection that will get us closer to the target.");
                        return platform;
                    } else {
                        runespan.log.info("Heading to a size 1 island that does not have the target. This causes a deadlock. Let's not go there.");
                    }
                } else {
                    runespan.log.info("Never been to the island we are heading too.");
                    if (hasRequiredRunes(platform)) {
                        runespan.log.info("Found a connection that will get us closer to the target.");
                        return platform;
                    } else {
                        runespan.log.info("We don't have what we need to find target. Removing target");
                    }
                }
            }
        }
        runespan.log.info("Issue finding platform. Removing target.");
        runespan.setPreviousIsland(null);
        runespan.setPreviousPlatform(null);
        runespan.setLocatableTarget(null);
        return null;
    }

    private PlatformConnection closestPlatformToTarget(Locatable target) {
        if(target != null){
            runespan.log.info("Finding closest platform to target");
            runespan.log.info("Connections: " + connections);

            final Stack<PlatformConnection> platforms = new Stack<PlatformConnection>();
            platforms.addAll(connections);

            PlatformConnection closest = platforms.pop();

            while(!platforms.empty()) {
                final PlatformConnection nextPlatform = platforms.pop();
                final int closestPlatformCost = distanceToDestination(closest.getPlatformTile(), target.tile());
                final int nextPlatformCost = distanceToDestination(nextPlatform.getPlatformTile(), target.tile());

                if(closestPlatformCost > nextPlatformCost) closest = nextPlatform;
            }

            runespan.log.info("Closest platform is: " + closest);
            return closest;
        } else {
            runespan.log.info("Invalid target. Setting target to null.");
            runespan.setLocatableTarget(null);
        }

        return null;
    }

    private int distanceToDestination(Tile origin, Tile destination) {
        return (Math.abs(origin.x() - destination.x())) + Math.abs((origin.y() - destination.y()));
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
        } else if(Ladder.hasLadder(gameObjectId)) {
            ladder = ctx.objects.nearest().peek();
        }

        final Tile base = ctx.game.mapOffset();
        int x = tile.x() - base.x();
        int y = tile.y() - base.y();
        final CollisionMap map = ctx.movement.collisionMap();
        // bounds check
        if(x < 0 && y < 0 && x < map.width() && y < map.height()) return;

        final CollisionFlag flag = map.flagAt(x, y);

        if(flag.getType() == 0 || ctx.players.local().tile().equals(tile)) {
            tiles.add(tile);
            floodFillTilesFromTile(new Tile(tile.x() + 1, tile.y(), tile.floor()));
            floodFillTilesFromTile(new Tile(tile.x() - 1, tile.y(), tile.floor()));
            floodFillTilesFromTile(new Tile(tile.x(), tile.y() + 1, tile.floor()));
            floodFillTilesFromTile(new Tile(tile.x(), tile.y() - 1, tile.floor()));
        }
    }
}
