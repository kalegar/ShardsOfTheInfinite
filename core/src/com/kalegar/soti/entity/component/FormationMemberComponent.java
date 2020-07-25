package com.kalegar.soti.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.fma.Formation;
import com.badlogic.gdx.ai.fma.FormationMember;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.kalegar.soti.entity.steering.SteeringLocation;

public class FormationMemberComponent implements Component, FormationMember<Vector2>, Pool.Poolable {

    public SteeringLocation targetLocation = new SteeringLocation();
    public Formation<Vector2> formation;

    @Override
    public Location<Vector2> getTargetLocation() {
        return targetLocation;
    }

    @Override
    public void reset() {
        targetLocation.getPosition().setZero();
        targetLocation.setOrientation(0);
        formation = null;
    }
}
