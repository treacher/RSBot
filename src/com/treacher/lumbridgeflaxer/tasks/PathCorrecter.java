package com.treacher.lumbridgeflaxer.tasks;

import com.treacher.lumbridgeflaxer.LumbridgeFlaxer;

import org.powerbot.script.Random;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

/**
 * Created by treach3r on 9/11/14.
 */
public class PathCorrecter extends Task<ClientContext> {

    private final LumbridgeFlaxer lumbridgeFlaxer;
    private int retryCount = 0;

    public PathCorrecter(ClientContext ctx, LumbridgeFlaxer lumbridgeFlaxer) {
        super(ctx);
        this.lumbridgeFlaxer = lumbridgeFlaxer;
    }

    @Override
    public boolean activate() {
        retryCount = 0;
        return LumbridgeFlaxer.timeSinceLastMovement != -1
                && (System.currentTimeMillis() - LumbridgeFlaxer.timeSinceLastMovement) > 15000;
    }

    @Override
    public void execute() {
        LumbridgeFlaxer.STATE = "Trying to resolve issue finding way to Spinning wheel";

        GameObject gameObject = ctx.objects.select().id(lumbridgeFlaxer.getCurrentGameObjectId()).poll();

        ctx.camera.turnTo(gameObject);
        ctx.camera.pitch(Random.nextInt(40, 55));

        /* BUGFIX: For some reason when opening the bank it gets caught in this task. We want to break if we get into that
                   Situation
        */
        if(ctx.bank.opened())
            return;

        if(!gameObject.interact(lumbridgeFlaxer.getCurrentGameObjectInteraction())) {
            // If we can't correct the path. The script should be stopped.
            if(retryCount > 5) {
                this.execute();
                retryCount++;
            } else {
                ctx.controller.stop();
                retryCount = 0;
            }
        }
    }
}
