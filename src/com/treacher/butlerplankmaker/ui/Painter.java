package com.treacher.butlerplankmaker.ui;

import com.treacher.butlerplankmaker.PlankMaker;
import org.powerbot.script.PaintListener;
import org.powerbot.script.rt6.GeItem;

import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Michael Treacher
 */

public class Painter implements PaintListener{



    public static long startTime = System.currentTimeMillis();

    private PlankMaker plankMaker;

    private int logPrice, plankPrice;
    private double sawMillPrice;

    public Painter(PlankMaker plankMaker) {
        this.plankMaker = plankMaker;
    }

    @Override
    public void repaint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 450, 200, 300);
        g.setColor(Color.white);
        g.drawString("treach3rs Butler Plank Maker", 20, 470);
        g.drawString("State: \t" + PlankMaker.STATE, 20, 490);
        g.drawString("Runtime: \t" + formatTime(millisElapsed()), 20, 510);
        g.drawString("Planks made: \t" + plankMaker.getPlankCount(), 20, 530);
        g.drawString("Planks per hour: \t" + planksPerHour(), 20, 550);
        g.drawString("Profit per hour: \t" + profitPerHour(), 20, 570);
    }

    public void setPrices(int logPrice, int plankPrice, double sawMillPrice) {
        this.logPrice = logPrice;
        this.plankPrice = plankPrice;
        this.sawMillPrice = sawMillPrice;
    }

    private int profitPerHour() {
        final double eightTripWage = 7500.0;

        final double planksPerTrip = (double) PlankMaker.PLANKS_PER_TRIP;
        final double sawMillCost = this.sawMillPrice * planksPerTrip;

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
        return (int) ((3600000.0 / millisElapsed()) * (double)plankMaker.getPlankCount());
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
