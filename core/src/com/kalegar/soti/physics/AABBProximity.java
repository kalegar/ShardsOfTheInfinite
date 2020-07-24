package com.kalegar.soti.physics;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.proximities.ProximityBase;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.kalegar.soti.entity.component.ComponentMappers;
import com.kalegar.soti.entity.component.SteeringComponent;
import com.kalegar.soti.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class AABBProximity<T extends Vector<T>> extends ProximityBase<T> implements QueryCallback {

    private final World world;
    private float radius;
    private ProximityCallback<T> callback;
    private Family include;
    private int count = 0;
    private float lastTime = 0;
    private List<Steerable<T>> neighbours = new ArrayList<>();

    public AABBProximity(World world, Steerable<T> owner, float radius, Family include) {
        super(owner, null);
        this.world = world;
        this.radius = radius;
        this.include = include;
    }

    /**
     * Finds neighbours within a square AABB with width/height = radius.
     * @param callback
     * @return Neighbour count. (Always zero)
     */
    @Override
    public int findNeighbors(ProximityCallback<T> callback) {
        //Store callback and initiate AABB query on physics world.
        //Stored callback is called within AABBQuery callback reportFixture()
        count = 0;
        this.callback = callback;
        float currentTime = GdxAI.getTimepiece().getTime();
        if (this.lastTime != currentTime) {
            neighbours.clear();
            // Save the current time
            this.lastTime = currentTime;
            Vector2 pos = (Vector2) owner.getPosition();
            world.QueryAABB(this, pos.x - radius, pos.y - radius, pos.x + radius, pos.y + radius);
        }else{
            for (Steerable<T> neighbour : neighbours) {
                if (callback.reportNeighbor(neighbour)) {
                    count ++;
                }
            }
        }
        return count;
    }

    @Override
    public boolean reportFixture(Fixture fixture) {
        if (callback != null) {
            PhysicsFixtureUserData fud = (PhysicsFixtureUserData) fixture.getUserData();
            //If found fixture is an entity, report it via callback.
            if (fud.getType().equals(PhysicsFixtureUserData.FixtureUserDataType.ENTITY)) {
                Entity e = (Entity) fixture.getBody().getUserData();
                //Don't report the owner, and ignore any types in ignoreList
                SteeringComponent steering = ComponentMappers.steering.get(e);
                if (steering != null && steering != owner && include.matches(e)) {
                    if (callback.reportNeighbor((Steerable<T>) steering)) {
                        count++;
                        neighbours.add((Steerable<T>) steering);
                    }
                }
            }
        }
        return true;
    }
}
