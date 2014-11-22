package com.treacher.runespan;

import com.treacher.runespan.enums.Rune;
import com.treacher.runespan.tasks.*;
import com.treacher.runespan.ui.Painter;
import com.treacher.runespan.util.AntiBan;
import com.treacher.runespan.util.FloatingIsland;
import com.treacher.runespan.util.PlatformConnection;
import com.treacher.util.Task;
import org.powerbot.script.BotMenuListener;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * Created by Michael Treacher
 */
@Script.Manifest(name = "Runespan", description = "Trains rune crafting in the Runepan. P2P only.", properties = "topic=1229948")
public class Runespan extends PollingScript<ClientContext> implements PaintListener, BotMenuListener{

    private List<FloatingIsland> floatingIslands = new ArrayList<FloatingIsland>();
    private List<Task<ClientContext>> taskList = new ArrayList<Task<ClientContext>>();
    private static Set<Rune> runesToExclude = new HashSet<Rune>();
    private double currentXpRate;
    private Painter painter = new Painter(ctx, this);
    private FloatingIsland previousIsland;
    private PlatformConnection previousPlatform;
    private AntiBan antiBan = new AntiBan(ctx);
    private boolean antiBanSwitch = true;

    public static String STATE = "Collecting Runes";

    @Override
    public void start() {
        taskList.addAll(
            Arrays.asList(
                    new GenerateFloatingIsland(ctx,this),
                    new BuildUpEssence(ctx, this),
                    new CollectRunes(ctx, this),
                    new SearchForBetter(ctx, this),
                    new GetEssence(ctx),
                    new ExcludeAndIncludeRunes(ctx, this),
                    new MoveIslands(ctx, this)
            )
        );
    }

    @Override
    public void poll() {
        for (Task<ClientContext> task : taskList) {
            if (task.activate()) {
                task.execute();
            }
        }
    }

    @Override
    public void repaint(Graphics g) {
        painter.repaint(g);
    }

    public FloatingIsland currentIsland() {
        for (FloatingIsland island : floatingIslands) {
            if (island.onIsland(ctx.players.local().tile())) {
                return island;
            }
        }
        return null;
    }

    @Override
    public void menuSelected(MenuEvent e) {
        final JMenu menu = (JMenu) e.getSource();
        final String switchState = antiBanSwitch ? "Off" : "On";
        final JCheckBoxMenuItem antiBanMenuItem = new JCheckBoxMenuItem("Turn off anti-ban");

        antiBanMenuItem.setText("Turn anti-ban " + switchState);

        antiBanMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                antiBanSwitch = !antiBanSwitch;
            }
        });
        menu.add(antiBanMenuItem);
    }

    @Override
    public void menuDeselected(MenuEvent e) {}

    @Override
    public void menuCanceled(MenuEvent e) {}

    public void triggerAntiBan() {
        if(antiBanSwitch) antiBan.execute();
    }

    public boolean getAntiBanSwitch() {
        return antiBanSwitch;
    }

    public void addRuneToExclusionList(Rune rune) {
        runesToExclude.add(rune);
    }

    public void removeRuneFromExclusionList(Rune rune) {
        runesToExclude.remove(rune);
    }

    public static Set<Rune> getExclusionList() {
        return runesToExclude;
    }

    public void setPreviousIsland(FloatingIsland floatingIsland) {
        this.previousIsland = floatingIsland;
    }

    public void setPreviousPlatform(PlatformConnection platformConnection) {
        this.previousPlatform = platformConnection;
    }

    public FloatingIsland getPreviousIsland() {
        return this.previousIsland;
    }

    public PlatformConnection getPreviousPlatform() {
        return this.previousPlatform;
    }

    public void buildIsland() {
        final PlatformConnection prevPlatform = getPreviousPlatform();
        final FloatingIsland newIsland = new FloatingIsland(ctx, this);

        if(prevPlatform != null)
            prevPlatform.setConnection(newIsland);

        floatingIslands.add(newIsland);
    }

    public void setCurrentXpRate(double xpRate) {
        this.currentXpRate = xpRate;
    }

    public double getCurrentXpRate() {
        return currentXpRate;
    }
}
