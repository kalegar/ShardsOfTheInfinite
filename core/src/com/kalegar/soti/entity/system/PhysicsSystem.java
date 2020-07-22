package com.kalegar.soti.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.kalegar.soti.entity.component.ComponentMappers;
import com.kalegar.soti.entity.component.PhysicsComponent;
import com.kalegar.soti.entity.component.TransformComponent;
import com.kalegar.soti.util.Constants;

public class PhysicsSystem extends IteratingSystem {

    private static float accumulator = 0f;

    private World world;

    public PhysicsSystem(World world) {
        super(Family.all(PhysicsComponent.class, TransformComponent.class).get());
        this.world = world;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent tc = ComponentMappers.transform.get(entity);
        PhysicsComponent pc = ComponentMappers.physics.get(entity);
        Vector2 position = pc.body.getPosition();
        tc.position.x = position.x;
        tc.position.y = position.y;
        tc.rotation = pc.body.getAngle() * MathUtils.radiansToDegrees;
    }

    @Override
    public void update(float deltaTime) {
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= Constants.PHYSICS_TIME_STEP) {
            world.step(Constants.PHYSICS_TIME_STEP, Constants.VELOCITY_ITERATIONS, Constants.POSITION_ITERATIONS);
            accumulator -= Constants.PHYSICS_TIME_STEP;
        }
        super.update(deltaTime);
    }
}
