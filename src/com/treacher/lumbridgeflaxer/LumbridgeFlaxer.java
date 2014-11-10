package com.treacher.lumbridgeflaxer;

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

    private final Tile bankTile = new Tile(3218,9623,0);
    private final Tile groundFloorTileToSpinWheel = new Tile(3210,3216,0);
    private final Tile secondFloorTileToSpinWheel = new Tile(3205,3209,1);
    private final Tile spinningWheelTile = new Tile(3209,3213,1);
    private final Tile groundFloorTileToBank = new Tile(3205,3209,0);
    private final Tile basementFloorTileToBank = new Tile(3208,9616,0);

    private List<Task<ClientContext>> taskList = new ArrayList<Task<ClientContext>>();

    private int bowStringsCount = 0;

    private final Painter painter = new Painter(this);

    private int currentGameObjectId;
    private String currentGameObjectInteraction;

    public static String STATE = "Starting up";
    public static long timeSinceLastMovement = 0;

    @Override
    public void start() {
        setPrices();

        final int basementLadderId = 29355;
        final int groundFloorStaircaseId = 36773;
        final int spinningWheelId = 36970;
        final int secondFloorStaircaseId = 36774;
        final int trapdoorId = 36687;
        final int bankId = 12308;

        taskList.addAll(Arrays.asList(
                pathToObjectAndInteract(basementLadderId, "Climb-up", bankTile, flaxId),
                pathToObjectAndInteract(groundFloorStaircaseId, "Climb-up", groundFloorTileToSpinWheel, flaxId),
                pathToObjectAndInteract(spinningWheelId, "Spin", secondFloorTileToSpinWheel, flaxId),
                new SpinTheWheel(ctx, flaxId),
                pathToObjectAndInteract(secondFloorStaircaseId, "Climb-down", spinningWheelTile, bowStringId),
                pathToObjectAndInteract(trapdoorId, "Climb-down", groundFloorTileToBank, bowStringId),
                pathToObjectAndInteract(bankId, "Bank", basementFloorTileToBank, bowStringId),
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
        STATE = "Setting prices";
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

    public PathToObjectAndInteract pathToObjectAndInteract(int objectId, String interaction, final Tile tile, final int inventoryItemId) {
        return new PathToObjectAndInteract(ctx, objectId, interaction, this) {
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