package com.treacher.butlerplankmaker.tasks;

import com.treacher.butlerplankmaker.PlankMaker;
import com.treacher.butlerplankmaker.enums.LogType;
import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.Hud.Window;
import org.powerbot.script.rt6.Item;
import org.powerbot.script.rt6.Npc;

import java.util.concurrent.Callable;

/**
 * Created by Michael Treacher
 */

public class UseLogsOnButler extends Task<ClientContext> {
    
    private Npc butler;

    private boolean startTheCount = false;
    private final PlankMaker plankMaker;
    private final LogType logType;

    /* entering text does not seem to be considered chatting with the butler so need this for now.*/
    private final Component butlerEnterTextWidget = ctx.widgets.component(1469, 2);
    
    public UseLogsOnButler(ClientContext ctx, PlankMaker plankMaker) {
        super(ctx);
        this.plankMaker = plankMaker;
        this.logType = plankMaker.getLogType();
    }
    
    @Override
    public boolean activate() {
        waitTillTalkingToButler();
        butler = ctx.npcs.select().id(PlankMaker.BUTLER_ID).poll();
        return butler.inViewport() && ctx.backpack.id(PlankMaker.BUTLER_ID).isEmpty() && !isTalkingToButler();
    }
    
    @Override
    public void execute() {
        if(startTheCount) {
            plankMaker.incrementPlankCount();
        } else {
            // Start the count after the first cycle
            startTheCount = true;
        }

        PlankMaker.STATE = "Using logs on butler";

        openBackBack();

        Item notedPlanks = ctx.backpack.select().id(logType.getNotedLogId()).poll();
        
        if(notedPlanks.valid()) {
            notedPlanks.interact("Use");
            butler.hover();
            if(logType.name().equals("Normal")){
                butler.interact("Use", "Logs -> Demon butler");
            } else {
                butler.interact("Use", logType.name() + " logs -> Demon butler");
            }
        } else {
            ctx.controller.stop();
        }
    }
    
    private void openBackBack() {
        if(!ctx.hud.opened(Window.BACKPACK)) ctx.hud.open(Window.BACKPACK);
    }

    private boolean isTalkingToButler() {
        return butlerEnterTextWidget.visible() || ctx.chat.chatting();
    }

    private void waitTillTalkingToButler() {
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return isTalkingToButler();

            }
        }, 150, 20);
    }
}
