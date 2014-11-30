package com.treacher.runespan.util;

import com.treacher.runespan.RuneSpan;
import com.treacher.runespan.enums.Ladder;
import com.treacher.runespan.enums.Platform;
import org.powerbot.script.Locatable;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.CollisionFlag;
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
    private final RuneSpan runeSpan;
    private GameObject ladder;

    public FloatingIsland(ClientContext ctx, RuneSpan runeSpan) {
        this.connections = new ArrayList<PlatformConnection>();
        this.ctx = ctx;
        this.runeSpan = runeSpan;
        currentFloor = setCurrentFloor();
        floodFillTilesFromTile(ctx.players.local().tile());
        runeSpan.log.info(tiles.size() + " <- tiles");
        runeSpan.log.info(connections.size() + "<- connections");
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
        runeSpan.log.info("We have a target!");

        final Locatable target = runeSpan.getLocatableTarget();
        final PlatformConnection platform = closestPlatformToTarget(target);

        if(target != null && platform != null && target.tile() != Tile.NIL) {
            final FloatingIsland nextIsland = platform.getConnection();
            if(runeSpan.getPreviousIsland() == null || !runeSpan.getPreviousIsland().equals(platform.getConnection())) {
                if (nextIsland != null) {
                    runeSpan.log.info("We have been to the island we are heading to before. We can make a smarter decision about heading to it.");
                    if (nextIsland.connections.size() > 1 || nextIsland.onIsland(target.tile())) {
                        runeSpan.log.info("Found a connection that will get us closer to the target.");
                        return platform;
                    } else {
                        runeSpan.log.info("Heading to a size 1 island that does not have the target. This causes a deadlock. Let's not go there.");
                    }
                } else {
                    runeSpan.log.info("Never been to the island we are heading too.");
                    if (hasRequiredRunes(platform)) {
                        runeSpan.log.info("Found a connection that will get us closer to the target.");
                        return platform;
                    } else {
                        runeSpan.log.info("We don't have what we need to find target. Removing target");
                    }
                }
            }
        }
        runeSpan.log.info("Issue finding platform. Removing target.");
        runeSpan.setLocatableTarget(null);
        return null;
    }

    private PlatformConnection closestPlatformToTarget(Locatable target) {
        if(target != null){
            runeSpan.log.info("Finding closest platform to target");
            runeSpan.log.info("Connections: " + connections);

            final Stack<PlatformConnection> platforms = new Stack<PlatformConnection>();
            platforms.addAll(connections);

            PlatformConnection closest = platforms.pop();

            while(!platforms.empty()) {
                final PlatformConnection nextPlatform = platforms.pop();
                final int closestPlatformCost = distanceToDestination(closest.getPlatformTile(), target.tile());
                final int nextPlatformCost = distanceToDestination(nextPlatform.getPlatformTile(), target.tile());

                if(closestPlatformCost > nextPlatformCost) closest = nextPlatform;
            }

            runeSpan.log.info("Closest platform is: " + closest);
            return closest;
        } else {
            runeSpan.log.info("Invalid target. Setting target to null.");
            runeSpan.setLocatableTarget(null);
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
            connections.add(new PlatformConnection(Platform.getPlatform(gameObjectId, ctx), tile, ctx, runeSpan));
            tiles.add(tile);
        } else if(Ladder.hasLadder(gameObjectId)) {
            ladder = ctx.objects.nearest().peek();
        }

        final Tile base = ctx.game.mapOffset();

        final CollisionFlag flag = ctx.movement.collisionMap().flagAt(tile.x() - base.x(), tile.y() - base.y());

        if(flag.getType() == 0 || ctx.players.local().tile().equals(tile)) {
            tiles.add(tile);
            floodFillTilesFromTile(new Tile(tile.x() + 1, tile.y(), tile.floor()));
            floodFillTilesFromTile(new Tile(tile.x() - 1, tile.y(), tile.floor()));
            floodFillTilesFromTile(new Tile(tile.x(), tile.y() + 1, tile.floor()));
            floodFillTilesFromTile(new Tile(tile.x(), tile.y() - 1, tile.floor()));
        }
    }
}
