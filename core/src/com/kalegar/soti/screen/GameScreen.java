package com.kalegar.soti.screen;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.kalegar.soti.ShardsOfTheInfinite;
import com.kalegar.soti.entity.EntityFactory;
import com.kalegar.soti.entity.system.PhysicsSystem;
import com.kalegar.soti.entity.system.RenderSystem;

public class GameScreen implements Screen {

    final ShardsOfTheInfinite game;

    private PooledEngine engine;
    private SpriteBatch batch;
    private World world;

    private EntityFactory entityFactory;

    public GameScreen(final ShardsOfTheInfinite game) {
        this.game = game;

        //Init Bullet
        Bullet.init();

        batch = new SpriteBatch();

        world = new World(new Vector2(), true);

        engine = new PooledEngine();
        engine.addSystem(new PhysicsSystem(world));
        engine.addSystem(new RenderSystem(batch,960,540));

        entityFactory = new EntityFactory(engine, world);


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

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Vector3 pos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            Vector3 mPos = engine.getSystem(RenderSystem.class).getCamera().unproject(pos);
            entityFactory.getFighter(new Vector2(mPos.x,mPos.y));
            Gdx.app.log("entity","Created fighter at " + mPos.x + ", " + mPos.y);
        }

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
}
