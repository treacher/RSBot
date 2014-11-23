package com.treacher.tasks;

import com.treacher.util.Task;
import org.powerbot.script.rt6.ChatOption;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;

/**
 * Created by Michael Treacher
 */

public class SelectOption extends Task<ClientContext> {

    private String chatText;
    private ChatOption chatOption;
    private Component customComponent;
    private String customComponentText;
    
    public SelectOption(ClientContext ctx, String chatText, Component customComponent, String customComponentText) {
        super(ctx);
        this.chatText = chatText;
        this.customComponent = customComponent;
        this.customComponentText = customComponentText;
    }
    
    @Override
    public boolean activate() {
        if(customComponent != null) {
            if(!(customComponent.text().equals(customComponentText))) return false;
        }
        for(ChatOption option : ctx.chat.get())
            if(option.text().contains(chatText)){
                chatOption = option;
                return true;
            }
        return false;
    }

    @Override
    public void execute() {
        chatOption.select();
    }
}