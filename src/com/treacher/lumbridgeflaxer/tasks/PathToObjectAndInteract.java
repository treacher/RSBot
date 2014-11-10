package com.treacher.lumbridgeflaxer.tasks;

import com.treacher.lumbridgeflaxer.LumbridgeFlaxer;
import org.powerbot.script.Random;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

/**
 * Created by Michael Treacher on 8/11/14.
 */
public class PathToObjectAndInteract extends Task<ClientContext> {

    private int gameObjectId;
    private String interaction;
    private final LumbridgeFlaxer lumbridgeFlaxer;

    public PathToObjectAndInteract(ClientContext ctx, int gameObjectId, String interaction, LumbridgeFlaxer lumbridgeFlaxer) {
        super(ctx);
        this.gameObjectId = gameObjectId;
        this.interaction = interaction;
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

        LumbridgeFlaxer.STATE = "Heading to: " + gameObject.name();

        // Keep track of current path so that we can correct it if need be.
        lumbridgeFlaxer.setCurrentGameObjectId(gameObjectId);
        lumbridgeFlaxer.setCurrentGameObjectInteraction(interaction);

        ctx.camera.turnTo(gameObject);
        ctx.camera.pitch(Random.nextInt(40, 60));

        gameObject.interact(interaction);
    }

}
