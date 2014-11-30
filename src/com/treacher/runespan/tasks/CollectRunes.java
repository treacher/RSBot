package com.treacher.runespan.tasks;

import com.treacher.runespan.RuneSpan;
import com.treacher.runespan.enums.ElementalNode;
import com.treacher.runespan.util.RuneSpanQuery;
import com.treacher.util.Task;
import org.powerbot.script.rt6.ClientContext;

/**
 * Created by Michael Treacher
 */
public class CollectRunes extends Task<ClientContext> {

    private RuneSpan runeSpan;
    private RuneSpanQuery runeSpanQuery;

    public CollectRunes(ClientContext ctx, RuneSpan runeSpan) {
        super(ctx);
        this.runeSpan = runeSpan;
    }

    @Override
    public boolean activate() {
        runeSpanQuery = new RuneSpanQuery(ctx, runeSpan);
        return ctx.players.local().idle()
                && runeSpanQuery.essenceStackSize() >= 50
                && runeSpanQuery.hasNodes()
                && !ctx.chat.chatting()
                && !runeSpan.hasTarget();
    }

    @Override
    public void execute() {
        runeSpan.log.info("Collecting runes");
        ElementalNode.siphonNode(runeSpanQuery.highestPriorityNodeOnIsland(), ctx, runeSpan);
    }
}
