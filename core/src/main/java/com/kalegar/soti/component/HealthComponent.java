package com.kalegar.soti.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class HealthComponent implements Component, Pool.Poolable {

    public float maxHealth = 100f;
    public float currentHealth = 100f;
    public boolean isDead = false;

    public void damage(float amount) {
        currentHealth -= amount;
        if (currentHealth <= 0f) {
            currentHealth = 0f;
            isDead = true;
        }
    }

    public void heal(float amount) {
        currentHealth = Math.min(currentHealth + amount, maxHealth);
        if (currentHealth > 0f) {
            isDead = false;
        }
    }

    public float getHealthPercent() {
        return maxHealth > 0f ? currentHealth / maxHealth : 0f;
    }

    @Override
    public void reset() {
        maxHealth = 100f;
        currentHealth = 100f;
        isDead = false;
    }
}
