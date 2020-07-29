package com.kalegar.soti.entity.component;

import com.badlogic.ashley.core.ComponentMapper;

public class ComponentMappers {
    public static final ComponentMapper<TransformComponent> transform = ComponentMapper.getFor(TransformComponent.class);
    public static final ComponentMapper<RenderComponent> render = ComponentMapper.getFor(RenderComponent.class);
    public static final ComponentMapper<PhysicsComponent> physics = ComponentMapper.getFor(PhysicsComponent.class);
    public static final ComponentMapper<SteeringComponent> steering = ComponentMapper.getFor(SteeringComponent.class);
    public static final ComponentMapper<SelectedComponent> select = ComponentMapper.getFor(SelectedComponent.class);
    public static final ComponentMapper<TeamComponent> team = ComponentMapper.getFor(TeamComponent.class);
    public static final ComponentMapper<FormationComponent> formation = ComponentMapper.getFor(FormationComponent.class);
    public static final ComponentMapper<FormationMemberComponent> formationMember = ComponentMapper.getFor(FormationMemberComponent.class);
    public static final ComponentMapper<RoomComponent> room = ComponentMapper.getFor(RoomComponent.class);
    public static final ComponentMapper<StationComponent> station = ComponentMapper.getFor(StationComponent.class);
}
