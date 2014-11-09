package com.treacher.butlerplankmaker.tasks;

import org.powerbot.script.rt6.ChatOption;
import org.powerbot.script.rt6.ClientContext;

import com.treacher.butlerplankmaker.PlankMaker;

/**
 * Created by Michael Treacher
 */

public class SelectOption extends Task <ClientContext>{

    private String chatText;
    private ChatOption chatOption;
    
    public SelectOption(ClientContext ctx, String chatText) {
        super(ctx);
        this.chatText = chatText;
    }
    
    @Override
    public boolean activate() {
        for(ChatOption option : ctx.chat.get())
            if(option.text().contains(chatText)){
                chatOption = option;
                return true;
            }
        return false;
    }

    @Override
    public void execute() {
        PlankMaker.STATE = "Selecting:" + chatText;
        chatOption.select();
    }
}