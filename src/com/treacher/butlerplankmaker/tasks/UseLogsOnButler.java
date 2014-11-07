package com.treacher.butlerplankmaker.tasks;

import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.Item;
import org.powerbot.script.rt6.Npc;
import org.powerbot.script.rt6.Hud.Window;

import com.treacher.butlerplankmaker.GameObjectIds;
import com.treacher.butlerplankmaker.PlankMaker;

public class UseLogsOnButler extends Task<ClientContext> {
    
    private Npc butler;
    
    private final Component butlerInteractionWidget = ctx.widgets.component(1188, 2);
    private final Component butlerEnterTextWidget = ctx.widgets.component(1469, 2);
    private final Component npcResponseWidget = ctx.widgets.component(1184, 13);
    private final Component charResponseWidget = ctx.widgets.component(1191, 6);

    private boolean startTheCount = false;
    
    public UseLogsOnButler(ClientContext ctx) {
        super(ctx);
    }
    
    @Override
    public boolean activate() {
        waitTillTalkingToButler();
        butler = ctx.npcs.select().id(GameObjectIds.BUTLER_ID).poll();
        return butler.inViewport() && ctx.backpack.id(GameObjectIds.BUTLER_ID).isEmpty() && !isTalkingToButler();
    }
    
    @Override
    public void execute() {
        // Going to make the assumption that people are going to be making the most planks they can.
        if(startTheCount) PlankMaker.PLANKS_MADE += PlankMaker.PLANKS_PER_TRIP;

        // Start the count after the first cycle
        startTheCount = true;

        PlankMaker.STATE = "Using logs on butler";

        openBackBack();

        Item notedPlanks = ctx.backpack.select().id(GameObjectIds.NOTED_OAK_PLANK_ID).poll();
        
        if(notedPlanks.valid()) {
            notedPlanks.interact("Use");
            butler.hover();
            butler.interact("Use", "Oak logs -> Demon butler");
        } else {
            ctx.controller.stop();
        }
    }
    
    private void openBackBack() {
        if(!ctx.hud.opened(Window.BACKPACK))
            ctx.hud.open(Window.BACKPACK);
    }
    
    private boolean isTalkingToButler() {
        return butlerInteractionWidget.visible() || butlerEnterTextWidget.visible() || npcResponseWidget.visible() || charResponseWidget.visible();
    }
    
    private void waitTillTalkingToButler() {
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return isTalkingToButler();
            }
        }, 150, 12);
    }
}
