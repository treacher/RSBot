package com.treacher.runespan.tasks;

import com.treacher.runespan.Runespan;
import com.treacher.runespan.util.RunespanQuery;
import com.treacher.util.Task;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Npc;

import javax.swing.plaf.nimbus.State;

/**
 * Created by Michael Treacher
 */
public class GenerateFloatingIsland extends Task<ClientContext> {

    private final Runespan runespan;
    private final RunespanQuery runespanQuery;

    public GenerateFloatingIsland(ClientContext ctx, Runespan runespan) {
        super(ctx);
        this.runespan = runespan;
        this.runespanQuery = new RunespanQuery(ctx,runespan.currentIsland());
    }

    @Override
    public boolean activate() {
        return ctx.players.local().idle() && runespan.currentIsland() == null;
    }

    @Override
    public void execute() {
        siphonNearestObject();
        runespan.triggerAntiBan();
        runespan.buildIsland();
    }

    private void siphonNearestObject() {
        Runespan.STATE = "Collecting Runes";
        // Fake it till you make it. Choose the nearest thing to extract runes from while we build the island
        final GameObject nearestNode = runespanQuery.nearestHighestPriorityNode();

        if(nearestNode.valid() && ctx.movement.reachable(ctx.players.local().tile(), nearestNode)) {
            nearestNode.interact("Siphon");
        } else {
            final Npc nearestMonster = runespanQuery.nearestHighestPriorityMonster();
            if(nearestMonster.valid() && ctx.movement.reachable(ctx.players.local().tile(), nearestMonster)) {
                nearestMonster.interact("Siphon");
            }
        }
    }


}