package com.treacher.runespan.tasks;

import com.treacher.util.Task;
import com.treacher.runespan.RuneSpan;
import com.treacher.runespan.enums.ElementalNode;
import com.treacher.runespan.util.FloatingIsland;
import com.treacher.runespan.util.RuneSpanQuery;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

/**
 * Created by Michael Treacher
 */
public class SearchForBetterAbroad extends Task<ClientContext> {

    private RuneSpan runeSpan;
    private GameObject betterNode;
    private long lastRan = System.currentTimeMillis();
    private long idleTime = 0;

    public SearchForBetterAbroad(ClientContext ctx, RuneSpan runeSpan) {
        super(ctx);
        this.runeSpan = runeSpan;
    }

    @Override
    public boolean activate() {
        if(ctx.players.local().idle() && idleTime == 0) idleTime = System.currentTimeMillis();

        if(ctx.players.local().idle() && (System.currentTimeMillis() - idleTime) > 5000
                || (System.currentTimeMillis() - lastRan) >= 30000) {

            idleTime = 0;
            lastRan = System.currentTimeMillis();

            final FloatingIsland currentIsland = runeSpan.currentIsland();

            if (currentIsland != null && !ctx.players.local().idle() && !runeSpan.hasTarget()) {

                final RuneSpanQuery runeSpanQuery = new RuneSpanQuery(ctx, runeSpan);

                betterNode = runeSpanQuery.highestPriorityNode();

                if (!currentIsland.onIsland(betterNode.tile()) && runeSpanQuery.essenceStackSize() >= 50) {
                    final ElementalNode elementalNode = ElementalNode.findNodeByGameObjectId(betterNode.id(), ctx);
                    return elementalNode != null && elementalNode.getXp() > runeSpan.getCurrentXpRate();
                }
            }
        }

        return false;
    }

    @Override
    public void execute() {
        runeSpan.log.info("Looking for better node on another island.");
        runeSpan.log.info("Found: " + betterNode.name() +  " on another island. Setting it as target");
        runeSpan.setLocatableTarget(betterNode);
    }
}
