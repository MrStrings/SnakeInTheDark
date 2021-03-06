package snake.map;

import box2dLight.Light;
import box2dLight.PointLight;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import snake.drone.Drone;
import snake.drone.IObserver;
import snake.engine.creators.ScreenCreator;
import snake.engine.dataManagment.Loader;
import snake.equipment.EquipmentCreator;
import snake.equipment.IEquipment;
import snake.visuals.Lights;
import snake.visuals.enhanced.ILightMapEntity;
import snake.visuals.enhanced.LightMapEntity;

import java.util.*;

public class MapManager implements IMapAccess, IObserver {

    private static final AssetManager assetManager;

    static {
        assetManager = Loader.getManager();
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
    }

    private TiledMap map;
    private String currentMap;
    private String nextMap;

    private int dronesToSpawn;

    private int spawnX, spawnY;

    private final List<IMapEntity> entities = new LinkedList<>();
    private final List<IMapEntity> entitiesWrapper = Collections.unmodifiableList(entities);
    private final List<IMapEntity> entitiesToAdd = new ArrayList<>();
    private final List<IMapEntity> entitiesToRemove = new ArrayList<>();

    private final List<String> availableEquipments = new ArrayList<>();

    private int mapWidth;
    private int mapHeight;
    private int tileWidth;
    private int tileHeight;

    private Light exitLight;
    private int exitX, exitY;

    private Random random = new Random();

    @Override
    public List<IMapEntity> getEntities() {
        return entitiesWrapper;
    }

    @Override
    public List<IMapEntity> getEntities(int x, int y) {
        List<IMapEntity> ret = new ArrayList<>();
        // Slow code
        for (IMapEntity entity : entities)
            if ((int) entity.getX() == x && (int) entity.getY() == y)
                ret.add(entity);
        return ret;
    }

    @Override
    public List<IMapEntity> getEntities(int x, int y, String type) {
        List<IMapEntity> ret = new ArrayList<>();
        // Slow code
        for (IMapEntity entity : entities)
            if ((int) entity.getX() == x && (int) entity.getY() == y && entity.getType().equals(type))
                ret.add(entity);
        return ret;
    }

    @Override
    public IMapEntity getEntity(int x, int y, String type) {
        // Slow code
        for (IMapEntity entity : entities)
            if ((int) entity.getX() == x && (int) entity.getY() == y && entity.getType().equals(type))
                return entity;
        return null;
    }

    @Override
    public boolean addEntity(IMapEntity entity) {
        return entitiesToAdd.add(entity);
    }

    /**
     * Cuidado deve ser tomado ao chamar esse método de dentro de um
     * {@link com.badlogic.gdx.scenes.scene2d.Group#act(float) act}, pois
     * pode gerar exceções.
     *
     * @param entity IMapEntity a ser adicionada
     * @return true se adicionou com sucesso
     */
    boolean addEntityDirect(IMapEntity entity) {
        return entities.add(entity);
    }

    @Override
    public boolean removeEntity(IMapEntity entity) {
        return entitiesToRemove.add(entity);
    }

    void clearEntities() {
        entitiesToRemove.addAll(entities);
        disposeEntities();
    }

    void tickEntities(float delta) {
        for (IMapEntity entity : entities)
            entity.act(delta);
        entities.removeAll(entitiesToRemove);
        entities.addAll(entitiesToAdd);
        entitiesToRemove.clear();
        entitiesToAdd.clear();
    }

    void drawEntities(Batch batch, float parentAlpha) {
        for (IMapEntity entity : entities)
            entity.draw(batch, parentAlpha);
    }

    void preloadMap(String name) {
        assetManager.load(name, TiledMap.class);
    }

    void loadMap(String name) {
        preloadMap(name);
        assetManager.finishLoadingAsset(name);
        map = assetManager.get(name);
        currentMap = name;

        clearEntities();
        availableEquipments.clear();

        MapProperties properties = map.getProperties();
        mapWidth = properties.get("width", Integer.class);
        mapHeight = properties.get("height", Integer.class);
        tileWidth = properties.get("tilewidth", Integer.class);
        tileHeight = properties.get("tileheight", Integer.class);

        nextMap = properties.get("nextMap", null, String.class);

        String[] tmp = properties.get("spawnPoint", "1,1", String.class).split(",");
        spawnX = Integer.parseInt(tmp[0]);
        spawnY = Integer.parseInt(tmp[1]);

        tmp = properties.get("exit", "1,1", String.class).split(",");
        exitX = Integer.parseInt(tmp[0]);
        exitY = Integer.parseInt(tmp[1]);

        String equips = properties.get("equipList", "", String.class);
        Collections.addAll(availableEquipments, equips.split(","));
        int equipQuantity = Integer.parseInt(properties.get("equipQuantity", "0", String.class));
        spawnEquipments(equipQuantity);

        dronesToSpawn = Integer.parseInt(properties.get("droneQuantity", "0", String.class));
        spawnRandomDrones();
    }

