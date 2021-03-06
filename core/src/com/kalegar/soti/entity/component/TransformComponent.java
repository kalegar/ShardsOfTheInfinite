package com.kalegar.soti.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;

public class TransformComponent implements Component, Pool.Poolable {

    public final Vector3 position = new Vector3();
    public final Vector2 scale = new Vector2(1,1);
    public float rotation = 0f;

    @Override
    public void reset() {
        position.setZero();
        scale.set(1,1);
    }
}
