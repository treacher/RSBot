package com.treacher.runespan.tasks;

import com.treacher.runespan.Runespan;
import com.treacher.runespan.enums.Rune;
import com.treacher.util.Task;
import org.powerbot.script.rt6.ClientContext;

/**
 * Created by Michael treacher
 */
public class ExcludeAndIncludeRunes extends Task<ClientContext> {

    private long lastChecked = System.currentTimeMillis();
    private boolean firstCheck = true;
    private Runespan runespan;

    public ExcludeAndIncludeRunes(ClientContext ctx, Runespan runespan)
    {
        super(ctx);
        this.runespan = runespan;
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
            if(!rune.removable(runespan)) continue;
            final int stackSize = ctx.backpack.select().id(rune.getGameObjectId()).poll().stackSize();

            if(stackSize > 20 || !runespan.members()) {
                runespan.addRuneToExclusionList(rune);
            } else {
                runespan.removeRuneFromExclusionList(rune);
            }
        }
    }
}
