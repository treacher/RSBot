package com.treacher.runespan.enums;

import com.treacher.runespan.Runespan;
import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Constants;
import org.powerbot.script.rt6.GameObject;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Michael Treacher
 */
public enum ElementalNode {
    JUMPER(70466,107.8, new Rune[]{Rune.LAW}, 54),
    SHIFTER(70465,86.8, new Rune[]{Rune.NATURE}, 44),
    NEBULA(70464,77, new Rune[]{Rune.COSMIC, Rune.ASTRAL}, 40),
    CHAOTIC_CLOUD(70463,61.6,new Rune[]{Rune.CHAOS}, 35),
    FIRE_STORM(70462,35,new Rune[]{Rune.AIR, Rune.FIRE}, 27),
    FLESHY_GROWTH(70461,46.2, new Rune[]{Rune.BODY}, 20),
    VINE(70460,30.3, new Rune[]{Rune.WATER, Rune.EARTH}, 17),
    FIREBALL(70459, 34.8,new Rune[]{Rune.FIRE}, 14),
    ROCK_FRAGMENT(70458, 28.6,new Rune[]{Rune.EARTH},9),
    WATER_POOL(70457,25.3, new Rune[]{Rune.WATER},5),
    MIND_STORM(70456,20, new Rune[]{Rune.MIND},1),
    CYCLONE(70455,19, new Rune[]{Rune.AIR}, 1),
    SKULLS(70467,120,new Rune[]{Rune.DEATH}, 65),
    BLOOD_POOL(70468,146.3,new Rune[]{Rune.BLOOD}, 77),
    BLOODY_SKULLS(70469,175.5,new Rune[]{Rune.BLOOD, Rune.DEATH}, 83),
    LIVING_SOUL(70470,213,new Rune[]{Rune.SOUL}, 90),
    UNDEAD_SOULS(70471,255.5,new Rune[]{Rune.SOUL, Rune.DEATH}, 95);

    private final int gameObjectId;
    private final double xp;
    private final Rune[] runes;
    private int levelRequirement;
    private static List<ElementalNode> exceptionNodes = Arrays.asList(
            FLESHY_GROWTH,
            VINE,
            FIRE_STORM
    );

    private ElementalNode(int gameObjectId, double xp, Rune[] runes, int levelRequirement) {
        this.gameObjectId = gameObjectId;
        this.xp = xp;
        this.runes = runes;
        this.levelRequirement = levelRequirement;
    }

    public double getXp() {
        return xp;
    }

    public static boolean hasNode(int id, ClientContext ctx) {
        return (findNodeByGameObjectId(id,ctx) != null);
    }

    public static ElementalNode findNodeByGameObjectId(int id, ClientContext ctx) {
        for(ElementalNode node : ElementalNode.values())
            if(node.gameObjectId == id && !node.excluded(ctx)) return node;
        return null;
    }

    public static void siphonNode(final GameObject node, final ClientContext ctx, final Runespan runespan) {
        if(!node.valid()) return;

        final ElementalNode elementalNode = ElementalNode.findNodeByGameObjectId(node.id(), ctx);

        if(elementalNode != null)
            runespan.setCurrentXpRate(elementalNode.getXp());

        ctx.camera.turnTo(node);
        ctx.camera.pitch(60);

        // Wait till interacting with monster

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                boolean siphoningNode = node.interact(false, "Siphon");
                if (!siphoningNode)
                    ctx.movement.findPath(node).traverse();
                return siphoningNode;
            }
        }, 1000, 4);

        // Wait till siphoning the node
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return !ctx.players.local().idle();
            }
        }, 2000, 2);

        runespan.triggerAntiBan();
    }

    private boolean excluded(ClientContext ctx) {
        boolean excluded = false;

        for(Rune rune : this.runes) {
            excluded = Runespan.getExclusionList().contains(rune);
        }

        if(ctx.skills.level(Constants.SKILLS_RUNECRAFTING) < this.levelRequirement) excluded = true;

        if(exceptionNodes.contains(this)) excluded = false;

        return excluded;
    }
}
