package com.treacher.butlerplankmaker.tasks;
import java.util.List;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;

import com.treacher.butlerplankmaker.PlankMaker;

/**
 * Created by Michael Treacher
 */

public class SelectOption extends Task <ClientContext>{
    
    private Component component;
    private List<String> validOptions;
    private String keyPress;
    
    public SelectOption(ClientContext ctx, Component component, List<String> validOptions, String keyPress) {
        super(ctx);
        this.validOptions = validOptions;
        this.keyPress = keyPress;
        this.component = component;
    }
    
    public boolean widgetIsAvailable() {
        return component != null && component.visible();
    }
    
    public boolean isValidOption() {
        boolean containsWidgetText = false;
    
        final String widgetText = component.text();
    
        //Fuzzy widget text search
        for(String option : validOptions) {
            containsWidgetText = widgetText.contains(option);
            if(containsWidgetText) break;
        }

        return widgetText != null && containsWidgetText;
    }
    
    @Override
    public boolean activate() {
        return widgetIsAvailable() && isValidOption();
    }

    @Override
    public void execute() {
        PlankMaker.STATE = "Talking with butler";
        ctx.input.send(keyPress);
    }
}