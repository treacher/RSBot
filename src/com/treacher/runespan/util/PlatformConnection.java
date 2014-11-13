package com.treacher.runespan.util;

import com.treacher.runespan.enums.Platform;
import org.powerbot.script.Tile;

/**
 * Created by treach3r on 10/11/14.
 */
public class PlatformConnection {

    private final FloatingIsland connection;
    private final Platform platform;
    private final Tile platformTile;

    public PlatformConnection(FloatingIsland connection, Platform platform, Tile platformTile){
        this.connection = connection;
        this.platform = platform;
        this.platformTile = platformTile;
    }

    public Platform getPlatform(){
        return platform;
    }

    public FloatingIsland getConnection(){
        return connection;
    }

    public String toString() {
        return "Floating Island: " + connection.getTitle() + ", Platform: " + platform.name();
    }

    public Tile getPlatformTile() {
        return platformTile;
    }
}
