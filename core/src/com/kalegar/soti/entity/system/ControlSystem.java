package com.kalegar.soti.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.kalegar.soti.entity.component.ComponentMappers;
import com.kalegar.soti.entity.component.SelectedComponent;
import com.kalegar.soti.entity.component.TeamComponent;
import com.kalegar.soti.physics.PhysicsFixtureUserData;
import com.kalegar.soti.util.Constants;

public class ControlSystem extends IteratingSystem implements QueryCallback {

    private OrthographicCamera camera;
    private World world;
    private Vector2 selectPosition = new Vector2();
    private Vector2 lower = new Vector2();
    private Vector2 upper = new Vector2();
    private boolean selecting = false;

    public ControlSystem(OrthographicCamera camera, World world) {
        super(Family.all(SelectedComponent.class, TeamComponent.class).get());
        this.camera = camera;
        this.world = world;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            selecting = true;
            Vector3 pos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            Vector3 mPos = camera.unproject(pos);

            selectPosition.set(mPos.x,mPos.y);
        }
        if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (selecting) {
                selecting = false;

                Vector3 pos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                Vector3 mPos = camera.unproject(pos);

                lower.set(Math.min(selectPosition.x,mPos.x),Math.min(selectPosition.y,mPos.y));
                upper.set(Math.max(selectPosition.x,mPos.x),Math.max(selectPosition.y,mPos.y));

                if (lower.dst2(upper) < 1) {
                    lower.sub(0.5f,0.5f);
                    upper.add(0.5f,0.5f);
                }

                lower.scl(1/Constants.PPM);
                upper.scl(1/Constants.PPM);

                world.QueryAABB(this,
                        lower.x,
                        lower.y,
                        upper.x,
                        upper.y
                );
            }
        }


    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (!Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                entity.remove(SelectedComponent.class);
            }
        }
    }

    @Override
    public boolean reportFixture(Fixture fixture) {
        PhysicsFixtureUserData fud = (PhysicsFixtureUserData) fixture.getUserData();
        if (fud.getType().equals(PhysicsFixtureUserData.FixtureUserDataType.ENTITY)) {
            Entity entity = (Entity) fixture.getBody().getUserData();
            if (!ComponentMappers.select.has(entity)) {
                //Select this entity
                SelectedComponent select = ((PooledEngine) getEngine()).createComponent(SelectedComponent.class);
                entity.add(select);
            }else{
                //Deselect this entity
                entity.remove(SelectedComponent.class);
            }
        }
        return true;
    }
}
