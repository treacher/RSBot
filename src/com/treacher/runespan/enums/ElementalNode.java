package com.treacher.runespan.enums;

import com.treacher.runespan.Runespan;
import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Constants;
import org.powerbot.script.rt6.GameObject;

import java.util.concurrent.Callable;

/**
 * Created by Michael Treacher
 */
public enum ElementalNode {
    JUMPER(70466,107.8, 54, true),
    SHIFTER(70465,86.8, 44, true),
    NEBULA(70464,77, 40, true),
    CHAOTIC_CLOUD(70463,61.6, 35, true),
    FIRE_STORM(70462,35, 27, false),
    FLESHY_GROWTH(70461,46.2, 20, false),
    VINE(70460,30.3, 17, false),
    FIREBALL(70459, 34.8, 14, false),
    ROCK_FRAGMENT(70458, 28.6,9, false),
    WATER_POOL(70457,25.3,5, false),
    MIND_STORM(70456,20,1, false),
    CYCLONE(70455,19, 1, false),
    SKULLS(70467,120, 65, true),
    BLOOD_POOL(70468,146.3, 77, true),
    BLOODY_SKULLS(70469,175.5, 83, true),
    LIVING_SOUL(70470,213, 90, true),
    UNDEAD_SOULS(70471,255.5, 95, true);

    private final int gameObjectId;
    private final double xp;
    private final int levelRequirement;
    private final boolean members;

    private ElementalNode(int gameObjectId, double xp, int levelRequirement, boolean members) {
        this.gameObjectId = gameObjectId;
        this.xp = xp;
        this.levelRequirement = levelRequirement;
        this.members = members;
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
        }, 500, 8);

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
        boolean levelTooHigh =  this.levelRequirement > ctx.skills.level(Constants.SKILLS_RUNECRAFTING);
        boolean exclude = false;
        if(!Runespan.members()) exclude = this.members;
        return exclude || levelTooHigh;
    }
}
