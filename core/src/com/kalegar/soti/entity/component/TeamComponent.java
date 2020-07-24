package com.kalegar.soti.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class TeamComponent implements Component, Pool.Poolable {

    public int team = 0;

    @Override
    public void reset() {
        team = 0;
    }
}
