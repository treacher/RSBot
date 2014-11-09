package com.treacher.butlerplankmaker.tasks;
import org.powerbot.script.rt6.ClientContext;

import com.treacher.butlerplankmaker.PlankMaker;

/**
 * Created by Michael Treacher
 */

public class HandleResponse extends Task<ClientContext> {

    public HandleResponse(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.chat.queryContinue();
    }

    @Override
    public void execute() {
        PlankMaker.STATE = "Handling Responses";
        ctx.chat.clickContinue(true);
    }

}