package com.treacher.runespan.tasks;

import com.treacher.runespan.RuneSpan;
import com.treacher.util.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;

import java.util.concurrent.Callable;

/**
 * Created by Michael Treacher
 */
public class BuyRunes extends Task<ClientContext> {

    private final int wizardId = 15418;
    public static boolean boughtRunes = false;
    private final Component pointsWidget = ctx.widgets.component(1274,5);

    public BuyRunes(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() {
        RuneSpan.STATE = "Buying Runes";
        ctx.npcs.select().id(wizardId).poll().interact("Buy runes");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.chat.chatting();
            }
        }, 1000, 4);
        boughtRunes = true;
    }

    @Override
    public boolean activate() {
        return !boughtRunes && !ctx.npcs.select().id(wizardId).isEmpty() && Integer.parseInt(pointsWidget.text()) > 200 && !ctx.chat.chatting();
    }
}
