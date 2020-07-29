package com.kalegar.soti.entity;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.kalegar.soti.AssetLoader;
import com.kalegar.soti.entity.component.FormationComponent;
import com.kalegar.soti.entity.component.FormationMemberComponent;
import com.kalegar.soti.entity.component.PhysicsComponent;
import com.kalegar.soti.entity.component.RenderComponent;
import com.kalegar.soti.entity.component.StationComponent;
import com.kalegar.soti.entity.component.SteeringComponent;
import com.kalegar.soti.entity.component.TeamComponent;
import com.kalegar.soti.entity.component.TransformComponent;
import com.kalegar.soti.physics.PhysicsFixtureUserData;
import com.kalegar.soti.util.Constants;
import com.kalegar.soti.util.Utils;

public class EntityFactory {

    private PooledEngine engine;
    private World world;

    public EntityFactory(PooledEngine engine, World world) {
        this.engine = engine;
        this.world = world;
    }

    public Entity getStation(Vector2 worldPosition) {
        Entity entity = engine.createEntity();

        TransformComponent transform = engine.createComponent(TransformComponent.class);
        StationComponent station = engine.createComponent(StationComponent.class);

        transform.position.set(worldPosition,0);

        entity.add(transform);
        entity.add(station);

        engine.addEntity(entity);
        return entity;
    }

    public Entity getFormation(Vector2 worldPosition) {
        Entity entity = engine.createEntity();

        PhysicsComponent physics = engine.createComponent(PhysicsComponent.class);
        SteeringComponent steering = engine.createComponent(SteeringComponent.class);
        FormationComponent formation = engine.createComponent(FormationComponent.class);

        physics.body = Utils.createDynamicBody(entity, worldPosition, new FormationPhysicsSettings(), PhysicsFixtureUserData.FixtureUserDataType.FORMATION);

        steering.body = physics.body;

        steering.zeroThreshold = 0.01f;
        steering.maxLinearSpeed = 5f;
        steering.maxLinearAcceleration = 50f;
        steering.maxAngularSpeed = 1f;
        steering.maxAngularAcceleration = 0.1f;

        entity.add(physics);
        entity.add(steering);
        entity.add(formation);

        engine.addEntity(entity);
        return entity;
    }

    /**
     * Creates and returns a fighter entity. The entity is added to the ECS engine.
     * @return the created fighter entity.
     */
    public Entity getFighter(Vector2 worldPosition) {
        Entity entity = engine.createEntity();

        TransformComponent transform = engine.createComponent(TransformComponent.class);
        PhysicsComponent physics = engine.createComponent(PhysicsComponent.class);
        SteeringComponent steering = engine.createComponent(SteeringComponent.class);
        RenderComponent render = engine.createComponent(RenderComponent.class);
        TeamComponent team = engine.createComponent(TeamComponent.class);
        FormationMemberComponent fmc = engine.createComponent(FormationMemberComponent.class);

        Vector2 pos = worldPosition.cpy().scl(1/ Constants.PPM);

        transform.position.x = pos.x;
        transform.position.y = pos.y;

        physics.body = Utils.createDynamicBody(entity, pos, new FighterPhysicsSettings());

        steering.body = physics.body;

        render.animation = AssetLoader.getInstance().getAnimation("fighter");

        entity.add(transform);
        entity.add(physics);
        entity.add(steering);
        entity.add(render);
        entity.add(team);
        entity.add(fmc);

        engine.addEntity(entity);
        return entity;
    }

    public static class FighterPhysicsSettings implements Utils.PhysicsBodySettings {

        public static Shape shape = new CircleShape();

        public FighterPhysicsSettings() {
            shape.setRadius(0.3f);
        }

        @Override
        public Shape getShape() {
            return shape;
        }

        @Override
        public float getDensity() {
            return 1;
        }

        @Override
        public float getFriction() {
            return 0.1f;
        }

        @Override
        public float getRestitution() {
            return 0.1f;
        }

        @Override
        public float getLinearDamping() {
            return 0.5f;
        }

        @Override
        public float getAngularDamping() {
            return 0.5f;
        }

        @Override
        public boolean isSensor() {
            return false;
        }

        @Override
        public boolean isBullet() {
            return false;
        }
    }

    public static class FormationPhysicsSettings implements Utils.PhysicsBodySettings {
        public static Shape shape = new CircleShape();

        public FormationPhysicsSettings() {
            shape.setRadius(0.4f);
        }

        @Override
        public Shape getShape() {
            return shape;
        }

        @Override
        public float getDensity() {
            return 0.1f;
        }

        @Override
        public float getFriction() {
            return 0.1f;
        }

        @Override
        public float getRestitution() {
            return 0;
        }

        @Override
        public float getLinearDamping() {
            return 0;
        }

        @Override
        public float getAngularDamping() {
            return 0;
        }

        @Override
        public boolean isSensor() {
            return true;
        }

        @Override
        public boolean isBullet() {
            return false;
        }
    }

}
