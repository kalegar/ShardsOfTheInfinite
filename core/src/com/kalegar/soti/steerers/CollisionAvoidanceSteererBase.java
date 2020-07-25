package com.kalegar.soti.steerers;

import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.CollisionAvoidance;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.math.Vector2;
import com.kalegar.soti.entity.component.PhysicsComponent;
import com.kalegar.soti.physics.AABBProximity;
import com.kalegar.soti.screen.GameScreen;
import com.kalegar.soti.util.Steerer;

public abstract class CollisionAvoidanceSteererBase extends Steerer {

    protected final CollisionAvoidance<Vector2> collisionAvoidanceSB;
    protected final AABBProximity<Vector2> proximity;

    protected final PrioritySteering<Vector2> prioritySteering;

    public CollisionAvoidanceSteererBase(final Steerable<Vector2> body) {
        super(body);

        this.proximity = new AABBProximity<>(GameScreen.getWorld(),body,body.getBoundingRadius()*3f, Family.all(PhysicsComponent.class).get());
        this.collisionAvoidanceSB = new CollisionAvoidance<>(body,proximity);

        this.prioritySteering = new PrioritySteering<>(body, 0.001f);
        this.prioritySteering.add(collisionAvoidanceSB);
    }

    public void setCollisionAvoidanceEnabled(boolean enabled) {
        this.collisionAvoidanceSB.setEnabled(enabled);
    }

    @Override
    public SteeringBehavior<Vector2> getSteeringBehavior() {
        return prioritySteering;
    }
}
