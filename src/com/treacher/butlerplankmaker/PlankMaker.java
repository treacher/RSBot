package com.treacher.butlerplankmaker;

import org.powerbot.script.Condition;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;

import com.treacher.butlerplankmaker.tasks.*;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

@Script.Manifest(name = "Butler Plank Maker", description = "Gets planks made using butler.")
public class PlankMaker extends PollingScript<ClientContext>{
    private List<Task<ClientContext>> taskList = new ArrayList<Task<ClientContext>>();

    private List<String> validFirstOptions = Arrays.asList("Un-cert", "Take them back to the bank");
    private List<String> validSecondOptions = Arrays.asList("Send logs to sawmill", "Pay servant 7500 coins and don't ask again.");

    private final Component firstOption = ctx.widgets.component(1188, 12);
    private final Component secondOption = ctx.widgets.component(1188, 18);

    public static String STATE = "Starting bot";

    @Override
    public void start() {
        taskList.addAll(Arrays.asList(
                new AntiBan(ctx),
                new HandleResponse(ctx),
                new EnterTextOption(ctx),
                new SelectOption(ctx,firstOption,validFirstOptions,"1"),
                new SelectOption(ctx,secondOption,validSecondOptions,"2"),
                new UseLogsOnButler(ctx)
        ));
    }

    @Override
    public void poll() {
        for(Task<ClientContext> task : taskList){
            if(task.activate()){
                task.execute();
                Condition.sleep();
            }
        }

    }

}
