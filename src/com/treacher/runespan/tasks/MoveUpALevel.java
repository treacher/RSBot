package com.treacher.runespan.tasks;

import com.treacher.runespan.Runespan;
import com.treacher.runespan.util.FloatingIsland;
import com.treacher.util.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import java.util.concurrent.Callable;

/**
 * Created by treach3r on 28/11/14.
 */
public class MoveUpALevel extends Task<ClientContext> {

    private Runespan runespan;
    private FloatingIsland currentIsland;

    public MoveUpALevel(ClientContext ctx, Runespan runespan){
        super(ctx);
        this.runespan = runespan;
    }
    @Override
    public boolean activate() {
        currentIsland = runespan.currentIsland();
        if(currentIsland != null && currentIsland.getLadder().valid()) {
            if (FloatingIsland.floor() == 0) {
                return ctx.skills.level(Constants.SKILLS_RUNECRAFTING) >= 33;
            } else if (FloatingIsland.floor() == 1 && runespan.members()) {
                return ctx.skills.level(Constants.SKILLS_RUNECRAFTING) >= 66;
            }
        }
        return false;
    }

    @Override
    public void execute() {
        Runespan.STATE = "Climb ladder";

        climbLadder();
        waitTillOnNextFloor();
    }

    private void climbLadder() {
        final GameObject ladder =  currentIsland.getLadder();

        ctx.camera.turnTo(ladder);

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                boolean interacting = ladder.interact(false, "Climb Up");
                if (!interacting)
                    System.out.println(ctx.movement.findPath(ladder).valid());
                    ctx.movement.findPath(Runespan.getReachableTile(ladder, ctx)).traverse();
                return interacting;
            }
        }, 1500, 6);
    }

    private void waitTillOnNextFloor() {
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return currentIsland == null;
            }
        });
    }
}
