package com.treacher.butlerplankmaker.tasks;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;

import com.treacher.butlerplankmaker.PlankMaker;


public class EnterTextOption extends Task<ClientContext>  {
    
    public EnterTextOption(ClientContext ctx) {
        super(ctx);
    }

    private final Component widget = ctx.widgets.component(1469, 2);
    
    public boolean widgetIsAvailable() {
        return widget != null && widget.visible();
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
        PlankMaker.STATE = "Talking with butler";
        ctx.input.sendln("26");
    }

}