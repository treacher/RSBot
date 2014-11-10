package com.treacher.lumbridgeflaxer.ui;


import com.treacher.lumbridgeflaxer.LumbridgeFlaxer;
import org.powerbot.script.PaintListener;

import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Michael Treacher
 */

public class Painter implements PaintListener {

    public static long startTime = System.currentTimeMillis();

    private LumbridgeFlaxer lumbridgeFlaxer;

    private double flaxPrice, bowStringPrice;

    public Painter(LumbridgeFlaxer lumbridgeFlaxer) {
        this.lumbridgeFlaxer = lumbridgeFlaxer;
    }

    @Override
    public void repaint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 450, 200, 300);
        g.setColor(Color.white);
        g.drawString("treach3rs Lumbridge flaxer", 20, 470);
        g.drawString("Runtime: \t" + formatTime(millisElapsed()), 20, 490);
        g.drawString("State: \t" + LumbridgeFlaxer.STATE, 20, 510);
        g.drawString("Bow strings made: \t" + lumbridgeFlaxer.getBowStringsCount(), 20, 530);
        g.drawString("Bow strings per hour: \t" + bowStringsPerHour(), 20, 550);
        g.drawString("Profit per hour: \t" + profitPerHour(), 20, 570);
    }

    public void setPrices(double flaxPrice, double bowStringPrice) {
        this.flaxPrice = flaxPrice;
        this.bowStringPrice = bowStringPrice;
    }

    private int profitPerHour() {
        double costPerHour = (double)bowStringsPerHour() * flaxPrice;
        double revenuePerHour = (double)bowStringsPerHour() * bowStringPrice;
        return ((int) (revenuePerHour - costPerHour));
    }

    private int bowStringsPerHour() {
        return (int) ((3600000.0 / millisElapsed()) * (double)lumbridgeFlaxer.getBowStringsCount());
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
