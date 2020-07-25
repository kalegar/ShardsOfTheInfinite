package com.kalegar.soti.screen;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.kalegar.soti.ShardsOfTheInfinite;
import com.kalegar.soti.entity.EntityFactory;
import com.kalegar.soti.entity.system.ControlSystem;
import com.kalegar.soti.entity.system.PhysicsSystem;
import com.kalegar.soti.entity.system.RenderSystem;
import com.kalegar.soti.entity.system.SteeringSystem;

public class GameScreen implements Screen {

    final ShardsOfTheInfinite game;

    private PooledEngine engine;
    private SpriteBatch batch;
    private static World world;

    private static EntityFactory entityFactory;

    public GameScreen(final ShardsOfTheInfinite game) {
        this.game = game;

        batch = new SpriteBatch();

        world = new World(new Vector2(), true);

        RenderSystem renderSystem = new RenderSystem(batch,1920,1080);
        ControlSystem controlSystem = new ControlSystem(renderSystem.getViewport(),world);

        engine = new PooledEngine();
        engine.addSystem(controlSystem);
        engine.addSystem(new SteeringSystem());
        engine.addSystem(new PhysicsSystem(world));
        engine.addSystem(renderSystem);

        entityFactory = new EntityFactory(engine, world);

        for (int i = 0; i < 8; i++) {
            entityFactory.getFighter(new Vector2(32*i+32, 32*i+32));
        }

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
            return;
        }
        GdxAI.getTimepiece().update(delta);
        engine.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        engine.getSystem(RenderSystem.class).resize(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        world.dispose();
    }

    public static World getWorld() {
        return world;
    }

    public static EntityFactory getEntityFactory() {
        return entityFactory;
    }
}
