package com.treacher.lumbridgeflaxer;

import com.treacher.lumbridgeflaxer.enums.FlaxerState;
import com.treacher.lumbridgeflaxer.tasks.*;
import com.treacher.lumbridgeflaxer.ui.Painter;
import org.powerbot.script.*;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GeItem;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Michael Treacher
 */

@Script.Manifest(name = "Lumbridge Flaxer", description = "Makes bowstrings in lumbridge castle.", properties = "topic=1227888")
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

    @Override
    public void start() {
        setPrices();

        final int spinningWheelId = 36970;
        final int secondFloorStaircaseId = 36774;
        final int topFloorStaircaseId = 36775;
        final int bankId = 36786;

        taskList.addAll(Arrays.asList(
                pathToObjectAndInteract(topFloorStaircaseId, "Climb-down", bankTile, flaxId, 35, 50),
                pathToObjectAndInteract(spinningWheelId, "Spin", secondFloorTileToSpinWheel, flaxId, 45, 72),
                new SpinTheWheel(ctx, flaxId),
                pathToObjectAndInteract(secondFloorStaircaseId, "Climb-up", spinningWheelTile, bowStringId, 45, 72),
                pathToObjectAndInteract(bankId, "Bank", topFloorTileToBank, bowStringId, 30, 45),
                new Banker(ctx, bankTile, this),
                new PathCorrecter(ctx, this),
                new AntiBan(ctx, new Tile[]{bankTile, spinningWheelTile})
        ));
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

        painter.setPrices(flaxPrice, bowStringPrice);
    }

    public int getCurrentGameObjectId() {
        return currentGameObjectId;
    }

    public void setCurrentGameObjectId(int objectId) {
        this.currentGameObjectId = objectId;
    }

    public String getCurrentGameObjectInteraction() {
        return currentGameObjectInteraction;
    }

    public void setCurrentGameObjectInteraction(String interaction) {
        this.currentGameObjectInteraction = interaction;
    }

    public int getFlaxId(){
        return flaxId;
    }

    public void incrementBowStringsCount() {
        this.bowStringsCount += 28;
    }

    public int getBowStringsCount() {
        return this.bowStringsCount;
    }

    public PathToObjectAndInteract pathToObjectAndInteract(int objectId, String interaction, final Tile tile, final int inventoryItemId, int minPitch, int maxPitch) {
        return new PathToObjectAndInteract(ctx, objectId, interaction, minPitch, maxPitch, this) {
            @Override
            public boolean activate() {
                waitTillPlayerIsNotMoving();
                return isOnTileWithFullInventoryOfItem(tile, inventoryItemId);
            }
        };
    }

    public boolean isOnTileWithFullInventoryOfItem(Tile tile, int itemId){
        return ctx.players.local().tile().equals(tile) && ctx.backpack.select().id(itemId).count() == 28;
    }

    public void waitTillPlayerIsNotMoving() {
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().animation() == -1;
            }
        },150 , 20);
    }



}