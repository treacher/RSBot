package com.treacher.lumbridgeflaxer;

import com.treacher.lumbridgeflaxer.antiban.AntiBan;
import com.treacher.lumbridgeflaxer.enums.FlaxerState;
import com.treacher.lumbridgeflaxer.tasks.*;
import com.treacher.lumbridgeflaxer.ui.Painter;
import org.powerbot.script.*;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GeItem;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Michael Treacher
 */

@Script.Manifest(name = "Lumbridge Flaxer", description = "Makes bowstrings in lumbridge castle. VIP+ only.", properties = "topic=1227888")
public class LumbridgeFlaxer extends PollingScript<ClientContext> implements PaintListener {

    private final int flaxId = 1779;
    private final int bowStringId = 1777;

    private final Tile bankTile = new Tile(3208,3220,2);
    private final Tile secondFloorTileToSpinWheel = new Tile(3205,3209,1);
    private final Tile spinningWheelTile = new Tile(3209,3213,1);
    private final Tile topFloorTileToBank = new Tile(3205,3209,2);

    private List<Task<ClientContext>> taskList = new ArrayList<Task<ClientContext>>();

    private int bowStringsCount = 0;

    private final Painter painter = new Painter(this);

    private int currentGameObjectId;
    private String currentGameObjectInteraction;

    public static FlaxerState STATE = FlaxerState.STARTING;

    public static long timeSinceLastMovement = 0;

    public final AntiBan antiBan = new AntiBan(ctx);

    @Override
    public void start() {
        if(!isVIP()) {
            ctx.controller.stop();
            JOptionPane.showMessageDialog(null, "This script has now become VIP only due to the effect the script is having on the market.");
        } else {

            setPrices();

            final int spinningWheelId = 36970;
            final int secondFloorStaircaseId = 36774;
            final int topFloorStaircaseId = 36775;
            final int bankId = 36786;

            taskList.addAll(Arrays.asList(
                    pathToObjectAndInteract(topFloorStaircaseId, "Climb-down", bankTile, flaxId, 28),
                    pathToObjectAndInteract(spinningWheelId, "Spin", secondFloorTileToSpinWheel, flaxId, 28),
                    new SpinTheWheel(ctx, flaxId, this),
                    pathToObjectAndInteract(secondFloorStaircaseId, "Climb-up", spinningWheelTile, flaxId, 0),
                    pathToObjectAndInteract(bankId, "Bank", topFloorTileToBank, flaxId, 0),
                    new Banker(ctx, bankTile, this),
                    new PathCorrecter(ctx, this)
            ));
        }
    }

    @Override
    public void poll() {
        for(Task<ClientContext> task : taskList){
            if(task.activate()) {
                task.execute();
            }
        }
    }

    @Override
    public void repaint(Graphics g) {
        if (painter != null) painter.repaint(g);
    }

    public void setPrices(){
        final int flaxPrice = GeItem.price(flaxId);
        final int bowStringPrice =  GeItem.price(bowStringId);

        this.painter.setPrices(flaxPrice, bowStringPrice);
    }

    public boolean isVIP() {
        return Boolean.valueOf(ctx.properties.getProperty("user.vip"));
    }

    public void triggerAntiBanCheck() {
        antiBan.execute();
    }

    public int getCurrentGameObjectId() {
        return currentGameObjectId;
    }

    public void setCurrentGameObjectId(int objectId) {
        currentGameObjectId = objectId;
    }

    public String getCurrentGameObjectInteraction() {
        return currentGameObjectInteraction;
    }

    public void setCurrentGameObjectInteraction(String interaction) {
        currentGameObjectInteraction = interaction;
    }

    public int getFlaxId(){
        return flaxId;
    }

    public void incrementBowStringsCount() {
        bowStringsCount += 28;
    }

    public int getBowStringsCount() {
        return bowStringsCount;
    }

    /**
     * Generates PathToObjectAndInteract tasks and overrides the activate method with an isOnTileWithInventoryCountOfItem check
     * @param objectId Object to interact with
     * @param interaction Interaction to be had with object
     * @param tileToBeOn Tile that you need to be on for this task to be triggered
     * @param itemId Item we are looking for in inventory
     * @param inventoryCount Count that we expect in the inventory as a prerequisite for triggering the task
     * @return PathToObjectAndInteract task
     */
    public PathToObjectAndInteract pathToObjectAndInteract(int objectId, String interaction, final Tile tileToBeOn, final int itemId, final int inventoryCount) {
        return new PathToObjectAndInteract(ctx, objectId, interaction, this) {
            @Override
            public boolean activate() {
                waitTillPlayerIsNotMoving();
                return isOnTileWithInventoryCountOfItem(tileToBeOn, itemId, inventoryCount);
            }
        };
    }

    public boolean isOnTileWithInventoryCountOfItem(Tile tile, int itemId, int inventoryCount){
        return ctx.players.local().tile().equals(tile) && ctx.backpack.select().id(itemId).count() == inventoryCount;
    }

    public void waitTillPlayerIsNotMoving() {
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().idle();
            }
        },150 , 20);
    }



}