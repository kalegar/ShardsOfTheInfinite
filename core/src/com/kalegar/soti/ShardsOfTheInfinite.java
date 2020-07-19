package com.kalegar.soti;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kalegar.soti.screen.GameScreen;

public class ShardsOfTheInfinite extends Game {

	private boolean assetsLoaded = false;
	
	@Override
	public void create () {
		AssetLoader.getInstance().loadSpriteAtlas();
	}

	@Override
	public void render () {
		super.render();

		if (!assetsLoaded && AssetLoader.getInstance().update()) {
			assetsLoaded = true;
			this.setScreen(new GameScreen(this));
		}
	}
	
	@Override
	public void dispose () {
		this.getScreen().dispose();
		AssetLoader.getInstance().dispose();
	}
}
