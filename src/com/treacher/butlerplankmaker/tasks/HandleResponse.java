package com.treacher.butlerplankmaker.tasks;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;

import com.treacher.butlerplankmaker.PlankMaker;

public class HandleResponse extends Task<ClientContext> {
	
	public HandleResponse(ClientContext ctx) {
		super(ctx);
	}

	private final Component npcResponseWidget = ctx.widgets.component(1184, 13);
	private final Component charResponseWidget = ctx.widgets.component(1191, 6);
	
	public boolean butlerResponse() {
		return npcResponseWidget != null && npcResponseWidget.visible();
	}
	
	public boolean charResponse() {
		return charResponseWidget != null && charResponseWidget.visible();
	}

	@Override
	public boolean activate() {
		return butlerResponse() || charResponse();
	}

	@Override
	public void execute() {
		PlankMaker.STATE = "Talking with butler";
		ctx.input.send(" ");
	}

}