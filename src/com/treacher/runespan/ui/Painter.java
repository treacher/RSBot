package com.treacher.runespan.ui;


import com.treacher.runespan.Runespan;
import org.powerbot.script.PaintListener;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Constants;

import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Michael Treacher
 */

public class Painter implements PaintListener {

    public static long startTime = System.currentTimeMillis();

    private ClientContext ctx;

    private final int startXp;

    private final Runespan runespan;

    public Painter(ClientContext ctx, Runespan runespan)
    {
        this.ctx = ctx;
        this.startXp = ctx.skills.experience(Constants.SKILLS_RUNECRAFTING);
        this.runespan = runespan;
    }

    @Override
    public void repaint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 450, 350, 300);
        g.setColor(Color.white);
        g.drawString("treach3rs Runespan", 20, 470);
        g.drawString("Runtime: \t" + formatTime(millisElapsed()), 20, 490);
        g.drawString("XP gained: \t " + xpGained(),20, 510);
        g.drawString("XP per hour: \t " + xpPerHour(), 20, 530);
        g.drawString("Last State: \t " + Runespan.STATE, 20, 550);
        g.drawString("AntiBan: \t " + (runespan.getAntiBanSwitch() ? "enabled" : "disabled"), 20, 570);
    }

    private int xpGained() {
        return ctx.skills.experience(Constants.SKILLS_RUNECRAFTING) - startXp;
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
