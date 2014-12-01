package com.treacher.runespan.tasks;

import com.treacher.runespan.Runespan;
import com.treacher.runespan.enums.Ladder;
import com.treacher.runespan.util.FloatingIsland;
import com.treacher.util.Task;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Constants;

/**
 * Created by Michael Treacher
 */
public class SearchForLadder extends Task<ClientContext> {

    private Ladder ladder;
    private Runespan runespan;

    public SearchForLadder(ClientContext ctx, Runespan runespan) {
        super(ctx);
        this.runespan = runespan;
    }
    @Override
    public boolean activate() {
        //Reset ladder
        ladder = null;

        if(BuyRunes.boughtRunes && runespan.currentIsland() != null && FloatingIsland.floor() != 2) {
            if (ctx.skills.level(Constants.SKILLS_RUNECRAFTING) >= 33 && FloatingIsland.floor() == 0) {
                ladder = Ladder.VINE_LADDER;
            } else if (ctx.skills.level(Constants.SKILLS_RUNECRAFTING) >= 66 && FloatingIsland.floor() == 1 && Runespan.members()) {
                ladder = Ladder.BONE_LADDER;
            }
        }
        return ladder != null;
    }

    @Override
    public void execute() {
        runespan.setLocatableTarget(ladder);
    }
}
