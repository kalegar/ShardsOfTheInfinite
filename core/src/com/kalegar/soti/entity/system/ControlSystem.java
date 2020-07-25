package com.kalegar.soti.entity.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.fma.Formation;
import com.badlogic.gdx.ai.fma.patterns.DefensiveCircleFormationPattern;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kalegar.soti.entity.component.ComponentMappers;
import com.kalegar.soti.entity.component.SelectedComponent;
import com.kalegar.soti.entity.component.SteeringComponent;
import com.kalegar.soti.entity.component.TeamComponent;
import com.kalegar.soti.entity.steering.SteeringLocation;
import com.kalegar.soti.formation.FormationSteerer;
import com.kalegar.soti.formation.VeeFormationPattern;
import com.kalegar.soti.physics.PhysicsFixtureUserData;
import com.kalegar.soti.steerers.ArriveSteerer;
import com.kalegar.soti.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class ControlSystem extends IteratingSystem implements QueryCallback {

    private Viewport viewport;
    private World world;
    private Vector2 selectPosition = new Vector2();
    private Vector2 lower = new Vector2();
    private Vector2 upper = new Vector2();
    private Vector2 mousePosition = new Vector2();
    private boolean selecting = false;
    private Rectangle selectionRectangle;
    private FormationSteerer formation;

    public ControlSystem(Viewport viewport, World world) {
        super(Family.all(SelectedComponent.class, TeamComponent.class, SteeringComponent.class).get());
        this.viewport = viewport;
        this.world = world;

        selectionRectangle = new Rectangle();
    }

    @Override
    public void update(float deltaTime) {
        if (formation != null) {
            formation.update();
        }

        super.update(deltaTime);
        Vector2 pos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        Vector2 mPos = viewport.unproject(pos);
        mousePosition.set(mPos.x,mPos.y);
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            selecting = true;
            selectPosition.set(mousePosition);
        }
        if (selecting) {
            lower.set(Math.min(selectPosition.x,mousePosition.x),Math.min(selectPosition.y,mousePosition.y));
            upper.set(Math.max(selectPosition.x,mousePosition.x),Math.max(selectPosition.y,mousePosition.y));
            selectionRectangle.set(lower.x,lower.y,upper.x-lower.x,upper.y-lower.y);

            if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                selecting = false;
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
        // If we have some entities selected
        if (getEntities().size() > 0) {
            if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
                if (formation == null) {
                    VeeFormationPattern pattern = new VeeFormationPattern(1.5f);
                    //DefensiveCircleFormationPattern<Vector2> pattern = new DefensiveCircleFormationPattern<>(1f);
                    formation = new FormationSteerer(pattern);
                    Arrive<Vector2> arrive = new Arrive<>(formation);
                    arrive.setTarget(new SteeringLocation(mousePosition.cpy().scl(1 / Constants.PPM)));
                    arrive.setDecelerationRadius(6f).setArrivalTolerance(0.5f).setTimeToTarget(1);
                    formation.setSteeringBehavior(arrive);
                }else{
                    Arrive<Vector2> arrive = new Arrive<>(formation);
                    arrive.setTarget(new SteeringLocation(mousePosition.cpy().scl(1 / Constants.PPM)));
                    arrive.setDecelerationRadius(6f).setArrivalTolerance(0.5f).setTimeToTarget(1);
                    formation.setSteeringBehavior(arrive);
                }
            }
        }
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (!Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                deselectEntity(entity);
                return;
            }
        }
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) && formation != null) {
            SteeringComponent steering = ComponentMappers.steering.get(entity);

            steering.steerer = new ArriveSteerer(steering,steering.targetLocation);
            formation.getFormation().removeMember(steering);
            formation.getFormation().addMember(steering);
            //steering.steerer = new ArriveSteerer(steering,new SteeringLocation(mousePosition.cpy().scl(1/Constants.PPM)));

        }
    }

    @Override
    public boolean reportFixture(Fixture fixture) {
        PhysicsFixtureUserData fud = (PhysicsFixtureUserData) fixture.getUserData();
        if (fud.getType().equals(PhysicsFixtureUserData.FixtureUserDataType.ENTITY)) {
            Entity entity = (Entity) fixture.getBody().getUserData();
            if (!ComponentMappers.select.has(entity)) {
                //Select this entity
                selectEntity(entity);
            }else{
                //Deselect this entity
                deselectEntity(entity);
            }
        }
        return true;
    }

    private void selectEntity(Entity entity) {
        SelectedComponent select = ((PooledEngine) getEngine()).createComponent(SelectedComponent.class);
        entity.add(select);
    }

    private void deselectEntity(Entity entity) {
        if (formation != null) {
            formation.getFormation().removeMember( ComponentMappers.steering.get(entity));
        }
        entity.remove(SelectedComponent.class);
    }

    public Rectangle getSelectionRectangle() {
        return selectionRectangle;
    }

    public boolean isSelecting() {
        return selecting;
    }

    public void draw(ShapeRenderer shapeRenderer) {
        if (formation != null) {
            SteeringLocation temp = new SteeringLocation();
            Vector2 anchor = formation.getFormation().getAnchorPoint().getPosition();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            for (Entity entity : getEntities()) {
                SteeringComponent steer = ComponentMappers.steering.get(entity);
                shapeRenderer.circle((steer.getTargetLocation().getPosition().x) * Constants.PPM,(steer.getTargetLocation().getPosition().y) * Constants.PPM,1);
            }
            Vector2 dir = new Vector2();
            temp.angleToVector(dir,formation.getFormation().getAnchorPoint().getOrientation());
            shapeRenderer.line(anchor.cpy().scl(Constants.PPM),anchor.cpy().add(dir).scl(Constants.PPM));
            shapeRenderer.end();

        }
    }
}
