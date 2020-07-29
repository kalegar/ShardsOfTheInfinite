package com.kalegar.soti.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;
import com.kalegar.soti.tile.TileGraph;

public class RoomComponent implements Component, Pool.Poolable {

    public TileGraph tileGraph;
    public Entity station;

    @Override
    public void reset() {
        tileGraph = null;
        station = null;
    }
}
