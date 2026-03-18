package com.kalegar.soti.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

public class CombatComponent implements Component, Pool.Poolable {

    public float attackDamage = 10f;
    public float attackRange = 48f;
    public float attackCooldown = 1.0f;
    public float timeSinceLastAttack = 0f;
    public Entity target = null;

    public boolean canAttack() {
        return timeSinceLastAttack >= attackCooldown && target != null;
    }

    public void resetAttackTimer() {
        timeSinceLastAttack = 0f;
    }

    @Override
    public void reset() {
        attackDamage = 10f;
        attackRange = 48f;
        attackCooldown = 1.0f;
        timeSinceLastAttack = 0f;
        target = null;
    }
}
