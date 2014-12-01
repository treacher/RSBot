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
        runespanQuery = new RunespanQuery(ctx, runespan);
        return ctx.players.local().idle()
                && runespanQuery.essenceStackSize() >= 50
                && runespanQuery.hasNodes()
                && !ctx.chat.chatting()
                && !runespan.hasTarget();
    }

    @Override
    public void execute() {
        runespan.log.info("Collecting runes");
        ElementalNode.siphonNode(runespanQuery.highestPriorityNodeOnIsland(), ctx, runespan);
    }
}
