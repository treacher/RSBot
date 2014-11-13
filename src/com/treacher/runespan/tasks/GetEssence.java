package com.treacher.runespan.tasks;

import com.treacher.runespan.enums.EssenceMonsters;
import com.treacher.runespan.enums.Rune;
import com.treacher.util.Task;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Npc;

/**
 * Created by Michael treacher
 */
public class GetEssence extends Task<ClientContext> {

    private int floatingEssenceId = 15402;

    public GetEssence(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.backpack.select().id(Rune.ESSENCE.getGameObjectId()).poll().stackSize() < 10 && ctx.players.local().idle();
    }

    @Override
    public void execute() {
        Npc runeEssenceNpc = ctx.npcs.select().id(floatingEssenceId).nearest().peek();
        if(runeEssenceNpc.valid()) {
            ctx.movement.findPath(runeEssenceNpc).traverse();
            runeEssenceNpc.interact("Collect");
        }
    }
}
