package com.treacher.runespan.tasks;

import com.treacher.runespan.Runespan;
import com.treacher.util.Task;
import org.powerbot.script.rt6.ClientContext;

/**
 * Created by treach3r on 14/11/14.
 */
public class MoveIslands extends Task<ClientContext> {

    private Runespan runespan;

    public MoveIslands(ClientContext ctx, Runespan runespan) {
        super(ctx);
        this.runespan = runespan;
    }

    @Override
    public boolean activate() {
        return !runespan.hasNodes() && !runespan.hasEssenceMonsters();
    }

    @Override
    public void execute() {
        System.out.println("FIND A NEW ISLAND!!");
    }
}
