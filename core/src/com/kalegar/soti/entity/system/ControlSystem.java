package com.kalegar.soti.entity.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
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
import com.kalegar.soti.entity.component.FormationComponent;
import com.kalegar.soti.entity.component.FormationMemberComponent;
import com.kalegar.soti.entity.component.PhysicsComponent;
import com.kalegar.soti.entity.component.SelectedComponent;
import com.kalegar.soti.entity.component.SteeringComponent;
import com.kalegar.soti.entity.component.TeamComponent;
import com.kalegar.soti.entity.component.TransformComponent;
import com.kalegar.soti.entity.steering.SteeringLocation;
import com.kalegar.soti.formation.VeeFormationPattern;
import com.kalegar.soti.physics.PhysicsFixtureUserData;
import com.kalegar.soti.screen.GameScreen;
import com.kalegar.soti.steerers.ArriveSteerer;
import com.kalegar.soti.steerers.CollisionAvoidanceSteererBase;
import com.kalegar.soti.util.Constants;

public class ControlSystem extends IteratingSystem implements QueryCallback {

    private Viewport viewport;
    private World world;
    private Vector2 selectPosition = new Vector2();
    private Vector2 lower = new Vector2();
    private Vector2 upper = new Vector2();
    private Vector2 mousePosition = new Vector2();
    private boolean selecting = false;
    private Rectangle selectionRectangle;

    private ImmutableArray<Entity> formations;
    private Entity formation;

    private Vector2 averagePosition = new Vector2();

    public ControlSystem(Viewport viewport, World world) {
        super(Family.all(SelectedComponent.class, TeamComponent.class, SteeringComponent.class).get());
        this.viewport = viewport;
        this.world = world;

        selectionRectangle = new Rectangle();
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        formations = getEngine().getEntitiesFor(Family.all(FormationComponent.class, SteeringComponent.class, PhysicsComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        // Clear current formation
        formation = null;
        // If we have some entities selected
        if (getEntities().size() > 0) {
            if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
                // Create a new formation
                VeeFormationPattern pattern = new VeeFormationPattern(1.5f);
                updateAveragePosition();
                Vector2 offset = mousePosition.cpy().scl(1/Constants.PPM).sub(averagePosition).scl(0.2f).limit(10f);
                formation = GameScreen.getEntityFactory().getFormation(averagePosition.add(offset));

                SteeringComponent steering = ComponentMappers.steering.get(formation);
                steering.steerer = new ArriveSteerer(steering,new SteeringLocation(mousePosition.cpy().scl(1 / Constants.PPM)));
                ((CollisionAvoidanceSteererBase) steering.steerer).setCollisionAvoidanceEnabled(false);

                FormationComponent fc = ComponentMappers.formation.get(formation);
                fc.init(pattern,steering);
                fc.formation.updateSlots();
                fc.formation.getAnchorPoint().setOrientation(fc.formation.getAnchorPoint().vectorToAngle(offset));
            }
        }
        // Update formations
        for (Entity formation : formations) {
            FormationComponent fc = ComponentMappers.formation.get(formation);
            fc.formation.updateSlots();
            if (fc.formation.getSlotAssignmentCount() == 0) {
                getEngine().removeEntity(formation);
            }
            SteeringComponent sc = ComponentMappers.steering.get(formation);
            if (!sc.isSteering) {
                getEngine().removeEntity(formation);
            }
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
            FormationMemberComponent formationMember = ComponentMappers.formationMember.get(entity);
            if (formationMember.formation != null) {
                formationMember.formation.removeMember(formationMember);
            }

            FormationComponent currentFormation = ComponentMappers.formation.get(formation);
            formationMember.formation = currentFormation.formation;
            currentFormation.formation.addMember(formationMember);

            steering.steerer = new ArriveSteerer(steering,formationMember.targetLocation);

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
        FormationMemberComponent fmc = ComponentMappers.formationMember.get(entity);
        /*if (fmc.formation != null) {
            FormationComponent formation = ComponentMappers.formation.get(fmc.formation);
            formation.formation.removeMember(fmc);
        }*/
        entity.remove(SelectedComponent.class);
    }

    public Rectangle getSelectionRectangle() {
        return selectionRectangle;
    }

    public boolean isSelecting() {
        return selecting;
    }

    public void draw(ShapeRenderer shapeRenderer) {
        for (Entity entity : formations) {
            FormationComponent formation = ComponentMappers.formation.get(entity);
            SteeringLocation temp = new SteeringLocation();
            Vector2 anchor = formation.formation.getAnchorPoint().getPosition();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            for (Entity selected : getEntities()) {
                FormationMemberComponent formationMember = ComponentMappers.formationMember.get(selected);
                shapeRenderer.circle((formationMember.getTargetLocation().getPosition().x) * Constants.PPM,(formationMember.getTargetLocation().getPosition().y) * Constants.PPM,1);
            }
            Vector2 dir = new Vector2();
            temp.angleToVector(dir,formation.formation.getAnchorPoint().getOrientation());
            shapeRenderer.line(anchor.cpy().scl(Constants.PPM),anchor.cpy().add(dir).scl(Constants.PPM));
            shapeRenderer.end();

        }
    }

    private void updateAveragePosition() {
        averagePosition.setZero();
        for (Entity entity : getEntities()) {
            TransformComponent tc = ComponentMappers.transform.get(entity);
            averagePosition.add(tc.position.x,tc.position.y);
        }
        averagePosition.scl(1/(float)getEntities().size());
    }
}
