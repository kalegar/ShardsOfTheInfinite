package com.kalegar.soti;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;

public class AssetLoader implements Disposable {
    private final static String spriteAtlasPath = "images/game.atlas";
    private static AssetLoader instance = null;
    private AssetManager assetManager;
    private boolean assetsLoaded = false;

    private TextureAtlas spriteAtlas;

    private AssetLoader() {
        assetManager = new AssetManager();

    }

    public static AssetLoader getInstance() {
        if (instance == null) {
            instance = new AssetLoader();
        }
        return instance;
    }

    public void loadSpriteAtlas() {
        assetsLoaded = false;
        assetManager.load(spriteAtlasPath, TextureAtlas.class);
    }

    public boolean update() {
        if (assetManager.update()) {
            spriteAtlas = assetManager.get(spriteAtlasPath, TextureAtlas.class);
            assetsLoaded = true;
            return true;
        }
        return false;
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
