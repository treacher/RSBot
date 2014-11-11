package com.treacher.lumbridgeflaxer.tasks;

import com.treacher.lumbridgeflaxer.LumbridgeFlaxer;

import com.treacher.lumbridgeflaxer.enums.FlaxerState;
import org.powerbot.script.Random;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

/**
 * Created by Michael Treacher
 */
public class PathCorrecter extends Task<ClientContext> {

    private final LumbridgeFlaxer lumbridgeFlaxer;
    private int retryCount = 0;
    private long idleTime = -1;

    public PathCorrecter(ClientContext ctx, LumbridgeFlaxer lumbridgeFlaxer) {
        super(ctx);
        this.lumbridgeFlaxer = lumbridgeFlaxer;
    }

    @Override
    public boolean activate() {
        System.out.println(ctx.players.local().idle());
        final boolean active = ctx.players.local().idle() && LumbridgeFlaxer.STATE != FlaxerState.SPINNING;

        if(active && idleTime == -1) {
            idleTime = System.currentTimeMillis();
        } else if(!active) {
            idleTime = -1;
        }

        return active;
    }

    @Override
    public void execute() {
        System.out.println(System.currentTimeMillis() - idleTime);
        // If idle for greater than 20 seconds try correct it.
        if((System.currentTimeMillis() - idleTime) <= 45000) return;

        LumbridgeFlaxer.STATE = FlaxerState.CORRECTING;

        final GameObject gameObject = ctx.objects.select().id(lumbridgeFlaxer.getCurrentGameObjectId()).poll();

        System.out.println(gameObject);

        ctx.camera.turnTo(gameObject);
        ctx.camera.pitch(Random.nextInt(45, 50));

        if(ctx.bank.opened()) return;

        gameObject.interact(lumbridgeFlaxer.getCurrentGameObjectInteraction());
    }
}
