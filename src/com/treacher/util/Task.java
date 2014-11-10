package com.treacher.util;

import org.powerbot.script.ClientAccessor;
import org.powerbot.script.rt6.ClientContext;

/**
 * Created by Michael Treacher
 */

public abstract class Task<C extends ClientContext> extends ClientAccessor<C> {

    public Task(C ctx) {
        super(ctx);
    }

    public abstract boolean activate();
    public abstract void execute();

}
