package com.treacher.runespan.tasks;

import com.treacher.runespan.Runespan;
import com.treacher.util.Task;
import org.powerbot.script.rt6.ClientContext;

/**
 * Created by Michael Treacher
 */
public class GenerateFloatingIsland extends Task<ClientContext> {

    private final Runespan runespan;

    public GenerateFloatingIsland(ClientContext ctx, Runespan runespan) {
        super(ctx);
        this.runespan = runespan;
    }

    @Override
    public boolean activate() {
        return ctx.players.local().idle() && runespan.currentIsland() == null && !ctx.chat.chatting();
    }

    @Override
    public void execute() {
        runespan.triggerAntiBan();
        long startTime = System.currentTimeMillis();
        runespan.buildIsland();
        long endTime = System.currentTimeMillis();
        runespan.log.info("Time taken: " + ((endTime - startTime) / 1000.0));
    }
}