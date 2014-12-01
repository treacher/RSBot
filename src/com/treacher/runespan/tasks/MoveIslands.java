package com.treacher.runespan.tasks;

import com.treacher.runespan.Runespan;
import com.treacher.runespan.util.FloatingIsland;
import com.treacher.runespan.util.PlatformConnection;
import com.treacher.util.Task;
import org.powerbot.script.rt6.ClientContext;

/**
 * Created by Michael Treacher
 */
public class MoveIslands extends Task<ClientContext> {

    private final Runespan runespan;

    public MoveIslands(ClientContext ctx, Runespan runespan) {
        super(ctx);
        this.runespan = runespan;
    }

    @Override
    public boolean activate() {
        return runespan.currentIsland() != null && runespan.hasTarget();
    }

    @Override
    public void execute() {
        Runespan.STATE = "Moving islands";
        runespan.log.info("Moving islands");
        final FloatingIsland currentIsland = runespan.currentIsland();
        final PlatformConnection nextPlatform = currentIsland.nextPlatform();

        if(nextPlatform != null) {
            runespan.setPreviousIsland(currentIsland);
            runespan.setPreviousPlatform(nextPlatform);
            nextPlatform.travelToIsland();
        }
    }
}
