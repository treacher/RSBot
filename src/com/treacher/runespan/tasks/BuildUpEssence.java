package com.treacher.runespan.tasks;

import com.treacher.runespan.Runespan;
import com.treacher.runespan.enums.EssenceMonster;
import com.treacher.runespan.util.RunespanQuery;
import com.treacher.util.Task;
import org.powerbot.script.rt6.ClientContext;

/**
 * Created by Michael Treacher
 */
public class BuildUpEssence extends Task<ClientContext> {

    private Runespan runespan;
    private RunespanQuery runespanQuery;

    public BuildUpEssence(ClientContext ctx, Runespan runespan) {
        super(ctx);
        this.runespan = runespan;
    }

    @Override
    public boolean activate() {
        runespanQuery = new RunespanQuery(ctx, runespan.currentIsland());
        final boolean hasNoNodes = !runespanQuery.hasNodes();
        return  ctx.players.local().idle()
                && runespanQuery.essenceStackSize() >= 25
                && (runespanQuery.essenceStackSize() <= 100 || hasNoNodes)
                && (runespanQuery.hasEssenceMonsters())
                && !ctx.chat.chatting();
    }

    @Override
    public void execute() {
        Runespan.STATE = "Building up essence";
        EssenceMonster.siphonMonster(runespanQuery.highestPriorityEssenceMonster(), ctx, runespan);
    }
}
