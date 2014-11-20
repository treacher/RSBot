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
public class SearchForBetterNodes extends Task<ClientContext> {

    private final Runespan runespan;
    private GameObject betterNode;
    private long lastChecked = System.currentTimeMillis();
    private RunespanQuery runespanQuery;

    public SearchForBetterNodes(ClientContext ctx, Runespan runespan) {
        super(ctx);
        this.runespan = runespan;
    }

    @Override
    public boolean activate() {
        runespanQuery = new RunespanQuery(ctx, runespan.currentIsland());

        if((System.currentTimeMillis() - lastChecked) > 3000) {
            lastChecked = System.currentTimeMillis();

            return !ctx.players.local().idle()
                    && betterNodeAvailable()
                    && runespanQuery.essenceStackSize() > 100;
        }
        return false;
    }

    @Override
    public void execute() {
        Runespan.STATE = "Searching for better nodes";
        ElementalNode.siphonNode(betterNode, ctx, runespan);
    }

    private boolean betterNodeAvailable() {
        final GameObject nodeObject = runespanQuery.highestPriorityNode();
        final ElementalNode currentNode = ElementalNode.findNodeByGameObjectId(runespan.getCurrentNodeId(), ctx);
        final ElementalNode newNode = ElementalNode.findNodeByGameObjectId(nodeObject.id(), ctx);
        betterNode = nodeObject;
        return currentNode != null && nodeObject.valid() && currentNode.getXp() < newNode.getXp();
    }
}
