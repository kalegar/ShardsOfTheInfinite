package com.kalegar.soti.entity.component;

import com.badlogic.ashley.core.ComponentMapper;

public class ComponentMappers {
    public static final ComponentMapper<TransformComponent> transform = ComponentMapper.getFor(TransformComponent.class);
    public static final ComponentMapper<RenderComponent> render = ComponentMapper.getFor(RenderComponent.class);
}
