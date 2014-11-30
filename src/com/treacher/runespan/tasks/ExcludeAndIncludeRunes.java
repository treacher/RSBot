package com.treacher.runespan.tasks;

import com.treacher.runespan.RuneSpan;
import com.treacher.runespan.enums.Rune;
import com.treacher.util.Task;
import org.powerbot.script.rt6.ClientContext;

/**
 * Created by Michael treacher
 */
public class ExcludeAndIncludeRunes extends Task<ClientContext> {

    private long lastChecked = System.currentTimeMillis();
    private boolean firstCheck = true;
    private RuneSpan runeSpan;

    public ExcludeAndIncludeRunes(ClientContext ctx, RuneSpan runeSpan)
    {
        super(ctx);
        this.runeSpan = runeSpan;
    }

    @Override
    public boolean activate() {
        return (((System.currentTimeMillis() - lastChecked) > 5000) && !ctx.players.local().idle()) || firstCheck;
    }

    @Override
    public void execute() {
        firstCheck = false;
        lastChecked = System.currentTimeMillis();

        for(Rune rune : Rune.values()) {
            if(!rune.removable(runeSpan)) continue;
            final int stackSize = ctx.backpack.select().id(rune.getGameObjectId()).poll().stackSize();

            if(stackSize >= 10 || !runeSpan.members()) {
                runeSpan.addRuneToExclusionList(rune);
            } else {
                runeSpan.removeRuneFromExclusionList(rune);
            }
        }
    }
}
