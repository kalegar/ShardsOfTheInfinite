package com.kalegar.soti.formation;

import com.badlogic.gdx.ai.fma.Formation;
import com.badlogic.gdx.ai.fma.FormationPattern;
import com.badlogic.gdx.ai.fma.FreeSlotAssignmentStrategy;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.kalegar.soti.entity.steering.SteeringLocation;
import com.kalegar.soti.physics.PhysicsFixtureUserData;
import com.kalegar.soti.util.Utils;

public class FormationSteerer implements Steerable<Vector2> {

    private float boundingRadius = 1f;
    private boolean tagged;

    private float zeroLinearSpeedThreshold = 0.01f;
    private float maxLinearSpeed = 5f;
    private float maxLinearAcceleration = 50f;
    private float maxAngularSpeed = 1f;
    private float maxAngularAcceleration = 0.1f;

    private SteeringBehavior<Vector2> steeringBehavior;
    private SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<>(new Vector2());

    private final Formation<Vector2> formation;
    private Body body;

    public FormationSteerer(FormationPattern<Vector2> pattern){
        this.body = Utils.createDynamicBody(this, new Vector2(), new Utils.PhysicsBodySettings() {
            @Override
            public Shape getShape() {
                CircleShape shape = new CircleShape();
                shape.setRadius(0.4f);
                return shape;
            }

            @Override
            public float getDensity() {
                return 0.1f;
            }

            @Override
            public float getFriction() {
                return 0;
            }

            @Override
            public float getRestitution() {
                return 0;
            }

            @Override
            public float getLinearDamping() {
                return 0;
            }

            @Override
            public float getAngularDamping() {
                return 0;
            }

            @Override
            public boolean isSensor() {
                return true;
            }

            @Override
            public boolean isBullet() {
                return false;
            }
        }, PhysicsFixtureUserData.FixtureUserDataType.FORMATION);
        this.formation = new Formation<>(this,pattern, new FreeSlotAssignmentStrategy<Vector2>());

    }

    public void update() {
        if (steeringBehavior != null) {
            steeringBehavior.calculateSteering(steeringOutput);
            applySteering(steeringOutput);
            if (steeringBehavior instanceof Arrive) {
                if (((Arrive<Vector2>) steeringBehavior).getTarget().getPosition().dst(getPosition()) <= 0.5f) {
                    steeringBehavior = null;
                    body.setLinearVelocity(0,0);
                }
            }
        }
        formation.updateSlots();
    }

    private void applySteering(SteeringAcceleration<Vector2> steering) {
        boolean anyAccelerations = false;

        if (!steeringOutput.linear.isZero()) {
            body.applyForceToCenter(steeringOutput.linear, true);
            anyAccelerations = true;
        }

        Vector2 linVel = getLinearVelocity();
        if (!linVel.isZero(getZeroLinearSpeedThreshold())) {
            float newOrientation = MathUtils.lerpAngle(body.getAngle(),vectorToAngle(linVel),0.1f);//Utils.lerp(body.getAngle(),vectorToAngle(linVel),0.1f);
            //body.setAngularVelocity((newOrientation - getAngularVelocity()) * deltaTime * maxAngularAcceleration);
            body.setTransform(body.getPosition(),newOrientation);
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

    public SteeringBehavior<Vector2> getSteeringBehavior() {
        return steeringBehavior;
    }

    public void setSteeringBehavior(SteeringBehavior<Vector2> steeringBehavior) {
        this.steeringBehavior = steeringBehavior;
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
        return boundingRadius;
    }

    @Override
    public boolean isTagged() {
        return tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return zeroLinearSpeedThreshold;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {
        this.zeroLinearSpeedThreshold = value;
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

    public Formation<Vector2> getFormation() {
        return formation;
    }
}
