package com.kalegar.soti.util;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.math.Vector2;

public abstract class Steerer {

    protected final Steerable<Vector2> body;

    public Steerer(Steerable<Vector2> body) {
        this.body = body;
    }


    /**
     * Calculate the steering acceleration produced by this steerer.
     * @param steering the output steering acceleration
     * @return {@code false} if steering has completed (for instance the end of the path has been reached); {@code true} otherwise
     */
    public boolean calculateSteering (SteeringAcceleration<Vector2> steering) {
        return processSteering(getSteeringBehavior().calculateSteering(steering));
    }

    /**
     * Returns the steering behavior of this steerer, usually a priority or a blended steering grouping other steering behaviors.
     */
    public abstract SteeringBehavior<Vector2> getSteeringBehavior();

    /**
     * Called by {@link Steerer#calculateSteering(SteeringAcceleration)} to give this steerer the chance to:
     * <ul>
     * <li>decide whether this steerer has completed, for instance the end of the path has been reached</li>
     * <li>manipulate the acceleration produced by the steering behavior of this steerer</li>
     * </ul>
     * @param steering the input/output steering acceleration
     * @return {@code false} if steering has completed; {@code true} otherwise
     */
    public abstract boolean processSteering(SteeringAcceleration<Vector2> steering);

    /**
     * Called by {@link SteerableBody#startSteering()} to give this steerer the chance to prepare resources, for instance.
     */
    public abstract void startSteering();

    /**
     * Called by {@link SteerableBody#stopSteering()} to give this steerer the chance to release resources, for instance.
     * @return {@code true} if the linear velocity must be cleared; {@code false} otherwise
     */
    public abstract boolean stopSteering();
}
