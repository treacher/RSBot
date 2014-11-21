package com.treacher.runespan.tasks;

import com.treacher.runespan.Runespan;
import com.treacher.runespan.enums.Rune;
import com.treacher.util.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Npc;

import java.util.concurrent.Callable;

/**
 * Created by Michael treacher
 */
public class GetEssence extends Task<ClientContext> {

    public GetEssence(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.players.local().idle() && ctx.backpack.select().id(Rune.ESSENCE.getGameObjectId()).poll().stackSize() < 10;
    }

    @Override
    public void execute() {
        Runespan.STATE = "Getting Essence";

        final int floatingEssenceId = 15402;
        final Npc runeEssenceNpc = ctx.npcs.select().id(floatingEssenceId).nearest().peek();

        if(runeEssenceNpc.valid()) {
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    boolean siphonEssence = runeEssenceNpc.interact("Collect");
                    if(!siphonEssence)
                        ctx.movement.findPath(runeEssenceNpc).traverse();
                    return siphonEssence;
                }
            }, 1500, 5);
        }
    }
}
