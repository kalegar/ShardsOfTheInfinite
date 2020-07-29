package com.kalegar.soti.util;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.kalegar.soti.physics.PhysicsFixtureUserData;
import com.kalegar.soti.screen.GameScreen;

public class Utils {

    //Don't allow this class to be instantiated
    private Utils() {

    }

    public interface PhysicsBodySettings {
        Shape getShape();

        float getDensity();

        float getFriction();

        float getRestitution();

        float getLinearDamping();

        float getAngularDamping();

        boolean isSensor();

        boolean isBullet();
    }

    /**
     * Creates a new dynamic physics body
     * @param entity
     * @param position the physics world position.
     * @param world
     * @param settings
     * @return
     */
    public static Body createDynamicBody(Object entity, Vector2 position, PhysicsBodySettings settings) {
        return createDynamicBody(entity,position,settings, PhysicsFixtureUserData.FixtureUserDataType.ENTITY);
    }

    public static Body createDynamicBody(Object entity, Vector2 position, PhysicsBodySettings settings, PhysicsFixtureUserData.FixtureUserDataType type) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x, position.y);
        bodyDef.bullet = settings.isBullet();

        Body body = GameScreen.getWorld().createBody(bodyDef);
        body.setUserData(entity);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = settings.getShape();
        fixtureDef.density = settings.getDensity();
        fixtureDef.friction = settings.getFriction();
        fixtureDef.restitution = settings.getRestitution();
        fixtureDef.isSensor = settings.isSensor();

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(new PhysicsFixtureUserData(type));

        body.setLinearDamping(settings.getLinearDamping());
        body.setAngularDamping(settings.getAngularDamping());

        return body;
    }

    public static float vectorToAngle(Vector2 vector) {
        return (float)Math.atan2(-vector.x, vector.y);
    }

    public static Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.x = -(float)Math.sin(angle);
        outVector.y = (float)Math.cos(angle);
        return outVector;
    }

    public static float lerp(float lower, float upper, float fraction) {
        return (lower + fraction * (upper - lower));
    }

    public static float manhattenDistance(float x1, float y1, float x2, float y2) {
        return Math.abs(x2 - x1) + Math.abs(y2 - y1);
    }

}
