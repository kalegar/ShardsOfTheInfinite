package com.kalegar.soti.entity.steering;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.kalegar.soti.util.Utils;

public class SteeringLocation implements Location<Vector2> {

    private Vector2 position;
    private float orientation;

    public SteeringLocation(){
        position = new Vector2();
        orientation = 0f;
    }

    public SteeringLocation(Vector2 position) {
        this.position = position;
        this.orientation = 0f;
    }

    public SteeringLocation(Vector2 position, float orientation) {
        this.position = position;
        this.orientation = orientation;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public float getOrientation() {
        return orientation;
    }

    public void setPosition(Vector2 position) {
        this.position.set(position);
    }

    @Override
    public void setOrientation(float orientation) {
        this.orientation = orientation;
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return Utils.vectorToAngle(vector);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        return Utils.angleToVector(outVector, angle);
    }

    @Override
    public Location<Vector2> newLocation() {
        return new SteeringLocation();
    }
}
