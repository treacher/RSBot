package com.treacher.butlerplankmaker;

import com.treacher.butlerplankmaker.tasks.*;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Script.Manifest(name = "Butler Plank Maker", description = "Gets planks made using butler.")
public class PlankMaker extends PollingScript<ClientContext> implements PaintListener {
    private List<Task<ClientContext>> taskList = new ArrayList<Task<ClientContext>>();

    private List<String> validFirstOptions = Arrays.asList("Un-cert", "Take them back to the bank");
    private List<String> validSecondOptions = Arrays.asList("Send logs to sawmill", "Pay servant 7500 coins and don't ask again.");

    private final Component firstOption = ctx.widgets.component(1188, 12);
    private final Component secondOption = ctx.widgets.component(1188, 18);

    public static String STATE = "Starting bot";

    public static int PLANKS_PER_TRIP = 27;
    public static int PLANKS_MADE = 0;
    public static int PLANK_ID = GameObjectIds.OAK_PLANK_ID;
    public static int LOG_ID = GameObjectIds.OAK_LOG_ID;
    public static int NOTED_LOG_ID = GameObjectIds.NOTED_OAK_PLANK_ID;

    final Painter painter = new Painter();

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
            }
        }
    }

    @Override
    public void repaint(Graphics g) {
        if (painter != null) painter.repaint(g);
    }


}