package com.treacher.lumbridgeflaxer.tasks;

import com.treacher.lumbridgeflaxer.LumbridgeFlaxer;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

/**
 * Created by Michael Treacher on 8/11/14.
 */
public class Banker extends Task<ClientContext> {

    private final Tile bankTile;
    private boolean startTheCount;
    private LumbridgeFlaxer lumbridgeFlaxer;

    public Banker(ClientContext ctx, Tile bankTile, LumbridgeFlaxer lumbridgeFlaxer) {
        super(ctx);
        this.bankTile = bankTile;
        this.lumbridgeFlaxer = lumbridgeFlaxer;
    }

    @Override
    public boolean activate() {
        return ctx.players.local().tile().equals(bankTile) && ctx.backpack.select().id(lumbridgeFlaxer.getFlaxId()).isEmpty();
    }

    @Override
    public void execute() {
        LumbridgeFlaxer.STATE = "Banking";
        LumbridgeFlaxer.timeSinceLastMovement = -1;
        if(ctx.bank.open()) ctx.bank.presetGear1();
        incrementBowStringsCount();
    }

    private void incrementBowStringsCount() {
        if(startTheCount) {
            lumbridgeFlaxer.incrementBowStringsCount();
        } else {
            // Start the count after the first cycle
            startTheCount = true;
        }
    }
}
