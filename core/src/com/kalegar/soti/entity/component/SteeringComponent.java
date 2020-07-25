package com.kalegar.soti.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.ai.fma.FormationMember;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;
import com.kalegar.soti.entity.steering.SteeringLocation;
import com.kalegar.soti.util.Steerer;
import com.kalegar.soti.util.Utils;

public class SteeringComponent implements Steerable<Vector2>, Component, Pool.Poolable, FormationMember<Vector2> {

    public Body body;

    public float maxLinearSpeed = 5f;
    public float maxLinearAcceleration = 10f;
    public float maxAngularSpeed = 5f;
    public float maxAngularAcceleration = 3f;
    public float zeroThreshold = 0.1f;
    public Steerer steerer;
    private static final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<>(new Vector2());
    public float boundingRadius = 0.375f;
    public boolean tagged = false;
    public boolean independentFacing = false;
    public boolean wasSteering;

    public Location<Vector2> targetLocation = new SteeringLocation();

    @Override
    public void reset() {
        wasSteering = false;
        targetLocation.getPosition().setZero();
    }

    public boolean isIndependentFacing() {
        return independentFacing;
    }

    public void setIndependentFacing(boolean independentFacing) {
        this.independentFacing = independentFacing;
    }

    public void update(float delta) {
        if (steerer != null) {
            if (steerer.calculateSteering(steeringOutput)) {
                if (!wasSteering) {
                    startSteering();
                }
                applySteering(steeringOutput, delta);
            }else{
                stopSteering(true);
            }
        }
    }

    protected void applySteering(SteeringAcceleration<Vector2> steering, float deltaTime) {
        boolean anyAccelerations = false;

        if (!steeringOutput.linear.isZero()) {
            body.applyForceToCenter(steeringOutput.linear, true);
            anyAccelerations = true;
        }

        if (isIndependentFacing()) {
            if (steeringOutput.angular != 0) {
                body.applyTorque(steeringOutput.angular, true);
                anyAccelerations = true;
            }
        } else {
            Vector2 linVel = getLinearVelocity();
            if (!linVel.isZero(getZeroLinearSpeedThreshold())) {
                float newOrientation = MathUtils.lerpAngle(body.getAngle(),vectorToAngle(linVel),0.1f);
                //body.setAngularVelocity((newOrientation - getAngularVelocity()) * deltaTime * maxAngularAcceleration);
                body.setTransform(body.getPosition(),newOrientation);
            }
        }

        if (anyAccelerations) {
            Vector2 velocity = body.getLinearVelocity();
            float currentSpeedSquare = velocity.len2();
            float maxLinearSpeed = getMaxLinearSpeed();
            if (currentSpeedSquare > (maxLinearSpeed * maxLinearSpeed)) {
                body.setLinearVelocity(velocity.scl(maxLinearSpeed / (float) Math.sqrt(currentSpeedSquare)));
            }
            float maxAngularVelocity = getMaxAngularSpeed();
            if (body.getAngularVelocity() > maxAngularVelocity) {
                body.setAngularVelocity(maxAngularVelocity);
            }
        }
    }

    protected void startSteering() {
        // Disable linear and angular damping as the body
        // is now being controlled by the steerer.
        wasSteering = true;
        body.setLinearDamping(0f);
        body.setAngularDamping(0f);
        if (steerer != null) {
            steerer.startSteering();
        }
    }

    protected void stopSteering(boolean clearLinearVelocity) {
        wasSteering = false;
        body.setAngularVelocity(0);
        body.setLinearDamping(1f);
        body.setAngularDamping(0.5f);
        if (steerer != null) {
            clearLinearVelocity = steerer.stopSteering();
        }

        steerer = null;
        steeringOutput.setZero();
        if (clearLinearVelocity) {
            body.setLinearVelocity(Vector2.Zero);
        }
    }

    @Override
    public Vector2 getLinearVelocity() {
        return body.getLinearVelocity();
    }

    @Override
    public float getAngularVelocity() {
        return body.getAngularVelocity();
    }

    @Override
    public float getBoundingRadius() {
        return this.boundingRadius;
    }

    @Override
    public boolean isTagged() {
        return this.tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return this.zeroThreshold;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {
        this.zeroThreshold = value;
    }

    @Override
    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearAcceleration = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }

    @Override
    public Vector2 getPosition() {
        return body.getPosition();
    }

    @Override
    public float getOrientation() {
        return body.getAngle();
    }

    @Override
    public void setOrientation(float orientation) {
        body.setTransform(getPosition(), orientation);
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

    @Override
    public Location<Vector2> getTargetLocation() {
        return targetLocation;
    }
}
