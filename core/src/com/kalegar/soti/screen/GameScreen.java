package com.kalegar.soti.screen;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kalegar.soti.ShardsOfTheInfinite;
import com.kalegar.soti.entity.system.RenderSystem;

public class GameScreen implements Screen {

    final ShardsOfTheInfinite game;

    private PooledEngine engine;
    private SpriteBatch batch;

    public GameScreen(final ShardsOfTheInfinite game) {
        this.game = game;

        batch = new SpriteBatch();

        engine = new PooledEngine();
        engine.addSystem(new RenderSystem(batch,480,270));
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

        engine.update(delta);
    }

    @Override
    public void resize(int width, int height) {

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

    }
}
