package com.treacher.runespan.tasks;

import com.treacher.runespan.Runespan;
import com.treacher.runespan.enums.ElementalNode;
import com.treacher.runespan.util.RunespanQuery;
import com.treacher.util.Task;
import org.powerbot.script.rt6.ClientContext;

/**
 * Created by Michael Treacher
 */
public class CollectRunes extends Task<ClientContext> {

    private Runespan runespan;
    private RunespanQuery runespanQuery;

    public CollectRunes(ClientContext ctx, Runespan runespan) {
        super(ctx);
        this.runespan = runespan;
    }

    @Override
    public boolean activate() {
        runespanQuery = new RunespanQuery(ctx, runespan.currentIsland());
        return ctx.players.local().idle()
                && runespanQuery.essenceStackSize() > 100
                && runespanQuery.hasNodes()
                && !ctx.chat.chatting();
    }

    @Override
    public void execute() {
        Runespan.STATE = "Collecting Runes";
        ElementalNode.siphonNode(runespanQuery.highestPriorityNode(), ctx, runespan);
    }
}
