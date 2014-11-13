package com.treacher.runespan.tasks;

import com.treacher.runespan.Runespan;
import com.treacher.runespan.enums.Rune;
import com.treacher.util.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Npc;

import java.util.concurrent.Callable;

/**
 * Created by Michael Treacher
 */
public class BuildUpEssence extends Task<ClientContext> {

    private Runespan runespan;

    public BuildUpEssence(ClientContext ctx, Runespan runespan) {
        super(ctx);
        this.runespan = runespan;
    }

    @Override
    public boolean activate() {
        int essenceStackSize = ctx.backpack.select().id(Rune.ESSENCE.getGameObjectId()).poll().stackSize();
        return  essenceStackSize >= 25
                && (essenceStackSize < 100 || !runespan.hasNodes())
                && runespan.hasEssenceMonsters() && ctx.players.local().idle();
    }

    @Override
    public void execute() {
        Npc essenceMonster = runespan.highestPriorityEssenceMonster();
        if(essenceMonster.valid()) {
            ctx.camera.turnTo(essenceMonster);
            ctx.camera.pitch(60);
            ctx.movement.findPath(essenceMonster).traverse();
            essenceMonster.interact("Siphon");
            waitTillSiphoning();
        }
    }
    private void waitTillSiphoning() {
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                System.out.println(ctx.players.local().idle());
                return !ctx.players.local().idle();
            }
        }, 1000, 2);
    }
}
