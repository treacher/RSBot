package com.treacher.runespan;

import com.treacher.runespan.enums.EssenceMonsters;
import com.treacher.runespan.util.FloatingIsland;
import org.powerbot.script.Condition;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Npc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael Treacher on 10/11/14.
 */
@Script.Manifest(name = "Runespan", description = "Trains runecrafting in the runespan.", properties = "topic=1227888")
public class Runespan extends PollingScript<ClientContext> {

    private List<FloatingIsland> floatingIslands = new ArrayList<FloatingIsland>();

    public FloatingIsland currentIsland() {
        for(FloatingIsland island : floatingIslands) {
            if(island.onIsland(ctx.players.local().tile())) return island;
        }
        return null;
    }

    @Override
    public void start() {

//        FloatingIsland island1 = new FloatingIsland("Island 1",
//                new Tile(4115,6026,1),
//                new Tile(4115,6021,1),
//                new Tile(4112,6022,1),
//                new Tile(4114,6027,1)
//        );
//
//        FloatingIsland island2 = new FloatingIsland("Island 2",
//                new Tile(4116,6034,1),
//                new Tile(4099,6036,1),
//                new Tile(4109, 6045,1),
//                new Tile(4110,6032,1)
//        );
//
//        FloatingIsland island3 = new FloatingIsland("Island 3",
//                new Tile(4104,6025,1),
//                new Tile(4102,6018,1),
//                new Tile(4098,6020,1),
//                new Tile(4106,6022,1)
//        );
//
//        FloatingIsland island4 = new FloatingIsland("Island 4",
//                new Tile(4109,6051,1),
//                new Tile(4108,6060,1),
//                new Tile(4105,6056,1),
//                new Tile(4113,6058,1)
//        );
//
//        FloatingIsland island5 = new FloatingIsland("Island 5",
//                new Tile(4119,6042,1),
//                new Tile(4125,6037,1),
//                new Tile(4132,6045,1),
//                new Tile(4130,6051,1)
//        );
//
//        FloatingIsland island6 = new FloatingIsland("Island 6",
//                new Tile(4125,6031,1),
//                new Tile(4132,6022,1),
//                new Tile(4133,6024,1),
//                new Tile(4123,6028,1)
//        );
//
//        FloatingIsland island7 = new FloatingIsland("Island 7",
//                new Tile(4135,6035,1),
//                new Tile(4146, 6026,1),
//                new Tile(4153,6029,1),
//                new Tile(4140,6037,1)
//        );
//
//        FloatingIsland island8 = new FloatingIsland("Island 8",
//                new Tile(4143,6021,1),
//                new Tile(4142,6020,1),
//                new Tile(4150,6018,1),
//                new Tile(4146,6017,1)
//        );
//
//        FloatingIsland island9 = new FloatingIsland("Island 8",
//                new Tile(4159,6029,1),
//                new Tile(4169,6027,1),
//                new Tile(4167,6025,1),
//                new Tile(4167,6031,1)
//        );
//
//        island1.addConnection(new PlatformConnection(island2, Platform.COMET_PLATFORM));
//        island2.addConnection(new PlatformConnection(island1, Platform.COMET_PLATFORM));
//
//        island2.addConnection(new PlatformConnection(island3, Platform.VINE_PLATFORM));
//        island3.addConnection(new PlatformConnection(island2, Platform.VINE_PLATFORM));
//
//        island2.addConnection(new PlatformConnection(island4, Platform.COMET_PLATFORM));
//        island4.addConnection(new PlatformConnection(island2, Platform.COMET_PLATFORM));
//
//        island2.addConnection(new PlatformConnection(island5, Platform.COMET_PLATFORM));
//        island5.addConnection(new PlatformConnection(island2, Platform.COMET_PLATFORM));
//
//        island6.addConnection(new PlatformConnection(island5, Platform.MIST_PLATFORM));
//        island5.addConnection(new PlatformConnection(island6, Platform.MIST_PLATFORM));
//
//        island5.addConnection(new PlatformConnection(island7, Platform.COMET_PLATFORM));
//        island7.addConnection(new PlatformConnection(island5, Platform.COMET_PLATFORM));
//
//        island7.addConnection(new PlatformConnection(island8, Platform.MISSILE_PLATFORM));
//        island8.addConnection(new PlatformConnection(island7, Platform.MISSILE_PLATFORM));
//
//        island7.addConnection(new PlatformConnection(island9, Platform.MIST_PLATFORM));
//        island9.addConnection(new PlatformConnection(island7, Platform.MIST_PLATFORM));
//
//        floatingIslands.add(island1);
//        floatingIslands.add(island2);
//        floatingIslands.add(island3);
//        floatingIslands.add(island4);
//        floatingIslands.add(island5);
//        floatingIslands.add(island6);
//        floatingIslands.add(island7);
//        floatingIslands.add(island8);
    }

    @Override
    public void poll(){
        Condition.sleep();

        Npc highestPriorityNpc = highestPriorityNpc();


        System.out.println(highestPriorityNpc.toString());
    }

    private Npc highestPriorityNpc() {
        for(int npcId : EssenceMonsters.PRIORITY_ID_LIST) {
            Npc npc = ctx.npcs.viewable().id(npcId).peek();
            System.out.println(npcId);
            if(npc.valid()) {
                return npc;
            }
        }
        return ctx.npcs.nil();
    }

}
