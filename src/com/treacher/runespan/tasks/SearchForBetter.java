package com.treacher.runespan.tasks;

import com.treacher.runespan.Runespan;
import com.treacher.runespan.enums.ElementalNode;
import com.treacher.runespan.util.RunespanQuery;
import com.treacher.util.Task;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

/**
 * Created by Michael Treacher
 */
public class SearchForBetter extends Task<ClientContext> {

    private final Runespan runespan;
    private GameObject betterNode;
    private long lastChecked = System.currentTimeMillis();
    private RunespanQuery runespanQuery;

    public SearchForBetter(ClientContext ctx, Runespan runespan) {
        super(ctx);
        this.runespan = runespan;
    }

    @Override
    public boolean activate() {
        runespanQuery = new RunespanQuery(ctx, runespan);

        if((System.currentTimeMillis() - lastChecked) > 6000) {
            lastChecked = System.currentTimeMillis();

            return !ctx.players.local().idle()
                    && betterNodeAvailable()
                    && runespanQuery.essenceStackSize() >= 50
                    && !ctx.chat.chatting()
                    && !runespan.hasTarget();
        }
        return false;
    }

    @Override
    public void execute() {
        Runespan.STATE = "Searching for better nodes";
        runespan.log.info("Searching for better nodes");
        if(betterNode != null && betterNode.valid())
            runespan.log.info("Switching to: " + betterNode.name());
            ElementalNode.siphonNode(betterNode, ctx, runespan);
    }

    private boolean betterNodeAvailable() {
        // Reset values;
        betterNode = null;

        final GameObject potentialNode = runespanQuery.highestPriorityNodeOnIsland();
        final ElementalNode potentialElementalNode = ElementalNode.findNodeByGameObjectId(potentialNode.id(), ctx);

        if(potentialNode.valid() && potentialElementalNode != null &&
                potentialElementalNode.getXp() > runespan.getCurrentXpRate()) {
            betterNode = potentialNode;
        }

        return betterNode != null;
    }
}
