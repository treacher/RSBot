package com.treacher.runespan.tasks;

import com.treacher.runespan.RuneSpan;
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
    private RuneSpan runeSpan;

    public SearchForLadder(ClientContext ctx, RuneSpan runeSpan) {
        super(ctx);
        this.runeSpan = runeSpan;
    }
    @Override
    public boolean activate() {
        //Reset ladder
        ladder = null;

        if(BuyRunes.boughtRunes && runeSpan.currentIsland() != null && FloatingIsland.floor() != 2) {
            if (ctx.skills.level(Constants.SKILLS_RUNECRAFTING) >= 33 && FloatingIsland.floor() == 0) {
                ladder = Ladder.VINE_LADDER;
            } else if (ctx.skills.level(Constants.SKILLS_RUNECRAFTING) >= 66 && FloatingIsland.floor() == 1 && RuneSpan.members()) {
                ladder = Ladder.BONE_LADDER;
            }
        }
        return ladder != null;
    }

    @Override
    public void execute() {
        runeSpan.setLocatableTarget(ladder);
    }
}
