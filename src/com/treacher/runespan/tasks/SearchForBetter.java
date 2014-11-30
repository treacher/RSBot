package com.treacher.runespan.tasks;

import com.treacher.runespan.RuneSpan;
import com.treacher.runespan.enums.ElementalNode;
import com.treacher.runespan.util.RuneSpanQuery;
import com.treacher.util.Task;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

/**
 * Created by Michael Treacher
 */
public class SearchForBetter extends Task<ClientContext> {

    private final RuneSpan runeSpan;
    private GameObject betterNode;
    private long lastChecked = System.currentTimeMillis();
    private RuneSpanQuery runeSpanQuery;

    public SearchForBetter(ClientContext ctx, RuneSpan runeSpan) {
        super(ctx);
        this.runeSpan = runeSpan;
    }

    @Override
    public boolean activate() {
        runeSpanQuery = new RuneSpanQuery(ctx, runeSpan);

        if((System.currentTimeMillis() - lastChecked) > 6000) {
            lastChecked = System.currentTimeMillis();

            return !ctx.players.local().idle()
                    && betterNodeAvailable()
                    && runeSpanQuery.essenceStackSize() >= 50
                    && !ctx.chat.chatting()
                    && !runeSpan.hasTarget();
        }
        return false;
    }

    @Override
    public void execute() {
        RuneSpan.STATE = "Searching for better nodes";
        runeSpan.log.info("Searching for better nodes");
        if(betterNode != null && betterNode.valid())
            runeSpan.log.info("Switching to: " + betterNode.name());
            ElementalNode.siphonNode(betterNode, ctx, runeSpan);
    }

    private boolean betterNodeAvailable() {
        // Reset values;
        betterNode = null;

        final GameObject potentialNode = runeSpanQuery.highestPriorityNodeOnIsland();
        final ElementalNode potentialElementalNode = ElementalNode.findNodeByGameObjectId(potentialNode.id(), ctx);

        if(potentialNode.valid() && potentialElementalNode != null &&
                potentialElementalNode.getXp() > runeSpan.getCurrentXpRate()) {
            betterNode = potentialNode;
        }

        return betterNode != null;
    }
}
