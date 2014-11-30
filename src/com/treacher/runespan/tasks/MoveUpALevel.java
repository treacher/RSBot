package com.treacher.runespan.tasks;

import com.treacher.runespan.RuneSpan;
import com.treacher.runespan.enums.Ladder;
import com.treacher.runespan.util.FloatingIsland;
import com.treacher.util.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import java.util.concurrent.Callable;

/**
 * Created by Michael Treacher
 */
public class MoveUpALevel extends Task<ClientContext> {

    private RuneSpan runeSpan;
    private FloatingIsland currentIsland;

    public MoveUpALevel(ClientContext ctx, RuneSpan runeSpan){
        super(ctx);
        this.runeSpan = runeSpan;
    }
    @Override
    public boolean activate() {
        currentIsland = runeSpan.currentIsland();
        if(currentIsland != null) {
            final GameObject gameObj = currentIsland.getLadder();
            if (gameObj.valid()) {
                final Ladder ladder = Ladder.findLadder(gameObj.id());
                return ladder.playerHasReqLevelToUse(ctx.skills.level(Constants.SKILLS_RUNECRAFTING));
            }
        }
        return false;
    }

    @Override
    public void execute() {
        final int originalFloor = FloatingIsland.floor();

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                climbLadder();
                return FloatingIsland.floor() != originalFloor;
            }
        }, 1000, 3);

        // Remove all targets
        runeSpan.removeAllIslands();
        runeSpan.setLocatableTarget(null);
    }

    private void climbLadder() {
        final GameObject ladder =  currentIsland.getLadder();

        ctx.camera.turnTo(ladder);
        ctx.camera.pitch(60);

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                runeSpan.log.info("attempting to Climb ladder");
                boolean interacting = ladder.interact("Climb Up");
                if (!interacting)
                    ctx.movement.findPath(RuneSpan.getReachableTile(ladder, ctx)).traverse();
                return interacting;
            }
        }, 1000, 5);
        runeSpan.log.info("Finished with ladder");
    }
}
