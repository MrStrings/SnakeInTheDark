package snake.map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import snake.engine.dataManagment.Loader;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MapManager implements IMapAccess {

    private static final AssetManager assetManager;

    static {
        assetManager = Loader.getManager();
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
    }

    private TiledMap map;

    private final List<IMapEntity> entities = new LinkedList<>();
    private final List<IMapEntity> immutableWrapper = Collections.unmodifiableList(entities);

    private int mapWidth;
    private int mapHeight;
    private int tileWidth;
    private int tileHeight;

    @Override
    public List<IMapEntity> getEntities() {
        return immutableWrapper;
    }

    @Override
    public void addEntity(IMapEntity entity) {
        entities.add(entity);
    }

    public void clearEntities() {
        entities.clear();
    }

    public void tickEntities(float delta) {
        for (IMapEntity entity : entities)
            entity.act(delta);
    }

    public void drawEntities(Batch batch, float parentAlpha) {
        for (IMapEntity entity : entities)
            entity.draw(batch, parentAlpha);
    }

    public void preloadMap(String name) {
        assetManager.load(name, TiledMap.class);
    }

    public void loadMap(String name) {
        preloadMap(name);
        assetManager.finishLoadingAsset(name);
        map = assetManager.get(name);

        MapProperties properties = map.getProperties();
        mapWidth = properties.get("width", Integer.TYPE);
        mapHeight = properties.get("height", Integer.TYPE);
        tileWidth = properties.get("tilewidth", Integer.TYPE);
        tileHeight = properties.get("tileheight", Integer.TYPE);
    }

    public MapRenderer createRenderer() {
        int tileSize = Math.min(tileWidth, tileHeight);
        return new OrthogonalTiledMapRenderer(map, 1f / tileSize);
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }
}
