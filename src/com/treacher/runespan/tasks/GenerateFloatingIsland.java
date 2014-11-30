package com.treacher.runespan.tasks;

import com.treacher.runespan.RuneSpan;
import com.treacher.runespan.util.RuneSpanQuery;
import com.treacher.util.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Npc;

import java.util.concurrent.Callable;

/**
 * Created by Michael Treacher
 */
public class GenerateFloatingIsland extends Task<ClientContext> {

    private final RuneSpan runeSpan;
    private final RuneSpanQuery runeSpanQuery;

    public GenerateFloatingIsland(ClientContext ctx, RuneSpan runeSpan) {
        super(ctx);
        this.runeSpan = runeSpan;
        this.runeSpanQuery = new RuneSpanQuery(ctx, runeSpan);
    }

    @Override
    public boolean activate() {
        return ctx.players.local().idle() && runeSpan.currentIsland() == null && !ctx.chat.chatting();
    }

    @Override
    public void execute() {
//        siphonNearestObject();
        runeSpan.triggerAntiBan();
        long startTime = System.currentTimeMillis();
        runeSpan.buildIsland();
        long endTime = System.currentTimeMillis();
        runeSpan.log.info("Time taken: " + ((endTime - startTime) / 1000.0));
    }

    private void siphonNearestObject() {
        if(runeSpanQuery.essenceStackSize() <= 0) return;

        RuneSpan.STATE = "Collecting Runes";

        runeSpan.log.info("Generating island");

        runeSpan.log.info("Choosing the nearest thing to siphon till we generate island");
        // Fake it till you make it. Choose the nearest thing to extract runes from while we build the island
        final GameObject nearestNode = runeSpanQuery.nearestHighestPriorityNode();

        if(nearestNode.valid() && ctx.movement.reachable(ctx.players.local().tile(), nearestNode)) {
            runeSpan.log.info("Found a node to siphon: " + nearestNode.name());
            ctx.camera.turnTo(nearestNode);
            ctx.camera.pitch(60);
            nearestNode.interact("Siphon");
        } else {
            final Npc nearestMonster = runeSpanQuery.nearestHighestPriorityMonster();
            if(nearestMonster.valid() && ctx.movement.reachable(ctx.players.local().tile(), nearestMonster)) {
                runeSpan.log.info("Found a monster to siphon: " + nearestMonster.name());
                ctx.camera.turnTo(nearestMonster);
                ctx.camera.pitch(60);
                nearestMonster.interact("Siphon");
            }
        }
    }


}