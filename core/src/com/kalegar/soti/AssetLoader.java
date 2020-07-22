package com.kalegar.soti;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.kalegar.soti.gfx.AnimationAdvanced;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AssetLoader implements Disposable {
    private final static String spriteAtlasPath = "images/game.atlas";
    private static AssetLoader instance = null;
    private AssetManager assetManager;
    private boolean assetsLoaded = false;
    private final Map<String, Vector2> spriteOrigins;
    private Map<String, AnimationAdvanced<TextureRegion>> animations = new HashMap<>();

    private TextureAtlas spriteAtlas;

    private AssetLoader() {
        assetManager = new AssetManager();
        Map<String, Vector2> aMap = new HashMap<>();
        aMap.put("fighter", new Vector2(16, 16));
        spriteOrigins = Collections.unmodifiableMap(aMap);
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

    public AnimationAdvanced<TextureRegion> getAnimation(String name) {
        if (animations.containsKey(name)) {
            return animations.get(name);
        } else {
            Array<TextureAtlas.AtlasRegion> regions = spriteAtlas.findRegions(name);
            AnimationAdvanced<TextureRegion> animation = new AnimationAdvanced<TextureRegion>(1f, regions, Animation.PlayMode.LOOP);
            if (spriteOrigins.containsKey(name)) {
                Vector2 origin = spriteOrigins.get(name);
                animation.setOrigin((int) origin.x, (int) origin.y);
            }else{
                //Default to center of image
                animation.setOrigin(animation.getKeyFrame(0).getRegionWidth()/2,animation.getKeyFrame(0).getRegionHeight()/2);
            }
            animations.put(name, animation);
            return animation;
        }
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }
}
