package com.treacher.runespan.tasks;

import com.treacher.runespan.RuneSpan;
import com.treacher.util.Task;
import org.powerbot.script.rt6.ClientContext;

/**
 * Created by Michael Treacher
 */
public class GenerateFloatingIsland extends Task<ClientContext> {

    private final RuneSpan runeSpan;

    public GenerateFloatingIsland(ClientContext ctx, RuneSpan runeSpan) {
        super(ctx);
        this.runeSpan = runeSpan;
    }

    @Override
    public boolean activate() {
        return ctx.players.local().idle() && runeSpan.currentIsland() == null && !ctx.chat.chatting();
    }

    @Override
    public void execute() {
        runeSpan.triggerAntiBan();
        long startTime = System.currentTimeMillis();
        runeSpan.buildIsland();
        long endTime = System.currentTimeMillis();
        runeSpan.log.info("Time taken: " + ((endTime - startTime) / 1000.0));
    }
}