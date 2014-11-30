package com.treacher.runespan.tasks;

import com.treacher.runespan.RuneSpan;
import com.treacher.runespan.util.FloatingIsland;
import com.treacher.runespan.util.PlatformConnection;
import com.treacher.util.Task;
import org.powerbot.script.rt6.ClientContext;

/**
 * Created by Michael Treacher
 */
public class MoveIslands extends Task<ClientContext> {

    private final RuneSpan runeSpan;

    public MoveIslands(ClientContext ctx, RuneSpan runeSpan) {
        super(ctx);
        this.runeSpan = runeSpan;
    }

    @Override
    public boolean activate() {
        return runeSpan.currentIsland() != null && runeSpan.hasTarget();
    }

    @Override
    public void execute() {
        RuneSpan.STATE = "Moving islands";
        runeSpan.log.info("Moving islands");
        final FloatingIsland currentIsland = runeSpan.currentIsland();
        final PlatformConnection nextPlatform = currentIsland.nextPlatform();

        if(nextPlatform != null) {
            runeSpan.setPreviousIsland(currentIsland);
            runeSpan.setPreviousPlatform(nextPlatform);
            nextPlatform.travelToIsland();
        }
    }
}
