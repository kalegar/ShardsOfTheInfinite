package com.kalegar.soti.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.fma.Formation;
import com.badlogic.gdx.ai.fma.FormationPattern;
import com.badlogic.gdx.ai.fma.FreeSlotAssignmentStrategy;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.kalegar.soti.screen.GameScreen;

public class FormationComponent implements Component, Pool.Poolable {

    public Formation<Vector2> formation;

    public void init(FormationPattern<Vector2> pattern, Steerable<Vector2> steerable) {
        formation = new Formation<>(steerable,pattern, new FreeSlotAssignmentStrategy<Vector2>());
    }

    @Override
    public void reset() {

    }
}
