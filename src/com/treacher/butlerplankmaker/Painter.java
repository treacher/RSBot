package com.treacher.butlerplankmaker;

import org.powerbot.script.PaintListener;
import org.powerbot.script.rt6.GeItem;

import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by treach3r on 7/11/14.
 */
public class Painter implements PaintListener{

    private final int plankPrice = GeItem.price(PlankMaker.PLANK_ID);
    private final int logPrice = GeItem.price(PlankMaker.LOG_ID);

    private final long startTime = System.currentTimeMillis();

    @Override
    public void repaint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 450, 200, 300);
        g.setColor(Color.white);
        g.drawString("treach3rs Butler Plank Maker", 20, 470);
        g.drawString("State: \t" + PlankMaker.STATE, 20, 490);
        g.drawString("Runtime: \t" + formatTime(millisElapsed()), 20, 510);
        g.drawString("Planks made: \t" + PlankMaker.PLANKS_MADE, 20, 530);
        g.drawString("Planks per hour: \t" + planksPerHour(), 20, 550);
        g.drawString("Profit per hour: \t" + profitPerHour(), 20, 570);
    }

    private int profitPerHour() {
        final double eightTripWage = 7500.0;
        final double sawMillCost = 6500.0;
        final double planksPerTrip = (double) PlankMaker.PLANKS_PER_TRIP;

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
        return (int) ((3600000.0 / millisElapsed()) * (double)PlankMaker.PLANKS_MADE);
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
}
