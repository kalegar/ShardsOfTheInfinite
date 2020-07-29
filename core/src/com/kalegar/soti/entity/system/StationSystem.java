package com.kalegar.soti.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.kalegar.soti.pathfind.Node;
import com.kalegar.soti.entity.component.ComponentMappers;
import com.kalegar.soti.entity.component.RoomComponent;
import com.kalegar.soti.entity.component.StationComponent;
import com.kalegar.soti.entity.component.TransformComponent;
import com.kalegar.soti.physics.PhysicsFixtureUserData;
import com.kalegar.soti.screen.GameScreen;
import com.kalegar.soti.util.Constants;

public class StationSystem extends IteratingSystem {

    public StationSystem() {
        super(Family.all(StationComponent.class,TransformComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }

    public void recalculatePhysicsBody(Entity stationEntity) {

        TransformComponent stationTransform = ComponentMappers.transform.get(stationEntity);
        StationComponent station = ComponentMappers.station.get(stationEntity);

        if (station.body != null) {
            GameScreen.getWorld().destroyBody(station.body);
        }

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(stationTransform.position.x, stationTransform.position.y);

        Body body = GameScreen.getWorld().createBody(bodyDef);
        body.setUserData(stationEntity);

        PolygonShape box = new PolygonShape();

        for (Node node : station.nodes) {
            Entity entity = (Entity) node.getData();
            RoomComponent room = ComponentMappers.room.get(entity);
            TransformComponent transform = ComponentMappers.transform.get(entity);

            float width = room.tileGraph.width * Constants.TILE_WIDTH;
            float height = room.tileGraph.height * Constants.TILE_HEIGHT;

            box.setAsBox(width / Constants.PPM / 2, height / Constants.PPM / 2, new Vector2(transform.position.x,transform.position.y),0);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = box;
            fixtureDef.density = 1;
            fixtureDef.friction = 0;
            fixtureDef.restitution = 0;

            Fixture fixture = body.createFixture(fixtureDef);
            fixture.setUserData(new PhysicsFixtureUserData(PhysicsFixtureUserData.FixtureUserDataType.ENTITY));
        }

        box.dispose();
    }
}
