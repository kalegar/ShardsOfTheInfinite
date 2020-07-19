package com.kalegar.soti.gfx;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class AnimationAdvanced<T> extends Animation<T> {

    private Vector2 origin;

    public AnimationAdvanced(float frameDuration, Array<? extends T> keyFrames) {
        super(frameDuration, keyFrames);
    }

    public AnimationAdvanced(float frameDuration, Array<? extends T> keyFrames, Animation.PlayMode playMode) {
        super(frameDuration, keyFrames, playMode);
    }

    public AnimationAdvanced(float frameDuration, T... keyFrames) {
        super(frameDuration, keyFrames);
    }

    public Vector2 getOrigin() {
        return origin;
    }

    public void setOrigin(int x, int y) {
        origin.x = x;
        origin.y = y;
    }
}
