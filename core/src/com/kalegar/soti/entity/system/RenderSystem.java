package com.kalegar.soti.entity.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kalegar.soti.entity.component.ComponentMappers;
import com.kalegar.soti.entity.component.RenderComponent;
import com.kalegar.soti.entity.component.TransformComponent;
import com.kalegar.soti.gfx.AnimationAdvanced;
import com.kalegar.soti.util.Constants;

import java.util.Comparator;

public class RenderSystem extends SortedIteratingSystem {

    private SpriteBatch batch;

    private OrthographicCamera camera;
    private Viewport viewport;

    private Array<Entity> renderQueue;

    public RenderSystem(SpriteBatch batch, float viewWidth, float viewHeight) {
        super(Family.all(TransformComponent.class, RenderComponent.class).get(), new ZComparator());

        this.batch = batch;

        renderQueue = new Array<>();

        camera = new OrthographicCamera();
        camera.setToOrtho(false,viewWidth,viewHeight);

        camera.position.x = viewWidth/2;
        camera.position.y = viewHeight/2;

        viewport = new ExtendViewport(viewWidth,viewHeight,camera);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        camera.update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        batch.enableBlending();
        batch.begin();

        for (Entity entity : renderQueue) {

            RenderComponent rc = ComponentMappers.render.get(entity);
            TransformComponent tc = ComponentMappers.transform.get(entity);

            if (!rc.visible) {
                continue;
            }
            AnimationAdvanced<TextureRegion> animation = rc.animation;
            if (rc.animate) {
                rc.stateTime += Gdx.graphics.getDeltaTime();
            }
            TextureRegion currentFrame = animation.getKeyFrame(rc.stateTime, true);
            Vector3 position = tc.position.cpy();
            position.x *= Constants.PPM;
            position.y *= Constants.PPM;
            Vector2 origin = animation.getOrigin();
            batch.draw(currentFrame,
                    position.x - origin.x,
                    position.y - origin.y,
                    origin.x,
                    origin.y,
                    currentFrame.getRegionWidth(),
                    currentFrame.getRegionHeight(),
                    tc.scale.x,
                    tc.scale.y,
                    tc.rotation);

        }

        batch.end();

        //Clear render queue
        renderQueue.clear();
    }

    public void resize(int width, int height) {
        viewport.update(width,height);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    private static class ZComparator implements Comparator<Entity> {
        private ComponentMapper<TransformComponent> tm = ComponentMappers.transform;

        @Override
        public int compare(Entity e1, Entity e2) {
            return (int)Math.signum(tm.get(e1).position.z - tm.get(e2).position.z);
        }
    }

}
