package com.treacher.runespan.tasks;

import com.treacher.runespan.Runespan;
import com.treacher.runespan.util.FloatingIsland;
import com.treacher.runespan.util.PlatformConnection;
import com.treacher.runespan.util.RunespanQuery;
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
        final RunespanQuery runespanQuery = new RunespanQuery(ctx, runespan.currentIsland());
        return !runespanQuery.hasNodes() && !runespanQuery.hasEssenceMonsters() && runespan.currentIsland() != null;
    }

    @Override
    public void execute() {
        System.out.println("Move islands");

        final FloatingIsland currentIsland = runespan.currentIsland();
        final PlatformConnection nextPlatform = currentIsland.nextPlatform();

        if(nextPlatform != null) {
            runespan.setPreviousIsland(currentIsland);
            runespan.setPreviousPlatform(nextPlatform);
            nextPlatform.travelToIsland();
        }
    }
}
