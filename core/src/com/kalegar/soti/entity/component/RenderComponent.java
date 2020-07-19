package com.kalegar.soti.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;
import com.kalegar.soti.gfx.AnimationAdvanced;

public class RenderComponent implements Component, Pool.Poolable {

    public AnimationAdvanced<TextureRegion> animation;
    public float stateTime;
    public boolean animate = false;
    public boolean visible = true;


    @Override
    public void reset() {
        visible = true;
    }
}
