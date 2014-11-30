package com.treacher.runespan.tasks;

import com.treacher.runespan.RuneSpan;
import com.treacher.runespan.util.FloatingIsland;
import com.treacher.util.Task;
import org.powerbot.script.rt6.ClientContext;

/**
 * Created by Michael Treacher
 */
public class FindTarget extends Task<ClientContext> {

    private RuneSpan runeSpan;

    public FindTarget(ClientContext ctx, RuneSpan runeSpan) {
        super(ctx);
        this.runeSpan = runeSpan;
    }
    @Override
    public boolean activate() {
        final FloatingIsland currentIsland = runeSpan.currentIsland();
        return runeSpan.hasTarget() && currentIsland != null && currentIsland.onIsland(runeSpan.getLocatableTarget().tile());
    }

    @Override
    public void execute() {
        runeSpan.log.info("Finding target");
        runeSpan.log.info("Found the target: " + runeSpan.getLocatableTarget());
        runeSpan.setLocatableTarget(null);
    }
}
