package com.treacher.lumbridgeflaxer.tasks;

import com.treacher.lumbridgeflaxer.LumbridgeFlaxer;
import com.treacher.lumbridgeflaxer.enums.FlaxerState;
import org.powerbot.script.Random;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

/**
 * Created by Michael Treacher
 */
public class PathToObjectAndInteract extends Task<ClientContext> {

    private int gameObjectId, minPitch, maxPitch;
    private String interaction;
    private final LumbridgeFlaxer lumbridgeFlaxer;

    public PathToObjectAndInteract(ClientContext ctx, int gameObjectId, String interaction, int minPitch, int maxPitch, LumbridgeFlaxer lumbridgeFlaxer) {
        super(ctx);
        this.gameObjectId = gameObjectId;
        this.interaction = interaction;
        this.minPitch = minPitch;
        this.maxPitch = maxPitch;
        this.lumbridgeFlaxer = lumbridgeFlaxer;
    }

    @Override
    public boolean activate() {
        // no-op this method needs to be overridden
        return false;
    }

    @Override
    public void execute() {
        LumbridgeFlaxer.timeSinceLastMovement = System.currentTimeMillis();

        final GameObject gameObject = ctx.objects.select().id(gameObjectId).poll();

        LumbridgeFlaxer.STATE = FlaxerState.WALKING;

        // Keep track of current path so that we can correct it if need be.
        lumbridgeFlaxer.setCurrentGameObjectId(gameObjectId);
        lumbridgeFlaxer.setCurrentGameObjectInteraction(interaction);

        ctx.camera.turnTo(gameObject);
        ctx.camera.pitch(Random.nextInt(minPitch, maxPitch));

        gameObject.interact(interaction);
    }

}
