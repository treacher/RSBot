package com.treacher.runespan.ui;


import org.powerbot.script.PaintListener;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Skills;

import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Michael Treacher
 */

public class Painter implements PaintListener {

    public static long startTime = System.currentTimeMillis();

    private ClientContext ctx;

    private final int startXp;

    public Painter(ClientContext ctx)
    {
        this.ctx = ctx;
        this.startXp = ctx.skills.experience(Skills.RUNECRAFTING);
    }

    @Override
    public void repaint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 450, 200, 300);
        g.setColor(Color.white);
        g.drawString("treach3rs Runespan", 20, 470);
        g.drawString("Runtime: \t" + formatTime(millisElapsed()), 20, 490);
        g.drawString("XP gained: \t " + xpGained(),20, 510);
        g.drawString("XP per hour: \t " + xpPerHour(), 20, 530);
    }

    private int xpGained() {
        return ctx.skills.experience(Skills.RUNECRAFTING) - startXp;
    }

    private int xpPerHour() {
        return (int) ((3600000.0 / millisElapsed()) * (double)xpGained());
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
