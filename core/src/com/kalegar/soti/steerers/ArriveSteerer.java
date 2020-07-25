package com.kalegar.soti.steerers;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.math.Vector2;
import com.kalegar.soti.entity.steering.SteeringLocation;

public class ArriveSteerer extends CollisionAvoidanceSteererBase {

    public final Arrive<Vector2> arriveSB;

    boolean deadlockDetection;
    float deadlockDetectionStartTime;
    float collisionDuration;
    private static final float DEADLOCK_TIME = 0.5f;
    private static final float MAX_NO_COLLISION_TIME = DEADLOCK_TIME + .5f;

    public ArriveSteerer(final Steerable<Vector2> body, SteeringLocation target) {
        super(body);

        this.arriveSB = new Arrive<>(body)
                .setTarget(target)
                .setTimeToTarget(1.5f)
                .setArrivalTolerance(0.1f)
                .setDecelerationRadius(6f);

        this.prioritySteering.add(arriveSB);
    }

    @Override
    public boolean processSteering(SteeringAcceleration<Vector2> steering) {

        float distanceToTargetSquared = body.getPosition().dst2(arriveSB.getTarget().getPosition());

        if (prioritySteering.getSelectedBehaviorIndex() == 0) {
            //Collision Avoidance Management
            float pr = proximity.getRadius() * 1.5f;
            if (distanceToTargetSquared < pr * pr) {
                // Disable collision avoidance when nearing the target position as
                // it will likely prevent us from reaching the target.
                collisionAvoidanceSB.setEnabled(false);
                deadlockDetectionStartTime = Float.POSITIVE_INFINITY;
            } else if (deadlockDetection) {
                // Accumulate collision time during deadlock detection
                collisionDuration += GdxAI.getTimepiece().getDeltaTime();

                if (GdxAI.getTimepiece().getTime() - deadlockDetectionStartTime > DEADLOCK_TIME && collisionDuration > DEADLOCK_TIME * .6f) {
                    // Disable collision avoidance since most of the deadlock detection period has been spent on collision avoidance
                    collisionAvoidanceSB.setEnabled(false);
                }
            } else {
                // Start deadlock detection
                deadlockDetectionStartTime = GdxAI.getTimepiece().getTime();
                collisionDuration = 0;
                deadlockDetection = true;
            }
            return true;
        }

        // Check if we have reached the target position
        if (steering.isZero() && distanceToTargetSquared < arriveSB.getArrivalTolerance() * arriveSB.getArrivalTolerance()) {
            return false;
        }

        // Check if collision avoidance must be re-enabled
        if (deadlockDetection && !collisionAvoidanceSB.isEnabled() && GdxAI.getTimepiece().getTime() - deadlockDetectionStartTime > MAX_NO_COLLISION_TIME) {
            collisionAvoidanceSB.setEnabled(true);
            deadlockDetection = false;
        }

        return true;
    }

    @Override
    public void startSteering() {

    }

    @Override
    public boolean stopSteering() {
        return false;
    }
}
