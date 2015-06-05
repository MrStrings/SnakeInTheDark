package snake.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapRenderer;
import snake.engine.creators.WorldSettings;
import snake.visuals.enhanced.LightMapEntity;
import snake.visuals.enhanced.VisualGameWorld;

public class TiledMapWorld extends VisualGameWorld {

    private MapManager manager;
    private MapRenderer renderer;

    public TiledMapWorld(String mapName) {
        manager = new MapManager();
        manager.loadMap(mapName);
        renderer = manager.createRenderer();
    }

    public IMapAccess getMapAccess() {
        return manager;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        manager.tickEntities(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        renderer.setView((OrthographicCamera) getStage().getCamera());
        renderer.render();

        manager.drawEntities(batch, parentAlpha);
    }

    @Override
    public void show() {
        WorldSettings.setAmbientColor(Color.WHITE);
        OrthographicCamera camera = (OrthographicCamera) getStage().getCamera();
        int width = manager.getMapWidth();
        int height = manager.getMapHeight();
        float wFactor = width / camera.viewportWidth;
        float hFactor = height / camera.viewportHeight;
        float factor = Math.max(wFactor, hFactor);
        camera.setToOrtho(false, width, height);
        camera.position.set(width * .5f, height * .5f, 0);
        camera.zoom = factor;
        camera.update();
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        for (MapEntity entity : manager.getEntities()) {
            entity.dispose();
            if (entity instanceof LightMapEntity)
                ((LightMapEntity) entity).disposeLights();
        }
    }

    @Override
    public void createLights() {
        for (MapEntity entity : manager.getEntities())
            if (entity instanceof LightMapEntity)
                ((LightMapEntity) entity).createLights();
    }
}
