package com.treacher.butlerplankmaker.tasks;

import com.treacher.butlerplankmaker.PlankMaker;
import com.treacher.util.Task;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;

/**
 * Created by Michael Treacher
 */

public class EnterTextOption extends Task<ClientContext> {
    
    public EnterTextOption(ClientContext ctx) {
        super(ctx);
    }

    private final Component widget = ctx.widgets.component(1469, 2);
    
    public boolean widgetIsAvailable() {
        return widget.valid() && widget.visible();
    }
    
    public boolean isInputWidget() {
        final String widgetText = widget.text();
        return widgetText != null && widgetText.equalsIgnoreCase("Enter amount:");
    }
    
    @Override
    public boolean activate() {
        return widgetIsAvailable() && isInputWidget();
    }

    @Override
    public void execute() {
        PlankMaker.STATE = "Entering text: " + PlankMaker.PLANKS_PER_TRIP;
        ctx.input.sendln(String.valueOf(PlankMaker.PLANKS_PER_TRIP));
    }

}