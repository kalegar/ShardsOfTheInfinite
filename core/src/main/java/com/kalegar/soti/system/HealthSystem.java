package com.kalegar.soti.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Array;
import com.kalegar.soti.component.HealthComponent;

public class HealthSystem extends IteratingSystem {

    private final ComponentMapper<HealthComponent> healthMapper;
    private final Array<Entity> deadEntities;

    public HealthSystem(int priority) {
        super(Family.all(HealthComponent.class).get(), priority);
        healthMapper = ComponentMapper.getFor(HealthComponent.class);
        deadEntities = new Array<>();
    }

    @Override
    public void update(float deltaTime) {
        deadEntities.clear();
        super.update(deltaTime);
        for (Entity entity : deadEntities) {
            getEngine().removeEntity(entity);
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        HealthComponent health = healthMapper.get(entity);
        if (health.isDead) {
            deadEntities.add(entity);
        }
    }
}
