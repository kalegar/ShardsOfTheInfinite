package com.kalegar.soti.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;
import com.kalegar.soti.gfx.AnimationAdvanced;

public class RenderComponent implements Component, Pool.Poolable {

    public AnimationAdvanced<TextureRegion> animation;
    public float stateTime;
    public boolean animate = false;
    public boolean visible = true;
    public Color tint = new Color(Color.WHITE);


    @Override
    public void reset() {
        tint.set(Color.WHITE);
        visible = true;
    }
}
