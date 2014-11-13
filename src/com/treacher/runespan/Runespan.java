package com.treacher.runespan;

import com.treacher.runespan.enums.ElementalNode;
import com.treacher.runespan.enums.Rune;
import com.treacher.runespan.tasks.*;
import com.treacher.util.Task;
import com.treacher.runespan.enums.EssenceMonsters;
import com.treacher.runespan.util.FloatingIsland;
import org.powerbot.script.*;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.MobileIdNameQuery;
import org.powerbot.script.rt6.Npc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Michael Treacher
 */
@Script.Manifest(name = "Runespan", description = "Trains runecrafting in the runespan.", properties = "topic=1227888")
public class Runespan extends PollingScript<ClientContext> {

    private List<FloatingIsland> floatingIslands = new ArrayList<FloatingIsland>();
    private List<Task<ClientContext>> taskList = new ArrayList<Task<ClientContext>>();
    private static List<Rune> runesToExclude = new ArrayList<Rune>();
    private int currentNodeId;

    @Override
    public void start() {

        FloatingIsland island1 = new FloatingIsland("Island 1",
                new Tile(4136, 6068, 1),
                new Tile(4137, 6091, 1),
                new Tile(4143, 6080, 1),
                new Tile(4126, 6083, 1)
        );

        floatingIslands.add(island1);

        taskList.addAll(
                Arrays.asList(
                        new MoveIslands(ctx, this),
                        new ExcludeAndIncludeRunes(ctx),
                        new SearchForBetterNodes(ctx, this),
                        new CollectRunes(ctx, this),
                        new BuildUpEssence(ctx, this),
                        new GetEssence(ctx)
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

    public FloatingIsland currentIsland() {
        for (FloatingIsland island : floatingIslands) {
            if (island.onIsland(ctx.players.local().tile())) return island;
        }
        return null;
    }

    public Npc highestPriorityEssenceMonster() {
        return highestPriorityEssenceMonsterQuery().peek();
    }

    public boolean hasEssenceMonsters() {
        return !highestPriorityEssenceMonsterQuery().isEmpty();
    }

    public GameObject highestPriorityNode() {
        return highestPriorityNodeQuery().peek();
    }

    public boolean hasNodes() {
        return !highestPriorityNodeQuery().isEmpty();
    }

    public static void addRuneToExclusionList(Rune rune) {
        runesToExclude.add(rune);
    }

    public static void removeRuneFromExclusionList(Rune rune) {
        runesToExclude.remove(rune);
    }

    public static List<Rune> getExclusionList() {
        return runesToExclude;
    }

    public void setCurrentNodeId(int nodeId) {
        this.currentNodeId = nodeId;
    }

    public int getCurrentNodeId() {
        return currentNodeId;
    }

    private MobileIdNameQuery<GameObject> highestPriorityNodeQuery() {
        return ctx.objects.select().select(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject gameObject) {
                return currentIsland().onIsland(gameObject.tile()) && ElementalNode.hasNode(gameObject.id());
            }
        }).sort(new Comparator<GameObject>() {
            @Override
            public int compare(GameObject o1, GameObject o2) {
                ElementalNode n1 = ElementalNode.findNodeByGameObjectId(o1.id());
                ElementalNode n2 = ElementalNode.findNodeByGameObjectId(o2.id());
                System.out.println(n1);
                System.out.println(n2);
                return new Double(n2.getXp()).compareTo(n1.getXp());
            }
        });
    }

    private MobileIdNameQuery<Npc> highestPriorityEssenceMonsterQuery() {
        return ctx.npcs.select().select(new Filter<Npc>() {
            @Override
            public boolean accept(Npc npc) {
                return currentIsland().onIsland(npc.tile()) && EssenceMonsters.hasMonster(npc.id());
            }
        }).sort(new Comparator<Npc>() {
            @Override
            public int compare(Npc o1, Npc o2) {
                EssenceMonsters m1 = EssenceMonsters.findMonsterByGameObjectId(o1.id());
                EssenceMonsters m2 = EssenceMonsters.findMonsterByGameObjectId(o2.id());

                return new Double(m2.getXp()).compareTo(m1.getXp());
            }
        });
    }
}
