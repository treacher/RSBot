package com.treacher.butlerplankmaker;

import com.treacher.butlerplankmaker.tasks.*;
import org.powerbot.script.Condition;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.GeItem;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Script.Manifest(name = "Butler Plank Maker", description = "Gets planks made using butler.")
public class PlankMaker extends PollingScript<ClientContext> implements PaintListener {
    private List<Task<ClientContext>> taskList = new ArrayList<Task<ClientContext>>();

    private List<String> validFirstOptions = Arrays.asList("Un-cert", "Take them back to the bank");
    private List<String> validSecondOptions = Arrays.asList("Send logs to sawmill", "Pay servant 7500 coins and don't ask again.");

    private final Component firstOption = ctx.widgets.component(1188, 12);
    private final Component secondOption = ctx.widgets.component(1188, 18);

    private int plankPrice = 0;
    private int logPrice = 0;

    private final long startTime = System.currentTimeMillis();

    public static String STATE = "Starting bot";

    public static int PLANKS_PER_TRIP = 27;

    public static int PLANKS_MADE = 0;

    @Override
    public void start() {
        fetchGrandExchangePrices();
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
        g.setColor(Color.black);
        g.fillRect(0, 450, 200, 300);
        g.setColor(Color.white);
        g.drawString("treach3rs Butler Plank Maker", 20, 470);
        g.drawString("State: \t" + STATE, 20, 490);
        g.drawString("Runtime: \t" + formatTime(millisElapsed()), 20, 510);
        g.drawString("Planks made: \t" + PLANKS_MADE, 20, 530);
        g.drawString("Planks per hour: \t" + planksPerHour(), 20, 550);
        g.drawString("Profit per hour: \t" + profitPerHour(), 20, 570);
    }

    private int profitPerHour() {
        if(this.logPrice  == 0 || this.plankPrice == 0) return 0;

        final double eightTripWage = 7500.0;
        final double sawMillCost = 6500.0;
        final double planksPerTrip = (double) PLANKS_PER_TRIP;

        final double planksPerHour = (double) planksPerHour();
        final double tripsPerHour = planksPerHour / planksPerTrip;

        final double chargePerTripWage = eightTripWage / 8.0;

        final double hourlyWage = chargePerTripWage * tripsPerHour;
        final double hourlySawmillCost = sawMillCost * tripsPerHour;

        final double hourlyLogCost = planksPerHour * ((double)this.logPrice);
        final double totalCost = hourlyWage + hourlySawmillCost + hourlyLogCost;
        final double hourlyRevenue = planksPerHour * ((double)this.plankPrice);

        return ((int)(hourlyRevenue - totalCost));
    }

    private int planksPerHour() {
        return (int) ((3600000.0 / millisElapsed()) * (double)PLANKS_MADE);
    }

    private long millisElapsed() {
        return System.currentTimeMillis() - startTime;
    }

    /*
        Copied from: http://stackoverflow.com/questions/9027317/how-to-convert-milliseconds-to-hhmmss-format
        User: Bohemian
     */
    private String formatTime(long millis) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    private void fetchGrandExchangePrices() {
        this.plankPrice =  GeItem.price(GameObjectIds.OAK_PLANK_ID);
        this.logPrice = GeItem.price(GameObjectIds.OAK_LOG_ID);
    }

}
