package com.treacher.runespan.util;

import com.treacher.runespan.enums.ElementalNode;
import com.treacher.runespan.enums.EssenceMonster;
import com.treacher.runespan.enums.Rune;
import org.powerbot.script.Filter;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.MobileIdNameQuery;
import org.powerbot.script.rt6.Npc;

import java.util.Comparator;

/**
 * Created by Michael Treacher
 */
public class RunespanQuery {

    private final ClientContext ctx;
    private final FloatingIsland currentIsland;

    public RunespanQuery(ClientContext ctx, FloatingIsland currentIsland) {
        this.ctx = ctx;
        this.currentIsland = currentIsland;
    }
    public Npc highestPriorityEssenceMonster() {
        return highestPriorityEssenceMonsterOnIslandQuery().peek();
    }

    public boolean hasEssenceMonsters() {
        return (!hasTooMuchEssence() && !highestPriorityEssenceMonsterOnIslandQuery().isEmpty()) || needLawRunes();
    }

    public int essenceStackSize() {
        return ctx.backpack.select().id(Rune.ESSENCE.getGameObjectId()).poll().stackSize();
    }

    public boolean needLawRunes() {
        return highestPriorityEssenceMonster().id() == EssenceMonster.LAW_ESSHOUND_FLOOR_1.getGameObjectId()
                && ctx.backpack.select().id(Rune.LAW.getGameObjectId()).poll().stackSize() <= 20;
    }

    public boolean hasTooMuchEssence() {
        return essenceStackSize() >= 150;
    }

    public GameObject highestPriorityNode() {
        return highestPriorityNodeOnIslandQuery().peek();
    }

    public boolean hasNodes() {
        return !highestPriorityNodeOnIslandQuery().isEmpty();
    }

    public GameObject nearestHighestPriorityNode() {
        return ctx.objects.select().select(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject gameObject) {
                return ElementalNode.hasNode(gameObject.id(), ctx);
            }
        }).sort(new Comparator<GameObject>() {
            @Override
            public int compare(GameObject o1, GameObject o2) {
                ElementalNode n1 = ElementalNode.findNodeByGameObjectId(o1.id(), ctx);
                ElementalNode n2 = ElementalNode.findNodeByGameObjectId(o2.id(), ctx);
                return new Double(n2.getXp()).compareTo(n1.getXp());
            }
        }).nearest().peek();
    }

    public Npc nearestHighestPriorityMonster() {
        return ctx.npcs.select().select(new Filter<Npc>() {
            @Override
            public boolean accept(Npc npc) {
                return EssenceMonster.hasMonster(npc.id(), ctx);
            }
        }).sort(new Comparator<Npc>() {
            @Override
            public int compare(Npc o1, Npc o2) {
                EssenceMonster m1 = EssenceMonster.findMonsterByGameObjectId(o1.id(), ctx);
                EssenceMonster m2 = EssenceMonster.findMonsterByGameObjectId(o2.id(), ctx);
                return new Double(m2.getXp()).compareTo(m1.getXp());
            }
        }).nearest().peek();
    }

    private MobileIdNameQuery<GameObject> highestPriorityNodeOnIslandQuery() {
        return ctx.objects.select().select(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject gameObject) {
                return currentIsland != null
                        && currentIsland.onIsland(gameObject.tile())
                        && ElementalNode.hasNode(gameObject.id(), ctx);
            }
        }).sort(new Comparator<GameObject>() {
            @Override
            public int compare(GameObject o1, GameObject o2) {
                ElementalNode n1 = ElementalNode.findNodeByGameObjectId(o1.id(), ctx);
                ElementalNode n2 = ElementalNode.findNodeByGameObjectId(o2.id(), ctx);
                return new Double(n2.getXp()).compareTo(n1.getXp());
            }
        });
    }

    private MobileIdNameQuery<Npc> highestPriorityEssenceMonsterOnIslandQuery() {
        return ctx.npcs.select().select(new Filter<Npc>() {
            @Override
            public boolean accept(Npc npc) {
                return currentIsland != null
                        && currentIsland.onIsland(npc.tile())
                        && EssenceMonster.hasMonster(npc.id(), ctx);
            }
        }).sort(new Comparator<Npc>() {
            @Override
            public int compare(Npc o1, Npc o2) {
                EssenceMonster m1 = EssenceMonster.findMonsterByGameObjectId(o1.id(), ctx);
                EssenceMonster m2 = EssenceMonster.findMonsterByGameObjectId(o2.id(), ctx);
                return new Double(m2.getXp()).compareTo(m1.getXp());
            }
        });
    }
}
