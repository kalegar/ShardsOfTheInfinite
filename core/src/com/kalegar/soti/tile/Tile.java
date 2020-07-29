package com.kalegar.soti.tile;

import com.kalegar.soti.pathfind.Node;

public class Tile extends Node {

    public enum TileType {
        EMPTY,
        FLOOR,
        WALL
    }

    public TileType type;

    public Tile(int x, int y, int index) {
        this(x, y, index, TileType.EMPTY);
    }

    public Tile(int x, int y, int index, TileType type) {
        super(x, y, index,4);
        this.type = type;
    }
}
