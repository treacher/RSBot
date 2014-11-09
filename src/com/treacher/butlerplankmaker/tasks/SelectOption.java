package com.treacher.butlerplankmaker.tasks;
import java.util.List;

import org.powerbot.script.rt6.ChatOption;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;

import com.treacher.butlerplankmaker.PlankMaker;

/**
 * Created by Michael Treacher
 */

public class SelectOption extends Task <ClientContext>{

    private List<String> validOptions;
    
    public SelectOption(ClientContext ctx, List<String> validOptions) {
        super(ctx);
        this.validOptions = validOptions;
    }
    
    @Override
    public boolean activate() {
        return ctx.chat.chatting() && ctx.chat.get().size() > 0;
    }

    @Override
    public void execute() {
        PlankMaker.STATE = "Talking with butler";

        List<ChatOption> options = ctx.chat.get();

        for(ChatOption option : options) {
            for(String validOptionText : validOptions) {
                if(option.text().contains(validOptionText)) {
                    option.select();
                    return;
                }
            }

        }
    }
}