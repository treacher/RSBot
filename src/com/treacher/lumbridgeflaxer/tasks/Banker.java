package com.treacher.lumbridgeflaxer.tasks;

import com.treacher.lumbridgeflaxer.LumbridgeFlaxer;
import com.treacher.lumbridgeflaxer.enums.FlaxerState;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Hud;

/**
 * Created by Michael Treacher
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
        LumbridgeFlaxer.STATE = FlaxerState.BANKING;

        if(!ctx.hud.opened(Hud.Window.BACKPACK))
            ctx.hud.open(Hud.Window.BACKPACK);

        if(ctx.bank.open())
            ctx.bank.presetGear1();

        incrementBowStringsCount();
        lumbridgeFlaxer.triggerAntiBanCheck();
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
