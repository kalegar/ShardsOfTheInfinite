package com.kalegar.soti.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.kalegar.soti.component.CombatComponent;
import com.kalegar.soti.component.HealthComponent;
import com.kalegar.soti.component.TransformComponent;

public class CombatSystem extends IteratingSystem {

    private final ComponentMapper<CombatComponent> combatMapper;
    private final ComponentMapper<HealthComponent> healthMapper;
    private final ComponentMapper<TransformComponent> transformMapper;

    private final Vector2 tmpVec = new Vector2();

    public CombatSystem(int priority) {
        super(Family.all(CombatComponent.class, TransformComponent.class).get(), priority);
        combatMapper = ComponentMapper.getFor(CombatComponent.class);
        healthMapper = ComponentMapper.getFor(HealthComponent.class);
        transformMapper = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        CombatComponent combat = combatMapper.get(entity);
        combat.timeSinceLastAttack += deltaTime;

        if (combat.target == null) return;

        HealthComponent targetHealth = healthMapper.get(combat.target);
        if (targetHealth == null || targetHealth.isDead) {
            combat.target = null;
            return;
        }

        TransformComponent myTransform = transformMapper.get(entity);
        TransformComponent targetTransform = transformMapper.get(combat.target);
        if (targetTransform == null) {
            combat.target = null;
            return;
        }

        tmpVec.set(targetTransform.position.x, targetTransform.position.y)
               .sub(myTransform.position.x, myTransform.position.y);
        float distance = tmpVec.len();

        if (distance > combat.attackRange) {
            return;
        }

        if (combat.canAttack()) {
            targetHealth.damage(combat.attackDamage);
            combat.resetAttackTimer();
        }
    }
}
