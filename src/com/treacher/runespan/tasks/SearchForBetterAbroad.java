package com.treacher.runespan.tasks;

import com.treacher.util.Task;
import com.treacher.runespan.Runespan;
import com.treacher.runespan.enums.ElementalNode;
import com.treacher.runespan.util.FloatingIsland;
import com.treacher.runespan.util.RunespanQuery;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

/**
 * Created by Michael Treacher
 */
public class SearchForBetterAbroad extends Task<ClientContext> {

    private Runespan runespan;
    private GameObject betterNode;
    private long lastRan = System.currentTimeMillis();
    private long idleTime = 0;

    public SearchForBetterAbroad(ClientContext ctx, Runespan runespan) {
        super(ctx);
        this.runespan = runespan;
    }

    @Override
    public boolean activate() {
        if(ctx.players.local().idle() && idleTime == 0) idleTime = System.currentTimeMillis();

        if((ctx.players.local().idle() && (System.currentTimeMillis() - idleTime) > 5000)
                || (System.currentTimeMillis() - lastRan) >= 30000) {

            idleTime = 0;
            lastRan = System.currentTimeMillis();

            final FloatingIsland currentIsland = runespan.currentIsland();

            if (currentIsland != null && !runespan.hasTarget()) {

                final RunespanQuery runespanQuery = new RunespanQuery(ctx, runespan);

                betterNode = runespanQuery.highestPriorityNode();

                if (runespanQuery.essenceStackSize() >= 50) {
                    final ElementalNode elementalNode = ElementalNode.findNodeByGameObjectId(betterNode.id(), ctx);
                    return elementalNode != null && elementalNode.getXp() > runespan.getCurrentXpRate();
                }
            }
        }

        return false;
    }

    @Override
    public void execute() {
        runespan.log.info("Looking for better node on another island.");
        runespan.log.info("Found: " + betterNode.name() +  " on another island. Setting it as target");
        runespan.setLocatableTarget(betterNode);
    }
}
