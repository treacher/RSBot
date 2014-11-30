package com.treacher.runespan.tasks;

import com.treacher.runespan.RuneSpan;
import com.treacher.runespan.enums.EssenceMonster;
import com.treacher.runespan.util.RuneSpanQuery;
import com.treacher.util.Task;
import org.powerbot.script.rt6.ClientContext;

/**
 * Created by Michael Treacher
 */
public class BuildUpEssence extends Task<ClientContext> {

    private RuneSpan runeSpan;
    private RuneSpanQuery runeSpanQuery;

    public BuildUpEssence(ClientContext ctx, RuneSpan runeSpan) {
        super(ctx);
        this.runeSpan = runeSpan;
    }

    @Override
    public boolean activate() {
        runeSpanQuery = new RuneSpanQuery(ctx, runeSpan);
        final boolean hasNoNodes = !runeSpanQuery.hasNodes();
        return  ctx.players.local().idle()
                && runeSpanQuery.essenceStackSize() > -1
                && (runeSpanQuery.essenceStackSize() < 50 || hasNoNodes)
                && (runeSpanQuery.hasEssenceMonsters())
                && !ctx.chat.chatting()
                && !runeSpan.hasTarget();
    }

    @Override
    public void execute() {
        RuneSpan.STATE = "Building up essence";
        runeSpan.log.info("Building up essence");
        EssenceMonster.siphonMonster(runeSpanQuery.highestPriorityEssenceMonster(), ctx, runeSpan);
    }
}
