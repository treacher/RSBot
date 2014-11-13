package com.treacher.runespan.tasks;

import com.treacher.runespan.Runespan;
import com.treacher.runespan.enums.Rune;
import com.treacher.util.Task;
import org.powerbot.script.rt6.ClientContext;

/**
 * Created by treach3r on 14/11/14.
 */
public class ExcludeAndIncludeRunes extends Task<ClientContext> {

    public ExcludeAndIncludeRunes(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return !ctx.players.local().idle();
    }

    @Override
    public void execute() {
        System.out.println("RUNE TRIMMING");
        for(Rune rune : Rune.values()) {
            if(!rune.removable()) continue;;
            final int stackSize = ctx.backpack.select().id(rune.getGameObjectId()).poll().stackSize();

            if(stackSize > 20) {
                System.out.println("adding rune: " + rune.name());
                Runespan.addRuneToExclusionList(rune);
            } else {
                System.out.println("Removing rune: " + rune.name());
                Runespan.removeRuneFromExclusionList(rune);
            }
        }
    }
}
