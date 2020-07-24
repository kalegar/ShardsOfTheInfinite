package com.kalegar.soti.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.kalegar.soti.entity.component.ComponentMappers;
import com.kalegar.soti.entity.component.PhysicsComponent;
import com.kalegar.soti.entity.component.SteeringComponent;

public class SteeringSystem extends IteratingSystem {

    public SteeringSystem() {
        super(Family.all(SteeringComponent.class, PhysicsComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        SteeringComponent steering = ComponentMappers.steering.get(entity);
        steering.update(deltaTime);
    }
}
