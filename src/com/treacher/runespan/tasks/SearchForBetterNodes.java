package com.treacher.runespan.tasks;

import com.treacher.runespan.Runespan;
import com.treacher.runespan.enums.ElementalNode;
import com.treacher.runespan.enums.Rune;
import com.treacher.util.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import java.util.concurrent.Callable;

/**
 * Created by Michael Treacher
 */
public class SearchForBetterNodes extends Task<ClientContext> {

    private Runespan runespan;
    private GameObject betterNode;

    public SearchForBetterNodes(ClientContext ctx, Runespan runespan) {
        super(ctx);
        this.runespan = runespan;
    }

    @Override
    public boolean activate() {
        int essenceStackSize = ctx.backpack.select().id(Rune.ESSENCE.getGameObjectId()).poll().stackSize();
        return essenceStackSize > 100 && runespan.hasNodes() && !ctx.players.local().idle() && betterNodeAvailable();
    }

    @Override
    public void execute() {
        if(betterNode.valid()) {
            ctx.camera.turnTo(betterNode);
            ctx.movement.findPath(betterNode).traverse();
            betterNode.hover();
            betterNode.interact("Siphon");
            waitTillSiphoning();
        }
    }

    private void waitTillSiphoning() {
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                System.out.println(ctx.players.local().idle());
                return !ctx.players.local().idle();
            }
        }, 1000, 2);
    }

    private boolean betterNodeAvailable() {
        GameObject nodeObject = runespan.highestPriorityNode();
        ElementalNode currentNode = ElementalNode.findNodeByGameObjectId(runespan.getCurrentNodeId());
        ElementalNode newNode = ElementalNode.findNodeByGameObjectId(nodeObject.id());
        betterNode = nodeObject;
        return currentNode != null && nodeObject!= null && currentNode.getXp() < newNode.getXp();
    }
}
