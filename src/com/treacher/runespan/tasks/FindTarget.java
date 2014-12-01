package com.treacher.runespan.tasks;

import com.treacher.runespan.Runespan;
import com.treacher.runespan.util.FloatingIsland;
import com.treacher.util.Task;
import org.powerbot.script.rt6.ClientContext;

/**
 * Created by Michael Treacher
 */
public class FindTarget extends Task<ClientContext> {

    private Runespan runespan;

    public FindTarget(ClientContext ctx, Runespan runespan) {
        super(ctx);
        this.runespan = runespan;
    }
    @Override
    public boolean activate() {
        final FloatingIsland currentIsland = runespan.currentIsland();
        return runespan.hasTarget() && currentIsland != null && currentIsland.onIsland(runespan.getLocatableTarget().tile());
    }

    @Override
    public void execute() {
        runespan.log.info("Finding target");
        runespan.log.info("Found the target: " + runespan.getLocatableTarget());
        runespan.setLocatableTarget(null);
    }
}
