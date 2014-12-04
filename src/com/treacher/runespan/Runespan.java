package com.treacher.runespan;

import com.treacher.runespan.tasks.*;
import com.treacher.runespan.ui.GUI;
import com.treacher.runespan.ui.Painter;
import com.treacher.runespan.util.AntiBan;
import com.treacher.runespan.util.FloatingIsland;
import com.treacher.runespan.util.PlatformConnection;
import com.treacher.tasks.HandleResponse;
import com.treacher.tasks.SelectOption;
import com.treacher.util.Task;
import org.powerbot.script.*;
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
@Script.Manifest(name = "Runespan", description = "Trains rune crafting in the Runepan.", properties = "topic=1229948")
public class Runespan extends PollingScript<ClientContext> implements PaintListener, BotMenuListener{

    private List<FloatingIsland> floatingIslands = new ArrayList<FloatingIsland>();
    private List<Task<ClientContext>> taskList = new ArrayList<Task<ClientContext>>();
    private double currentXpRate = 0;
    private Painter painter = new Painter(ctx, this);
    private FloatingIsland previousIsland;
    private PlatformConnection previousPlatform;
    private AntiBan antiBan = new AntiBan(ctx);
    private boolean antiBanSwitch = true;
    public static String GAME_TYPE, CHANGE_LEVELS_OPTION, HOP_OPTION;
    private Locatable locatableTarget;

    public static String STATE = "Collecting Runes";

    @Override
    public void start() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GUI(Runespan.this);
            }
        });
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
        final String antiBanSwitchState = antiBanSwitch ? "Off" : "On";
        final String paintToggleSwitchState = painter.paintToggle() ? "Show" : "Hide";
        final JCheckBoxMenuItem antiBanMenuItem = new JCheckBoxMenuItem("Turn off anti-ban");
        final JCheckBoxMenuItem paintMenuItem = new JCheckBoxMenuItem("Hide paint");

        antiBanMenuItem.setText("Turn anti-ban " + antiBanSwitchState);
        paintMenuItem.setText(paintToggleSwitchState + " paint");

        antiBanMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                antiBanSwitch = !antiBanSwitch;
            }
        });
        paintMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                painter.togglePaint();
            }
        });
        menu.add(antiBanMenuItem);
        menu.add(paintMenuItem);
    }

    public static boolean members() {
        return GAME_TYPE.equals("P2P");
    }
    public static boolean hopping() { return HOP_OPTION.equals("Hop"); }
    public static boolean changeLevels() { return CHANGE_LEVELS_OPTION.equals("Yes"); }

    @Override
    public void menuDeselected(MenuEvent e) {}

    @Override
    public void menuCanceled(MenuEvent e) {}

    public void addTasks() {
        log.info("Adding tasks");

        taskList.addAll(
            Arrays.asList(
                    new BuyRunes(ctx),
                    new HandleResponse(ctx),
                    new SelectOption(ctx,"Yes", ctx.widgets.component(1188,14), "Do you want to buy some runes?"),
                    new SelectOption(ctx,"No", ctx.widgets.component(1188,14), "Would you like to subscribe?"),
                    new GenerateFloatingIsland(ctx,this),
                    new MoveUpALevel(ctx, this),
                    new BuildUpEssence(ctx, this),
                    new CollectRunes(ctx, this),
                    new SearchForBetter(ctx, this),
                    new FindTarget(ctx, this),
                    new GetEssence(ctx),
                    new MoveIslands(ctx, this)
            )
        );

        if(hopping()) {
            log.info("Adding island hopping task");
            taskList.add(new SearchForBetterAbroad(ctx, this));
        }
        if(changeLevels()) {
            log.info("Adding level changing task");
            taskList.add(new SearchForLadder(ctx, this));
        }
    }

    public void removeAllIslands() {
        floatingIslands.removeAll(floatingIslands);
        log.info("Island count: " + floatingIslands.size());
    }

    public void triggerAntiBan() {
        if(antiBanSwitch) antiBan.execute();
    }

    public boolean getAntiBanSwitch() {
        return antiBanSwitch;
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
        log.info("Building Island");

        final PlatformConnection prevPlatform = getPreviousPlatform();
        final FloatingIsland newIsland = new FloatingIsland(ctx, this);

        if(prevPlatform != null)
            prevPlatform.setConnection(newIsland);

        floatingIslands.add(newIsland);

        log.info("Island Built");
    }

    public void setCurrentXpRate(double xpRate) {
        this.currentXpRate = xpRate;
    }

    public double getCurrentXpRate() {
        return currentXpRate;
    }

    public void setLocatableTarget(Locatable target) {
        this.locatableTarget = target;
    }

    public Locatable getLocatableTarget() {
        return this.locatableTarget;
    }

    public boolean hasTarget() {
        return this.locatableTarget != null;
    }

    public static Tile getReachableTile(Locatable locatable, ClientContext ctx) {
        final Tile gameObjTile = locatable.tile();
        Tile[] tiles = new Tile[] {
                new Tile(gameObjTile.x() + 1, gameObjTile.y() + 1, gameObjTile.floor()),
                new Tile(gameObjTile.x() + 1, gameObjTile.y(), gameObjTile.floor()),
                new Tile(gameObjTile.x(), gameObjTile.y() + 1, gameObjTile.floor()),
                new Tile(gameObjTile.x() - 1, gameObjTile.y() - 1, gameObjTile.floor()),
                new Tile(gameObjTile.x() - 1, gameObjTile.y(), gameObjTile.floor()),
                new Tile(gameObjTile.x(), gameObjTile.y() - 1, gameObjTile.floor()),
        };

        for(Tile tile : tiles)
            if(ctx.movement.reachable(ctx.players.local().tile(), tile)) return tile;

        return Tile.NIL;
    }
}
