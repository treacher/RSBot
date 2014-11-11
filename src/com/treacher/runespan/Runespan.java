package com.treacher.runespan;

import com.treacher.runespan.util.FloatingIsland;
import com.treacher.runespan.enums.Platform;
import com.treacher.runespan.util.PlatformConnection;
import org.powerbot.script.Condition;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

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

        FloatingIsland island1 = new FloatingIsland("Island 1",
                new Tile(4115,6026,1),
                new Tile(4115,6021,1),
                new Tile(4112,6022,1),
                new Tile(4114,6027,1)
        );

        FloatingIsland island2 = new FloatingIsland("Island 2",
                new Tile(4116,6034,1),
                new Tile(4099,6036,1),
                new Tile(4109, 6045,1),
                new Tile(4110,6032,1)
        );

        FloatingIsland island3 = new FloatingIsland("Island 3",
                new Tile(4104,6025,1),
                new Tile(4102,6018,1),
                new Tile(4098,6020,1),
                new Tile(4106,6022,1)
        );

        FloatingIsland island4 = new FloatingIsland("Island 4",
                new Tile(4109,6051,1),
                new Tile(4108,6060,1),
                new Tile(4105,6056,1),
                new Tile(4113,6058,1)
        );

        FloatingIsland island5 = new FloatingIsland("Island 5",
                new Tile(4119,6042,1),
                new Tile(4125,6037,1),
                new Tile(4132,6045,1),
                new Tile(4130,6051,1)
        );

        FloatingIsland island6 = new FloatingIsland("Island 6",
                new Tile(4125,6031,1),
                new Tile(4132,6022,1),
                new Tile(4133,6024,1),
                new Tile(4123,6028,1)
        );

        island1.addConnection(new PlatformConnection(island2, Platform.COMET_PLATFORM));
        island2.addConnection(new PlatformConnection(island1, Platform.COMET_PLATFORM));
        island2.addConnection(new PlatformConnection(island3, Platform.VINE_PLATFORM));
        island2.addConnection(new PlatformConnection(island4, Platform.COMET_PLATFORM));
        island2.addConnection(new PlatformConnection(island5, Platform.COMET_PLATFORM));
        island6.addConnection(new PlatformConnection(island5, Platform.MIST_PLATFORM));

        floatingIslands.add(island1);
        floatingIslands.add(island2);
        floatingIslands.add(island3);
        floatingIslands.add(island4);
        floatingIslands.add(island5);
        floatingIslands.add(island6);
    }

    @Override
    public void poll(){
        Condition.sleep();
        if(currentIsland() != null) {
            System.out.println("Current Island:" + currentIsland().getTitle());
        }
    }

}
