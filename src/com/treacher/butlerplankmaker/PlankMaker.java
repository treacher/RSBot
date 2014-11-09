package com.treacher.butlerplankmaker;

import com.treacher.butlerplankmaker.enums.LogType;
import com.treacher.butlerplankmaker.tasks.*;
import com.treacher.butlerplankmaker.ui.GUI;
import com.treacher.butlerplankmaker.ui.Painter;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GeItem;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Michael Treacher
 */

@Script.Manifest(name = "Treach3rs Butler Plank Maker", description = "Makes any plank using the demon butler.", properties = "topic=1227653")
public class PlankMaker extends PollingScript<ClientContext> implements PaintListener {
    private List<Task<ClientContext>> taskList = new ArrayList<Task<ClientContext>>();

    public static String STATE = "Starting bot";

    public static int PLANKS_PER_TRIP = 26;
    public static int BUTLER_ID = 4243;

    private LogType logType;
    private int planksMade;

    final Painter painter = new Painter(this);

    @Override
    public void start() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GUI(PlankMaker.this);
            }
        });
    }

    @Override
    public void poll() {
        for(Task<ClientContext> task : taskList){
            if(task.activate()) task.execute();
        }
    }

    @Override
    public void repaint(Graphics g) {
        if (painter != null) painter.repaint(g);
    }

    public void setPrices(){
        final int logPrice = GeItem.price(this.logType.getLogId());
        final int plankPrice =  GeItem.price(this.logType.getPlankId());

        painter.setPrices(logPrice, plankPrice, this.logType.getSawmillCost());
    }

    public void setLogType(LogType logType) {
        this.logType = logType;
    }

    public LogType getLogType() {
        return this.logType;
    }

    public void incrementPlankCount() {
        planksMade += PLANKS_PER_TRIP;
    }

    public int getPlankCount() {
        return planksMade;
    }

    public void addTasks() {
        taskList.addAll(Arrays.asList(
                new AntiBan(ctx),
                new HandleResponse(ctx),
                new EnterTextOption(ctx),
                new SelectOption(ctx, "Un-cert"),
                new SelectOption(ctx, "Take them back to the bank"),
                new SelectOption(ctx, "Send logs to sawmill"),
                new SelectOption(ctx, "Pay servant 7500 coins and don't ask again."),
                new UseLogsOnButler(ctx, this)
        ));
    }
}