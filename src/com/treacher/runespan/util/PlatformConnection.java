package com.treacher.runespan.util;

import com.treacher.runespan.Runespan;
import com.treacher.runespan.enums.Platform;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Michael Treacher
 */
public class PlatformConnection {

    private FloatingIsland connection;
    private final Platform platform;
    private final Tile platformTile;
    private ClientContext ctx;
    private int travelledCount;
    private Runespan runespan;

    public PlatformConnection(Platform platform, Tile platformTile, ClientContext ctx, Runespan runespan){
        this.platform = platform;
        this.platformTile = platformTile;
        this.ctx = ctx;
        this.runespan = runespan;
        if(ctx.players.local().tile().equals(platformTile)) travelledCount++;
    }

    public void setConnection(FloatingIsland connection){
        this.connection = connection;
    }

    public FloatingIsland getConnection(){
        return this.connection;
    }

    public Tile getPlatformTile() {
        return platformTile;
    }

    public Tile getReachableTile(GameObject gameObject) {
        final Tile gameObjTile = gameObject.tile();
        Tile[] tiles = new Tile[] {
                new Tile(gameObjTile.x() + 1, gameObjTile.y() + 1, gameObjTile.floor()),
                new Tile(gameObjTile.x() + 1, gameObjTile.y(), gameObjTile.floor()),
                new Tile(gameObjTile.x(), gameObjTile.y() + 1, gameObjTile.floor()),
                new Tile(gameObjTile.x() - 1, gameObjTile.y() - 1, gameObjTile.floor()),
                new Tile(gameObjTile.x() - 1, gameObjTile.y(), gameObjTile.floor()),
                new Tile(gameObjTile.x(), gameObjTile.y() - 1, gameObjTile.floor()),
        };

        for(Tile tile : tiles)
            if(ctx.movement.reachable(ctx.players.local().tile(), tile)) return tile;

        return Tile.NIL;
    }

    public void travelToIsland() {
        final GameObject nextPlatform = ctx.objects.select().id(platform.getPlatformId()).at(this.getPlatformTile()).peek();
        final Tile tile = getReachableTile(nextPlatform);

        if(tile != null) {
            ctx.camera.turnTo(nextPlatform);

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    boolean interactHappened = nextPlatform.interact(false, "Use");
                    if(interactHappened) {
                        waitTillOnNewIsland();
                        travelledCount++;
                        return true;
                    } else {
                        ctx.movement.findPath(tile).traverse();
                        return false;
                    }
                }
            }, 1000, 10);
        }
    }

    public int compareTo(PlatformConnection pc) {
        return Integer.valueOf(this.travelledCount).compareTo(pc.travelledCount);
    }

    public String toString() {
        return "Platform: " + platform.name() + " TravelledCount: " + travelledCount;
    }

    private void waitTillOnNewIsland() {
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().idle() && !runespan.getPreviousIsland().onIsland(ctx.players.local().tile());
            }
        }, 1000,10);
    }
}
