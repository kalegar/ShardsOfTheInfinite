package com.kalegar.soti.system.render;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.kalegar.soti.component.HealthComponent;
import com.kalegar.soti.component.TransformComponent;

public class HealthBarRenderer {

    private static final float BAR_WIDTH = 32f;
    private static final float BAR_HEIGHT = 4f;
    private static final float BAR_OFFSET_Y = 20f;

    private static final Color BG_COLOR = new Color(0.2f, 0.2f, 0.2f, 0.8f);
    private static final Color HEALTHY_COLOR = new Color(0.1f, 0.8f, 0.1f, 0.9f);
    private static final Color DAMAGED_COLOR = new Color(0.9f, 0.7f, 0.1f, 0.9f);
    private static final Color CRITICAL_COLOR = new Color(0.9f, 0.1f, 0.1f, 0.9f);

    private final ComponentMapper<HealthComponent> healthMapper;
    private final ComponentMapper<TransformComponent> transformMapper;

    public HealthBarRenderer() {
        healthMapper = ComponentMapper.getFor(HealthComponent.class);
        transformMapper = ComponentMapper.getFor(TransformComponent.class);
    }

    public void render(ShapeRenderer shapeRenderer, Entity entity) {
        HealthComponent health = healthMapper.get(entity);
        TransformComponent transform = transformMapper.get(entity);

        if (health == null || transform == null) return;
        if (health.getHealthPercent() >= 1f) return;

        float x = transform.position.x - BAR_WIDTH / 2f;
        float y = transform.position.y + BAR_OFFSET_Y;
        float pct = health.getHealthPercent();

        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(BG_COLOR);
        shapeRenderer.rect(x, y, BAR_WIDTH, BAR_HEIGHT);

        shapeRenderer.setColor(getBarColor(pct));
        shapeRenderer.rect(x, y, BAR_WIDTH * pct, BAR_HEIGHT);
    }

    private Color getBarColor(float pct) {
        if (pct > 0.6f) return HEALTHY_COLOR;
        if (pct > 0.25f) return DAMAGED_COLOR;
        return CRITICAL_COLOR;
    }
}
