package com.treacher.runespan.util;

import com.treacher.runespan.enums.Platform;

/**
 * Created by treach3r on 10/11/14.
 */
public class PlatformConnection {

    private final FloatingIsland connection;
    private final Platform platform;

    public PlatformConnection(FloatingIsland connection, Platform platform){
        this.connection = connection;
        this.platform = platform;
    }

    public Platform getPlatform(){
        return platform;
    }

    public FloatingIsland getConnection(){
        return connection;
    }
}