    @Override
    public void spawnDrone() {
        dronesToSpawn++;
    }

    @Override
    public void loadNextMap() {
        try {
            ScreenCreator.switchAndGo("snakescreen", "tiledmap", nextMap);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void reloadMap() {
        try {
            ScreenCreator.switchAndGo("snakescreen", "tiledmap", currentMap);
        } catch (Exception ignored) {
        }
    }

    public MapRenderer createRenderer() {
        int tileSize = Math.min(tileWidth, tileHeight);
        return new OrthogonalTiledMapRenderer(map, 1f / tileSize);
    }

    void moveToSpawnPoint(IMapEntity entity) {
        entity.setPosition(spawnX, spawnY);
    }

    private void spawnEquipments(int equipQuantity) {
        TiledMapTileLayer baseLayer = (TiledMapTileLayer) map.getLayers().get("base");

        for (int i = 0; i < equipQuantity; i++) {
            String cellType;
            Cell cell;
            int x, y;
            boolean alreadySpawned;

            do {
                x = random.nextInt(mapWidth);
                y = random.nextInt(mapHeight);

                cell = baseLayer.getCell(x, y);
                MapProperties properties = cell.getTile().getProperties();

                cellType = properties.get("type", "", String.class);
                alreadySpawned = properties.get("spawned", false, Boolean.class);
            } while (!cellType.equals("floor") && !alreadySpawned);

            cell.getTile().getProperties().put("spawned", true);

            int index = random.nextInt(availableEquipments.size());
            IEquipment equipment = EquipmentCreator.createFactory(availableEquipments.get(index)).create(x, y, true, this);
            equipment.createLights();

            addEntityDirect(equipment);
        }
    }

    private void spawnRandomDrones() {
        int droneQuantity = random.nextInt(dronesToSpawn + 1);
        dronesToSpawn -= droneQuantity;
        spawnDrones(droneQuantity);
    }

    private void spawnDrones(int droneQuantity) {
        TiledMapTileLayer baseLayer = (TiledMapTileLayer) map.getLayers().get("base");

        for (int i = 0; i < droneQuantity; i++) {
            String cellType;
            Cell cell;
            int x, y;
            String direction;

            do {
                x = random.nextInt(mapWidth - 2) + 1;
                y = random.nextInt(mapHeight - 2) + 1;

                if (random.nextBoolean()) {
                    if (random.nextBoolean()) {
                        x = 0;
                        direction = "direita";
                    } else {
                        x = mapWidth - 1;
                        direction = "esquerda";
                    }
                } else {
                    if (random.nextBoolean()) {
                        y = 0;
                        direction = "cima";
                    } else {
                        y = mapHeight - 1;
                        direction = "baixo";
                    }
                }

                cell = baseLayer.getCell(x, y);
                MapProperties properties = cell.getTile().getProperties();

                cellType = properties.get("type", "", String.class);
            } while (!cellType.equals("wall"));

            ILightMapEntity drone = new Drone(this, x, y, direction);
            drone.createLights();

            addEntity(drone);
        }
    }

    void createLights() {
        exitLight = new PointLight(Lights.getRayhandler(), 5000, new Color(.3f, .3f, .3f, 1f), .8f, exitX + .5f, exitY + .5f);
        exitLight.setStaticLight(true);
        exitLight.setXray(true);

        for (IMapEntity entity : entities)
            if (entity instanceof ILightMapEntity)
                ((LightMapEntity) entity).createLights();
    }

    void disposeEntities() {
        for (IMapEntity entity : entitiesToRemove)
            entity.dispose();
        if (exitLight != null) {
            exitLight.dispose();
            exitLight = null;
        }
    }

    @Override
    public CellType getCellType(int x, int y) {
        TiledMapTileLayer baseLayer = (TiledMapTileLayer) map.getLayers().get("base");
        Cell cell = baseLayer.getCell(x, y);
        MapProperties properties = cell.getTile().getProperties();

        String cellType = properties.get("type", "", String.class);

        try {
            return CellType.valueOf(cellType.toUpperCase(Locale.ENGLISH));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public void setCellType(int x, int y, CellType type) {
        TiledMapTileLayer baseLayer = (TiledMapTileLayer) map.getLayers().get("base");
        Cell cell = baseLayer.getCell(x, y);
        MapProperties properties = cell.getTile().getProperties();

        properties.put("type", type.toString().toLowerCase(Locale.ENGLISH));
    }

    @Override
    public int getMapWidth() {
        return mapWidth;
    }

    @Override
    public int getMapHeight() {
        return mapHeight;
    }

    @Override
    public int getTileWidth() {
        return tileWidth;
    }

    @Override
    public int getTileHeight() {
        return tileHeight;
    }

    @Override
    public void update(float ignored) {
        spawnRandomDrones();
    }
}
